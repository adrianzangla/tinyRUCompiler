package lexer;

public class Lexeme {
    private final StringBuilder sb;
    Type t;

    public Lexeme() {
        sb = new StringBuilder();
    }

    public Lexeme(char c) {
        sb = new StringBuilder(String.valueOf(c));
    }

    public Lexeme(char c, Type t) {
        sb = new StringBuilder(String.valueOf(c));
        this.t = t;
    }


    public Lexeme(String s) {
        sb = new StringBuilder(s);
    }

    public String getValue() {
        return sb.toString();
    }

    public void append(char c) {
        sb.append(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lexeme l = (Lexeme) o;
        return sb.toString().contentEquals(l.sb);
    }

    @Override
    public String toString() {
        return String.format(" %s | %s ", t.name(), sb.toString());
    }
}
