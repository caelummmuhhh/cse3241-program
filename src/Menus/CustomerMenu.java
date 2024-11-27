package Menus;

import java.sql.Connection;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;

import Database.CustomerSQL;
import Database.DatabaseManager;
import Database.InventorySQL;
import Database.MembersSQL;
import Database.RentalsSQL;
import Helpers.DataTypeHelpers;
import Helpers.InputVerifier;
import Helpers.PrintHelpers;
import Models.EquipmentModel;
import Models.MemberModel;

public class CustomerMenu {

    public static void PromptRentItem(Scanner scanner) {
        PrintHelpers.printHeader("Rent Item");
        Connection con = DatabaseManager.CON;
        // Get member
        MemberModel member = GetMemberPrompt(scanner);
        if (member == null) {
            return;
        }
        
        // Get item
        CustomerSQL.PrintAvailableItems(con);
        System.out.println();
        int equipmentId = InputVerifier.getValidIntegerInput(
            scanner,
            "Choose an Equipment ID from the table above to rent: ",
            CustomerSQL.GetAvailableItemsIDs(con)
        );
        EquipmentModel item = InventorySQL.GetEquipmentByID(con, equipmentId);
        Date startDt = InputVerifier.promptValidDate(scanner, "Please enter rental start date (yyyy-mm-dd): ");
        Date endDt = InputVerifier.promptValidDate(scanner, "Please enter rental end date (yyyy-mm-dd): ");
        int fee = DataTypeHelpers.getRandomInt(10, 10000);

        System.out.println(
            "Renting: \n" + 
            "\tItem ID: " + item.ItemID + "\n" +
            "\tName: " + item.Name + "\n" +
            "\tType: " + item.Type + "\n" +
            "\tSize: " + item.Size + "\n" +
            "\tWeight: " + item.Weight + "\n" +
            "\tColor: " + item.Color + "\n" +
            "\tYear: " + item.Year + "\n\n" +
            "\tFee: " + fee + "\n" +
            "\tStart Date: " + startDt.toString() + "\n" +
            "\tEnd Date: " + endDt.toString() + "\n"
        );

        if (!InputVerifier.promptBoolean(scanner, "Rent Item? (y/n): ")) {
            System.out.println("Cancelling...");
            return;
        }
        boolean success = false;
        if (InputVerifier.promptBoolean(scanner, "Set delivery right now? This can be set later. (y/n): ")) {
            System.out.print("\nEnter the delivery address: ");
            String deliveryAddr = scanner.nextLine();
    
            success = RentalsSQL.CreateRental(con, equipmentId, member.MemberID, startDt, endDt, fee, deliveryAddr);
        } else {
            success = RentalsSQL.CreateRental(con, equipmentId, member.MemberID, startDt, endDt, fee);
        }
        
        if (success) {
            System.out.println("Successfully rented " + item.Name + "!");
        }
    }


    public static void PromptReturnItem(Scanner scanner) {
        PrintHelpers.printHeader("Return Rental");
        Connection con = DatabaseManager.CON;

        MemberModel member = GetMemberPrompt(scanner);
        if (member == null) {
            return;
        }
        ArrayList<Integer> activeRentalIds = RentalsSQL.GetActiveRentalIDs(con, member.MemberID);

        if (activeRentalIds.size() <= 0) {
            System.out.println("Nothing to return... Member does not have any active rentals.");
            return;
        }
        System.out.println("");
        RentalsSQL.PrintActiveRentalEquipments(con, member.MemberID);
        System.out.println("");
        int returnRentalId = InputVerifier.getValidIntegerInput(scanner, "Enter rental ID you wish to return: ",
                activeRentalIds);
        EquipmentModel item = InventorySQL.GetEquipmentFromRentalID(con, returnRentalId);
        if (!InputVerifier.promptBoolean(scanner, "Return Item? (y/n): ")) {
            System.out.println("Cancelling...");
            return;
        }

        boolean success = false;

        if (InputVerifier.promptBoolean(scanner, "Set up return item pick up right now? This can be set later. (y/n): ")) {
            System.out.print("\nEnter the address to pick up: ");
            String deliveryAddr = scanner.nextLine();    
            success = RentalsSQL.ReturnRental(con, member.MemberID, returnRentalId, deliveryAddr);
        }
        else {
            success = RentalsSQL.ReturnRental(con, member.MemberID, returnRentalId);
        }
        
        if (success) {
            System.out.println("Successfully created return for Rental #" + returnRentalId + " (" + item.Name + ")");
            System.out.println("Please anticipate drone's arrival to retrieve the returned item.");
        }
    }


    public static void PromptItemDelivery() {

    }

    public static void PromptItemPickupForReturn() {

    }

    private static MemberModel GetMemberPrompt(Scanner scanner) {
        int memberId = InputVerifier.getValidIntegerInput(scanner, "Please enter your Member ID: ", 0,
                Integer.MAX_VALUE);
        MemberModel member = MembersSQL.GetMemberByID(DatabaseManager.CON, memberId);

        if (member == null) {
            System.out.println("Could not find member with ID: " + memberId);
            return null;
        }
        return member;
    }

}
