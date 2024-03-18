package lexer;

public class LexerException extends Throwable {
    Token token;
    String message;
    public LexerException (Token token, String message) {
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
}
