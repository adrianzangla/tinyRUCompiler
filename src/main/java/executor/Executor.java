package executor;

import lexer.Lexer;
import lexer.LexerException;
import lexer.Tag;
import lexer.Token;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Executor {
    private final Lexer lexer;
    private final String out;
    private List<Token> tokens;
    private final String rowFormat = "| %-10s | %-20s | LINEA %d (COLUMNA %d) |\n";


    public Executor(String in) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(in));
        lexer = new Lexer(reader);
        out = in.replace(".ru", ".txt");
    }

    public Executor(String in, String out) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(in));
        lexer = new Lexer(reader);
        this.out = out;
    }

    public void analize() throws LexerException, IOException {
        List<Token> tokenList = new LinkedList<>();
        Token token = null;
        do {
            token = lexer.scan();
            tokenList.add(token);
        } while (token.getTag() != Tag.BAD_TOKEN && token.getTag() != Tag.EOF);
        tokens = tokenList;
    }

    private BufferedWriter getWriter() throws IOException {
        File file = new File(out);
        if (file.exists()) file.delete();
        file.createNewFile();
        return new BufferedWriter(new FileWriter(file));
    }

    public void report() throws IOException {
        BufferedWriter writer = getWriter();
        writer.write("CORRECTO: ANALISIS LEXICO\n");
        for (Token t : tokens) {
            writer.write(String.format(rowFormat, t.getTag(), t.getLexeme(), t.getLine(), t.getColumn()));
        }
        writer.close();
    }

    public void report(LexerException e) throws IOException {
        BufferedWriter writer = getWriter();
        writer.write("ERROR: LEXICO\n");
        Token t = e.getToken();
        writer.write(String.format(rowFormat, t.getTag(), t.getLexeme(), t.getLine(), t.getColumn()));
        writer.close();
    }
}
