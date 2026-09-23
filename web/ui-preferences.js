// Per-device UI conveniences only (read markers, remembered tabs). Never game
// state: losing this storage only re-shows a receipt, it never changes CP/stock.
const KEY='chick-kitchen-ui-v1';
function read(){try{const v=JSON.parse(globalThis.localStorage?.getItem(KEY)??'{}');return v&&typeof v==='object'&&!Array.isArray(v)?v:{};}catch{return {};}}
export function uiPreference(name,fallback=null){return Object.hasOwn(read(),name)?read()[name]:fallback;}
export function setUiPreference(name,value){try{const v=read();v[name]=value;globalThis.localStorage?.setItem(KEY,JSON.stringify(v));}catch{/* private mode: keep in memory only */}}
