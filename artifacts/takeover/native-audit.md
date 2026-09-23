# Native audit — 2026-09-23

Read-only source audit of the current schema 6 implementation. No APK installation, player save access, release signing, or production source edits. Temporary tests use synthetic fixtures only.

## Fresh checks

| Check | Result |
|---|---|
| Java SaveRepository / SaveBridge | 279 behavioral assertions passed |
| Java hatch notifications | 60 assertions passed (JVM platform fakes) |
| Android Java compilation | 8 sources compiled against Android 35, existing deprecation note only |
| Native generated registry drift | Passed; 241 frozen identities |
| Golden saves / fully populated schema 6 portable backup / asset availability | 19 tests passed |
| Python backup, update, snapshot and APK-byte verification simulations | 29 tests passed; isolated mocks, no device contacted |
| Current generated-assets/source manifest | 657 files matched bytes and hashes; content hash `0c9127e477457f5e7190d0bc1215d99f0fd28bdf383fe0bf6e1274ea2ef2309c` |
| Additional current Web→Native positive trajectories | 41/41 accepted by both |
| Additional corrupt schema 6 negative cases | **13/13 rejected by Web but incorrectly accepted by Native** |

Python's first two sandbox attempts failed solely on the temporary-directory ACL, including an attempt with a workspace temp directory. The approved rerun passed all 29 tests. The final `native-python-tests.txt` contains that successful run.

## Required correction

`SaveRepository.validateBusiness` (line 528 onward) checks only a subset of the frozen business, order and fact schemas. It accepts:

- a business record with `roles` removed, negative frozen price, forged window income, or invalid role cursor;
- a fact map with material ID 999 and negative count, an invalid order template counter, or an invalid predicate identity;
- an order with an invalid variant ID, replaced allowed list, negative paidCP, or invalid template progress;
- a project with payment 200.5 where exactly 200 is required (`getInt` truncation), or pinned identity `0:999`.

These are reproducible with `native-audit-fixtures.mjs` and `NativeAudit.java`; complete cross-platform results are in `native-audit-results.json`. The normal Web command path rejects these candidates before writing, so this audit does not demonstrate ordinary gameplay corruption. It does demonstrate that the claimed strict Native schema 6/corrupt-save gate is incomplete: direct native import/persistence can accept a state that the Web loader then refuses.

The correction should mirror current Web contracts, including exact keys, numeric types before coercion, stable allowed identities, frozen snapshot semantics, per-window amount/stock conservation, order variant/group/payment terms and bounded facts. Keep accepting versions 4 and 5 for the migration chain. Do not weaken Web validation to make the platforms agree.

## Migration / transactions / backup assessment

Current version is 6 and migration history is checked sequentially. The 279-assertion suite freshly verifies legacy reads, pre-upgrade byte preservation, future schema 7 blocking fallback and automatic overwrite, UTF-8 size limits, AtomicFile failure paths, revision conflicts, exact-candidate ACK retry, and notification failure after durable commit. The source separates durable ACK from notification side effects. Golden tests verify migration does not read ambient time/randomness or settle in-flight old tickets and retains old reward claims. Current actual regional, business, order, regular and project trajectories all passed the additional 41-state Native round-trip gate.

## Build and platform limits

JDK 17, Android 35 SDK/build-tools, Gradle 8.9 and local Gradle dependency cache are present. `android/app/build.gradle` attaches permanent signing enforcement only to `preReleaseBuild`; `assembleDebug` is a distinct usable compilation path with debug signing. Thus lack of release signing approval/device should not itself block producing an isolated debug APK. No permanent signing key/credentials were read during this audit. A debug package still must not be installed over a real player installation.

The existing `tools/build-android.ps1` production path explicitly loads permanent signing material and builds release. It should not be used merely for debug compile verification. A fresh APK's final byte consistency and Android lifecycle behavior remain unverified here; the 657-file check verifies generated asset bytes, not a new APK. Real device lifecycle, process death and restore are separate from JVM fakes.

## Audited hashes

- SaveRepository.java: `ae7c673e78bfa1bfbb770b61968e0bb40967854ffe73fde8208c2da588faec5d`
- web/save-migrations.js: `1e2c9cff2d1ea6e5d3bdd469cca2408a00ea565f3f65f85ac21cfbedab7a3594`
- web/business-save.js: `b973b9f4b2308916aebab58df583d3c7bc219591bf98e10ee17b1134981e07f1`
- web/orders.js: `ace1e636d96af5883f2ea44bbd57d91d4b7f1f63263067d5662ad4394f81a516`
- web/projects.js: `c46ee60ed3469a51adaacb75e32e839ce30b7c3fe6d64b9f2ff0b6d400cb7cb2`
