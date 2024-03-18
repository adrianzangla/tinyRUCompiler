package lexer;

public class Lexeme {
    private StringBuilder value;
    private Type type;

    public Lexeme() {
        value = new StringBuilder();
    }

    public Lexeme(char value) {
        this.value = new StringBuilder(String.valueOf(value));
    }

    public Lexeme(String value) {
        this.value = new StringBuilder(value);
    }

    public Lexeme(char value, Type type) {
        this.value = new StringBuilder(String.valueOf(value));
        this.type = type;
    }

    public StringBuilder getValue() {
        return value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getType() {
        return type.name();
    }
}
