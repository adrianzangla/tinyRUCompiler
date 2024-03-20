package lexer;

public class Token {
    private final Lexeme l;
    final int ln;
    final int cn;

    public Token(int ln, int cn) {
        this.l = new Lexeme();
        this.ln = ln;
        this.cn = cn;
    }

    public Token(String l, int ln, int cn) {
        this.l = new Lexeme(l);
        this.ln = ln;
        this.cn = cn;
    }

    public Token(char l, Type t, int ln, int cn) {
        this.l = new Lexeme(l, t);
        this.ln = ln;
        this.cn = cn;
    }

    public Token(char l, int ln, int cn) {
        this.l = new Lexeme(l);
        this.ln = ln;
        this.cn = cn;
    }

    public Type getType() {
        return l.t;
    }

    public String getLexeme() {
        return l.getValue();
    }

    public void append(char c) {
        l.append(c);
    }

    public void setType(Type t) {
        l.t = t;
    }

    @Override
    public String toString() {
        return l.toString() + String.format("| LINEA %d (COLUMNA %d) ", ln, cn);
    }
}
