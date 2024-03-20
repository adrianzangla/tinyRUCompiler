package lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class Source {

    PushbackReader pr;
    int ln = 1;
    int cn = 1;

    public Source(String s) throws FileNotFoundException {
        pr = new PushbackReader(new FileReader(s));
    }

    public char read() throws IOException {
        char c = (char) pr.read();
        if (c == '\n') {
            ln++;
            cn = 1;
        } else cn++;
        return c;
    }

    public void unread(char c) throws IOException {
        if (c == '\n') {
            ln--;
            cn = 1;
        } else cn--;
        pr.unread(c);
    }


}
