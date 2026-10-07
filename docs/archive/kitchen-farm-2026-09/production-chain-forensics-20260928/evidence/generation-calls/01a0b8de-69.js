for (const v of load("kitchenVariants")) {
 const r=load("kitchenOutput_"+v.key);
 text({variant:v.name, output_hint:r?.output_hint});
}
const prompt=`Use case: precise-object-edit.
Asset type: clean portrait background plate for the SAME existing 2D chicken game.
Image 1 is the ORIGINAL GAME background to edit. Image 2 is another ORIGINAL GAME background, ONLY a reference for drawing a very simple window. These original drawings are the authoritative style. Keep that plain flat sprite-era rendering, muted warm palette, simple uniform outlines and intentionally economical artwork. Do not modernize or polish the art.

Edit image 1 as follows:
1. Completely remove the big central straw nest and the partial wicker basket at the right. Reconstruct the table and wooden floor seamlessly behind them. The entire tabletop must now be absolutely empty. No replacement props.
2. The back wall occupies the top 30% of the whole portrait. Move the broad wooden tabletop rear edge to 30% of image height, and its front edge to 66%. Show the broad empty table from a slightly overhead angle. Below it are plain table legs and simple planked floor. Preserve the original wide frontal game camera, no narrow central vanishing point, no 3D view.
3. In the back-wall band between 10% and 28% of image height, replace the old small wall box with a small plain empty wooden shelf at left, and add a small rectangular four-pane window on the right. Window has only solid pale-blue panes, no landscape and no rendered lighting.
4. Make the wooden room modest and neatly repaired, slightly brighter than the original: wall a solid muted sand-brown, table a solid biscuit tan, dark muted brown joins and outline, lower floor a single muted brown. Keep the low visual density of the original, not dense modern cozy-game decoration.

The drawing medium is hard opaque FLAT COLOR SHAPES, each plane filled uniformly, with at most one simple hard-edged shadow patch where structurally necessary. Flat 2D game background, no spatial lighting simulation at all. All broad surfaces must be truly flat: no gradient, no texture, no smooth shading, no grain/noise, no ambient shadows, no sunlight, no specular shine, no edge glints, no vignette. Wood is identified by board seams and a maximum of four short simple grain lines in the entire image. Straightforward, slightly naive original game drawing, not premium concept art.
No nests, bowls, baskets, beds, cushions, birds, animals, eggs, people, UI, lettering, labels, logos or watermark. Single full-bleed portrait illustration, approximately 9:16. The whole central table is deliberately plain empty game space.`;
store("kitchenExtra",{key:"d",name:"D 原素材直接改绘",prompt});
text({extra:"Prepare fourth candidate directly from original artwork, preserving its flatter rendering."});
