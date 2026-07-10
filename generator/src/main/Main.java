package main;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

import HTML.*;

/**
 * @author awfulbananas
 * This is a script to generate parts of my website based on the files already in the site folder.
 * Yes, I could have used any number of static site generators already out there, but doing things
 * from scratch is fun.
 */

public class Main {
    public static boolean DEBUG_OUT;

    public static void main(String[] args) throws IOException {
        if(args.length > 0) {
            DEBUG_OUT = Boolean.parseBoolean(args[0]);
        } else {
            DEBUG_OUT = false;
        }

        Path projectRoot = findProjectRoot();
        Path siteFolder = projectRoot.resolve("site");
        Path siteOutputFolder = projectRoot.resolve("siteBuild");
        //if there's already a build output, replace it
        if(Files.exists(siteOutputFolder)) {
            delDirRecursive(siteOutputFolder.toFile());
        }
        Files.createDirectory(siteOutputFolder);
        if(DEBUG_OUT) System.out.println("loading css files");
        writeCSS(siteFolder, siteOutputFolder);

        if(DEBUG_OUT) System.out.println("loading template file");
        File templateFile = siteFolder.resolve("template.html").toFile();
        HTMLDocument template = HTMLDocument.parseHTML(new FileInputStream(templateFile));
        template.getRoot().purgeWhitespaceText();

        if(DEBUG_OUT) System.out.println("creating home index file");
        HTMLDocument index = template.copy();
        buildHomePage(index);

        if(DEBUG_OUT) {
            System.out.println("full index html:");
            index.print(System.out);
        }

        index.print(new PrintStream(siteOutputFolder.resolve("index.html").toFile()));
    }

    public static void buildHomePage(HTMLDocument page) {
        page.getHead().getChildElementByTag("title").add(new HTMLText("Awfulbananas' Portfolio | Home"));
        page.getBody().getChildElementByTag("header").add(new HTMLText("Home Page"));
        page.getBody().getChildElementByTag("main").add(new HTMLText("Currently under construction, check back later!"));
    }

    public static void writeCSS(Path siteFolder, Path buildFolder) throws IOException {
        Path cssFolder = siteFolder.resolve("css");
        Path cssBuildFolder =  buildFolder.resolve("css");
        Files.createDirectory(cssBuildFolder);

        Iterator<Path> cssFileItr = Files.list(cssFolder).iterator();
        while(cssFileItr.hasNext()) {
            Path p = cssFileItr.next();
            Files.copy(p, cssBuildFolder.resolve(p.getFileName()));
        }
    }

    public static Path findProjectRoot() {
        Path curLoc = FileSystems.getDefault().getPath(".");
        while(!Files.exists(curLoc.resolve("website"))) {
            curLoc = curLoc.resolve("..");
        }
        if(DEBUG_OUT) System.out.println("found project folder: " + curLoc + "/website");
        return curLoc.resolve("website");
    }

    public static void copyToBuild(Path siteFolder, Path buildFolder, String fileName) throws IOException {
        Files.copy(siteFolder.resolve(fileName), buildFolder.resolve(fileName));
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
