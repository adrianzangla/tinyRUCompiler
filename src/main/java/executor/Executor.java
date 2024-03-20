package executor;

import lexer.Lexer;
import lexer.LexerException;
import lexer.Source;
import lexer.Token;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Executor {
    Lexer l;
    String of;
    final String CORRECT = "CORRECTO: ANALISIS LEXICO\n";
    final String CORRECTHEADER = "| TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |\n";
    final String ERRORHEADER = "| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |\n";
    final String ERROR = "ERROR: LEXICO\n";
    final String ROW = "|%s|\n";
    List<Token> tl = new LinkedList<>();

    public Executor(String sf) {
        Source s = null;
        try {
            s = new Source(sf);
            l = new Lexer(s);
        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo");
        }
        of = sf.replace(".ru", ".txt");
    }

    public Executor(String sf, String of) throws FileNotFoundException {
        Source s = new Source(sf);
        l = new Lexer(s);
        this.of = of;
    }

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

    private void log(LexerException le) {
        try (var writer = new BufferedWriter(new FileWriter(of))) {
            writer.write(ERROR);
            writer.write(ERRORHEADER);
            writer.write(le.toString());
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo " + of);
        }
    }
}
