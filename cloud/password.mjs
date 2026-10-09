import {scrypt,randomBytes,timingSafeEqual} from 'node:crypto';
import {promisify} from 'node:util';
const derive=promisify(scrypt);
// OWASP scrypt alternative: N=2^15, r=8, p=3. Never lower to fit a free CPU tier.
export async function hashPassword(password){
  const salt=randomBytes(16).toString('hex');
  const key=await derive(password,salt,32,{N:32768,r:8,p:3,maxmem:64*1024*1024});
  return `scrypt:32768:8:3:${salt}:${key.toString('hex')}`;
}
export async function verifyPassword(password,encoded){
  const [algo,n,r,p,salt,hex]=encoded.split(':');
  if(algo!=='scrypt'||n!=='32768'||r!=='8'||p!=='3'||!salt||!hex)return false;
  const key=await derive(password,salt,32,{N:32768,r:8,p:3,maxmem:64*1024*1024});
  const expected=Buffer.from(hex,'hex');return expected.length===key.length&&timingSafeEqual(key,expected);
}
