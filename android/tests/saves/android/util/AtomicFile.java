package android.util;
import java.io.*;
import java.nio.file.*;
/** File-backed atomic-write adapter for JVM tests, with injected write failure. */
public final class AtomicFile {
    private final File target;
    public static boolean failFinish=false;
    public static String failFileName=null;
    public AtomicFile(File target) { this.target=target; }
    public FileInputStream openRead() throws IOException {
        File backup=new File(target+".bak");
        if(backup.exists())Files.move(backup.toPath(),target.toPath(),StandardCopyOption.REPLACE_EXISTING);
        return new FileInputStream(target);
    }
    public FileOutputStream startWrite() throws IOException {
        Files.createDirectories(target.getParentFile().toPath());
        return new FileOutputStream(target+".new");
    }
    public void finishWrite(FileOutputStream out) {
        try {
            out.getFD().sync();out.close();
            if(failFinish||target.getName().equals(failFileName))throw new IOException("injected write failure");
            Files.move(Path.of(target+".new"),target.toPath(),StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException error){throw new IllegalStateException(error);}
    }
    public void failWrite(FileOutputStream out) {
        try{out.close();Files.deleteIfExists(Path.of(target+".new"));}catch(IOException ignored){}
    }
}
