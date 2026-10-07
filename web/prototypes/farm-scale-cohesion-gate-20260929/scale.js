// Screen-space art calibration, not world metres or a Runtime camera model.
export const SCALE=Object.freeze({
 unit:26,
 viewport:[390,844],
 scene:[390,696],
 central:[86,277,220,210],
 buildingUnits:{house:6,shop:4.4,shrine:4.2,display:5},
 roadUnits:{main:1.4,link:1.15,entry:1},
 chickUnits:[.96,1,.92,.96,1.04],
});
export const px=units=>Math.round(SCALE.unit*units);
