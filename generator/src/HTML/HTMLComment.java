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

    public String toString() {
        return "<!--" + text + "-->";
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    public boolean isText() {
        return false;
    }
}
