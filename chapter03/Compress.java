package chapter03;
import java.io.*;
import java.util.zip.*;

public class Compress {
    public static void gzipFile(String from, String to) throws IOException {
        FileInputStream in = new FileInputStream(from);
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(to));
        byte[] buffer = new byte[4096];
        int bytes_read;
        while ((bytes_read = in.read(buffer)) != -1)
            out.write(buffer, 0, bytes_read);
        in.close();
        out.close();
    }
    public static void zipDirectory(String dir, String zipfile) throws IOException, IllegalArgumentException {
        File d = new File(dir);
        if (!d.isDirectory())
            throw new IllegalArgumentException("Compress: Isn't directory: " + dir);
        String[] entries = d.list();
        byte[] buffer = new byte[4096];
        int bytes_read;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        for (int i = 0; i < entries.length; i++) {
            File f = new File(d, entries[i]);
            if (f.isDirectory()) continue;
            FileInputStream in = new FileInputStream(f);
            ZipEntry entry = new ZipEntry(f.getName());
            out.putNextEntry(entry);
            while ((bytes_read = in.read(buffer)) != -1)
                out.write(buffer, 0, bytes_read);
            in.close();
        }
        out.close();
    }
    public static void main(String[] args) throws IOException {
        if ((args.length != 1) && (args.length != 2)) {
            System.err.println("The command format: java <this package>.Compress <from> [to]");
            System.exit(0);
        }
        String from = args[0], to;
        File f = new File(from);
        boolean directory = f.isDirectory();
        if (args.length == 2) to = args[1];
        else {
            if (directory) to = from + ".zip";
            else to = from + ".gz";
        }
        if ((new File(to)).exists()) {
            System.err.println("Compress: Don't overwrite existing files: " + to);
            System.exit(0);
        }
        if (directory) zipDirectory(from, to);
        else gzipFile(from, to);
    }
}