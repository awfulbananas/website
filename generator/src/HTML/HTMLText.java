package HTML;

import java.io.PrintStream;

public class HTMLText extends HTMLNode {
    protected String text;

    public HTMLText() {
        this("");
    }

    public HTMLText(String text) {
        this.text = text;
    }

    public boolean isEmpty() {
        if(text == null || text.isEmpty()) return true;
        for(int i = 0; i < text.length(); i++) {
            if(!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    public void printNode(PrintStream out, int depth) {
        out.print(this);
    }
}
