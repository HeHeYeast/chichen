// Original, code-drawn vector studies. No source raster is modified.
// Reproducible placeholders, not approved final character illustrations.
import {mkdir,writeFile} from 'node:fs/promises';
import {REGIONAL,resolveSpecies,CONTENT_TEXT} from '../web/content-registry.js';
const root='web/art/polish',inventory=[];
const ink='#684d36',pal={V:['#e8c776','#8ca366'],R:['#ebcd7d','#7caba6'],T:['#a8b67b','#c79c58'],B:['#b8d1cf','#779b9e']};
const svg=(body,w=128,h=128)=>`<svg xmlns="http://www.w3.org/2000/svg" width="${w}" height="${h}" viewBox="0 0 ${w} ${h}"><g stroke="${ink}" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">${body}</g></svg>`;
async function put(category,id,body,w=128,h=128,status='vector-placeholder'){
 await mkdir(`${root}/${category}`,{recursive:true});const path=`/${root}/${category}/${id}.svg`;
 await writeFile('.'+path,svg(body,w,h));inventory.push({category,id,path,width:w,height:h,status,source:'Original code-drawn SVG; tools/build-polish-art.mjs'});return path;
}
const leaf='<path d="M61 90Q24 73 37 39Q68 38 61 90M63 84Q59 46 93 30Q109 71 63 84" fill="#91ae68"/><path d="m49 59 13 39 22-48" fill="none"/>';
const grain='<path d="M60 103V30" fill="none"/><path d="M60 77Q33 74 36 58Q59 58 60 77M61 64Q87 63 87 46Q64 44 61 64M60 49Q36 44 42 29Q61 32 60 49M61 33Q62 13 73 15Q85 26 61 33" fill="#e7bb5e"/>';
const fruit='<path d="M62 40Q69 21 91 26Q85 44 62 40" fill="#97ae65"/><path d="M65 39C13 22 16 101 61 102C103 112 116 30 65 39Z" fill="#edcf6c"/><path d="M39 52q-9 11-7 25" fill="none" stroke="#fff4cf" stroke-width="6"/>';
const celery='<path d="m53 105-9-56m20 56 4-61m3 63 17-57" fill="none" stroke="#8da566" stroke-width="9"/><path d="M44 60Q16 49 31 28Q53 19 54 43Q64 22 83 30Q109 50 81 59Q59 70 44 60" fill="#9ab878"/>';
const tea='<path d="M39 93Q28 53 83 25Q111 73 39 93Z" fill="#8d9c63"/><path d="m40 94 41-59m-25 42-5-18m13 9 17-4" fill="none"/><path d="M78 99q22-6 22-26q-25 2-22 26" fill="#b6bd83"/>';
const flower='<path d="M42 101q15-23 44-46" fill="none" stroke="#90a474"/><g fill="#efd076"><path d="M65 49C29 42 34 13 55 27C65 3 90 20 76 38C112 25 116 58 86 60C106 88 75 99 68 71C41 93 25 63 65 49Z"/></g><circle cx="70" cy="52" r="9" fill="#c99848"/>';
const salt='<path d="m22 87 16-27 22 29-9 15H28Zm33 5 19-57 26 31 4 37H61Z" fill="#eef7ed"/><path d="m74 35 2 62 24-31M22 87l38 2" fill="none" stroke="#91a7a2"/><path d="m35 30 5-10m4 23 11-2m43 4 7-7" fill="none" stroke="#d2aa5a"/>';
const samphire='<path d="M62 107V30m-1 34L40 44m23 39 24-21M43 80 31 61m42-13 13-18" fill="none" stroke="#82a080" stroke-width="10"/>';
const items=[leaf,grain,fruit,celery,tea,flower,salt,samphire];
for(let i=0;i<8;i++)await put('materials',`material-${75+i}`,`<ellipse cx="64" cy="107" rx="43" ry="8" fill="#80603c" opacity=".12" stroke="none"/>${items[i]}`,128,128,'vector-icon');
const shapes=[
 '<ellipse cx="64" cy="73" rx="43" ry="29"/>',
 '<path d="m24 57 66-15 16 39-68 18Z"/><path d="m25 66 71-15m-64 28 68-16" fill="none"/>',
 '<path d="M23 91Q25 37 64 36Q100 35 106 91Z"/><ellipse cx="65" cy="46" rx="12" ry="5" fill="#988557"/>',
 '<path d="M22 81Q24 44 43 47Q59 29 74 44Q101 37 105 77Q95 103 69 93Q45 108 22 81Z"/><path d="m42 55 20 26m9-33 15 33" fill="none"/>',
 '<path d="M64 101 17 58Q53 21 108 51Z"/><path d="m64 96-29-43m29 43 4-54m-4 54 28-46" fill="none"/>',
 '<path d="M29 89Q18 59 38 42Q34 17 51 33Q64 9 70 34Q96 13 90 42Q112 71 98 91Q61 110 29 89Z"/>',
 '<path d="M22 84V52Q31 33 54 47L102 71V95Z"/><ellipse cx="100" cy="82" rx="13" ry="16" fill="#fff0bb"/>',
 '<path d="m26 46 58-10 19 14v43l-63 9-14-11Z"/><path d="m28 65 72-3m-69 18 69-3" fill="none"/>',
 '<path d="M27 82Q6 50 36 41Q53 31 64 60Q69 34 95 43Q120 62 97 83Q80 100 64 76Q48 105 27 82Z"/>',
 '<path d="M28 43h64l12 13v40H28Z"/><path d="m28 59 76-3M88 44v49" fill="none"/>',
 '<path d="M28 93Q14 83 29 64L56 32Q65 23 76 37L104 83Q113 102 86 102Z"/>',
 '<path d="M22 81Q15 49 45 42Q38 27 57 33Q80 26 92 45Q125 62 100 89Q62 112 22 81Z"/>'
];
for(const r of REGIONAL.recipes){
 const s=resolveSpecies(r.key),n=Number(s.authorId.slice(-1))-1+(s.egg?6:0),[body,accent]=pal[s.region];
 const motif=items['VRTB'.indexOf(s.region)*2+(n%2)];
 await put('species',`species-${s.authorId}`,`<ellipse cx="64" cy="111" rx="33" ry="6" fill="#7e6342" opacity=".15" stroke="none"/><path d="m46 99-6 9h13m27-9 6 9H73" fill="none" stroke="#be883f"/><g fill="${body}">${shapes[n]}</g><g transform="translate(41 -4) scale(.43)">${motif}</g><path d="M32 78q9 13 19 0" fill="${accent}"/><circle cx="54" cy="70" r="3" fill="${ink}" stroke="none"/><circle cx="78" cy="70" r="3" fill="${ink}" stroke="none"/>${s.egg?'<path d="M57 79q8-7 18 0q-9 10-18 0Z" fill="#d89548"/>':'<path d="m61 77 9 0-5 8Z" fill="#d89548"/>'}<ellipse cx="45" cy="80" rx="5" ry="3" fill="#d99a7a" stroke="none"/><ellipse cx="87" cy="80" rx="5" ry="3" fill="#d99a7a" stroke="none"/>`);
}
for(const c of REGIONAL.cards){
 const [a,b]=pal[c.region],motif=c.type==='specimen'?items[c.material-75]:c.type==='lore'?'<path d="M24 40q20-12 40 0q20-12 40 0v58q-20-12-40 0q-20-12-40 0Z" fill="#fff4d4"/><path d="M64 42v54M35 54h18m-18 12h18m22-12h18m-18 12h18" fill="none"/>':'<path d="M29 94V46h69v48" fill="#fff4d4"/><path d="m21 45 12-22h61l13 22Z" fill="#e7b97f"/><path d="M37 45v10m17-10v10m17-10v10m17-10v10M47 94V72h30v22" fill="none"/>';
 await put('discoveries',`discovery-${c.id}`,`<circle cx="64" cy="65" r="54" fill="${a}" stroke="${b}" stroke-dasharray="3 5"/>${motif}<path d="m101 11 2 8 8 2-8 3-2 8-3-8-8-3 8-2Z" fill="#f0c869" stroke-width="2"/>`);
}
for(const [i,m]of REGIONAL.mementos.entries()){
 const shapes=['<path d="M31 28h61v76H31Z" fill="#f6e6bf"/><path d="M43 22h33v16H43Z" fill="#a6b69e"/><path d="M43 60h38M43 73h28" fill="none"/>','<path d="m24 42 68-10 14 64-69 10Z" fill="#d6c699"/><path d="m31 55 62 29m-42-45 17 61" fill="none"/>','<ellipse cx="64" cy="79" rx="46" ry="23" fill="#dfbd7e"/><path d="M19 71q45 26 90 0m-75-10 56 27m-45-34 55 27" fill="none"/>','<path d="M31 41h63v56H31Z" fill="#a9bca3"/><ellipse cx="63" cy="39" rx="33" ry="12" fill="#dfca98"/><path d="M45 63h33v20H45Z" fill="#fff1c9"/>'];
 await put('mementos',`memento-${m.id}`,shapes[i%4]+`<circle cx="88" cy="98" r="15" fill="${pal['VRTB'[i%4]][0]}"/><path d="m81 97 6 6 9-12" fill="none"/>`);
}
for(const id of ['V','R','T','B']){
 const [a,b]=pal[id];
 const foreground=id==='B'?'<path d="M0 116q65-16 130 0t130 0t140 0v44H0Z" fill="#92b7b4"/><path d="M0 135q65-16 130 0t130 0t140 0" fill="none" stroke="#e7f0db"/>':id==='T'?'<path d="m0 130 400-50m-400 70 400-50m-355 60 355-40" fill="none" stroke="#739579" stroke-width="12"/>':id==='R'?'<path d="M255 77q-100 22-49 40t-20 43" fill="none" stroke="#a9cfcd" stroke-width="27"/>':'<path d="M0 135q100-45 220 1t180-2v26H0Z" fill="#bbbd7d"/><path d="m28 137 4-26m-4 20-8-8m72 12 4-21" fill="none"/>';
 await put('regions',`region-${id}`,`<path d="M0 0h400v160H0Z" fill="#f6e9c7" stroke="none"/><circle cx="330" cy="35" r="21" fill="${a}" stroke="none"/><path d="M0 110 85 32l68 60 58-49 90 64 65-41 34 30v64H0Z" fill="${b}" stroke="none" opacity=".55"/><path d="M0 126 82 97l76 36 102-28 140 26v29H0Z" fill="${b}" stroke="none"/>${foreground}<path d="m44 107 0-42m-16 0h38l8 10-8 10H28Z" fill="#e1bd81" stroke-width="2"/>`,400,160);
}
await put('ui','unknown', '<path d="M32 98C15 71 43 20 64 17C85 20 113 71 96 98Q64 120 32 98Z" fill="#b4b29a" stroke="#898770"/><path d="M54 51c1-15 27-15 25 0-1 9-15 9-15 21" fill="none" stroke="#fff4d6" stroke-width="6"/><circle cx="64" cy="85" r="3" fill="#fff4d6" stroke="none"/>',128,128,'vector-icon');
await writeFile(`${root}/inventory.json`,JSON.stringify({version:1,generatedBy:'tools/build-polish-art.mjs',assets:inventory},null,2)+'\n');
console.log(`Wrote ${inventory.length} SVG assets and inventory.`);
