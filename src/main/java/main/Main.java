package main;

import executor.Executor;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        new Executor(args[0]).execute();
    }
}
