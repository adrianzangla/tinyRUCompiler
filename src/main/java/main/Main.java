package main;

import executor.Executor;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var executor = new Executor(args[0]);
        executor.execute();
    }
}
