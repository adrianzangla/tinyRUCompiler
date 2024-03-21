package executor;

import lexer.Lexer;
import lexer.LexerException;
import lexer.Source;
import lexer.Token;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Executor class for executing lexical analysis.
 */
public class Executor {
    Lexer l;
    String of;
    final String CORRECT = "CORRECTO: ANALISIS LEXICO\n";
    final String CORRECTHEADER = "| TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |\n";
    final String ERRORHEADER = "| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |\n";
    final String ERROR = "ERROR: LEXICO\n";
    final String ROW = "|%s|\n";
    List<Token> tl = new LinkedList<>();

    /**
     * Constructor for Executor class.
     *
     * @param sf Path to the source file to be analyzed.
     * @throws FileNotFoundException If the source file is not found.
     */
    public Executor(String sf) throws FileNotFoundException {
        Source s = null;
        s = new Source(sf);
        l = new Lexer(s);
        of = sf.replace(".ru", ".txt");
    }

    /**
     * Constructor for Executor class.
     *
     * @param sf Path to the source file to be analyzed.
     * @param of Path to the output file for logging analysis results.
     * @throws FileNotFoundException If the source file is not found.
     */
    public Executor(String sf, String of) throws FileNotFoundException {
        Source s = new Source(sf);
        l = new Lexer(s);
        this.of = of;
    }

    /**
     * Executes lexical analysis on the provided source file.
     */
    public void execute() {
        try {
            while (true) {
                var token = l.scan();
                tl.add(token);
            }
        } catch (EOFException e) {
            System.out.println(CORRECT);
            log();
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo del codigo fuente");
        } catch (LexerException e) {
            System.out.println(ERROR);
            log(e);
        }
    }

    /**
     * Logs the analysis results to an output file.
     */
    private void log() {
        try (var writer = new BufferedWriter(new FileWriter(of))) {
            writer.write(CORRECT);
            writer.write(CORRECTHEADER);
            for (var t : tl) {
                writer.write(String.format(ROW, t.toString()));
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo " + of);
        }
    }

    /**
     * Logs the analysis error to an output file.
     * @param le The LexerException that occurred during analysis.
     */
    private void log(LexerException le) {
        try (var writer = new BufferedWriter(new FileWriter(of))) {
            writer.write(ERROR);
            writer.write(ERRORHEADER);
            writer.write(String.format(ROW, le.toString()));
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo " + of);
        }
    }
}
