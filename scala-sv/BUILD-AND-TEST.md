# Swedish Kojo — build and test it yourself

A guide for the Swedish contributors picking up `scala-sv/`. It takes you from a
fresh clone to a running Kojo where `värde x = 5` compiles and highlights, and
then to changing the Swedish words and seeing your changes live.

You do **not** need to publish anything to GitHub to try out your own word
choices — see Part B.

---

## 0. Where the code lives right now

The keyword-localization machinery (called "Level 4" in `../localization.md`) is
**not yet in** [litan/kojo](https://github.com/litan/kojo) master. It is under
review in three pull requests, plus a fourth that adds the generalization and
Swedish. Until those land, build from the branch that already has all of it:

    repo:   https://github.com/bulent2k2/kojo
    branch: test-level4-upstream

That branch **is** Lalit's master plus exactly those pending PRs — nothing else.
When they merge, this same content moves onto upstream master and you can build
from litan/kojo directly, with no change to the instructions below.

## 1. Prerequisites

| need | why |
|---|---|
| **Java 11** (or 8) | Kojo's build and its forked JVMs pass `-XX:+UseConcMarkSweepGC`, a GC removed in Java 14. On Java 17+ the build fails to start. Java 11 is the smoothest. |
| `scala` 2.13 on your PATH | `makezip.sh` runs a small Scala script to stage jars. |
| `git`, `zip`, `unzip`, `curl` | ordinary build tools |

No separate sbt install needed — the repo ships `sbt.sh` and a launcher jar.

Check: `java -version` should say 11 (or 1.8), and `scala -version` should work.

---

## Part A — Run Swedish Kojo (no compiler building)

The Swedish compiler and formatter are already published, and Kojo downloads
them on demand. This part is just a normal Kojo build.

```bash
git clone https://github.com/bulent2k2/kojo.git kojo-sv
cd kojo-sv
git switch test-level4-upstream

./makezip.sh          # builds Kojo and stages the package into ./Kojo-z
```

On a headless Linux box some Swing tests need a display; use
`xvfb-run -a ./makezip.sh`.

Then run it:

```bash
./Kojo-z/bin/kojo
```

In Kojo: **Settings → Language → Svenska**, then **restart Kojo**. On the next
start you will see it fetch the Swedish toolchain once:

    [INFO] Fetching the 'sv' Scala toolchain 2.13.18 from https://github.com/...
    [INFO] 'sv' Scala toolchain ready in <installation>/toolchains/scala-sv/2.13.18
    [INFO] Scala toolchain: <installation>/toolchains/scala-sv/2.13.18 (user language 'sv', ...)

Now this should compile, run, and show `värde`/`variabel` in keyword colour:

```scala
värde x = 5
variabel y = 0
om (x > 1) y = x annars y = 0
println(s"värde: $y")
```

**Why "restart" and why the menu rather than a flag.** Selecting the language
writes a preference. On the next launch the *launcher* JVM reads it to pick the
toolchain, and the *Kojo* JVM reads it to switch keyword highlighting and
completion. `-Dkojo.toolchain=sv` forces only the first of those two, so use the
menu — it is the path real users take anyway.

---

## Part B — Change the Swedish words

This is the actual contribution. The words currently in the patch are
AI-suggested drafts (see `KEYWORDS.md`); replacing them with good Swedish is the
job.

### B1. Four places must agree

A keyword exists in four files. Change a word in all four, or you get a compiler
that accepts a word the editor does not colour, or vice versa:

| file | what it controls |
|---|---|
| `scala-sv/swedish-keywords.patch` | the **compiler** — the `kw("...")` strings in the `StdNames.scala` hunk |
| `scala-sv/scalariform-swedish.patch` | **formatting + highlighting** — the `"ord" -> TOKEN` entries |
| `src/main/scala/net/kogics/kojo/lite/i18n/sv/package.scala` | `keywordList`, used for highlighting and code completion |
| `scala-sv/KEYWORDS.md` | the human-facing table (keep it honest) |

Two rules when picking words:

1. **A keyword can no longer be used as a variable or method name.** Once `ny`
   means `new`, no Swedish Kojo program may call anything `ny`. The Turkish team
   hit this and had to rename things. `med`, `ge`, `typ`, `fall`, `ny`, `lat` are
   the riskiest — `KEYWORDS.md` flags them.
2. **Prefer the word a child reads as the concept**, not a dictionary gloss.
   Long is fine: Turkish uses `yineleDoğruKaldıkça` for `while`.

### B2. Rebuild the compiler

```bash
git clone https://github.com/scala/scala ~/src/scala-sv
cd ~/src/scala-sv
git checkout v2.13.18
git apply ~/kojo-sv/scala-sv/swedish-keywords.patch      # your edited patch
sbt 'set Global/baseVersionSuffix := ""' dist/mkPack     # ~3 minutes
# -> build/pack/lib/{scala-library,scala-reflect,scala-compiler}.jar
```

`baseVersionSuffix := ""` gives the jars a clean `version.number=2.13.18`.
Without it they carry a meaningless build-id suffix.

### B3. Rebuild scalariform

```bash
git clone https://github.com/bulent2k2/scalariform ~/src/scalariform-sv
cd ~/src/scalariform-sv
git apply ~/kojo-sv/scala-sv/scalariform-swedish.patch   # your edited patch
sbt packageBin
# -> scalariform/target/scala-2.13/scalariform_2.13-0.2.10.jar
```

> **The jar must know English + Swedish only.** The patch *replaces* the Turkish
> keyword block in that fork with the Swedish one — it does not add Swedish
> alongside Turkish. scalariform classifies keywords itself and Kojo trusts that
> verdict, so a jar that also knew Turkish would colour `dez`/`den` in Swedish
> mode. Check it:
>
>     unzip -p scalariform.jar 'scalariform/lexer/Keywords$.class' | strings | grep -E '^(dez|nesne)$'   # nothing
>     unzip -p scalariform.jar 'scalariform/lexer/Keywords$.class' | strings | grep -E '^(värde|klass)$' # värde, klass
>
> Use `packageBin`, not `assembly` — `assembly` produces a fat jar with a bundled
> scala-library. Sanity check: `unzip -l scalariform.jar | grep -c scala/collection/`
> should print `0`.

### B4. Try your jars **without publishing anything**

Put the four jars in one directory with a checksum file, and point Kojo at it:

```bash
mkdir -p ~/sv-toolchain && cd ~/sv-toolchain
cp ~/src/scala-sv/build/pack/lib/scala-{library,reflect,compiler}.jar .
cp ~/src/scalariform-sv/scalariform/target/scala-2.13/scalariform_2.13-0.2.10.jar scalariform.jar
shasum -a 256 scala-library.jar scala-reflect.jar scala-compiler.jar scalariform.jar > SHA256SUMS

# A valid local set is verified offline and reused, not compared with the remote
# build. Remove this installation's version directory before testing rebuilt jars:
rm -rf ~/kojo-sv/Kojo-z/toolchains/scala-sv/2.13.18

_JAVA_OPTIONS="-Dkojo.toolchain.url=file://$HOME/sv-toolchain" ~/kojo-sv/Kojo-z/bin/kojo
```

`-Dkojo.toolchain.url` overrides the download location, so this is a complete
edit → rebuild → try loop with no GitHub release involved. **Clear the cache
directory every time**, otherwise Kojo keeps using the jars it already has.

All four jars plus `SHA256SUMS` must be present, or Kojo refuses the whole
toolchain and retains stock Scala with an explicit localized-keywords-unavailable diagnostic.

### B5. Also update the Kojo-side keyword list

Edit `src/main/scala/net/kogics/kojo/lite/i18n/sv/package.scala` so `keywordList`
matches your words, then rebuild Kojo (`./makezip.sh`). This drives code
completion and the language gate.

---

## Part C — Run the test suite

```bash
cd ~/kojo-sv
./sbt.sh test                       # xvfb-run -a ./sbt.sh test on headless Linux
```

The tests that matter for this work:

- `KeywordLangsTest` — the language registry
- `ScalariformTokenMakerTest` — a localized keyword is a reserved word only when
  its language is active
- `ScalaToolchainTest`, `ScalaToolchainFetcherTest` — toolchain selection and the
  on-demand fetch (driven from a local directory, no network)

## Part D — When the words are settled

Tell Bulent (`ben@scala.org`) and he will publish a new
`v2.13.18-sv` release of the four jars, so every Swedish user gets them
automatically. Send the two edited patches; the release is built from those.
`ReleaseNotes-sv.md` in the release describes the format.

---

## What is *not* done yet

Keyword localization is only the deepest of four levels (see `../localization.md`).
A full Swedish edition of Kojo also wants:

- **Level 1** — the UI strings (menus, dialogs, messages)
- **Level 2** — a Swedish turtle/drawing API (`fram`, `höger`, … instead of
  `forward`, `right`), the layer children actually type
- **Level 3** — Swedish sample programs and lessons

Those are larger, and none of them need the patched compiler. The Turkish
edition (Koco) did Levels 1–3 first and Level 4 last; that is probably the right
order for Swedish too. `i18n/sv/package.scala` deliberately leaves the code
template maps empty until then.

## Gotchas, collected

- **The `sbt.sh` wrapper requires Java 8** (its CMS flag); `build.sbt` itself
  selects CMS/test flags conditionally for newer JDKs.
- **Local toolchains are verified offline on reuse.** Remove this installation's
  `toolchains/scala-sv/2.13.18` directory after rebuilding remote jars, so the new
  build is fetched. Missing/corrupt local sets are rejected; replace or remove
  them before retrying. See [installation setup](../localization.md#level-4).
- **`scalariform.jar` must be English + your language only** — see B3.
- **Language changes need a Kojo restart** — the toolchain is chosen by the
  launcher before the app starts.
- **Nothing is fetched for English users.** The download happens only when a
  keyword language is actually selected, and a failure is never fatal: Kojo logs
  a warning and falls back to stock Scala.
