package Helpers;

import java.util.Collection;

public class PrintHelpers {
    public static void printHeader(String msg) {
        System.out.println(
            "\n------------------- " + msg + " -------------------"
        );
    }

    public static void printMenu(Collection<String> menuOptions) {
        System.out.println(
                "Please choose from one of the following options " +
                        "(enter the number corresponding to the desired option):");

        for (String option : menuOptions) {
            System.out.println("\t" + option);
        }
    }
}
