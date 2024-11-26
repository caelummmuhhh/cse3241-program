package Menus;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.Random;

import Database.CustomerSQL;
import Database.DatabaseManager;
import Database.MembersSQL;
import Helpers.InputVerifier;
import Models.MemberModel;

public class CustomerMenu {

    public static void PromptRentItem(Scanner scanner) {
        int memberId = InputVerifier.getValidIntegerInput(scanner, "Please enter your Member ID: ", 0, Integer.MAX_VALUE);
        MemberModel member = MembersSQL.GetMemberByID(DatabaseManager.CON, memberId);

        if (member == null) {
            System.out.println("Could not find member with ID: " + memberId);
            return;
        }
        
        CustomerSQL.PrintAvailableItems(DatabaseManager.CON);
        ArrayList<Integer> availableItemIds = CustomerSQL.GetAvailableItemsIDs(DatabaseManager.CON);
        System.err.println();

        int equipmentId = InputVerifier.getValidIntegerInput(
            scanner,
            "Choose an Equipment ID from the table above to rent: ",
            availableItemIds
        );

        Date startDt = InputVerifier.promptValidDate(scanner, "Please enter rental start date (yyyy-mm-dd): ");
        Date endDt = InputVerifier.promptValidDate(scanner, "Please enter rental end date (yyyy-mm-dd): ");

        Random random = new Random();
        int fee = random.nextInt(10000 - 10 + 1) + 10;

        System.out.println("Renting ");
    }

    public static void PromptReturnItem() {

    }

    public static void PromptItemDelivery() {

    }

    public static void PromptItemPickupForReturn() {

    }
}
