const refs=load("directionRefs");store("directionRefs",[refs[0],refs[1],refs[2],refs[4],refs[5]]);
const prompts=load("directionPrompts");
for(const k of Object.keys(prompts))prompts[k]=prompts[k].replace(/4 approved standalone business basket:.*?\n/,"").replace("5 OLD level-2 background:","4 OLD level-2 background:").replace("6 and 7 are REJECTED NEGATIVE references:","5 is a REJECTED NEGATIVE reference:");
store("directionPrompts",prompts);
text(await tools.apply_patch("*** Begin Patch\n*** Delete File: D:/gxy_code/game/chicken_duck_test/artifacts/kitchen-background-direction-gate-20260927/prompts.json\n*** Add File: D:/gxy_code/game/chicken_duck_test/artifacts/kitchen-background-direction-gate-20260927/prompts.json\n+"+JSON.stringify({tool:"built-in image_gen",references:load("directionRefs"),prompts},null,2).split("\n").join("\n+")+"\n*** End Patch"));
