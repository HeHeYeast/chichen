import {mkdir,readFile,writeFile} from 'node:fs/promises';
const base=new URL('./',import.meta.url),old=new URL('../farm-modular-asset-gate-20260928/',base);
await mkdir(new URL('assets/',base),{recursive:true});
const source=JSON.parse(await readFile(new URL('asset-manifest.json',old),'utf8'));
const kit={};
for(const id of ['house','shop','shrine','display'])kit[id]={...source[id],file:'../farm-modular-asset-gate-20260928/'+source[id].file};
async function asset(id,body,w=120,h=120,stroke=3.6){
 const text=`<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${w} ${h}" fill="none" stroke="#563c29" stroke-width="${stroke}" stroke-linejoin="round" stroke-linecap="round">${body}</svg>`;
 await writeFile(new URL(`assets/${id}.svg`,base),text);
 kit[id]={file:`assets/${id}.svg`,width:w,height:h,frame:[0,0,w,h],kind:'environment',source:'native SVG redrawn for scale cohesion',stroke};
}
await asset('tree-round',`
 <path d="M49 78 72 78 72 108Q62 117 49 109Z" fill="#c8904f"/><path d="m63 81 9-3v30q-5 5-10 5Z" fill="#97633a" stroke="none"/>
 <path d="M22 80C5 78 6 63 12 56 2 39 18 25 31 28 27 11 43 4 57 13 69 0 91 8 91 25 112 23 120 42 109 56 119 74 105 89 89 84 79 99 61 97 53 91 38 99 23 91 22 80Z" fill="#62a153"/>
 <path d="M109 56C101 67 91 68 83 65 77 80 62 77 54 73 44 82 27 82 20 72 13 74 10 70 10 67 10 77 14 79 22 80 23 91 38 99 53 91 61 97 79 99 89 84 105 89 119 74 109 56Z" fill="#3f7847" stroke="none"/>
 <path d="M31 28C30 16 42 10 53 16 45 23 47 31 38 35 27 32 23 40 16 43 16 31 24 27 31 28ZM62 18C72 10 85 15 85 26 75 25 73 34 64 31Z" fill="#8eb967" stroke="none"/>
 <path d="M26 54q5-5 10-2m23-7q7-5 12 0m9 32 7-2" stroke="#427c48" stroke-width="2.5"/>
 `);
await asset('tree-pine',`
 <path d="m51 86 20-1v24l-11 7-10-6Z" fill="#bb844b"/><path d="m62 87 9-2v24l-11 7Z" fill="#8d603a" stroke="none"/>
 <path d="M60 7Q53 23 39 34L44 38Q34 51 25 57L30 62Q21 75 12 81L19 88 31 87 30 93 49 94 59 99 70 94 89 95 89 88 103 87 109 81Q93 69 88 60L95 57Q80 47 77 37L83 34Q67 22 60 7Z" fill="#4e9151"/>
 <path d="M60 7Q69 23 83 34L77 37Q80 47 95 57L88 60Q93 69 109 81L103 87 89 88 89 95 70 94 59 99 49 94 30 93 31 87 43 83Q67 90 73 80L66 61 57 66 64 44 54 49 65 32Z" fill="#34704a" stroke="none"/>
 <path d="M59 14 47 34 51 37 35 54 44 54 50 49 48 43 63 30ZM37 68l-12 13 11 2 7-4Z" fill="#7bad60" stroke="none"/>
 <path d="m47 63-5 4m35 9 6 4" stroke="#366d44" stroke-width="2.5"/>
 `);
await asset('bush-low',`
 <path d="M10 45C3 33 13 22 26 25 28 10 47 7 57 19 70 8 90 15 91 29 110 25 118 42 109 54 94 63 75 60 61 64 44 60 22 65 10 53Z" fill="#7ba957"/>
 <path d="M10 45Q29 54 44 47 59 54 75 48 96 51 111 41L109 54Q92 65 61 64 43 60 22 65 10 53Z" fill="#508449" stroke="none"/>
 <path d="M29 27Q36 15 48 22M65 24q9-6 15 2" stroke="#a3c674" stroke-width="6"/>
 `,120,74,4.8);
await asset('bush-tall',`
 <path d="M17 89C2 78 9 63 23 60 10 44 20 28 37 29 36 11 58 7 68 21 84 11 100 26 94 40 111 45 117 62 103 74 116 97 94 108 79 100 61 113 37 101 35 95 26 100 18 97 17 89Z" fill="#5f984d"/>
 <path d="M103 74Q88 86 78 76 66 92 51 83 30 90 19 76L17 89Q18 97 35 95 37 101 61 113 79 100 94 108 116 97 103 74Z" fill="#3d7547" stroke="none"/>
 <path d="M34 49q-3-14 12-12m6-7q5-11 13-3" stroke="#8cb464" stroke-width="7"/>
 `,120,120,4.8);
await asset('rock-wide',`
 <path d="M8 61 25 32Q49 20 69 24L99 38 111 65 94 80 33 82Z" fill="#9ea685"/>
 <path d="m25 32 44-8 15 23-45 9Z" fill="#c5c7a0" stroke="none"/>
 <path d="m84 47 15-9 12 27-17 15-55 2Z" fill="#7c896f" stroke="none"/>
 <path d="m25 32 14 24 45-9" stroke="#8c9478" stroke-width="3"/>
 `,120,96,6);
await asset('rock-tall',`
 <path d="m23 96-3-40 26-35 34-7 22 34 0 45-38 17Z" fill="#9ba789"/>
 <path d="m20 56 26-35 34-7-17 39Z" fill="#c5c7a0" stroke="none"/>
 <path d="m63 53 39-5v45l-38 17Z" fill="#7c896f" stroke="none"/>
 `,120,120,6);
await asset('fence-front',`
 <path d="m12 28 96 18v12L12 40Zm0 26 96 18v12L12 66Z" fill="#d7a461"/>
 <path d="m9 16 8-5 9 5v69l-8 5-9-5ZM99 32l8-5 9 5v69l-8 5-9-5Z" fill="#bc874c"/>
 <path d="m18 11 8 5v69l-8 5ZM108 27l8 5v69l-8 5Z" fill="#95653c" stroke="none"/>
 <path d="m34 37 21 4m17 32 16 3" stroke="#b2844c" stroke-width="2.5"/>
 `,126,110,4.6);
await asset('fence-return',`
 <path d="m18 51 69-33v12L18 65Zm0 25 69-33v12L18 90Z" fill="#c39453"/>
 <path d="m10 42 8-4 9 4v62l-9 5-8-5ZM81 12l8-4 9 4v62l-9 5-8-5Z" fill="#c28e51"/>
 <path d="m18 38 9 4v62l-9 5ZM89 8l9 4v62l-9 5Z" fill="#946239" stroke="none"/>
 `,108,116,4.6);
await asset('bench',`
 <path d="m19 42 15 4v34l-9 5-6-4ZM87 53l15-3v36l-9 5-6-4Z" fill="#9f6a3d"/>
 <path d="m7 31 29-14 78 16-25 18Z" fill="#e0ae67"/>
 <path d="m7 31 82 20v14L7 45Z" fill="#c68d4b"/>
 <path d="m89 51 25-18v15L89 65Z" fill="#936036"/>
 <path d="m28 29 54 13" stroke="#ba874b" stroke-width="2.6"/>
 <path d="m22 52 7 2m65 12 5-2" stroke="#e5b779" stroke-width="2.5"/>
 `,120,100,5.3);
await asset('basin',`
 <path d="M13 41 19 69Q31 88 63 88 95 86 105 68L111 42Z" fill="#bba781"/>
 <path d="M82 56 110 44 105 68Q95 86 63 88L67 71Z" fill="#948365" stroke="none"/>
 <ellipse cx="62" cy="41" rx="49" ry="23" fill="#ead8b2"/>
 <ellipse cx="62" cy="41" rx="36" ry="14" fill="#74aeb1"/>
 <path d="M34 42Q52 51 76 45" stroke="#c7e0cb" stroke-width="3.5"/>
 <path d="M27 71q26 13 55 3" stroke="#dac39b" stroke-width="3"/>
 `,124,103,7);
await asset('campfire',`
 <path d="m15 79 77 23 14-12-78-26Z" fill="#ad7641"/>
 <path d="m17 94 76-28 12 13-75 29Z" fill="#d09b56"/>
 <path d="M33 76C25 59 43 49 44 34L54 44Q70 29 68 11 92 28 79 46 104 68 93 82 82 100 59 92 39 97 33 76Z" fill="#e99540"/>
 <path d="M51 81Q45 71 64 52 65 69 76 77 79 91 64 89 54 91 51 81Z" fill="#ffe098" stroke="none"/>
 `,120,116,6);
await asset('edge-stone',`<path d="m4 20 7-9 24 3 2 14-28 3Zm41-7 28-3 10 11-8 12-31-3Zm44 5 20-5 8 10-4 13-23-2Z" fill="#b7b99b" stroke="#7d8967" stroke-width="3"/>`,120,44,3);
await writeFile(new URL('asset-manifest.json',base),JSON.stringify(kit,null,2));
console.log(`Prepared ${Object.keys(kit).length} independent assets: four original transparent buildings, twelve revised SVG modules.`);
