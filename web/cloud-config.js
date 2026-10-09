// Public endpoint only. Never place credentials, AppSecret or administrator keys here.
// A test build may set a verified HTTPS endpoint. The ordinary build stays offline-capable.
export const CLOUD_ENDPOINT='';
export function cloudEndpoint(location){
  if(CLOUD_ENDPOINT)return CLOUD_ENDPOINT;
  if(['localhost','127.0.0.1'].includes(location.hostname)&&new URLSearchParams(location.search).get('cloud')==='local')return 'http://127.0.0.1:4174';
  return '';
}
