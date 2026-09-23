package android.content;
import java.io.File;
public class Context {
    private final File folder;
    public Context(File folder) { this.folder=folder; }
    public File getFilesDir() { return folder; }
}
