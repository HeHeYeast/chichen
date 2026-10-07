const baseResult=load("platformSetOutput_02-shabby");
const basePath=baseResult.output_hint.match(/as (C:\\.*?\.png) by default\./)?.[1];
if (!basePath) throw new Error("Missing base");
store("platformSetBasePath",basePath);
const r=await tools.view_image({path:basePath});image(r.image_url);
const common=load("platformSetCommon");
const start=common.indexOf("LOCKED COMPOSITION");
const end=common.indexOf("Rendering:");
const locked=`LOCKED COMPOSITION: reproduce the EXACT geometry and horizontal boundaries in reference 1, the approved working base of a shabby wood wall plus empty platform. Keep the wall/platform seam, platform front edge, crop, camera, perspective, total empty surface and relative area of the three main planes exactly unchanged. Do not attempt a new room layout. All four stage studies must align when overlaid.
The TOP area is the wall, the MIDDLE broad empty area is the platform top, and the BOTTOM area is the supporting front face. No room floor, no legs, no shelves, no extra furniture, no window. Only these three joined planes. Material changes do not move boundaries or add shapes on top of the playable surface.
Change BOTH wall and platform to the tier specified below. Replace or remove reference1's shabby patch/scuffs as appropriate for the requested tier. The decorative/material lines inside the wall and platform may change, but their shared border and platform perimeter must stay fixed. Keep the center of the platform clear and visually quiet for game sprites; avoid texture noise.
Reference 1 is the shabby base for geometry. Reference 2 is the original B warm cartoon style. The generated luxury stage must share the SAME simplicity and drawing medium as the other stages.
`;
store("platformSetDerivedCommon",common.slice(0,start)+locked+"\n"+common.slice(end));
text({baseReady:true,remaining:3});
