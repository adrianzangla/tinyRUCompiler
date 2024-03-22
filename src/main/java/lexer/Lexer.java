package lexer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The Lexer class is responsible for tokenizing source code input. It scans the input characters
 * and generates tokens based on predefined rules. Each token represents a lexical element of the
 * source code, such as identifiers, literals, and keywords.
 * <p>
 * The Lexer recognizes various types of tokens, including integers, strings, characters,
 * keywords, and punctuation symbols, among others.
 */
public class Lexer {

    /** The source to be tokenized. */
    final Source s;
    /**
     * A mapping of reserved words to their corresponding token types.
     * This map is used during tokenization to recognize keywords and special symbols.
     */
    final Map<String, Type> rw;

    /**
     * Constructs a Lexer object with the provided source.
     *
     * @param s The source to be tokenized by the Lexer.
     * @throws FileNotFoundException If the provided source file cannot be found.
     */
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
        rw.put(".", Type.DOT);
        rw.put(":", Type.COLON);
        rw.put(",", Type.COMMA);
        rw.put(";", Type.SEMICOLON);
        rw.put("(", Type.OPEN_PARENTHESIS);
        rw.put(")", Type.CLOSE_PARENTHESIS);
        rw.put("[", Type.OPEN_SQUARE_BRACKET);
        rw.put("]", Type.CLOSE_SQUARE_BRACKET);
        rw.put("{", Type.OPEN_CURLY_BRACKET);
        rw.put("}", Type.CLOSE_CURLY_BRACKET);
        rw.put("*", Type.MULTIPLICATION);
        rw.put("%", Type.MODULO);
        rw.put("/", Type.DIVISION);
        rw.put("&&", Type.AND);
        rw.put("||", Type.OR);
        rw.put("+", Type.PLUS);
        rw.put("++", Type.INCREMENT);
        rw.put("-", Type.MINUS);
        rw.put("--", Type.DECREMENT);
        rw.put("->", Type.RETURN_TYPE);
        rw.put("!", Type.NOT);
        rw.put("!=", Type.NOT_EQUALS);
        rw.put("=", Type.ASSIGNMENT);
        rw.put("==", Type.EQUALS);
        rw.put("<=", Type.LESS_THAN_OR_EQUALS);
        rw.put(">=", Type.GRATER_THAN_OR_EQUALS);
        rw.put(">", Type.GREATER_THAN);
        rw.put("<", Type.LESS_THAN);
    }

    /**
     * Scans the input source and returns the next token encountered.
     *
     * @return The next token in the source code.
     * @throws IOException    If an I/O error occurs while reading the source.
     * @throws LexerException If a lexical error occurs during tokenization.
     */
    public Token scan() throws IOException, LexerException {
        char c = s.next();
        Token t = new Token(s.ln, s.cn);
        if (r0_9(c)) {
            t = scanInt(c);
        } else if (rA_Z(c)) {
            t = scanStructId(c);
            t.setType(rw.getOrDefault(t.getLexeme(), Type.STRUCT_ID));
        } else if (ra_z(c)) {
            t = scanMemberId(c);
            t.setType(rw.getOrDefault(t.getLexeme(), Type.MEMBER_ID));
        } else if (c == '\'') {
            t = scanChar(c);
        } else if (c == '"') {
            t = scanStr(c);
        } else {
            t.append(c);
            var t2 = new Token(t.getLexeme(), s.ln, s.cn);
            c = s.read();
            t2.append(c);
            if (rw.containsKey(t2.getLexeme())) {
                t2.setType(rw.get(t2.getLexeme()));
                t = t2;
            } else {
                s.unread(c);
                if (rw.containsKey(t.getLexeme())) t.setType(rw.get(t.getLexeme()));
                else {
                    t.setType(Type.BAD_TOKEN);
                    throw new LexerException(t, "Caracter ilegal");
                }
            }
        }
        return t;
    }

    /**
     * Checks if the provided character is a digit (0-9).
     *
     * @param match The character to be checked.
     * @return True if the character is a digit, otherwise false.
     */
    private static boolean r0_9(char match) {
        return match >= '0' && match <= '9';
    }

    /**
     * Checks if the provided character is a lowercase letter (a-z).
     *
     * @param c The character to be checked.
     * @return True if the character is a lowercase letter, otherwise false.
     */
    private boolean ra_z(char c) {
        return c >= 'a' && c <= 'z';
    }

    /**
     * Checks if the provided character is an uppercase letter (A-Z).
     *
     * @param match The character to be checked.
     * @return True if the character is an uppercase letter, otherwise false.
     */
    private boolean rA_Z(char match) {
        return match >= 'A' && match <= 'Z';
    }

    /**
     * Checks if the provided character is an escape character.
     *
     * @param match The character to be checked.
     * @return True if the character is an escape character, otherwise false.
     */
    private boolean escape(char match) {
        return switch (match) {
            case 'b', 'f', 'n', 'r', 't', 'v', '\'', '"', '\\' -> true;
            default -> false;
        };
    }

    /**
     * Checks if the provided character is a valid character for the source code.
     *
     * @param match The character to be checked.
     * @return True if the character is a valid character, otherwise false.
     */
    private boolean alphabet(char match) {
        return switch (match) {
            case '#', '^', '$', '%', '&', '@', '¿', '¡', '!', '?', 'ñ', 'Ñ', '.', ':', ',', ';', '(', ')', '[', ']', '{', '}', '=', '+', '-', '*', '/', '|', '>', '<', ' ', '_', '\\', '\'', '"' ->
                    true;
            default -> r0_9(match) || ra_z(match) || rA_Z(match);
        };
    }

    /**
     * Scans the input source and returns a token representing an integer literal.
     *
     * @param c The first character of the integer literal.
     * @return A token representing the scanned integer literal.
     * @throws IOException If an I/O error occurs while reading the source.
     */
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

    /**
     * Scans the input source and returns a token representing a struct identifier.
     *
     * @param c The first character of the struct identifier.
     * @return A token representing the scanned struct identifier.
     * @throws IOException    If an I/O error occurs while reading the source.
     * @throws LexerException If a lexical error occurs during tokenization.
     */
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
            throw new LexerException(t, "Identificador de struct invalido");
        }
        t.setType(rw.getOrDefault(t.getLexeme(), Type.STRUCT_ID));
        return t;
    }

    /**
     * Scans the input source and returns a token representing a member identifier.
     *
     * @param c The first character of the member identifier.
     * @return A token representing the scanned member identifier.
     * @throws IOException If an I/O error occurs while reading the source.
     */
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

    /**
     * Scans the input source and returns a token representing a character literal.
     *
     * @param c The first character of the character literal.
     * @return A token representing the scanned character literal.
     * @throws IOException    If an I/O error occurs while reading the source.
     * @throws LexerException If a lexical error occurs during tokenization.
     */
    private Token scanChar(char c) throws IOException, LexerException {
        Token t = new Token(s.ln, s.cn);
        int l = 0;
        do {
            l++;
            t.append(c);
            c = s.read();
            if (c == '\n' || c == '\uffff') {
                t.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(t, "Literal de Char sin cerrar");
            }
            if (!alphabet(c)) {
                t.append(c);
                t.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(t, "Caracter ilegal en literal de Char");
            }
            if (c == '\\' && t.getLexeme().charAt(t.getLexeme().length() - 1) != '\\') {
                t.append(c);
                c = s.read();
                t.append(c);
                l++;
                if (!escape(c)) {
                    t.setType(Type.BAD_CHAR_LITERAL);
                    throw new LexerException(t, "Caracter de escape ilegal en literal de Char");
                }
                c = s.read();
            }
            if (l - 1 > 1) {
                t.setType(Type.BAD_CHAR_LITERAL);
                throw new LexerException(t, "Demasiados caracteres en literal de Char");
            }
        } while (c != '\'');
        t.append(c);
        if (l - 1 == 0) {
            t.setType(Type.BAD_CHAR_LITERAL);
            throw new LexerException(t, "Caracter nulo en literal de Char");
        }
        t.setType(Type.CHAR_LITERAL);
        return t;
    }

    /**
     * Scans the input source and returns a token representing a string literal.
     *
     * @param c The first character of the string literal.
     * @return A token representing the scanned string literal.
     * @throws IOException    If an I/O error occurs while reading the source.
     * @throws LexerException If a lexical error occurs during tokenization.
     */
    private Token scanStr(char c) throws IOException, LexerException {
        Token t = new Token(s.ln, s.cn);
        int l = 0;
        do {
            t.append(c);
            c = s.read();
            if (c == '\n' || c == '\uffff') {
                t.setType(Type.BAD_STR_LITERAL);
                throw new LexerException(t, "Literal de Str sin cerrar");
            }
            if (!alphabet(c)) {
                t.append(c);
                t.setType(Type.BAD_STR_LITERAL);
                throw new LexerException(t, "Caracter ilegal en literal de Str");
            }
            if (c == '\\' && t.getLexeme().charAt(t.getLexeme().length() - 1) != '\\') {
                t.append(c);
                c = s.read();
                t.append(c);
                l++;
                if (!escape(c)) {
                    t.setType(Type.BAD_STR_LITERAL);
                    throw new LexerException(t, "Caracter de escape ilegal en literal de Str");
                }
                c = s.read();
            }
            if (l - 1 > 1024) {
                t.setType(Type.BAD_STR_LITERAL);
                throw new LexerException(t, "Demasiados caracteres en literal de Str");
            }
        } while (c != '"');
        t.append(c);
        t.setType(Type.STR_LITERAL);
        return t;
    }
}
