package main;

import executor.Executor;

import java.io.FileNotFoundException;

/**
 * Main class for executing the program.
 */
public class Main {
    /**
     * Main method to start the program.
     * @param args Command-line arguments. The first argument should be the filename to execute.
     */
    public static void main(String[] args) {
        // Setting a default filename for testing purposes
//        args[0] = "test.ru";
        System.out.println("Inicio");
        try {
            System.out.println("Ejecutando analisis lexico...");
            new Executor(args[0]).execute();
        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo " + args[0]);
        } finally {
            System.out.println("Fin");
        }
    }
}
