// @exec: {"yield_time_ms": 120000, "max_output_tokens": 500}
const stage=load("platformSetStages")[1];
const prompt=load("platformSetCommon")+"\n"+stage.direction;
store("platformSetPrompt_"+stage.key,prompt);
const result=await tools.image_gen__imagegen({prompt,referenced_image_paths:load("platformSetRefs")});
store("platformSetOutput_"+stage.key,result);
text({stage:stage.name,output_hint:result.output_hint});
generatedImage(result);
