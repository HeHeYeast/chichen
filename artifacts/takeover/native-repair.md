# Native schema 6 repair

Date: 2026-09-23. Scope: Android persistence validation, generated Native content contracts, shared interoperability fixtures and isolated debug build. No Web gameplay/schema/source edits, installation, player-save access or permanent signing key use.

## Repaired contracts

- Added `BusinessSaveValidator.java`, a read-only validator corresponding to `web/business-save.js` and `web/orders.js`. It checks exact record fields, safe integer/Boolean types, frozen stock/price/role/semantic snapshots, role rotation and menu fit for all eight menus, each window's stock and CP conservation, bonuses/credits, close boundaries, report witnesses, and all bounded fact identities/values/sources.
- Order validation reconstructs frozen groups from the generated template/variant/region/season definitions, checks exact ordered allowed lists and quantities, validates delivered base-price totals, proposal/template progress and reservations against outstanding need. T/R/S/Q remains derived from owner records; no second inventory ledger was added.
- `SaveRepository` now delegates the complete business/order/fact contract to that validator and retains the cross-owner inventory check. Removed the former partial business validator instead of maintaining two divergent interpretations.
- Project payments require exact numeric integers before conversion; completed stages require Boolean true; pinned kinds must be unique, permitted and attached to the correct consuming stage. A legitimate regular activation baseline above 32-bit integer range is accepted up to the same safe-integer bound as Web.
- Non-import automatic writes cannot reduce schema version even with an otherwise valid next revision. Explicit old-backup import remains supported. This prevents an older bridge candidate from discarding schema 6 progress.
- Native registry generation includes base sale prices used by frozen order validation and the new fixed predicate `RG3-2:regionalBatch`. It still uses the author-built runtime registry; no manual duplicate allowed lists were introduced.

## Fresh verification

| Check | Result |
|---|---|
| Production SaveRepository/SaveBridge + AOSP JSON + real isolated files | **649 assertions passed**, freshly rerun for the final build (`native-save-tests-final.txt`) |
| Shared malformed-state corpus | **107** Web-rejected states also rejected by Native; every attempt retains the previous durable raw bytes |
| Original independent audit | All **13** originally accepted bad states now rejected; **42** legal trajectories pass both validators (original 41 plus complete Q loss); **0 mismatches** in `native-audit-results-fixed.json` |
| Legal menu/order contract matrix | **103** states covering all eight menus at open/2h/manual close/24h, every order variant and partial delivery, plus a regular baseline beyond 32-bit range |
| Golden Native compatibility | Six golden raw sources accepted, all six Web schema 6 migrations byte-round-tripped, all six pre-upgrade envelopes preserved exactly |
| Migration / ACK | Real empty/v3/v4/v5 first and second commands; schema 5 live business/trip/batch migration, exact-candidate ACK replay, and automatic downgrade rejection |
| Completely lost Q reservation | Actual `farmLossAt` inside `execute` removes the zero hold, marks restock, preserves CP and produces a Native-readable candidate |
| Notifications | 60 JVM assertions passed in the final rerun (`native-notification-tests-final.txt`) |
| Android API compilation | 9 production Java sources compiled against Android 35 in the final rerun (`native-java-compile-final.txt`; existing deprecation note only) |
| Golden/backup/asset Node checks | 19 passed |
| Native generated content | `--check` passed |

Permanent regressions are in `android/tests/save-negative-cases.mjs`, `save-contract-matrix.mjs`, `build-save-fixtures.mjs`, and `saves/com/jibao/kitchen/SaveRepositoryTest.java`, invoked by the normal `run-save-tests.ps1` gate. The previous audit report is retained as historical evidence, not the current pass/fail result.

## Debug build

After the root agent confirmed stable Web sources including the cross-second business stock-input fix and final legacy-navigation wording corrections in `app.js` and `settings-ui.js`, the final isolated debug APK compiled successfully offline using Gradle 8.9/JDK 17/Android 35. It uses the **30-day debug-only certificate** under `artifacts/takeover`; no release signing material was read. The Gradle log reports `BUILD SUCCESSFUL`; unchanged Java compilation outputs were reused and changed assets were rebuilt. APK v2 signature verification passed, and all **662** packaged runtime files match current source bytes and their manifest hashes.

- **Final deliverable:** `artifacts/takeover/chick-kitchen-241-debug-c1e5d00d61ea.apk` (53,623,356 bytes).
- APK SHA-256: `c1e5d00d61ea690f5545883ecba8ea3b13db313c4fb9465ebfa7090ded3a2dbe`.
- Runtime content SHA-256: `0fbce8929a674fad2f1294e198e17451ff2f5a3825c1b8c9c452b7bb9dfa9bc6`.
- Runtime manifest SHA-256: `9dd863a6e19c09e422464221ba19d9b45f77ec420e04cea57bd5af54dd3e007f`.
- Debug certificate SHA-256: `3924e188c4b7427bbce6611271e5a23789f7c7c4937ab769b7ec6aa5a85291d2`.
- Final evidence: `native-final-evidence.json`, `native-runtime-manifest-final.json`, `native-debug-build-final.txt`, `native-apk-signature-final.txt`, `native-apk-bytes-final.txt`, and `native-apk-manifest-final.txt`.

An earlier build correctly failed source-byte comparison while `web/app.js` was still changing. The subsequently sealed `9b2fd40148e7` and `e91aba27f70f` debug packages are retained as **superseded**: they predate the stock-input or navigation-wording corrections. Only `c1e5d00d61ea` is the current deliverable. `native-final-evidence.json` lists superseded packages explicitly. This is a debug package with application ID `com.jibao.kitchen`, version 1.5.0 (16); it is not a release-signed update. No APK was installed. Actual Android lifecycle/process death/file picker restore still requires a test device and is not represented by these JVM or build checks.

## Source traceability

`native-fix-source-hashes.json` records SHA-256 of all 10 changed production/generator/test/documentation files. `native-final-evidence.json` additionally records all 9 production Java source hashes and the verified APK/resource identity. The repaired production files are `SaveRepository.java`, `BusinessSaveValidator.java` and generated `RuntimeContent.java`. No save version or content identity changed in this repair.
