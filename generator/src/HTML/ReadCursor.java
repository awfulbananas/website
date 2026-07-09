package HTML;

import java.io.IOException;
import java.io.InputStream;

//it's genuinely so convenient to store the most recent value, I might make a more generic version of
//this class later so I can use it in other things
public class ReadCursor {
    private char curChar;
    private InputStream in;

    public ReadCursor(InputStream in) {
        this.in = in;
    }

    public void read() throws IOException {
        curChar = (char)in.read();
    }

    public void read(int n) throws IOException {
        for(int i = 0; i < n; i++) read();
    }

    public char val() {
        return curChar;
    }

    public void skipWhitespace() throws IOException {
        while(Character.isWhitespace(curChar)) curChar = (char)in.read();
    }

    public String toString() {
        return Character.toString(curChar);
    }
}
