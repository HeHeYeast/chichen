import {createService,maintenance} from './service.mjs';
export default {async fetch(request,env){
  if(env.ENABLE_TEST_API!=='true')return new Response('Cloud API is not enabled',{status:503});
  return createService({db:env.DB,origin:env.WEB_ORIGIN,epoch:env.CLOUD_EPOCH,allowedUsernames:(env.ALLOWED_USERNAMES??'').split(',').filter(Boolean)})(request,{peer:request.headers.get('cf-connecting-ip')??'unknown'});
},async scheduled(_event,env){if(env.ENABLE_TEST_API==='true')await maintenance(env.DB);}};
