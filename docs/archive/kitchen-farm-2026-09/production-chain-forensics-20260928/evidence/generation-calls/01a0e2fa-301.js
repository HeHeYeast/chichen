const root="D:/gxy_code/game/chicken_duck_test/";
const refs=[
"artifacts/golden-business-r3/active-390x844.png",
"artifacts/journey-convergence/after/map-390.png",
"artifacts/golden-collection-v2/browser-390x844.png",
"artifacts/kitchen-asset-language-gate-20260927/references/basket.png",
"web/art/kitchen-stage-1-v7.png",
"artifacts/kitchen-concept-remaster-20260927/concepts/Kitchen-Concept-B-Lv2-390x844.png",
"artifacts/kitchen-visual-language-lv2-20260927/Lv2-Style-C-390x844.png"
]; store("directionRefs",refs.map(x=>root+x));
for(const p of refs)image((await tools.view_image({path:root+p})).image_url);
text(await tools.exec_command({cmd:"New-Item -ItemType Directory -Force artifacts/kitchen-background-direction-gate-20260927 | Out-Null; Get-Content artifacts/kitchen-remaster-lv2-20260927/visual-forensics-source.md | Select-Object -Last 57","max_output_tokens":4500}));
