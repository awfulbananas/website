package HTML;

import java.io.PrintStream;
import java.util.*;

public class HTMLElement extends HTMLNode {
    private String tagName;
    private TreeMap<String, String> attributes;
    private List<HTMLNode> children;
    protected HTMLElement parent;

    public HTMLElement(String tagName, TreeMap<String, String> attributes) {
        this(tagName, attributes, new ArrayList<>());
    }

    public HTMLElement(String tagName, List<HTMLNode> children) {
        this(tagName, new TreeMap<>(), children);
    }

    public HTMLElement(String tagName, TreeMap<String, String> attributes, List<HTMLNode> children) {
        this.tagName = tagName;
        this.attributes = attributes;
        this.children = children;
    }

    @Override
    public boolean isElement() {
        return true;
    }

    @Override
    protected void printNode(PrintStream out, int depth) {
        out.print("  ".repeat(depth) + "<" + tagName);
        for(String attribute : attributes.navigableKeySet()) {
            out.print(" " + attribute + "=\"" +attributes.get(attribute) + '"');
        }
        if(!children.isEmpty()) {
            out.print(">");
            if(!children.getFirst().isText()) {
                out.println();
            }
            for (HTMLNode child : children) {
                child.printNode(out, depth + 1);
            }
            if(!children.getLast().isText()) {
                out.print("  ".repeat(depth));
            }
        } else {
            out.print(">");
        }
        if(HTMLNode.ONE_SIDED_TAGS.contains(tagName)) {
            out.println();
        } else {
            out.println("</" + tagName + ">");
        }
    }

    public void purgeWhitespaceText() {
        Iterator<HTMLNode> cItr = children.iterator();
        while(cItr.hasNext()) {
            HTMLNode child = cItr.next();
            if(child.isElement()) {
                ((HTMLElement)child).purgeWhitespaceText();
            } else if(((HTMLText)child).isEmpty()) {
                cItr.remove();
            }
        }
    }

}
