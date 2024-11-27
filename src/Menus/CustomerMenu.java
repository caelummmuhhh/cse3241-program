package Menus;

import java.sql.Connection;
import java.util.Scanner;
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

        System.out.print("\nEnter the delivery address: ");
        String deliveryAddr = scanner.nextLine();

        RentalsSQL.CreateRental(con, equipmentId, member.MemberID, startDt, endDt, fee, deliveryAddr);
        System.out.println("Successfully rented item!");
    }

    public static void PromptReturnItem() {

    }

    public static void PromptItemDelivery() {

    }

    public static void PromptItemPickupForReturn() {

    }





    private static MemberModel GetMemberPrompt(Scanner scanner) {
        int memberId = InputVerifier.getValidIntegerInput(scanner, "Please enter your Member ID: ", 0, Integer.MAX_VALUE);
        MemberModel member = MembersSQL.GetMemberByID(DatabaseManager.CON, memberId);

        if (member == null) {
            System.out.println("Could not find member with ID: " + memberId);
            return null;
        }
        return member;
    }

}
