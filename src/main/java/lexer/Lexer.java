package lexer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    final Source s;
    final Map<String, Type> rw;

    public Lexer(Source s) throws FileNotFoundException {
        this.s = s;
        rw = new HashMap<String, Type>();
        rw.put("struct", Type.STRUCT);
        rw.put("impl", Type.IMPL);
        rw.put("start", Type.START);
        rw.put("if", Type.IF);
        rw.put("else", Type.ELSE);
        rw.put("while", Type.WHILE);
        rw.put("fn", Type.FN);
        rw.put("ret", Type.RET);
        rw.put("st", Type.SCOPE);
        rw.put("priv", Type.SCOPE);
        rw.put("new", Type.NEW);
        rw.put("self", Type.STRUCT_ID);
        rw.put("true", Type.BOOL_LITERAL);
        rw.put("false", Type.BOOL_LITERAL);
        rw.put("void", Type.TYPE);
        rw.put("Int", Type.TYPE);
        rw.put("Str", Type.TYPE);
        rw.put("Char", Type.TYPE);
        rw.put("Bool", Type.TYPE);
        rw.put("nil", Type.NIL);
        rw.put("Array", Type.ARRAY);
    }

    private char whitespace(char c) throws IOException {
        while (Character.isISOControl(c) || Character.isWhitespace(c)) {
            c = s.read();
            if (c == '\uffff') throw new EOFException();
        }
        return comment(c);
    }

    private char comment(char c) throws IOException {
        if (c == '/') {
            c = s.read();
            if (c == '?') {
                do {
                    c = s.read();
                    if (c == '\uffff') throw new EOFException();
                } while (c != '\n');
                c = whitespace(c);
            } else {
                s.unread(c);
                c = '/';
            }
        }
        return c;
    }

    public Token scan() throws IOException, LexerException {
        char c = whitespace(s.read());
        var t = new Token(s.ln, s.cn);
        if (r0_9(c)) {
            t = scanInt(c);
        } else if (rA_Z(c)) {
            t = scanStructId(c);
        } else if (ra_z(c)) {
            t = scanMemberId(c);
        } else if (c == '\'') {
            t = scanChar(c);
        } else if (c == '"') {
            t = scanStr(c);
        } else {
            t.append(c);
            switch (c) {
                case '.':
                    t.setType(Type.DOT);
                    break;
                case ':':
                    t.setType(Type.COLON);
                    break;
                case ',':
                    t.setType(Type.COMMA);
                    break;
                case ';':
                    t.setType(Type.SEMICOLON);
                    break;
                case '(':
                    t.setType(Type.OPEN_PARENTHESIS);
                    break;
                case ')':
                    t.setType(Type.CLOSE_PARENTHESIS);
                    break;
                case '[':
                    t.setType(Type.OPEN_SQUARE_BRACKET);
                    break;
                case ']':
                    t.setType(Type.CLOSE_SQUARE_BRACKET);
                    break;
                case '{':
                    t.setType(Type.OPEN_CURLY_BRACKET);
                    break;
                case '}':
                    t.setType(Type.CLOSE_CURLY_BRACKET);
                    break;
                case '*':
                    t.setType(Type.MULTIPLICATION);
                    break;
                case '%':
                    t.setType(Type.MODULO);
                    break;
                case '\uffff':
                    throw new EOFException();
                case '/':
                    t.setType(Type.DIVISION);
                    break;
                case '=':
                    c = s.read();
                    if (c == '=') {
                        t.append(c);
                        t.setType(Type.EQUALS);
                    } else {
                        s.unread(c);
                        t.setType(Type.ASSIGNMENT);
                    }
                    break;
                case '<':
                    c = s.read();
                    if (c == '=') {
                        t.append(c);
                        t.setType(Type.LESS_THAN_OR_EQUALS);
                    } else {
                        s.unread(c);
                        t.setType(Type.LESS_THAN);
                    }
                    break;
                case '>':
                    c = s.read();
                    if (c == '=') {
                        t.append(c);
                        t.setType(Type.GRATER_THAN_OR_EQUALS);
                    } else {
                        s.unread(c);
                        t.setType(Type.GREATER_THAN);
                    }
                    break;
                case '!':
                    c = s.read();
                    if (c == '=') {
                        t.append(c);
                        t.setType(Type.NOT_EQUALS);
                    } else {
                        s.unread(c);
                        t.setType(Type.NOT);
                    }
                    break;
                case '-':
                    c = s.read();
                    if (c == '>') {
                        t.append(c);
                        t.setType(Type.RETURN_TYPE);
                    } else if (c == '-') {
                        t.append(c);
                        t.setType(Type.DECREMENT);
                    } else {
                        s.unread(c);
                        t.setType(Type.MINUS);
                    }
                    break;
                case '+':
                    c = s.read();
                    if (c == '+') {
                        t.append(c);
                        t.setType(Type.INCREMENT);
                    } else {
                        s.unread(c);
                        t.setType(Type.PLUS);
                    }
                    break;
                case '&':
                    c = s.read();
                    if (c == '&') {
                        t.append(c);
                        t.setType(Type.AND);
                    } else {
                        s.unread(c);
                        t.setType(Type.BAD_TOKEN);
                        throw new LexerException(t, "Caracter ilegal");
                    }
                    break;
                case '|':
                    c = s.read();
                    if (c == '|') {
                        t.append(c);
                        t.setType(Type.OR);
                    } else {
                        s.unread(c);
                        t.setType(Type.BAD_TOKEN);
                        throw new LexerException(t, "Caracter ilegal");
                    }
                    break;
                default:
                    t.setType(Type.BAD_TOKEN);
                    throw new LexerException(t, "Caracter ilegal");
            }
        }
        return t;
    }

    public static boolean r0_9(char match) {
        return match >= '0' && match <= '9';
    }

    private boolean ra_z(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean rA_Z(char match) {
        return match >= 'A' && match <= 'Z';
    }

    private boolean escape(char match) {
        return switch (match) {
            case 'b', 'f', 'n', 'r', 't', 'v', '\'', '"', '\\' -> true;
            default -> false;
        };
    }

    private boolean alphabet(char match) {
        return switch (match) {
            case '#', '$', '%', '&', '@', '¿', '¡', '!', '?', 'ñ', '.', ':', ',', ';', '(', ')', '[', ']', '{', '}', '=', '+', '-', '*', '/', '|', '>', '<', ' ', '_', '\\', '\'', '"' ->
                    true;
            default -> r0_9(match) || ra_z(match) || rA_Z(match);
        };
    }

    private Token scanInt(char c) throws IOException {
        Token t = new Token(s.ln, s.cn);
        if (c != '0') {
            do {
                t.append(c);
                c = s.read();
            } while (r0_9(c));
            s.unread(c);
        } else t.append(c);
        t.setType(Type.INT_LITERAL);
        return t;
    }

    private Token scanStructId(char c) throws IOException, LexerException {
        Token t = new Token(s.ln, s.cn);
        do {
            t.append(c);
            c = s.read();
        } while (ra_z(c) || rA_Z(c) || r0_9(c) || c == '_');
        s.unread(c);
        var lastChar = t.getLexeme().charAt(t.getLexeme().length() - 1);
        if (r0_9(lastChar) || lastChar == '_') {
            t.setType(Type.BAD_STRUCT_ID);
            throw new LexerException(t, "Invalid struct identifier");
        }
        t.setType(rw.getOrDefault(t.getLexeme(), Type.STRUCT_ID));
        return t;
    }

    private Token scanMemberId(char c) throws IOException {
        Token t = new Token(s.ln, s.cn);
        do {
            t.append(c);
            c = s.read();
        } while (ra_z(c) || rA_Z(c) || r0_9(c) || c == '_');
        s.unread(c);
        t.setType(rw.getOrDefault(t.getLexeme(), Type.MEMBER_ID));
        return t;
    }

    private Token scanChar(char c) throws IOException, LexerException {
        Token t = new Token(s.ln, s.cn);
        boolean e;
        boolean b = false;
        int l = 0;
        do {
            t.append(c);
            e = c == '\\';
            c = s.read();
            if (c == '\n' || c == '\uffff') {
                t.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(t, "Literal de Char sin cerrar");
            }
            if (e && !escape(c)) {
                t.append(c);
                t.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(t, "Caracter de escape ilegal literal de Char");
            } else if (!alphabet(c)) b = true;
            else l++;
        } while ((e || c != '\''));
        t.append(c);
        if (l != 1) {
            t.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(t, "Demasiados caracteres en literal de Char");
        }
        if (b) {
            t.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(t, "Caracter ilegal literal de Char");
        }
        t.setType(Type.CHAR_LITERAL);
        return t;
    }

    private Token scanStr(char c) throws IOException, LexerException {
        Token t = new Token(s.ln, s.cn);
        boolean e;
        boolean b = false;
        int l = 0;
        do {
            t.append(c);
            e = c == '\\';
            c = s.read();
            if (c == '\n' || c == '\uffff') {
                t.setType(Type.BAD_STR_LITERAL);
                throw new LexerException(t, "Literal de Str sin cerrar");
            }
            if (e && !escape(c)) {
                t.setType(Type.BAD_STR_LITERAL);
                throw new LexerException(t, "Caracter de escape ilegal literal de Str");
            } else if (!alphabet(c)) b = true;
            else l++;
        } while (e || c != '"');
        t.append(c);
        if (l > 1024) {
            t.setType(Type.BAD_STR_LITERAL);
            throw new LexerException(t, "Demasiados caracteres en literal de Str");
        }
        if (b) {
            t.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(t, "Caracter ilegal literal de Str");
        }
        t.setType(Type.STR_LITERAL);
        return t;
    }
}
