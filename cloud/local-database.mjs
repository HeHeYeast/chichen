import {DatabaseSync} from 'node:sqlite';
import {readFileSync} from 'node:fs';
export function createLocalDatabase(file){
  const sql=new DatabaseSync(file);sql.exec(readFileSync(new URL('schema.sql',import.meta.url),'utf8'));sql.exec('PRAGMA journal_mode=WAL;');
  return {
    prepare(query){let values=[];const statement={bind(...args){values=args;return statement;},async first(){return sql.prepare(query).get(...values)??null;},async all(){return {results:sql.prepare(query).all(...values)};},async run(){const result=sql.prepare(query).run(...values);return {meta:{changes:Number(result.changes)}};}};return statement;},
    async batch(statements){sql.exec('BEGIN IMMEDIATE');try{const result=[];for(const s of statements)result.push(await s.run());sql.exec('COMMIT');return result;}catch(e){sql.exec('ROLLBACK');throw e;}},
    close(){sql.close();}
  };
}
