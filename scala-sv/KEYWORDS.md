# Swedish keyword table — DRAFT for review

These are the Scala keywords mapped to **suggested** Swedish words, as encoded in
`swedish-keywords.patch`. **They are AI-generated and must be checked by a native
Swedish speaker.** Change the string in the patch (the `kw("…")` values in the
`StdNames.scala` hunk) and rebuild; the mechanism does not care what the words are.

Two rules a reviewer should keep in mind:

1. **A keyword can no longer be used as an identifier.** These words are *added*
   to the language, so once `ny` means `new`, no Swedish Kojo program (or the
   Swedish Level-2 API) may use `ny` as a variable or method name. The Turkish
   team hit exactly this and had to rename a local `den`. Check each word against
   the Swedish API in `lite/i18n/svInit.scala` and the Swedish samples.
2. **Prefer words a child reads as the concept**, not a literal dictionary gloss.

## The table

| Scala | suggested | note |
|---|---|---|
| `val` | `värde` | "value". NB: `val` itself is a Swedish word (whale/choice) — don't reuse it. |
| `var` | `variabel` | "variable". NB: `var` is Swedish for was/where — don't reuse it. |
| `def` | `definiera` | "define". Long; `metod` (method) is an option. |
| `object` | `objekt` | |
| `class` | `klass` | |
| `trait` | `egenskap` | "characteristic/property". |
| `extends` | `ärver` | "inherits" — pedagogically clearer than a literal "extends". |
| `new` | `ny` | ⚠ very common word; check it isn't wanted as an identifier. |
| `if` | `om` | |
| `else` | `annars` | |
| `for` | `för` | |
| `while` | `medan` | |
| `do` | `utför` | "carry out"; `gör` (do) is shorter but extremely common. |
| `yield` | `ge` | ⚠ short and common ("give") — high collision risk. |
| `match` | `matcha` | |
| `case` | `fall` | ⚠ common word. |
| `try` | `försök` | |
| `catch` | `fånga` | |
| `finally` | `slutligen` | |
| `throw` | `kasta` | ⚠ must not collide with a compiler-internal identifier (see the note in the patch — the Turkish `at` did). |
| `return` | `returnera` | |
| `true` | `sant` | |
| `false` | `falskt` | |
| `null` | `ingen` | "none"; `tomt` (empty) or `ingenting` (nothing) are options. |
| `this` | `denna` | |
| `super` | `super` | kept as-is; `överordnad` is a Swedish option. |
| `import` | `importera` | |
| `package` | `paket` | |
| `private` | `privat` | |
| `protected` | `skyddad` | |
| `override` | `åsidosätt` | |
| `abstract` | `abstrakt` | |
| `final` | `slutlig` | |
| `sealed` | `förseglad` | |
| `lazy` | `lat` | ⚠ short; genuinely means "lazy" though. |
| `implicit` | `implicit` | kept as-is; `underförstådd` is the Swedish word. |
| `type` | `typ` | ⚠ `typ` is also very common Swedish slang ("like/about"). |
| `with` | `med` | ⚠ extremely common word — highest collision risk of all. |
| `forSome` | `förNågra` | rare in practice; low priority. |

## Words flagged ⚠

`ny`, `ge`, `fall`, `kasta`, `lat`, `typ`, `med` are the ones most likely to
clash with ordinary identifiers or to read oddly. `med`, `ge`, and `typ` are the
riskiest — a reviewer may want longer, less ambiguous words there even at the
cost of brevity (the Turkish team used the whole phrase `yineleDoğruKaldıkça`
for `while`, so long keywords are acceptable).
