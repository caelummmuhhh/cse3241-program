package Menus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.Date;

import Helpers.InputVerifier;
import Helpers.PrintHelpers;
import Models.MemberModel;
import Database.DatabaseManager;
import Database.MembersSQL;

public class ManageMembersMenu {
    private static ArrayList<String> menuOptions = new ArrayList<String>() {
        {
            add("1. Add Member");
            add("2. Search Members (by ID)");
            add("3. Check Member Status");
            add("4. Edit Member Info\n");

            add("5. Exit to Main Menu");
        }
    };

    private Scanner scanner;
    private Connection con;

    public ManageMembersMenu(Scanner scanner) {
        this.scanner = scanner;
        this.con = DatabaseManager.CON;
    }

    public void prompt() {
        boolean continueMenu = true;
        while (continueMenu) {
            PrintHelpers.printHeader("Manage Member Menu");
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
                addMember();
                break;
            case 2:
                searchMember();
                break;
            case 3:
                checkMemberStatus();
                break;
            case 4:
                editMember();
                break;

            case 5:
                return false;
        }
        return true;
    }

    private void addMember() {
        Date startDate = new Date();
        String memberStatus = "Active";

        PrintHelpers.printHeader("Add Member");

        System.out.print("Enter member first name: ");
        String fname = scanner.nextLine();

        System.out.print("Enter member last name: ");
        String lname = scanner.nextLine();

        System.out.print("Enter member address: ");
        String address = scanner.nextLine();

        System.out.print("Enter member phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter member email: ");
        String email = scanner.nextLine();

        int memberId = MembersSQL.GetNextMemberID(con);
        MemberModel newMember = new MemberModel(memberId, fname, lname, address, phone, email, startDate, memberStatus);

        MembersSQL.AddMemberRecord(con, newMember);

        System.out.println("\nAdded the following member: ");
        printMember(newMember);
    }

    private void searchMember() {
        PrintHelpers.printHeader("Search for Member");

        int id = InputVerifier.getValidIntegerInput(scanner, "Enter ID to search for: ", 0, Integer.MAX_VALUE);
        MemberModel member = MembersSQL.GetMemberByID(con, id);

        if (member == null) {
            System.out.println("Could not find member with ID: " + id);
            return;
        }

        printMember(member);
    }

    private void checkMemberStatus() {
        PrintHelpers.printHeader("Check Member Status");

        int id = InputVerifier.getValidIntegerInput(scanner, "Enter Member ID: ", 0, Integer.MAX_VALUE);
        MemberModel member = MembersSQL.GetMemberByID(con, id);

        if (member == null) {
            System.out.println("Could not find member with ID: " + id);
            return;
        }

        System.out.println("\nMember status is: " + member.MembershipStatus);
    }

    private void editMember() {
        PrintHelpers.printHeader("Edit Member");

        int id = InputVerifier.getValidIntegerInput(scanner, "Enter Member ID: ", 0, Integer.MAX_VALUE);
        MemberModel member = MembersSQL.GetMemberByID(con, id);

        if (member == null) {
            System.out.println("Could not find member with ID: " + id);
            return;
        }

        printMember(member);

        System.out.print("\nEnter new member first (leave blank to not edit): ");
        String FName = scanner.nextLine();
        if (!FName.trim().isEmpty()) {
            member.FName = FName;
        }

        System.out.print("Enter new member last name (leave blank to not edit): ");
        String LName = scanner.nextLine();
        if (!LName.trim().isEmpty()) {
            member.LName = LName;
        }

        System.out.print("Enter new member address (leave blank to not edit): ");
        String Address = scanner.nextLine();
        if (!Address.trim().isEmpty()) {
            member.Address = Address;
        }

        System.out.print("Enter new member phone number (leave blank to not edit): ");
        String PhoneNumber = scanner.nextLine();
        if (!PhoneNumber.trim().isEmpty()) {
            member.PhoneNumber = PhoneNumber;
        }

        System.out.print("Enter new member email (leave blank to not edit): ");
        String Email = scanner.nextLine();
        if (!Email.trim().isEmpty()) {
            member.Email = Email;
        }
        
        MembersSQL.UpdateExistingMember(con, member);

        System.out.println("\nUpdated Member: ");
        printMember(member);
    }

    private static void printMember(MemberModel member) {
        System.out.println(
                "\tMember ID: " + member.MemberID + "\n" +
                        "\tName: " + member.FName + " " + member.LName + "\n" +
                        "\tAddress: " + member.Address + "\n" +
                        "\tPhone Number: " + member.PhoneNumber + "\n" +
                        "\tEmail: " + member.Email + "\n" +
                        "\tMembership Start: " + member.MembershipStartDt.toString() + "\n" +
                        "\tStatus: " + member.MembershipStatus);
    }
}
