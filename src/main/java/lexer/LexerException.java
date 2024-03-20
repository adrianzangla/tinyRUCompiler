package lexer;

public class LexerException extends Throwable {
    Token token;
    String message;

    public LexerException(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format(" LINEA %d (COLUMNA %d) | %s ", token.ln, token.cn, message + ": " + token.getLexeme());
    }
}
