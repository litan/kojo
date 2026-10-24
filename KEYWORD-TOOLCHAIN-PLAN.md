# Language-specific Scala toolchains: implementation handoff

Prepared from the investigation and design discussion on September 6, 2026.
Implemented on October 23, 2026. The design and scope below are retained as a
record of the agreed implementation.

## Implementation and verification

- Added lightweight `LanguageRegistry` / `LanguageDescriptor` registrations;
  launcher codes and localized download messages now come from that metadata.
- Restored stock Scala packaging in ordinary `lib` and removed the experimental
  stock-toolchain staging script. Existing build flags and unrelated language
  work remain intact.
- Alternatives resolve to `<installation>/toolchains/scala-<code>/<version>`
  using the explicit installation override or incoming `lib/rithica.jar`.
  Selection replaces exactly the four toolchain artifacts, including versioned
  Maven/Coursier entries, while preserving other dependencies in order.
- Downloads publish a fully validated directory. Reuse validates retained
  checksums, archive structure, and all three Scala component versions offline.
  Valid provisioned sets need no writes; invalid existing sets are reported and
  require manual replacement/removal rather than being silently overwritten.
- Scratchpads inherit the running Kojo's classpath and toolchain without
  selecting again. Language changes require a full Kojo restart, preserving
  existing scratchpad behavior. Original-classpath tracking and toolchain
  system-property forwarding were removed in the October 24 review.
- Downloader tests use temporary roots and unconditional cleanup. Coverage
  includes incomplete, corrupt, incompatible, offline and unwritable sets,
  cancellation, concurrent downloads, overrides and both classpath layouts.
- `./sbt.sh test buildDist`: 389 tests passed across JUnit and ScalaTest,
  with two existing tests skipped; distribution build passed. The focused launcher/downloader/keyword suites
  also passed (41 tests).
- Before the October 24 scratchpad simplification, real Turkish and English
  compilation probes passed using the child classpaths produced by both
  `DesktopMain` and `NewKojoInstance`, for an IDE/Coursier
  classpath and a staged packaged `lib` classpath. Each snippet printed `1`;
  English selection downloaded nothing. Probes used temporary installations
  and a local release fixture copied from the previously available Turkish jars.
- After the October 24 simplification, the focused toolchain, downloader and
  keyword suites passed (36 tests), including a regression proving that
  scratchpads retain the running toolchain when selection would choose another.
- Shell syntax and installer/launch4j XML checks passed. Windows/macOS runtime
  launches and interactive IntelliJ UI operation were not exercised here.

See [localization.md](localization.md#level-4) for the implemented workflow,
provisioning requirements, overrides, failure diagnostics and cleanup guidance.

## Context and agreed direction

This branch explores localized Scala keywords as a **new Kojo feature**. Main
Kojo already uses stock Scala jars from the launch environment's normal
classpath. The `scala-en` directory was introduced only on this experimental
branch.

Preserve main Kojo's stock dependency and launch conventions. Add
installation-owned alternative toolchains for Turkish and other languages
requiring patched compilers.

Define an installation as:

- The application directory for packaged Kojo.
- An individual repository checkout for IDE/development runs.

Avoid runtime dependence on downloaded toolchains under `~/.kojo/lite`.

## Relationship to the language-interface refactor

The companion task is described in
[LANGUAGE-SUPPORT-REFACTOR-PLAN.md](LANGUAGE-SUPPORT-REFACTOR-PLAN.md).
The tasks are largely independent. Recommended order: establish the small
shared registration boundary below, implement this toolchain task, then do the
editor/presentation refactor. The broader refactor is not a prerequisite for
fixing the IDE launch failure.

Both tasks must minimize changes to existing core code. The comparison baseline
is `/home/lalit/t1/work/kojo` at commit
`e20ccf0087db51a9bcf8978b7fa37c27e39fadf1`, confirmed as an ancestor of this
branch during review. Use that commit for the feature diff even if the main
checkout advances. Preserve unrelated Turkish API, sample, and resource work.

PR #91 is now merged at `aa7409d5`. It does not replace this toolchain work;
preserve its Turkish collections/help, samples, and macOS fullscreen fix.

### Shared registration boundary

- Introduce a small language-neutral `LanguageRegistry` with lightweight
  `LanguageDescriptor` entries. Each entry identifies the language, its optional
  alternative toolchain code, and its download-progress message. No alternative
  means the stock launch-environment Scala dependencies.
- Register Turkish and Swedish requirements explicitly. Derive the launcher's
  supported alternative-toolchain codes from this registry rather than keeping
  another hardcoded language list.
- Keep descriptor construction independent of editor classes, Turkish API
  initialization, Swing object creation, filesystem access, and downloading.
- Keep download URLs, Scala-release resolution, storage, validation, and
  classpath construction in shared services. Language implementations supply
  metadata; they do not implement their own downloaders or launchers.
- Move the existing Turkish/Swedish progress strings out of the shared Swing
  progress implementation into the descriptors; pass the selected text to the
  UI, with the existing English fallback.
- The companion task will attach lazy editor-support factories to these same
  registrations. Do not create that larger interface as a prerequisite here.
  If the companion task has already introduced the registry, reuse it.
- Preserve the existing distinction between UI language and an explicit compiler
  override: `kojo.toolchain=tr` may select a Turkish compiler without changing
  the user's UI language.

## Confirmed findings

DesktopMain correctly selected `scala-tr`, but the debugger observed
`toolchainRoots=List()`. `ScalaToolchain.select` recognizes jars only when their
immediate parent directory is `scala-en`, `scala-tr`, or `scala-sv`. IntelliJ
supplies stock jars from Coursier's versioned directories, so the selector
returns the original classpath unchanged.

Compilation probes confirmed:

- `dez x = 1; println(x)` fails on the actual IDE-launched child JVM classpath.
- Replacing its Scala library, reflect, compiler, and scalariform jars with the
  cached Turkish versions makes the same snippet print `1`.
- Ordinary English Scala works with the original classpath.
- The version-mismatch check passes because all three stock Scala components
  report `2.13.18`; it does not detect missing localized keyword support.

Downloads currently use direct GitHub URLs, not Maven/Ivy/Coursier:

```text
https://github.com/bulent2k2/scala-2/releases/download/v<version>-<lang>/
```

`kojo.toolchain.url` overrides the release-directory URL. There is no
language-to-URL configuration file.

The empty `~/.kojo/lite/scala-sv/` directory was left by downloader tests. The
test report and directory modification timestamp matched at 10:12:15 on
September 6, 2026. Tests use Swedish explicitly, create dummy toolchains under
the real user directory, and delete version subdirectories while leaving their
parent. The separate repository-root `scala-sv/` directory contains tracked
Swedish compiler patches and documentation.

## Implementation changes

### 1. Restore stock launch conventions

- Compare this branch's launcher and packaging changes with main.
- Remove the requirement for `lib/scala-en` and restore stock Scala jars to the
  ordinary packaged `lib` classpath.
- Update affected Unix, Windows, and installer launch definitions and staging
  scripts together, preserving unrelated branch changes.
- Preserve PR #91's JDK-conditional CMS flags and test `--add-opens` in
  `build.sbt` when restoring stock dependency conventions.
- Keep the build and desktop launcher on stock Scala. Apply localized compiler
  substitution when constructing the desktop Kojo child JVM's classpath.
  Scratchpad launchers inherit the running Kojo's already-selected classpath.

### 2. Resolve installation-owned alternatives

- Store downloaded variants under
  `<installation>/toolchains/scala-<lang>/<scala-version>/`.
- Keep this directory outside ordinary dependency `lib` and Git-ignore it in
  development checkouts.
- Support `-Dkojo.install.dir` as an explicit root override.
- Otherwise derive the installation root from the incoming classpath's
  `lib/rithica.jar`, matching the existing installation convention. Do not
  silently substitute the user's home or working directory when discovery
  fails.
- Pass the resolved destination into the downloader explicitly; remove its
  hardcoded user-home destination.
- Derive the Scala release from the launcher's compiler, rather than hardcoding
  `2.13.18`.
- Preserve existing language-selection and `kojo.toolchain` override precedence.
- For this experimental feature, standardize alternative toolchains on the new
  location; no compatibility layer for the branch-only `scala-en` layout is
  required.

### 3. Substitute complete toolchains

- Identify the four components by the existing artifact-filename helper,
  including versioned Maven names.
- Remove Scala library, reflect, compiler, and scalariform entries from the
  child classpath and prepend the selected replacement set.
- Preserve unrelated dependencies and their relative order.
- Select the toolchain in DesktopMain. NewKojoInstance inherits the running
  Kojo's classpath without reselecting, including after a language change.
- Leave English and non-keyword-language launches on their existing stock
  dependencies.
- Do not download anything for an unselected language.

### 4. Validate and report failures

- Require all four jars before selecting a toolchain.
- Preserve download checksum, archive, and Scala-version validation.
- Retain downloaded checksums for offline integrity validation of reused
  toolchains.
- Validate the complete downloaded set before exposing it for selection.
- Log the selected language, Scala release, toolchain location, and fallback
  reason.
- If installation discovery, downloading, validation, or writing fails, retain
  the original classpath and explicitly report that localized keyword
  compilation is unavailable.
- For protected installations, allow already-provisioned toolchains. Otherwise
  explain that provisioning or a writable installation is required; do not
  silently fall back to a home-directory cache.

### 5. Isolate tests and document the workflow

- Give every downloader test a temporary destination root and clean it in
  `finally`.
- Keep Swedish as a test language, but eliminate writes to real Kojo user
  directories.
- Replace the existing "development classpath remains unchanged when Turkish
  is requested" expectation with successful substitution coverage.
- Update documentation to describe installation ownership, stock dependencies,
  IDE behavior, overrides, and fallback diagnostics.
- Remove the outdated assumption that development runs pin a patched compiler
  through `scalaHome`.

## Acceptance checks

- Turkish keyword snippets compile from both packaged and IntelliJ DesktopMain
  launches.
- English launches retain their normal dependencies and perform no
  localized-toolchain downloads.
- Coursier-style classpaths are handled without recognizing special
  parent-directory names.
- Only the four toolchain components are replaced.
- Separate installations and Scala-version directories remain independent.
- Missing, corrupt, incomplete, incompatible, offline, and unwritable
  toolchains produce clear failures without constructing a partial replacement
  classpath.
- Downloader tests leave real user directories untouched.
- Packaging includes stock Scala jars through the ordinary launch classpath.
- Startup diagnostics distinguish version mismatch from missing localized
  compiler support.
- Resolving toolchain metadata does not initialize editor or language API
  implementations. Stock/default metadata requires no alternative download.
- Core launcher and downloader code contains no language-specific branching;
  language registration owns the available variants and progress text.

## Deferred work and boundaries

- Do not automatically delete or migrate existing `~/.kojo/lite/scala-*`
  directories. Document manual cleanup after older installations no longer
  need them.
- Accept duplicate downloads between installations initially. Shared download
  caching and Maven/Ivy integration are separate decisions.
- Defer automatic pruning and language-specific URL configuration.
- Defer explicit patched-build revisions. Two Turkish builds can target the
  same upstream Scala release; a version directory and locally retained
  checksums alone cannot detect a newer replacement published remotely.
- Do not modify unrelated preferences, extensions, or user-library storage.
- Do not refactor editor help, output translation, or type presentation in this
  task; those belong to the companion plan.
- Treat the branch's general stray-Scala-jar safeguards as a separate concern.
  Preserve them while changing toolchain selection, and do not expand or remove
  their effect on English launches as incidental cleanup.

## Code entry points

- `src/main/scala/net/kogics/kojo/lite/ScalaToolchain.scala`: language selection,
  current directory-layout detection, and classpath substitution.
- `src/main/scala/net/kogics/kojo/lite/ScalaToolchainFetcher.scala`: destination,
  download URL, validation, and cache reuse.
- `src/main/scala/net/kogics/kojo/lite/ScalaToolchainCheck.scala`: artifact-name
  identification and the existing Scala-version mismatch check.
- `src/main/scala/net/kogics/kojo/lite/DesktopMain.scala`,
  `NewKojoInstance.scala`, and `StubMain.scala`: child JVM launch classpath.
- `src/main/scala/net/kogics/kojo/util/Utils.scala`: existing `installDir`
  discovery, including its unsuitable fallback to the user's home directory.
- `src/test/scala/net/kogics/kojo/lite/ScalaToolchainTest.scala` and
  `ScalaToolchainFetcherTest.scala`: selection expectations and downloader test
  isolation.
- `build.sbt`, `stage-scala-toolchains.sh`, `makezip.sh`,
  `make-windows-zip.sh`, `installer/bin/`, and
  `installer.i4j/kojo.install4j`: stock dependencies and packaged launch setup.

The earlier investigation made no source/configuration changes. Its debugger
logpoint was removed and its debug session stopped.
