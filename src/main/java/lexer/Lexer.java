package lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    private final BufferedReader source;
    int line = 0;
    int column = 0;
    char current;
    Hashtable<String, Word> words = new Hashtable<>();

    void reserve(Word w) {
        words.put(w.getLexeme(), w);
    }

    public Lexer(BufferedReader source) {
        reserve(new Word("struct", Tag.STRUCT));
        reserve(new Word("impl", Tag.IMPL));
        reserve(new Word("start", Tag.START));
        reserve(new Word("pri", Tag.PRIV));
        reserve(new Word("st", Tag.ST));
        reserve(new Word("Int", Tag.BASIC));
        reserve(new Word("void", Tag.BASIC));
        reserve(new Word("Str", Tag.BASIC));
        reserve(new Word("Bool", Tag.BASIC));
        reserve(new Word("Char", Tag.BASIC));
        reserve(new Word("self", Tag.SELF));
        reserve(new Word("new", Tag.NEW));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("ret", Tag.RET));
        reserve(new Word("true", Tag.TRUE));
        reserve(new Word("false", Tag.FALSE));
        reserve(new Word("nil", Tag.NIL));
        reserve(new Word("Object", Tag.ID));
        reserve(new Word("IO", Tag.ID));
        reserve(new Word("Array", Tag.ARRAY));
        reserve(new Word("fn", Tag.FN));
        this.source = source;
    }

    void read() throws IOException {
        current = (char) source.read();
        if (current == '\n' || current == '\r' || current == '\013') {
            line++;
            column = 0;
        } else if (current == '\t') column = column + 8;
        else column++;
    }

    char peek() throws IOException {
        source.mark(1);
        char peeked = (char) source.read();
        source.reset();
        return peeked;
    }

    boolean peek(char c) throws IOException {
        return c == peek();
    }

    boolean validIdentifier(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
    }

    public Token scan() throws LexerException, IOException {
        do {
            read();
            if (Character.isLetter(current)) {
                boolean valid = validIdentifier(current);
                StringBuilder buffer = new StringBuilder();
                int startLine = line;
                int startColumn = column;
                buffer.append(current);
                char peeked = peek();
                while (Character.isLetterOrDigit(peeked) || peeked == '_') {
                    read();
                    buffer.append(current);
                    peeked = peek();
                    if (valid) valid = validIdentifier(current);
                }
                String lexeme = buffer.toString();
                char lastChar = buffer.charAt(buffer.length() - 1);
                if (lastChar == '_' || (lastChar >= '0' && lastChar <= '9') || !valid)
                    throw new LexerException(new Token(lexeme, Tag.BAD_TOKEN, startLine, startColumn), "IDENTIFICADOR NO VALIDO " + lexeme);
                else {
                    Word word = words.get(lexeme);
                    if (word == null) {
                        word = new Word(lexeme, Tag.ID);
                        words.put(word.getLexeme(), word);
                    }
                    return new Token(word, startLine, startColumn);
                }
            }
            if (Character.isDigit(current)) {
                char peeked = peek();
                boolean valid = current != '0' || !Character.isDigit(peeked);
                StringBuilder buffer = new StringBuilder();
                int startLine = line;
                int startColumn = column;
                buffer.append(current);
                while (Character.isDigit(peeked)) {
                    read();
                    buffer.append(current);
                    peeked = peek();
                }
                String lexeme = buffer.toString();
                Token token = new Token(lexeme, Tag.INT, startLine, startColumn);
                if (valid) return token;
                else throw new LexerException(token, "LITERAL ENTERO NO VALIDO " + lexeme);
            }
            StringBuilder buffer;
            int startLine;
            int startColumn;
            char peeked;
            switch (current) {
                case '\uffff':
                    return new Token("\0", Tag.EOF, line, column);
                case '/':
                    if (peek('?')) {
                        read();
                        peeked = peek();
                        while (peeked != '\n' && peeked != '\r' && peeked != '\013') {
                            read();
                            peeked = peek();
                        }
                        continue;
                    } else return new Token("/", Tag.DIV, line, column);
                case '=':
                    if (peek('=')) {
                        read();
                        return new Token("==", Tag.EQ, line, column - 1);
                    } else return new Token("=", Tag.ASSIGNMENT, line, column);
                case '!':
                    if (peek('=')) {
                        read();
                        return new Token("!=", Tag.NE, line, column - 1);
                    } else return new Token("!", Tag.NOT, line, column);
                case '<':
                    if (peek('=')) {
                        read();
                        return new Token("<=", Tag.LE, line, column - 1);
                    } else return new Token("<", Tag.LT, line, column);
                case '>':
                    if (peek('=')) {
                        read();
                        return new Token(">=", Tag.GE, line, column - 1);
                    } else return new Token(">", Tag.GT, line, column);
                case '+':
                    if (peek('+')) {
                        read();
                        return new Token("++", Tag.INCR, line, column - 1);
                    } else return new Token("+", Tag.PLUS, line, column);
                case '-':
                    if (peek('-')) {
                        read();
                        return new Token("--", Tag.DECR, line, column - 1);
                    } else if (peek('>')) {
                        read();
                        return new Token("->", Tag.RET_TYPE, line, column - 1);
                    } else return new Token("-", Tag.MINUS, line, column);
                case '*':
                    return new Token("*", Tag.MULT, line, column);
                case '%':
                    return new Token("%", Tag.MOD, line, column);
                case '&':
                    if (peek('&')) {
                        read();
                        return new Token("&", Tag.AND, line, column - 1);
                    } else
                        throw new LexerException(new Token("&", Tag.BAD_TOKEN, line, column), "IDENTIFICADOR NO VALIDO");
                case '|':
                    if (peek('|')) {
                        read();
                        return new Token("|", Tag.OR, line, column - 1);
                    } else
                        throw new LexerException(new Token("|", Tag.BAD_TOKEN, line, column), "IDENTIFICADOR NO VALIDO");
                case '.':
                    return new Token(".", Tag.CONSTR, line, column);
                case ',':
                    return new Token(",", Tag.COMMA, line, column);
                case ';':
                    return new Token(";", Tag.SC, line, column);
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                case '\013':
                    continue;
                case '(':
                    return new Token("(", Tag.OP, line, column);
                case ')':
                    return new Token(")", Tag.CP, line, column);
                case '[':
                    return new Token("[", Tag.OB, line, column);
                case ']':
                    return new Token("]", Tag.CB, line, column);
                case '{':
                    return new Token("{", Tag.OC, line, column);
                case '}':
                    return new Token("}", Tag.CC, line, column);
                case '\'':
                    buffer = new StringBuilder();
                    buffer.append(current);
                    startLine = line;
                    startColumn = column;
                    peeked = peek();
                    while (peeked != '\'' && peeked != '\uffff') {
                        read();
                        buffer.append(current);
                        peeked = peek();
                    }
                    if (peeked == '\'') {
                        read();
                        buffer.append(current);
                        String lexeme = buffer.toString();
                        if (lexeme.length() > 3)
                            throw new LexerException(new Token(lexeme, Tag.BAD_TOKEN, line, column), "LITERAL NO VALIDO " + lexeme);
                        else return new Token(lexeme, Tag.CHAR, startLine, startColumn);
                    } else {
                        String lexeme = buffer.toString();
                        throw new LexerException(new Token(lexeme, Tag.BAD_TOKEN, startLine, startColumn), "LITERAL NO VALIDO " + lexeme);
                    }
                case '"':
                    buffer = new StringBuilder();
                    buffer.append(current);
                    startLine = line;
                    startColumn = column;
                    peeked = peek();
                    while (peeked != '"' && peeked != '\uffff') {
                        read();
                        buffer.append(current);
                        peeked = peek();
                    }
                    if (peeked == '"') {
                        read();
                        buffer.append(current);
                        String lexeme = buffer.toString();
                        return new Token(lexeme, Tag.STR, startLine, startColumn);
                    } else {
                        String lexeme = buffer.toString();
                        throw new LexerException(new Token(lexeme, Tag.BAD_TOKEN, startLine, startColumn), "LITERAL NO VALIDO " + lexeme);
                    }
                default:
                    throw new LexerException(new Token(String.valueOf(current), Tag.BAD_TOKEN, line, column), "IDENTIFICADOR NO VALIDO " + current);
            }
        } while (true);
    }
}
