package HTML;

import java.io.PrintStream;
import java.util.*;

public class HTMLElement extends HTMLNode {
    private String tagName;
    private TreeMap<String, String> attributes;
    private List<HTMLNode> children;

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
        for(HTMLNode c : children) {
            c.parent = this;
        }
    }

    public HTMLElement getChildElementByTag(String tag) {
        for(HTMLNode c : children) {
            if(c instanceof HTMLElement && ((HTMLElement)c).tagName.equals(tag)) {
                return (HTMLElement) c;
            }
        }
        return null;
    }

    public void add(HTMLNode newNode) {
        children.add(newNode);
    }

    public void add(int index, HTMLNode newNode) {
        children.add(index, newNode);
    }

    @Override
    public HTMLNode copy() {
        List<HTMLNode> childrenCopy = new ArrayList<>();
        for(HTMLNode c : children) {
            childrenCopy.add(c.copy());
        }
        TreeMap<String, String> attributesCopy = new TreeMap<>();
        for(String key : attributes.navigableKeySet()) {
            attributesCopy.put(key, attributes.get(key));
        }
        return new HTMLElement(tagName, attributesCopy, childrenCopy);
    }

    @Override
    protected void printNode(PrintStream out, int depth) {
        out.print("  ".repeat(depth) + "<" + tagName);
        for(String attribute : attributes.navigableKeySet()) {
            out.print(" " + attribute + "=\"" +attributes.get(attribute) + '"');
        }
        if(!children.isEmpty()) {
            out.print(">");
            if(!(children.getFirst() instanceof HTMLText)) {
                out.println();
            }
            for (HTMLNode child : children) {
                child.printNode(out, depth + 1);
            }
            if(!(children.getLast() instanceof HTMLText)) {
                out.print("  ".repeat(depth));
            }
        } else {
            out.print(">");
        }
        if(HTMLDocument.ONE_SIDED_TAGS.contains(tagName)) {
            out.println();
        } else {
            out.println("</" + tagName + ">");
        }
    }

    public void purgeWhitespaceText() {
        Iterator<HTMLNode> cItr = children.iterator();
        while(cItr.hasNext()) {
            HTMLNode child = cItr.next();
            if(child instanceof HTMLElement) {
                ((HTMLElement)child).purgeWhitespaceText();
            } else if(((HTMLText)child).isEmpty()) {
                cItr.remove();
            }
        }
    }

}
