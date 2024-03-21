package lexer;

/**
 * LexerException class represents an exception that occurs during lexical analysis.
 */

public class LexerException extends Throwable {
    Token token;
    String message;

    /**
     * Constructs a LexerException with the given token and message.
     * @param token The token associated with the exception.
     * @param message The error message describing the exception.
     */
    public LexerException(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    /**
     * Generates a string representation of the LexerException.
     * @return The string representation of the LexerException.
     */
    @Override
    public String toString() {
        return String.format(" LINEA %d (COLUMNA %d) | %s ", token.ln, token.cn, message + ": " + token.getLexeme());
    }
}
