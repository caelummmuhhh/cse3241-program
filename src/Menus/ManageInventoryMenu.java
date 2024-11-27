package Menus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

import Database.DatabaseManager;
import Database.InventorySQL;
import Database.ManufacturerSQL;
import Database.WarehouseSQL;
import Helpers.InputVerifier;
import Helpers.PrintHelpers;
import Models.EquipmentModel;

public class ManageInventoryMenu {
    private static ArrayList<String> menuOptions = new ArrayList<String>() {
        {
            add("1. Add Item");
            add("2. Search Item (by ID)");
            add("3. View All Available Items");
            add("4. Remove Item");
            add("5. Exit to Main Menu");
        }
    };

    private Scanner scanner;
    private Connection con;

    public ManageInventoryMenu(Scanner scanner) {
        this.scanner = scanner;
        this.con = DatabaseManager.CON;
    }

    public void prompt() {
        boolean continueMenu = true;
        while (continueMenu) {
            PrintHelpers.printHeader("Manage Inventory Menu");
            PrintHelpers.printMenu(menuOptions);

            int userInput = InputVerifier.getValidIntegerInput(scanner, "Your Option: ", 1, menuOptions.size());

            System.out.println();
            continueMenu = routeInput(userInput);
            System.out.println();
        }
    }

    private boolean routeInput(int input) {
        switch (input) {
            case 1:
                addItem();
                break;
            case 2:
                searchItem();
                break;
            case 3:
                viewAllItems();
                break;
            case 4:
                removeItem();
                break;

            case 5:
                return false;
        }
        return true;
    }

    private void addItem() {
        PrintHelpers.printHeader("Add Item");
        EquipmentModel newItem = new EquipmentModel();
        newItem.ItemID = InventorySQL.GetNextEquipmentID(con);

        System.out.print("Enter item name: ");
        newItem.Name = scanner.nextLine();

        System.out.print("Enter equipment type: ");
        newItem.Type = scanner.nextLine();

        System.out.print("Enter item size: ");
        newItem.Size = scanner.nextLine();

        newItem.Weight = InputVerifier.getValidIntegerInput(scanner, "Enter weight: ", 0, Integer.MAX_VALUE);

        System.out.print("Enter color: ");
        newItem.Color = scanner.nextLine();

        newItem.Weight = InputVerifier.getValidIntegerInput(scanner, "Enter year: ", 0, Integer.MAX_VALUE);

        System.out.println("\n");
        WarehouseSQL.PrintAll(con);
        System.out.println("\n");
        newItem.WarehouseID = InputVerifier.getValidIntegerInput(scanner, "Please choose and enter warehouse ID: ",
                WarehouseSQL.GetListOfAllIDs(con));

        System.out.println("\n");
        ManufacturerSQL.PrintAll(con);
        System.out.println("\n");
        newItem.ManufacturerID = InputVerifier.getValidIntegerInput(scanner, "Please choose and enter manufacturer ID: ",
                ManufacturerSQL.GetListOfAllIDs(con));

        InventorySQL.AddEquipmentRecord(con, newItem);

        System.out.println("\nAdded the following item: ");
        PrintHelpers.printItem(newItem);
    }

    private void searchItem() {
        PrintHelpers.printHeader("Search for Item");

        int id = InputVerifier.getValidIntegerInput(scanner, "Enter ID to search for: ", 0, Integer.MAX_VALUE);
        EquipmentModel item = InventorySQL.GetEquipmentByID(con, id);

        if (item == null) {
            System.out.println("Could not find equipment with ID: " + id);
            return;
        }

        System.out.println("Found Item:");
        PrintHelpers.printItem(item);
    }

    public void viewAllItems() {
        InventorySQL.PrintAll(con);
    }

    private void removeItem() {
        PrintHelpers.printHeader("Remove Item");

        int id = InputVerifier.getValidIntegerInput(scanner, "Enter Item ID: ", 0, Integer.MAX_VALUE);
        EquipmentModel item = InventorySQL.GetEquipmentByID(con, id);

        if (item == null) {
            System.out.println("Could not find equipment with ID: " + id);
            return;
        }
        PrintHelpers.printItem(item);
        if (!InputVerifier.promptBoolean(scanner, "Delete item? (y/n): ")) {
            System.out.println("Aborting...");
            return;
        }

        InventorySQL.DeleteEquipment(con, id);
        System.out.println("Deleted item.");
    }
}