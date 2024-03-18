package lexer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Lexer {
    private final PushbackReader source;
    private char current;
    private int line;
    private int column;
    private Token token;
    private final Map<String, Type> reserved;
    private final int TAB_SIZE = 4;

    public Lexer(String source) throws FileNotFoundException {
        this.source = new PushbackReader(new FileReader(source));
        reserved = new HashMap<String, Type>();
        reserved.put("struct", Type.STRUCT);
        reserved.put("impl", Type.IMPL);
        reserved.put("start", Type.START);
        reserved.put("if", Type.IF);
        reserved.put("else", Type.ELSE);
        reserved.put("fn", Type.FN);
        reserved.put("ret", Type.RET);
        reserved.put("st", Type.SCOPE);
        reserved.put("priv", Type.SCOPE);
        reserved.put("new", Type.NEW);
        reserved.put("self", Type.STRUCT_ID);
        reserved.put("true", Type.BOOL_LITERAL);
        reserved.put("false", Type.BOOL_LITERAL);
        reserved.put("void", Type.TYPE);
        reserved.put("Int", Type.TYPE);
        reserved.put("Str", Type.TYPE);
        reserved.put("Char", Type.TYPE);
        reserved.put("Bool", Type.TYPE);
        reserved.put("nil", Type.NIL);
        reserved.put("Array", Type.ARRAY);
    }

    private void state() {
        switch (current) {
            case '\t':
                column += TAB_SIZE;
                break;
            case '\r':
                column = 0;
                break;
            case '\n':
                line++;
                column = 0;
                break;
            case '\u000B':
                line++;
                break;
            default:
                column++;
                break;
        }
    }

    private void read() throws IOException {
        current = (char) source.read();
    }

    private void unread() throws IOException {
        source.unread(current);
    }

    private void tokenize() {
        token.append(current);
        state();
    }

    public Token scan() throws IOException, LexerException {
        while (true) {
            read();
            token = new Token(line, column);
            tokenize();
            if (Character.isWhitespace(current) || Character.isISOControl(current)) continue;
            switch (current) {
                case '.':
                    token.setType(Type.DOT);
                    break;
                case ':':
                    token.setType(Type.COLON);
                    break;
                case ',':
                    token.setType(Type.COMMA);
                    break;
                case ';':
                    token.setType(Type.SEMICOLON);
                    break;
                case '(':
                    token.setType(Type.OPEN_PARENTHESIS);
                    break;
                case ')':
                    token.setType(Type.CLOSE_PARENTHESIS);
                    break;
                case '[':
                    token.setType(Type.OPEN_SQUARE_BRACKET);
                    break;
                case ']':
                    token.setType(Type.CLOSE_SQUARE_BRACKET);
                    break;
                case '{':
                    token.setType(Type.OPEN_CURLY_BRACKET);
                    break;
                case '}':
                    token.setType(Type.CLOSE_CURLY_BRACKET);
                    break;
                case '*':
                    token.setType(Type.MULTIPLICATION);
                    break;
                case '%':
                    token.setType(Type.MODULO);
                    break;
                case '\uffff':
                    throw new EOFException();
                case '/':
                    read();
                    if (current == '?') {
                        scanComment();
                        continue;
                    } else {
                        unread();
                        token.setType(Type.DIVISION);
                        break;
                    }
                case '=':
                    read();
                    if (current == '=') {
                        tokenize();
                        token.setType(Type.EQUALS);
                    } else {
                        unread();
                        token.setType(Type.ASSIGNMENT);
                    }
                    break;
                case '<':
                    read();
                    if (current == '=') {
                        tokenize();
                        token.setType(Type.LESS_THAN_OR_EQUALS);
                    } else {
                        unread();
                        token.setType(Type.LESS_THAN);
                    }
                    break;
                case '>':
                    tokenize();
                    read();
                    if (current == '=') {
                        tokenize();
                        token.setType(Type.GRATER_THAN_OR_EQUALS);
                    } else {
                        unread();
                        token.setType(Type.GREATER_THAN);
                    }
                    break;
                case '!':
                    tokenize();
                    read();
                    if (current == '=') {
                        tokenize();
                        token.setType(Type.NOT_EQUALS);
                    } else {
                        unread();
                        token.setType(Type.NOT);
                    }
                    break;
                case '-':
                    read();
                    if (current == '>') {
                        tokenize();
                        token.setType(Type.RETURN_TYPE);
                    } else if (current == '-') {
                        tokenize();
                        token.setType(Type.DECREMENT);
                    } else {
                        unread();
                        token.setType(Type.MINUS);
                    }
                    break;
                case '+':
                    tokenize();
                    read();
                    if (current == '+') {
                        tokenize();
                        token.setType(Type.INCREMENT);
                    } else {
                        unread();
                        token.setType(Type.PLUS);
                    }
                    break;
                case '&':
                    read();
                    if (current == '&') {
                        tokenize();
                        token.setType(Type.AND);
                    } else {
                        unread();
                        token.setType(Type.BAD_TOKEN);
                        throw new LexerException(token, "Caracter ilegal");
                    }
                    break;
                case '|':
                    read();
                    if (current == '|') {
                        tokenize();
                        token.setType(Type.OR);
                    } else {
                        unread();
                        token.setType(Type.BAD_TOKEN);
                        throw new LexerException(token, "Caracter ilegal");
                    }
                    break;
                case '\'':
                    scanChar();
                    token.setType(Type.CHAR_LITERAL);
                    break;
                case '"':
                    scanStr();
                    token.setType(Type.STR_LITERAL);
                    break;
                default:
                    if (rA_Z(current)) {
                        scanStructId();
                        token.setType(reserved.getOrDefault(token.getLexeme().toString(), Type.STRUCT_ID));
                    } else if (ra_z(current)) {
                        scanMemberID();
                        token.setType(reserved.getOrDefault(token.getLexeme().toString(), Type.MEMBER_ID));
                    } else if (current == '0') {
                        token.setType(Type.INT_LITERAL);
                    } else if (r0_9(current)) {
                        scanInt();
                        token.setType(Type.INT_LITERAL);
                    } else {
                        token.setType(Type.BAD_TOKEN);
                        throw new LexerException(token, "Caracter ilegal");
                    }
            }
            return token;
        }
    }

    public static boolean r0_9(char match) {
        return match >= '0' && match <= '9';
    }


    private boolean rA_Z(char match) {
        return match >= 'A' && match <= 'Z';
    }

    private boolean ra_z(char match) {
        return match >= 'a' && match <= 'z';
    }

    private boolean alphabet(char match) {
        return switch (match) {
            case '#', '$', '%', '&', '@', '¿', '¡', '!', '?', 'ñ', '.', ':', ',', ';', '(', ')', '[', ']', '{', '}', '=', '+', '-', '*', '/', '|', '>', '<', ' ', '_' ->
                    true;
            default -> r0_9(match) || ra_z(match) || rA_Z(match);
        };
    }

    private boolean escape(char match) {
        return switch (match) {
            case 'b', 'f', 'n', 'r', 't', 'v', '\'', '"', '\\' -> true;
            default -> false;
        };
    }

    private void scanInt() throws IOException {
        read();
        while (r0_9(current)) {
            tokenize();
            read();
        }
        unread();
    }

    private void scanMemberID() throws IOException {
        read();
        while (ra_z(current) || rA_Z(current) || r0_9(current) || current == '_') {
            tokenize();
            read();
        }
        unread();
    }

    private void scanStructId() throws IOException, LexerException {
        scanMemberID();
        var last = token.getLexeme().charAt(token.getLexeme().length() - 1);
        if (r0_9(last) || last == '_') {
            token.setType(Type.BAD_STRUCT_ID);
            throw new LexerException(token, "Ultimo caracter de ID de Struct ilegal");
        }
    }

    private void scanStr() throws IOException, LexerException {
        read();
        while (current != '"') {
            if (current == '\\') {
                tokenize();
                read();
                tokenize();
                if (!escape(current)) {
                    token.setType(Type.BAD_STR_LITERAL);
                    throw new LexerException(token, "Caracter de escape ilegal en literal de Str");
                }
            } else if (current == '\uffff' || current == '\n') {
                token.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(token, "Literal de Str no cerrado");
            } else if (!alphabet(current)) {
                tokenize();
                token.setType(Type.BAD_STR_LITERAL);
                throw new LexerException(token, "Caracter ilegal en literal de Str");
            } else tokenize();
            read();
        }
        tokenize();
    }

    private void scanChar() throws IOException, LexerException {
        read();
        if (current == '\\') {
            tokenize();
            read();
            tokenize();
            if (!escape(current)) {
                token.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(token, "Caracter de escape ilegal en literal de Char");
            }
        } else if (current == '\uffff' || current == '\n') {
            token.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(token, "Literal de Char no cerrado");
        } else if (!alphabet(current)) {
            tokenize();
            token.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(token, "Caracter ilegal en literal de Char");
        }
        read();
        tokenize();
        if (current != '\'') {
            token.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(token, "Literal de Char no cerrado");
        }
    }

    private void scanComment() throws IOException {
        while (current != '\n' && current != '\uffff') {
            read();
            state();
        }
    }
}
