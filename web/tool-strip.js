// Transient input only: never stored in the player's save.
export const TOOL_DRAG_THRESHOLD=8;
export const clampToolScroll=(value,max)=>Math.max(0,Math.min(max,value));
export const toolScrollFor=(id,max)=>clampToolScroll(id-3,max);
export function beginToolDrag(point,scroll){
  return {start:point,origin:scroll,position:scroll,drag:false,cancelled:false};
}
export function moveToolDrag(gesture,point,stride,max){
  const dx=point.x-gesture.start.x,dy=point.y-gesture.start.y;
  if(!gesture.drag&&!gesture.cancelled&&Math.hypot(dx,dy)>TOOL_DRAG_THRESHOLD){
    if(Math.abs(dx)>Math.abs(dy))gesture.drag=true;
    else gesture.cancelled=true;
  }
  if(gesture.drag)gesture.position=clampToolScroll(gesture.origin-dx/stride,max);
  return gesture;
}
export const settleToolDrag=gesture=>Math.round(gesture.position);
