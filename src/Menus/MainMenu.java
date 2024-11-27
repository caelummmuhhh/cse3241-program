package Menus;

import java.util.Scanner;
import java.util.ArrayList;

import Helpers.InputVerifier;
import Helpers.PrintHelpers;

public class MainMenu {
    private Scanner scanner;
    private ArrayList<String> menuOptions = new ArrayList<String>() {
        {
            add("1. Manage Members (implemented)");
            add("2. Manage Rentals");
            add("3. Manage Drones");
            add("4. Manage Inventory (implemented)");
            add("5. Manage Shipments");
            add("6. Manage Payments\n");

            add("7. Rent Item (implemented)");
            add("8. Return Item (implemented)");
            add("9. Delivery of Item (partially implemented)");
            add("10. Pickup of Item (partially implemented)\n");

            add("11. Useful Reports\n");

            add("12. Clear Outputs");
            add("13. Exit Program");
        }
    };

    private ManageMembersMenu membersMenu;
    private ManageInventoryMenu inventoryMenu;

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
        membersMenu = new ManageMembersMenu(this.scanner);
        inventoryMenu = new ManageInventoryMenu(scanner);
    }

    public boolean prompt() {
        PrintHelpers.printHeader("Main Menu");
        PrintHelpers.printMenu(menuOptions);

        int userInput = InputVerifier.getValidIntegerInput(scanner, "Your Option: ", 1, menuOptions.size());
        return routeInput(userInput);
    }

    private boolean routeInput(int input) {
        switch (input) {
            case 1: // Manage Members
                membersMenu.prompt();
                break;
            case 2: // Manage Rentals
                System.out.println("Option has not been implemented yet...\n");
                break;

            case 3: // Manage Drones
                System.out.println("Option has not been implemented yet...\n");
                break;

            case 4: // Manage Inventory
                inventoryMenu.prompt();
                break;
            case 5: // Manage Shipments
                System.out.println("Option has not been implemented yet...\n");
                break;
            case 6: // Manage Payments
                System.out.println("Option has not been implemented yet...\n");
                break;

                case 7: // Rent Item
                CustomerMenu.PromptRentItem(scanner);
                System.out.println("");
                break;

            case 8: // Return Item
                CustomerMenu.PromptReturnItem(scanner);
                System.out.println("");
                break;

            case 9: // Delivery of Item
                CustomerMenu.PromptItemDelivery(scanner);
                System.out.println("");
                break;

            case 10: // Pickup of Item
                CustomerMenu.PromptItemPickupForReturn(scanner);
                System.out.println("");
                break;

            case 11: // Useful Reports
                new UsefulReportsMenu(scanner).prompt();
                break;
                
            case 12: // Clear output
                System.out.print("\033[H\033[2J");
                System.out.flush();
                break;

            case 13: // Terminate program
                return false;
        }
        return true;
    }
}
