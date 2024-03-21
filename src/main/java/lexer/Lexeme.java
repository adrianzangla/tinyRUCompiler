package lexer;


/**
 * Lexeme class represents a lexical unit in the source code.
 */
public class Lexeme {
    private final StringBuilder sb;
    Type t;

    /**
     * Constructs a Lexeme object with an empty string.
     */
    public Lexeme() {
        sb = new StringBuilder();
    }

    /**
     * Constructs a Lexeme object with a single character.
     * @param c The character value.
     */
    public Lexeme(char c) {
        sb = new StringBuilder(String.valueOf(c));
    }

    /**
     * Constructs a Lexeme object with a single character and a type.
     * @param c The character value.
     * @param t The type of the lexeme.
     */
    public Lexeme(char c, Type t) {
        sb = new StringBuilder(String.valueOf(c));
        this.t = t;
    }

    /**
     * Constructs a Lexeme object with the given string.
     * @param s The string value.
     */
    public Lexeme(String s) {
        sb = new StringBuilder(s);
    }

    /**
     * Retrieves the value of the Lexeme.
     * @return The value of the Lexeme.
     */
    public String getValue() {
        return sb.toString();
    }

    /**
     * Appends a character to the Lexeme.
     * @param c The character to append.
     */
    public void append(char c) {
        sb.append(c);
    }


    /**
     * Generates a string representation of the Lexeme.
     * @return The string representation of the Lexeme.
     */
    @Override
    public String toString() {
        return String.format(" %s | %s ", t.name(), sb.toString());
    }


}
