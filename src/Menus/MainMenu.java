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

            add("7. Rent Item (partially implemented)");
            add("8. Return Item (partially implemented)");
            add("9. Delivery of Item (partially implemented)");
            add("10. Pickup of Item (partially implemented)\n");

            add("11. Clear Outputs");
            add("12. Exit Program");
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
                System.out.print("\nEnter the item you wish to rent: ");
                int rentItemId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter your name: ");
                scanner.nextLine();
                System.out.print("Enter rental duration (in days): ");
                int rentalDuration = scanner.nextInt();
                System.out.printf("You have rented item #%d for %d days.\n\n", rentItemId, rentalDuration);
                break;

            case 8: // Return Item
                System.out.print("\nEnter the item ID you wish to return: ");
                int returnItemId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter your name: ");
                String returnerName = scanner.nextLine();
                System.out.printf("Return process for item #%d has was started by %s.\n\n", returnItemId, returnerName);
                break;

            case 9: // Delivery of Item
                System.out.print("\nEnter the item ID for delivery: ");
                int deliveryItemId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter delivery address: ");
                String deliveryAddress = scanner.nextLine();
                System.out.printf("Item #%d will be delivered to %s.\n\n", deliveryItemId, deliveryAddress);
                break;

            case 10: // Pickup of Item
                System.out.print("\nEnter the item ID for pickup: ");
                int pickupItemId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter your name: ");
                String pickerName = scanner.nextLine();
                System.out.printf("Item #%d will be picked up by %s.\n\n", pickupItemId, pickerName);
                break;
                
            case 11: // Clear output
                System.out.print("\033[H\033[2J");
                System.out.flush();
                break;

            case 12: // Terminate program
                return false;
        }
        return true;
    }
}
