package ui;

import java.util.Date;
import java.util.Scanner;

public class Prompt {
    private static Scanner scanner = new Scanner(System.in);
    
    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
    
    public static int promptInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static Date promptDate(String message) {
        while (true) {
            System.out.print(message);
            try {
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return new java.text.SimpleDateFormat("dd/MM/yyyy").parse(input);
            } else {
                System.out.println("Invalid input. Please enter a date in the format dd/MM/yyyy.");
            }
            } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    public static Float promptFloat(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid float number. Please try again.");
            }
        }
    }
}
