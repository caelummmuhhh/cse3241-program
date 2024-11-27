package Helpers;

import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;

public class InputVerifier {
    /**
     * Determines if a string is an integer.
     * 
     * @param str - the string to check
     * @return whether or not the string is an integer
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();
        if (length == 0) {
            return false;
        }

        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }

        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }

    /**
     * Get a valid integer input with an allowable valid range.
     * 
     * @param scanner - the scanner to read values from
     * @param prompt - the prompt to the entity (user) to read the integer from
     * @param min - the minimum value allowed, inclusive
     * @param max - the maximum value allowed, inclusive
     * @return - the user inputted valid value.
     */
    public static int getValidIntegerInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                String input = scanner.nextLine();
                System.out.println(
                        "Unknown option: \"" + input + "\"\n" +
                                "Please enter a number...\n");
                continue;
            }

            int intInput = scanner.nextInt();
            if (intInput < min || intInput > max) {
                System.out.println(
                        "Unknown option: \"" + intInput + "\"\n" +
                                "Value outside of valid range, please choose number between "
                                + min + "-" + max + "\n");
                continue;
            }
            
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            return intInput;
        }
    }

    /**
     * Get a valid integer input with valid values.
     * 
     * @param scanner - the scanner to read values from
     * @param prompt - the prompt to the entity (user) to read the integer from
     * @param validValues - a list of valid values, if user enters int not in list, keep prompting
     * @return - the user inputted valid value.
     */
    public static int getValidIntegerInput(Scanner scanner, String prompt, ArrayList<Integer> validValues) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                String input = scanner.nextLine();
                System.out.println(
                        "Unknown option: \"" + input + "\"\n" +
                                "Please enter a number...\n");
                continue;
            }

            int intInput = scanner.nextInt();
            if (!validValues.contains(intInput)) {
                System.out.println(
                        "Unknown option: \"" + intInput + "\"\n" +
                                "Value outside of valid domain. ");
                continue;
            }
            
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            return intInput;
        }
    }
    
    /**
     * Prompts the user for a date until a valid response.
     * 
     * @param scanner - the scanner to read values from
     * @param prompt - the prompt to the entity (user) to read the date from
     * @return the user inputted valid date
     */
    public static Date promptValidDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String userInp = scanner.nextLine();
            if (userInp.trim().isEmpty()) {
                System.out.println("Please enter a valid date with the format yyyy-mm-dd (e.g. 2022-02-22).\n");
                continue;
            }

            Date parsedDate = DataTypeHelpers.parseDateString(userInp);
            if (parsedDate == null) {
                System.out.println("Please enter a valid date with the format yyyy-mm-dd (e.g. 2022-02-22).\n");
                continue;
            }

            return parsedDate;
        }
    }

    /**
     * Prompts the user for a boolean value (yes or no) until valid response.
     * 
     * @param scanner  - the scanner to read values from
     * @param prompt - the prompt to the entity (user) to read the boolean from
     * @return the user inputed value
     */
    public static boolean promptBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String userInp = scanner.nextLine();
            userInp = userInp.toLowerCase();
            if (userInp.startsWith("y")) {
                return true;
            }
            if (userInp.startsWith("n")) {
                return false;
            }

            System.out.println("Please enter a valid value (y/n).");
        }
    }
}
