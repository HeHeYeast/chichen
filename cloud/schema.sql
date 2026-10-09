PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS users(uid TEXT PRIMARY KEY, username TEXT NOT NULL UNIQUE, password_hash TEXT NOT NULL, created_at INTEGER NOT NULL);
CREATE TABLE IF NOT EXISTS sessions(token_hash TEXT PRIMARY KEY, uid TEXT NOT NULL REFERENCES users(uid) ON DELETE CASCADE, expires_at INTEGER NOT NULL);
CREATE INDEX IF NOT EXISTS sessions_user ON sessions(uid);
-- Immutable revisions: a conditional INSERT is the single atomic commit point.
CREATE TABLE IF NOT EXISTS saves(uid TEXT NOT NULL REFERENCES users(uid) ON DELETE CASCADE, revision INTEGER NOT NULL, upload_id TEXT NOT NULL, raw TEXT NOT NULL, hash TEXT NOT NULL, schema_version INTEGER NOT NULL, created_at INTEGER NOT NULL, PRIMARY KEY(uid,revision), UNIQUE(uid,upload_id));
CREATE TRIGGER IF NOT EXISTS save_retention AFTER INSERT ON saves BEGIN
 DELETE FROM saves WHERE uid=NEW.uid AND revision < NEW.revision-20;
END;
CREATE TABLE IF NOT EXISTS rate_limits(key TEXT PRIMARY KEY, bucket INTEGER NOT NULL, count INTEGER NOT NULL);
CREATE INDEX IF NOT EXISTS rate_expiry ON rate_limits(bucket);
CREATE TABLE IF NOT EXISTS feedback(id TEXT PRIMARY KEY, owner TEXT NOT NULL, body TEXT NOT NULL, created_at INTEGER NOT NULL);
CREATE INDEX IF NOT EXISTS feedback_expiry ON feedback(created_at);
