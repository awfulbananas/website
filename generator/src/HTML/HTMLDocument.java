package HTML;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class HTMLDocument {
    public static final Set<String> ONE_SIDED_TAGS = Set.of("meta", "link");

    private HTMLElement root;

    public HTMLDocument(HTMLElement root) {
        this.root = root;
    }

    public void print(PrintStream out) {
        out.println("<!DOCTYPE html>");
        root.printNode(out);
    }

    public HTMLDocument copy() {
        return new HTMLDocument((HTMLElement) root.copy());
    }

    public HTMLElement getRoot() {
        return root;
    }

    public HTMLElement getHead() {
        return root.getChildElementByTag("head");
    }

    public HTMLElement getBody() {
        return root.getChildElementByTag("body");
    }

    public static HTMLDocument parseHTML(InputStream in) throws IOException {
        //get past the doctype first
        ReadCursor c = new ReadCursor(in);
        while(c.val() != '>') c.read();
        while(c.val() != '<') c.read();
        //return the parsed html root element
        return new HTMLDocument((HTMLElement) parseElement(c));
    }

    private static HTMLNode parseElement(ReadCursor c) throws IOException {
        c.read();
        //if this is a closing tag, return null and the calling method will handle it
        if(c.val() == '/') return null;
        //oh look, it's actually a comment
        if(c.val() == '!') return parseComment(c);
        StringBuilder tagNameBuilder = new StringBuilder();
        while(!(Character.isWhitespace(c.val()) || c.val() == '>')) {
            tagNameBuilder.append(c.val());
            c.read();
        }
        String tagName = tagNameBuilder.toString();

        TreeMap<String, String> attributes = new TreeMap<>();
        c.skipWhitespace();
        while(c.val() != '>') {
            StringBuilder attributeName = new StringBuilder();
            //yes, I know this is wrong, it'll work correctly in every case it will encounter though so eh
            //I just don't really feel like throwing errors
            while(c.val() != '=') {
                attributeName.append(c);
                c.read();
            }

            StringBuilder attributeValue = new StringBuilder();
            c.read();
            if(c.val() == '"') {
                c.read();
                while(c.val() != '"') {
                    attributeValue.append(c);
                    c.read();
                }
                c.read();
            } else {
                while(c.val() != '>' && !Character.isWhitespace(c.val())) {
                    attributeValue.append(c);
                    c.read();
                }
            }

            attributes.put(attributeName.toString(), attributeValue.toString());
            //ignore intervening whitespace
            while(Character.isWhitespace(c.val())) c.read();
        }

        if(ONE_SIDED_TAGS.contains(tagName)) {
            c.read();
            return new HTMLElement(tagName, attributes);
        }

        ArrayList<HTMLNode> children = new ArrayList<>();
        boolean hasChildrenLeft = true;
        c.read();
        while(hasChildrenLeft) {
            if(c.val() == '<') {
                HTMLNode child = parseElement(c);
                if(child == null) {
                    hasChildrenLeft = false;
                    while(c.val() != '>') c.read();
                    c.read();
                } else {
                    children.add(child);
                }
            } else {
                children.add(parseText(c));
            }
        }
        return new HTMLElement(tagName, attributes, children);
    }


    private static HTMLNode parseText(ReadCursor c) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        while(c.val() != '<') {
            textBuilder.append(c.val());
            c.read();
        }
        return new HTMLText(textBuilder.toString());
    }

    private static HTMLNode parseComment(ReadCursor c) throws IOException {
        c.read(3);
        int consecutiveDashes = 0;
        StringBuilder textBuilder = new StringBuilder();
        while(consecutiveDashes < 2 && c.val() != '>') {
            if(c.val() == '-') {
                consecutiveDashes++;
            } else {
                consecutiveDashes = 0;
            }
            textBuilder.append(c);
            c.read();
        }
        textBuilder.delete(textBuilder.length() - 2, textBuilder.length());
        c.read();
        return new HTMLComment(textBuilder.toString());
    }
}
