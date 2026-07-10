package HTML;

import java.io.PrintStream;

public class HTMLComment extends HTMLText {

    public HTMLComment(String text) {
        super(text);
    }

    @Override
    public void printNode(PrintStream out, int depth) {
        out.println("  ".repeat(depth) + this);
    }

    @Override
    public HTMLNode copy() {
        return new HTMLComment(text);
    }

    public String toString() {
        return "<!--" + text + "-->";
    }
}
