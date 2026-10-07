// @exec: {"yield_time_ms": 120000, "max_output_tokens": 1200}
const r=await tools.image_gen__imagegen({prompt:load("commonKitchen")+"\n\n"+load("kitchenVariants").C,referenced_image_paths:load("kitchenRefs"),transparent_background:false});store("kitchenC",r);generatedImage(r);
