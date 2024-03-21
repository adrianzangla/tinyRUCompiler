package lexer;

/**
 * Token class represents a lexical token in the source code.
 */
public class Token {
    private final Lexeme l;
    final int ln;
    final int cn;

    /**
     * Constructs a Token object with an empty lexeme, line number, and column number.
     *
     * @param ln The line number where the token is found.
     * @param cn The column number where the token is found.
     */
    public Token(int ln, int cn) {
        this.l = new Lexeme();
        this.ln = ln;
        this.cn = cn;
    }


    /**
     * Constructs a Token object with the specified lexeme, line number, and column number.
     *
     * @param l  The lexeme value of the token.
     * @param ln The line number where the token is found.
     * @param cn The column number where the token is found.
     */
    public Token(String l, int ln, int cn) {
        this.l = new Lexeme(l);
        this.ln = ln;
        this.cn = cn;
    }

    /**
     * Retrieves the lexeme of the token.
     *
     * @return The lexeme of the token.
     */
    public String getLexeme() {
        return l.getValue();
    }


    /**
     * Appends a character to the lexeme of the token.
     *
     * @param c The character to append.
     */
    public void append(char c) {
        l.append(c);
    }

    /**
     * Sets the type of the token.
     *
     * @param t The type of the token.
     */
    public void setType(Type t) {
        l.t = t;
    }

    /**
     * Generates a string representation of the token.
     *
     * @return The string representation of the token.
     */
    @Override
    public String toString() {
        return l.toString() + String.format("| LINEA %d (COLUMNA %d) ", ln, cn);
    }
}
