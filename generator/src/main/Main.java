import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import HTML.*;

/**
 * @author awfulbananas
 * This is a script to generate parts of my website based on the files already in the site folder.
 * Yes, I could have used any number of static site generators already out there, but doing things
 * from scratch is fun.
 */

public class Main {

    public static void main(String[] args) throws IOException {
        Path siteFolder = FileSystems.getDefault().getPath("./../../site");
        Path siteOutputFolder = FileSystems.getDefault().getPath("./../../siteBuild");
        //if there's already a build output, replace it
        if(Files.exists(siteOutputFolder)) {
            delDirRecursive(siteOutputFolder.toFile());
        }
        Files.createDirectory(siteOutputFolder);

        File baseIndexFile = siteFolder.resolve("index.html").toFile();

        InputStream indexIn = new FileInputStream(baseIndexFile);
        HTMLElement index = HTMLNode.parseHTML(indexIn);
        index.purgeWhitespaceText();

        File outputIndexFile = siteOutputFolder.resolve("index.html").toFile();
        writeHTMLToFile(outputIndexFile, index);

    }

    public static void writeHTMLToFile(File f, HTMLElement html) throws FileNotFoundException {
        PrintStream out = new PrintStream(f);
        out.println("<!DOCTYPE html>  ");
        html.printNode(out);
    }

    public static void delDirRecursive(File dir) {
        for(File f : dir.listFiles()) {
            if(f.isDirectory()) {
                delDirRecursive(f);
            } else {
                f.delete();
            }
        }
        dir.delete();
    }

}
