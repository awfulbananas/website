package HTML;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * represents a node of an HTML DOM tree.
 * for the purposes of this program, I'm not going to fully follow spec, both because I don't feel like,
 * and because I don't really need to here (no one else ever needs to run this code.)
 * this means no doctype and no processing instructions, and I'm not precisely following
 * the spec instructions for parsing.
 * Also, I'm assuming that a larger subset of elements than specified are required to have both opening and
 * closing tags, to make parsing easier, and assuming that certain elements are guaranteed to *not* have a closing tag
 */
public abstract class HTMLNode {
    protected HTMLElement parent;

    public HTMLElement asElement() {
        if(this instanceof HTMLElement) {
            return (HTMLElement) this;
        } else {
            return null;
        }
    }

    public void printNode(PrintStream out) {
        printNode(out, 0);
    }

    public abstract HTMLNode copy();

    protected abstract void printNode(PrintStream out, int depth);
}
