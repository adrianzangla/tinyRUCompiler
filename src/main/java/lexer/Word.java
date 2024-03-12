package lexer;

public class Word {
    private final String lexeme;
    private final Tag tag;

    public Word(String l, Tag t) {
        lexeme = l;
        tag = t;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Tag getTag() {
        return tag;
    }
}
