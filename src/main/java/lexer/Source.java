package lexer;

import java.io.*;

/**
 * Source class represents the source code file and provides methods for reading characters from it.
 */
public class Source {

    PushbackReader pr;
    int ln = 1;
    int cn = 0;
    private int lc;

    /**
     * Constructs a Source object to read characters from the specified file.
     *
     * @param s The path to the source code file.
     * @throws FileNotFoundException If the specified file is not found.
     */
    public Source(String s) throws FileNotFoundException {
        pr = new PushbackReader(new FileReader(s));
    }

    /**
     * Reads the next character from the source code file.
     *
     * @return The next character read from the source code.
     * @throws IOException If an I/O error occurs.
     */
    public char read() throws IOException {
        char c = (char) pr.read();
        lc = cn;
        if (c == '\n') {
            ln++;
            cn = 0;
        } else cn++;
        return c;
    }

    /**
     * Pushes back the last character read into the source code file.
     *
     * @param c The character to be pushed back.
     * @throws IOException If an I/O error occurs.
     */
    public void unread(char c) throws IOException {
        if (c == '\n') {
            ln--;
            cn = lc;
        } else cn--;
        pr.unread(c);
    }

    /**
     * Reads and returns the next character from the source code file that is not whitespace, control character,
     * or part of a comment.
     *
     * @return The next character that is not whitespace, control character, or part of a comment.
     * @throws IOException If an I/O error occurs.
     */
    public char next() throws IOException {
        char c = read();
        while (c != '\uffff') {
            if (c == '/') {
                c = read();
                if (c == '?') {
                    c = read();
                    while (c != '\n' && c != '\uffff') {
                        c = read();
                    }
                } else {
                    unread(c);
                    return '/';
                }
            } else if (!Character.isWhitespace(c)) {
                return c;
            }
            c = read();
        }
        throw new EOFException();
    }
}
