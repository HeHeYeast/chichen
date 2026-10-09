import http from 'node:http';
import {Readable} from 'node:stream';
import {mkdirSync} from 'node:fs';
import {createLocalDatabase} from './local-database.mjs';
import {createService} from './service.mjs';
mkdirSync(new URL('.local/',import.meta.url),{recursive:true});
const db=createLocalDatabase(process.env.CHICK_DB_FILE??new URL('.local/development.sqlite',import.meta.url));
const handle=createService({db,origin:process.env.CHICK_WEB_ORIGIN??'http://127.0.0.1:4173',epoch:'local-development-v1',allowedUsernames:(process.env.CHICK_TEST_USERS??'alice,bob').split(',')});
const server=http.createServer(async(req,res)=>{
  try{
    const request=new Request('http://127.0.0.1'+req.url,{method:req.method,headers:req.headers,...(['GET','HEAD'].includes(req.method)?{}:{body:Readable.toWeb(req),duplex:'half'})});
    const response=await handle(request,{peer:'loopback'});res.writeHead(response.status,Object.fromEntries(response.headers));res.end(Buffer.from(await response.arrayBuffer()));
  }catch{res.writeHead(500,{'content-type':'application/json'}).end(JSON.stringify({error:{code:'INTERNAL_ERROR',message:'本地测试服务出错。'}}));}
});
server.listen(Number(process.env.CHICK_API_PORT??4174),'127.0.0.1',()=>console.log('Development save API listening (loopback only, local SQLite, no cloud resources)'));
process.on('SIGINT',()=>server.close(()=>{db.close();process.exit(0);}));
