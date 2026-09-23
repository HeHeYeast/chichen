// Small kitchen objects, drawn on the same 64px grid. No network or image decoding.
const pot='<path d="M15 31h34v13c0 7-8 11-17 11S15 51 15 44Z" fill="var(--skill-color)"/><path d="M11 32h42M15 37H9v8h7m33-8h6v8h-7"/>';
const book='<path d="M13 16h16l4 4 4-4h15v34H37l-4 4-4-4H13Z" fill="#fff9e7"/><path d="M33 21v29M19 26h8m-8 7h8m-8 7h8"/>';
const basket='<path d="M13 30h38l-5 23H18Z" fill="var(--skill-color)"/><path d="M21 29c0-22 22-22 22 0M10 30h44M23 37l2 10m7-10v10m9-10-2 10"/>';
const coin='<circle cx="41" cy="39" r="13" fill="#ffd05a"/><path d="M45 33c-9-6-14 9-4 12l5-1"/>';
export const SKILL_GLYPHS=Object.freeze({
 'CUL-1':'<path d="m9 43 7-5 10 5h10c5 0 5 6 0 7H22l-9 5" fill="#fff9e7"/><circle cx="36" cy="24" r="14" fill="#ffd05a"/><path d="M40 18c-11-5-13 12-3 12l4-2M14 14v8m-4-4h8m34 16v6m-3-3h6"/>',
 'CUL-2':pot+'<path d="M22 26c-10-11 7-13 7-21 12 11 17 13 10 21" fill="#ef9160"/><path d="M29 25c-3-4 2-6 4-9 4 5 5 7 2 10" fill="#ffd05a"/>',
 'CUL-3':'<path d="M20 18h25v8l5 6v19q0 5-5 5H20q-5 0-5-5V32l5-6Z" fill="#fff9e7"/><path d="M18 14h29v7H18Z" fill="var(--skill-color)"/><path d="M23 34h19v14H23Z" fill="#c7db9d"/><path d="M31 44V29m0 10c-9 0-10-9-10-9 9-1 10 9 10 9m0-4c0-9 11-11 11-11-1 9-11 11-11 11" fill="#83b883"/>',
 'CUL-4':'<path d="M8 32h37v8c0 10-31 14-37 0Z" fill="var(--skill-color)"/><path d="m44 33 13-5M14 24C9 10 39 4 46 20m-1-10 2 11-11-1"/><path d="M20 51c-4-6 3-7 4-12 8 6 10 10 4 15" fill="#ef9160"/>',
 'CUL-5':'<path d="M12 10h28l8 8v34H12Z" fill="#fff9e7"/><path d="M39 10v10h9M18 27h14m-14 7h10m-10 7h9"/><path d="M39 29h12l-2 13h-8Z" fill="var(--skill-color)"/><path d="M35 43h20v9H35Z" fill="#ef9160"/><path d="m20 17 2 3 5-5"/>',
 'CUL-S':'<path d="M18 34C1 34 5 13 20 17 22 3 41 4 43 17c17-3 21 18 3 18v19H18Z" fill="#fff9e7"/><path d="M18 43h28v11H18Z" fill="#ffd05a"/><path d="M25 30v8m14-8v8"/>',
 'HOME-1':'<path d="M10 44c0-29 44-29 44 0Z" fill="var(--skill-color)"/><path d="M7 45h50l-5 7H12ZM27 18c-3-10 13-10 10 0M18 36c1-6 4-9 8-11" fill="#fff9e7"/>',
 'HOME-2':'<path d="M8 40q24-18 48 0l-7 13H16Z" fill="#d9b276"/><path d="M13 42q19 9 39 0m-36 6q16 6 32 0"/><path d="M23 35c-14-1-2-26 4-25s15 27 2 26" fill="#fff9e7"/><path d="M37 36c-15-1-3-26 3-25s16 26 2 26" fill="#ffd05a"/>',
 'HOME-3':'<path d="M16 12h28v7H16Zm3 7h22v35H19Z" fill="#fff9e7"/><path d="M24 26h11m-11 7h11m-11 7h6"/><path d="m46 24-6 20-6 7 16 5-2-10 6-20Z" fill="var(--skill-color)"/><path d="m41 44 7 2M10 22v8m-4-4h8"/>',
 'HOME-4':'<path d="M8 37c0-23 38-23 38 0H8Zm-2 0h42M23 17v-5h6v5" fill="var(--skill-color)"/><circle cx="43" cy="44" r="14" fill="#fff9e7"/><path d="M43 34v11l7 3M43 29v-4m-4 0h8"/>',
 'HOME-5':pot+'<path d="M35 6c-10 12 0 22 12 18C32 39 16 18 35 6Z" fill="#ffd05a"/><path d="M49 9v8m-4-4h8M24 42q8 7 16 0"/>',
 'HOME-S':'<path d="m8 28 24-19 24 19M15 24v29h34V24" fill="var(--skill-color)"/><path d="m32 28 12 5v9c0 7-12 13-12 13S20 49 20 42v-9Z" fill="#fff9e7"/><path d="m26 40 4 4 8-9"/>',
 'TRADE-1':'<path d="m20 18-5-10h28l-5 10 12 16c12 23-44 27-41 6Z" fill="var(--skill-color)"/><path d="M20 18h18M22 12l4 6m8-6-3 6"/>'+coin,
 'TRADE-2':'<path d="M12 27h40v28H12Z" fill="#fff9e7"/><path d="m11 12-4 17q6 8 12 0 7 8 13 0 7 8 13 0 6 8 12 0l-4-17Z" fill="var(--skill-color)"/><path d="M23 55V38h17v17M17 12l2 17m13-17v17m15-17-2 17"/>',
 'TRADE-3':basket+'<path d="M21 29c-10-2-3-15 1-16 5 1 12 14 2 16m11 0c-8-1-1-14 3-15 6 1 11 14 2 15" fill="#fff9e7"/>',
 'TRADE-4':'<ellipse cx="32" cy="42" rx="26" ry="14" fill="#fff9e7"/><ellipse cx="32" cy="40" rx="19" ry="8" fill="var(--skill-color)"/><path d="M13 31c-2-18 16-18 13 0Zm23 0c-2-18 16-18 13 0ZM24 41c-3-20 20-20 17 0Z" fill="#ffd05a"/><path d="M24 13h6m9-3h6"/>',
 'TRADE-5':'<path d="M8 31h22v23H8Zm26 0h22v23H34ZM21 8h23v19H21Z" fill="var(--skill-color)"/><path d="M8 39h22M34 39h22M21 16h23m-15 7h8M15 47h8m18 0h8"/>',
 'TRADE-S':'<path d="M12 30h40v25H12Z" fill="#fff9e7"/><path d="m8 22 5 14h38l5-14Z" fill="var(--skill-color)"/><path d="m17 7 7 8 8-10 8 10 7-8-3 14H20Z" fill="#ffd05a"/><path d="M25 55V42h14v13"/>',
 'OBS-1':'<path d="m37 38 15 15" stroke-width="8"/><circle cx="27" cy="26" r="18" fill="#fff9e7"/><path d="M20 29c-5-4 1-13 5-14 7 3 14 20 4 22" fill="var(--skill-color)"/><path d="M17 19q2-5 7-6"/>',
 'OBS-2':'<path d="M12 35h34q-2 20-17 20T12 35Z" fill="var(--skill-color)"/><path d="M9 35h40m-5-25-9 20m4-20 7 3M18 14v8m-4-4h8"/><path d="M34 32c-5-3 1-16 6-13s-1 16-6 13Z" fill="#fff9e7"/><path d="M22 29c0-5 7-5 7-10 0-4-7-5-9-1m4 12v1"/>',
 'OBS-3':'<path d="M15 10h34v45H15Z" fill="#fff9e7"/><path d="M15 10h8v45h-8Z" fill="var(--skill-color)"/><path d="M10 18h10m-10 10h10m-10 10h10m-10 10h10M29 43h12"/><path d="M31 33c-7-3 0-17 4-17s13 17 4 19" fill="#ffd05a"/>',
 'OBS-4':book+'<path d="M40 23h7v14l-3-3-4 3Z" fill="var(--skill-color)"/><path d="m43 43 4 4 9-12"/>',
 'OBS-5':'<path d="M8 12h22v29H8Zm26 12h22v29H34Z" fill="#fff9e7"/><path d="M13 21h11m-11 7h8m26 6v9m-5-4h10M17 47c3 9 12 7 12 7m-4-4 5 4-5 4M36 14h13l-3-4m3 4-3 4"/><path d="M12 12h14v5H12Zm26 12h14v5H38Z" fill="var(--skill-color)"/>',
 'OBS-S':book+'<circle cx="42" cy="34" r="13" fill="var(--skill-color)"/><path d="m46 27-1 11-10 4 2-11Z" fill="#fff9e7"/>',
 'TRIP-1':'<path d="M11 55c29-13-8-19 10-33S47 12 43 7" stroke-width="6" stroke-dasharray="2 7"/>'+coin,
 'TRIP-2':basket+'<path d="M28 12h9v8h8L33 33 21 20h7Z" fill="#fff9e7"/>',
 'TRIP-3':'<path d="M9 49c6-10 12-11 20-4s18 10 28-2M30 43V25"/><path d="M30 31C8 35 10 13 10 13c20-1 22 18 20 18Zm0-7C26 6 50 6 50 6c2 19-20 18-20 18Z" fill="var(--skill-color)"/><path d="M45 33c-8 13-3 17 3 15s6-7-3-15Z" fill="#9fcdd7"/>',
 'TRIP-4':'<path d="m8 15 15-5 19 6 14-5v39l-14 5-19-6-15 5Z" fill="#fff9e7"/><path d="M23 10v39m19-33v39"/><path d="m14 40 4-9 14 9 4-13" stroke-dasharray="2 5"/><circle cx="44" cy="23" r="7" fill="var(--skill-color)"/><path d="m42 23 2 2 4-4"/>',
 'TRIP-5':'<path d="M19 44C5 23 36 5 53 9 57 32 32 52 19 44Z" fill="var(--skill-color)"/><path d="m13 54 32-35M25 39l-1-13m7 7 11-1M6 29h8m-8 7h8"/>',
 'TRIP-S':'<path d="M10 25h34v26H10Z" fill="var(--skill-color)"/><path d="M10 34h34M17 25v-8h19v8M23 34v9h8v-9"/><circle cx="45" cy="20" r="14" fill="#fff9e7"/><path d="m50 13-2 11-10 3 3-10Z" fill="#ffd05a"/>'
});
const palette={CUL:['#fae3ab','#edb05f'],HOME:['#e6edcf','#a9c987'],TRADE:['#f8dfc5','#efa378'],OBS:['#dfebdf','#98b9a0'],TRIP:['#e0ebdc','#a7c69a']};
export function skillIcon(id){
 const glyph=SKILL_GLYPHS[id];if(!glyph)return '';
 const [paper,color]=palette[id.split('-')[0]];
 return `<svg class="skill-icon" data-skill-icon="${id}" viewBox="0 0 64 64" aria-hidden="true" focusable="false" xmlns="http://www.w3.org/2000/svg" style="--skill-color:${color}"><circle cx="32" cy="32" r="31" fill="${paper}"/><g fill="none" stroke="#68472f" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">${glyph}</g>${id.endsWith('-S')?'<path d="m53 43 2 5 6 1-4 4 1 6-5-3-5 3 1-6-4-4 6-1Z" fill="#ffd05a" stroke="#68472f" stroke-width="1.8"/>':''}</svg>`;
}
