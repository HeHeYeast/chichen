// Short sound effects decoded once and played from memory. Cloning an <audio>
// element per play makes Android WebView build a new media player (and reload
// the file) for every chick collected, which stalls the harvest animation.
// Until Web Audio is ready, or where it is missing, the old clone path plays.
export function createSfx(urls,{volume=.65}={}){
  const fallback=urls.map(url=>new Audio(url));
  const Context=globalThis.AudioContext??globalThis.webkitAudioContext;
  let context=null,gain=null;const buffers=[];
  if(Context){
    try{
      context=new Context();gain=context.createGain();gain.gain.value=volume;gain.connect(context.destination);
      urls.forEach((url,i)=>fetch(url).then(r=>r.ok?r.arrayBuffer():Promise.reject(Error(r.status))).then(data=>new Promise((resolve,reject)=>context.decodeAudioData(data,resolve,reject))).then(buffer=>{buffers[i]=buffer;}).catch(()=>{}));
      // Autoplay policy keeps a new context suspended until the first gesture.
      const unlock=()=>{if(context.state==='suspended')context.resume().catch(()=>{});};
      for(const type of ['pointerdown','keydown'])window.addEventListener(type,unlock,{capture:true,passive:true});
      // A running context keeps the phone's audio output awake in the background.
      document.addEventListener('visibilitychange',()=>{if(document.hidden&&context.state==='running')context.suspend().catch(()=>{});});
    }catch{context=null;}
  }
  return {
    play(id){
      const buffer=buffers[id];
      if(context&&buffer&&context.state==='running'){
        try{const source=context.createBufferSource();source.buffer=buffer;source.connect(gain);source.start();return;}catch{}
      }
      const a=fallback[id].cloneNode();a.volume=volume;a.play().catch(()=>{});
    },
  };
}
