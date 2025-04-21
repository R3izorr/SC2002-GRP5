package main;

public class Main {
    public static void main(String[] args) {
        ISystem system = new App();
        system.initialize();
        system.run();
    }
}