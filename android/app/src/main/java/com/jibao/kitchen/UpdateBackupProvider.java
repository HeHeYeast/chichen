package com.jibao.kitchen;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Read-only export for an authorized ADB connection; never imports or changes saves. */
public final class UpdateBackupProvider extends ContentProvider {
    private static final String URI = "content://com.jibao.kitchen.update-backup/snapshot";
    @Override public boolean onCreate() { return true; }

    private static void authorize(Uri uri) {
        // DUMP in the manifest is defense in depth. Even another app granted
        // DUMP cannot use this endpoint: only Android's actual shell UID may.
        if (Binder.getCallingUid() != 2000) throw new SecurityException("ADB shell only");
        if (!URI.equals(uri.toString())) throw new IllegalArgumentException("Unknown backup URI");
    }

    @Override public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        authorize(uri);
        if (!"r".equals(mode)) throw new SecurityException("Read-only backup");
        if (MainActivity.hasWebGame) throw new FileNotFoundException("Close the game before taking an update snapshot");
        final byte[] bytes;
        try {
            bytes = SaveRepository.updateBackup(getContext(), BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE, System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8);
        } catch (Exception error) { throw new FileNotFoundException("Backup refused: " + error.getMessage()); }
        return openPipeHelper(uri, "application/json", null, bytes, (output, ignoredUri, type, options, data) -> {
            try (ParcelFileDescriptor.AutoCloseOutputStream stream = new ParcelFileDescriptor.AutoCloseOutputStream(output)) {
                stream.write(data);
            } catch (IOException ignored) { /* The installer rejects incomplete JSON/checksums. */ }
        });
    }
    @Override public String getType(Uri uri) { authorize(uri); return "application/json"; }
    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] args, String order) {
        authorize(uri); throw new UnsupportedOperationException("Use read-only snapshot");
    }
    @Override public Uri insert(Uri uri, ContentValues values) { authorize(uri); throw new SecurityException("Read only"); }
    @Override public int update(Uri uri, ContentValues values, String selection, String[] args) { authorize(uri); throw new SecurityException("Read only"); }
    @Override public int delete(Uri uri, String selection, String[] args) { authorize(uri); throw new SecurityException("Read only"); }
}
