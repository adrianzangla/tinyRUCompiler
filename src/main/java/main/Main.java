package main;

import executor.Executor;
import lexer.LexerException;
import lexer.Token;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Uso: java -jar etapa1.jar <ARCHIVO_FUENTE> [<ARCHIVO_SALIDA>]");
            return;
        }
        Executor executor = null;
        try {
            if (args.length < 2) executor = new Executor(args[0]);
            else executor = new Executor(args[0], args[1]);
            executor.analize();
            executor.report();
        } catch (FileNotFoundException err) {
            System.err.println("Archivo no encontrado");
        } catch (IOException err) {
            System.err.println(err.getMessage());
            System.err.println("Error operando en el archivo");
        } catch (LexerException e) {
            executor.report(e);
        }
    }
}
