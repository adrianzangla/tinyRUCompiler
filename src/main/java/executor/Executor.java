package executor;

import lexer.Lexer;
import lexer.LexerException;
import lexer.Token;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Executor {
    private Lexer lexer;
    private String out;
    private final String CORRECT = "CORRECTO: ANALISIS LEXICO\n";
    private final String ERROR = "ERROR: LEXICO\n";
    private final String CORRECTHEADER = "| TOKEN                | LEXEMA                    | NUMERO DE LINEA (NUMERO DE COLUMNA) |\n";
    private final String ERRORHEADER = "| NUMERO DE LINEA: | NUMERO DE COLUMNA:     | DESCRIPCION: |\n";
    private final String CORRECTFORMAT = "| %-20s | %-25s | (%-2d, %-2d) |\n";
    private final String ERRORFORMAT = "| %-17d | %-22d | %-12s |\n";

    List<Token> tokens = new LinkedList<>();

    public Executor(String source) throws FileNotFoundException {
        lexer = new Lexer(source);
        out = source.replace(".ru", ".txt");
    }

    public Executor(String source, String out) throws FileNotFoundException {
        lexer = new Lexer(source);
        this.out = out;
    }

    private String stringify(String format, Token token) {
        return String.format(format, token.getType(), token.getLexeme().toString(), token.getLine(), token.getColumn());
    }

    public void execute() {
        try {
            while (true) {
                var token = lexer.scan();
                System.out.println(stringify(CORRECTFORMAT, token));
                tokens.add(token);
            }
        } catch (EOFException e) {
            System.out.println(CORRECT);
            output();
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo del codigo fuente");
        } catch (LexerException e) {
            System.out.println(ERROR);
            error(e.getToken());
        }
    }

    private void output() {
        try (var writer = new BufferedWriter(new FileWriter(out))) {
            writer.write(CORRECTHEADER);
            for (var token : tokens) {
                writer.write(stringify(CORRECTFORMAT, token));
            }
        } catch (IOException e) {
            System.out.println("Error escribiendo en el archivo " + out);
        }
    }

    private void error(Token token) {
        try (var writer = new BufferedWriter(new FileWriter(out))) {
            writer.write(ERRORHEADER);
            writer.write(stringify(ERRORFORMAT, token));
        } catch (IOException e) {
            System.out.println("Error escribiendo en el archivo " + out);
        }
    }


}
