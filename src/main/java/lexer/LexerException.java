package lexer;

public class LexerException extends Throwable {

    private Token token;
    private String message;

    public LexerException(Token token, String message) {
        super();
        this.token = token;
        this.message = message;
    }

    public Token getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
