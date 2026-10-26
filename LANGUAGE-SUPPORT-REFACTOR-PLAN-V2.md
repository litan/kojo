# Language support refactor: v2 implementation plan

Prepared and implemented October 26, 2026. The design and implementation
sequence below are retained as the implementation record.

Start from `bef0af2a` on `keyword-localization`. The stash named
`Language support refactor before revised design` contains the earlier
implementation and tests; use it as reference, without reapplying it wholesale.
The [toolchain work](KEYWORD-TOOLCHAIN-PLAN.md) is complete and stays intact.
Use `e20ccf0087db51a9bcf8978b7fa37c27e39fadf1` as the master comparison baseline.

## Implementation and verification record

- Added early Swedish/Turkish `preInit()` registrations, immutable editor data,
  shared lookups/cached enumeration tables, and the small `LanguageBehavior`
  interface. Core consumers delegate presentation and completion-owner rules.
  Launcher metadata and the completed toolchain implementation are unchanged.
- Implemented base-first keyword-template/snippet lookup, preserved legacy
  method-template/API-help precedence, and kept Turkish keyword help first in
  turtle mode under its existing English keys. Restored shared English help
  content to the comparison baseline.
- Kept RSTA installation at editor construction, moved completion readiness
  after phase-two initialization, and added no late activation or consumer data
  caches. Documented the extension points in `localization.md`.
- Focused behavior, initialization, completion, help, highlighting, registration,
  and actor-restart checks passed. The restart regression exercises the real
  interpreter actor's `postRestart` path using a test-only failure message.
- `./sbt.sh test buildDist` passed: **416 tests passed, 2 skipped, 2 ignored**;
  the distribution build passed. The skipped tests require a Turkish compiler;
  the full suite ran with stock Scala. The ignored tests were already ignored.
- Real Linux/X11 editor startup and compilation probes passed for English and
  Turkish with both development/Coursier and built packaged classpaths. These
  checked both panes' initial highlighting, runner keyword completion, keyword
  help, installed snippets and completion expansion, output/error/worksheet
  presentation, and execution of a freshly compiled keyword-based program.
- English and Turkish tracing probes launched the actual separate tracing JVM,
  completed initialization and user code, and retained the editor process's
  registered record. All probes used temporary home/preferences directories;
  Turkish reused this installation's provisioned 2.13.18 toolchain.
- Existing RSTA limitation observed: its native trigger matcher accepts only
  ASCII letters/digits and underscores, so a trigger such as `dez_türlü` is
  installed but not found by native trigger lookup. Direct invocation of the
  installed template and completion-based expansion both passed. Unicode native
  trigger matching remains separate work; this refactor preserves that behavior.
- Windows/macOS launches were not exercised. No stash was reapplied.
- Constructor-injection follow-up: all four behavior consumers accept a private
  optional constructor parameter and pass it to their children where applicable.
  All 24 affected tests passed, including injected output/error presentation and
  Show Type/completion-provider propagation. An isolated real execution-support
  constructor probe also verified output-pane propagation and worksheet
  translation while the process-wide selected behavior remained unchanged.
- Simplified the editor-data registry to one active record and `register(data)`,
  retaining the volatile reference. Removed per-language storage and selection
  from registration. All 46 tests across the eight affected suites passed.

## 1. Agreed design and lookup order

Keep core changes small and language neutral. Register static editor data before
constructing consumers, through language-specific `preInit()` methods. Keep
builtins-dependent language initialization in the existing `init(builtins)`
methods. Introduce a small behavior interface for language-specific operations.

All lookups below are **first match wins**:

| Lookup | Order |
| --- | --- |
| Keyword template | English/base template → localized template → `null` |
| Snippet by trigger | Base snippet → localized snippet → missing |
| Method template | Builtins template → mode-specific template → registered language method template → `null` |
| Help | Registered localized keyword help for the active coding mode → common English help → mode-specific English help → registered language API help → `null` |

Keyword help is the deliberate exception to base-first lookup. Existing Turkish
keyword explanations use English keys such as `for`, `def`, `if`, and `val`;
they must still replace the English help in turtle mode. Preserve these keys,
the current explanations, and existing topic coverage. Do not move the entries
to localized spellings as part of this refactor. Ordinary registered API help
keeps its existing fallback position.

Current localized template/snippet keys differ from their base counterparts.
For any future collision, base entries win consistently in lookup, completion,
rendering, and RSTA expansion. A cached effective map therefore uses
`localized ++ base`, since Scala map concatenation gives the right-hand value
precedence.

Keyword completion retains the current enumeration order: localized keywords
followed by English keywords. This is list order, not lookup precedence.

## 2. Early static-data registration

Add these internal interfaces under `lite.i18n`:

```scala
final case class LanguageEditorData(
    keywords: List[String] = Nil,
    keywordTemplates: Map[String, String] = Map.empty,
    snippets: Map[String, String] = Map.empty,
    keywordHelp: Map[CodingMode, Map[String, String]] = Map.empty
)

// On the internal LanguageEditorDataRegistry object:
def register(data: LanguageEditorData): Unit
def current: LanguageEditorData
```

- Hold one active immutable record, initially the shared empty record. The
  selected language's `preInit()` registers its complete record; repeated
  registration replaces it. Retain the immutable source lists/maps and publish
  records safely for reads by the EDT and interpreter actor through a private
  volatile reference. `current` is a read-only getter for that reference.
  No per-language storage or language comparison is needed.
- Add parameterless `preInit(): Unit` methods to `SvInit` and `TurkishInit`.
  Register Swedish/Turkish keywords, keyword templates, snippets, and Turkish
  keyword help directly through the registry. These hooks require no builtins.
  The enclosing objects' existing eager template/help fields are static data
  and can initialize at this point. Other languages need no new method yet.
- Extend `LangInit.init(prefs)`: after establishing the locale and
  `user.language`, capture the active UI language for editor data/behavior,
  dispatch to that language's `preInit()`, and select the behavior before
  returning. This entry point already runs during `KojoCtx` construction,
  before the output pane, runner, provider, or editor exists. Load only the
  selected language's contributions.
- Keep the captured active editor language fixed for the process. Language-menu
  preference changes take effect after restart; compiler overrides do not
  select editor data or behavior.
- Leave language API bindings, greetings, method-template/API-help hooks, and
  builtins-dependent settings in `init(builtins)`. Preserve the public
  `b.addCodeTemplates` and `b.addHelpContent` APIs and their existing `Builtins`
  branches. Do not add `b.addLanguageEditorData`.
- Tracing runs its generated `LangInit()` / `initPhase2` calls in a separate JVM
  through `Tracing.launchVM`. Early registration there is local to that process;
  add no tracing-specific guard to the registry or `preInit()` hooks.

Keep `LanguageRegistry` / `LanguageDescriptor` as lightweight launcher metadata.
Editor contributions and alternative compiler availability remain independent.
Do not attach editor factories or behavior to launcher descriptors.

## 3. Shared lookups and editor integration

- Highlighting queries the registered data's cached keyword set directly.
  Remove the old keyword facade, its eager Turkish/Swedish pack inventory, and
  per-token system-property selection. Cache each record's keyword set once.
- Use shared point lookups for templates, snippets, and help with the order in
  section 1. Retain existing accessor names where useful; turn snapshot `val`s
  into lookup `def`s where necessary. The runner and provider query these shared
  accessors as needed. Keyword-template and snippet lookup/rendering use the
  same cached effective maps as enumeration; method-template/help lookups retain
  their fallback chains.
- Centrally cache the combined keyword list and effective snippet table for
  enumeration. Retain any required aggregate template accessor with a centrally
  cached base-winning table. Keep base tables as shared `val`s and derived
  collections as safe lazy values. Base-table initialization must not call back
  into active-registration accessors. Do not merge collections per request or
  reread locale properties for the new data lookups.
- Use the same snippet resolution for completion proposals, snippet help/text
  rendering, and RSTA installation. `ScriptEditor` continues installing the
  complete effective snippets during construction. Both editor panes see
  localized keywords on their first tokenization.
- Restore shared English keyword-help entries to the master baseline. Move the
  Turkish strings/dictionary logic from shared `Help` into Turkish registration
  data. Query registered keyword help using the live coding mode before the
  existing common/mode/API help chain. Keep Turkish keyword help turtle-only.
- Keep editor data independent of the mode-specific clearing of legacy API help
  and method templates. Those clear operations retain their existing semantics.
- Move `startingUp = false` to after `LangInit.initPhase2(builtins)` completes.
  Completion must stay gated until method templates and API help are registered.
- Add no late editor activation, provider/actor data snapshots, cache refresh
  protocol, snippet reinstall, startup token invalidation, data-passing helper
  overloads, or actor-to-EDT synchronization. Actor restart continues using the
  shared registered data.

## 4. Language behavior and compatibility

Introduce `LanguageBehavior` under `lite.i18n`:

```scala
trait LanguageBehavior {
  def updateResult(text: String): String = text
  def updateTypes(text: String): String = text
  def recognizesOwner(owner: String): Boolean = false
  def allowsPlainNameFallback(owner: String): Boolean = false
}
```

- Provide the default identity/false implementation and a Turkish
  implementation. Swedish uses the default. Expose `forLanguage(code)` and a
  process-wide `lazy val current` based on the captured active UI language;
  force `current` at the end of `LangInit.init` after `preInit()`.
- Turkish delegates to the existing raw result/type translations and recognizes
  the full `net.kogics.kojo.lite.i18n.tr.` owner prefix with null-safe checks.
  Preserve translation algorithms and public Turkish API accessors.
- All four consumers accept an optional constructor-injected behavior, default
  to `LanguageBehavior.current`, and retain it in a private `val`.
  `CodeExecutionSupport` passes it to `OutputPane`; `ScriptEditor` passes it to
  `KojoCompletionProvider`, so child components use the supplied behavior too.
  Replace direct Turkish calls at output/error display, worksheet insertion, completion
  presentation/owner checks, and Show Type.
- Preserve core known-owner and plain-name-fallback rules. Keep qualified-name
  lookup before permitted plain-name fallback. The empty method-template string
  must continue suppressing fallback and allow reflection-based completion.
- Preserve existing Vanilla/Embedded behavior, all other language API
  initialization, PR #91's Turkish API/collections/help and samples, scratchpad
  toolchain inheritance, and completed toolchain/download/packaging behavior.
  Live language switching, Vanilla improvements, and translation-algorithm
  changes remain outside this task.
- Update `localization.md` to document `preInit()`, data registration, behavior,
  and the lookup-order exception. Point the companion toolchain plan to this v2
  plan while retaining its completed implementation record.

## 5. Implementation sequence and acceptance checks

1. Adapt useful characterization tests from the stash for output, types,
   owner fallback, keyword help, and insertion. Test observable behavior rather
   than the abandoned `LanguageSupport` interface.
2. Add behavior selection and the small consumer delegations. Verify English
   identity, Turkish translations, Swedish defaults, null owners, qualified-name
   priority, permitted fallback, and empty-template suppression. Verify Turkish
   output before the builtins-dependent initialization phase.
3. Add the registry, `preInit()` dispatch, and shared accessors. Exercise Turkish,
   Swedish, English, unknown languages, and synthetic registered data without
   requiring a matching compiler. Replace tests asserting equality between
   editor-language and toolchain-language sets with independent capability tests.
4. Extract keyword help and test the full help chain. In Turkish turtle mode,
   English keys such as `for` still produce the existing Turkish explanations;
   other modes/languages retain the baseline fallback. Cover unknown-topic
   `null`, mode changes, and live legacy help/method-template registrations.
5. Exercise actual provider proposals, runner keyword requests, and RSTA snippet
   expansion after early registration. Use deliberate key collisions to prove
   base templates/snippets win in every consumer while localized keyword help
   wins over English help. Check distinct localized entries remain available,
   initial highlighting in both panes, duplicate registration, and keyword
   completion after actor restart.
6. Verify startup ordering without builtins in `preInit()`, completion gating
   until phase two finishes, fixed editor-language selection after preference
   changes/compiler overrides, and safe concurrent first access to derived
   collections. Restore shared test registry state in `finally`; use explicit
   data/behavior inputs where possible, no `ThreadLocal`, and make
   `Test / parallelExecution := false` explicit as previously agreed.
7. Run the affected behavior, help, completion, keyword, and toolchain suites,
   then `./sbt.sh test buildDist`. Smoke-test Turkish/English compilation and
   editor startup for IDE and packaged classpaths, plus tracing startup in its
   separate JVM. Record the runtime/platform launches actually exercised.
8. Review the diff against the clean starting commit and master baseline.
   Confirm core edits are small delegations, shared lookups, and startup
   registration; language content and decisions remain in `i18n`. Preserve the
   completed toolchain implementation record.
