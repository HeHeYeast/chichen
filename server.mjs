import http from 'node:http';
import { readFile } from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
const root = path.dirname(fileURLToPath(import.meta.url));
const mime = { '.html':'text/html; charset=utf-8', '.js':'text/javascript; charset=utf-8', '.css':'text/css; charset=utf-8', '.png':'image/png', '.jpg':'image/jpeg', '.mp3':'audio/mpeg', '.ttf':'font/ttf', '.json':'application/json', '.svg':'image/svg+xml' };
const server=http.createServer(async (req,res) => {
  try {
    let url = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);
    if (url === '/') url = '/web/index.html';
    if (url === '/classic') url = '/web/classic.html';
    if (url === '/review') url = '/web/review.html';
    const target = path.resolve(root, '.' + url);
    if (!target.startsWith(root + path.sep) || !['web','assets','res'].includes(path.relative(root,target).split(path.sep)[0])) { res.writeHead(403).end(); return; }
    const body = await readFile(target);
    res.writeHead(200, {'Content-Type':mime[path.extname(target)] || 'application/octet-stream','Cache-Control':'no-cache'}).end(body);
  } catch { res.writeHead(404).end('Not found'); }
});
server.listen(Number(process.argv[2]??4173), '127.0.0.1', () => console.log(`Chick Kitchen: http://127.0.0.1:${server.address().port}`));
