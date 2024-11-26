package Menus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Database.DatabaseManager;
import Database.QueryManager;
import Helpers.InputVerifier;
import Helpers.PrintHelpers;

public class UsefulReportsMenu {
    private static ArrayList<String> menuOptions = new ArrayList<String>() {
        {
            add("1. Renting Checkouts by Member");
            add("2. Popular Items");
            add("3. Popular Manufacturer");
            add("4. Popular Drone");
            add("5. Members w/ Most Items Checked Out");
            add("6. Equipment By Type of Equipment\n");

            add("7. Exit to Main Menu");
        }
    };

    private Scanner scanner;
    private Connection con;

    public UsefulReportsMenu(Scanner scanner) {
        this.scanner = scanner;
        this.con = DatabaseManager.CON;
    }

    public void prompt() {
        boolean continueMenu = true;
        while (continueMenu) {
            PrintHelpers.printHeader("Useful Reports Menu");
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
                printRentingCheckouts();
                break;
            case 2:
                printPopularItems();
                break;
            case 3:
                printPopularManufacturer();
                break;
            case 4:
                printPopularDrone();
                break;
            case 5:
                printMostItemsCheckedOut();
                break;
            case 6:
                printEquipmentByTypeOfEquipment();
                break;

            case 7:
                return false;
        }
        return true;
    }

    private void printRentingCheckouts() {
        String mainSql = "SELECT i.Name AS Equipment, r.StartDt AS CheckoutDate\n" + //
                        "FROM RENTALS r\n" + //
                        "JOIN INVENTORY i ON r.ItemID = i.ItemID\n" + //
                        "JOIN MEMBERS m ON r.MemberID = m.MemberID\n" + //
                        "WHERE LOWER(m.FName) = LOWER(?) AND LOWER(m.LName) = LOWER(?);";

        String memberExists = "SELECT *\n" + //
                        "FROM MEMBERS\n" + //
                        "WHERE LOWER(FName) = LOWER(?) AND LOWER(LName) = LOWER(?);";

        System.out.print("Enter the first name of the member: ");
        String fname = scanner.nextLine();
        System.out.print("Enter the last name of the member: ");
        String lname = scanner.nextLine();
        System.out.println("");

        // Check if name exists
        ResultSet membersRs = QueryManager.query(con, memberExists, new String[] {fname, lname});
        try {
            if (!membersRs.isBeforeFirst()) {
                System.out.println("No member found with name \"" + fname + " " + lname + "\".");
                return;
            }
        }
        catch (SQLException err) {
            System.out.println("Error occurred when member name info.");
            return;
        }

        // Actually print it.
        ResultSet mainRs = QueryManager.query(con, mainSql, new String[] {fname, lname});
        try {
            // Member has no rentals, indicate it.
            if (!mainRs.isBeforeFirst()) {
                System.out.println("Member has no rented items.");
                return;
            }

            // Member has rentals, print it.
            QueryManager.printResultsAsTable(mainRs);
        }
        catch (SQLException err) {
            System.out.println("Error occurred when retrieving member renting info.");
        }
    }

    private void printPopularItems() {
        String sql = "SELECT INVENTORY.Name, COALESCE(COUNT(R.RENTALID), 0) AS RentalCount\n" + //
                        "FROM INVENTORY\n" + //
                        "LEFT JOIN RENTALS R ON INVENTORY.ItemID = R.ItemID\n" + //
                        "GROUP BY INVENTORY.Name\n" + //
                        "ORDER BY RentalCount DESC;\n";
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }

    private void printPopularManufacturer() {
        String sql = "SELECT m.ManufacturerID,m.Name AS ManufacturerName,COUNT(r.RentalID) AS TotalRentals\n" + //
                        "FROM RENTALS r\n" + //
                        "JOIN INVENTORY inv ON r.ItemID = inv.ItemID\n" + //
                        "JOIN MANUFACTURER m ON inv.ManufacturerID = m.ManufacturerID\n" + //
                        "GROUP BY m.ManufacturerID, m.Name\n" + //
                        "LIMIT 1;\n";
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }

    private void printPopularDrone() {
        String sql = "SELECT DRONES.DroneID, DRONES.Model, SUM(SHIPMENTS.Distance) AS TotalMiles, COUNT(SHIPMENTS.DeliveryID) AS DeliveriesCount\n" + //
                        "FROM DRONES\n" + //
                        "LEFT JOIN SHIPMENTS ON DRONES.DroneID = SHIPMENTS.DroneID\n" + //
                        "GROUP BY DRONES.DroneID, DRONES.Model\n" + //
                        "ORDER BY DeliveriesCount DESC;\n";
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }

    private void printMostItemsCheckedOut() {
        String sql = "SELECT m.FName, m.LName, COUNT(r.ItemID) AS TotalItemsCheckedOut\n" + //
                        "FROM MEMBERS m\n" + //
                        "JOIN RENTALS r ON m.MemberID = r.MemberID\n" + //
                        "GROUP BY m.MemberID\n" + //
                        "ORDER BY TotalItemsCheckedOut DESC\n" + //
                        "LIMIT 1;";
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }

    private void printEquipmentByTypeOfEquipment() {
        String mainQuery = "SELECT Name AS EquipmentName, Year\n" + //
                        "FROM INVENTORY\n" + //
                        "WHERE LOWER(Type) = LOWER(?) AND Year > ?;";
        
        String typesQuery = "SELECT DISTINCT Type AS EquipmentTypes FROM INVENTORY;";

        // Print all the types and let users choose
        QueryManager.queryAndPrint(con, typesQuery, new String[0]);
        System.out.print("\nEnter the equipment type: ");
        String usrEquipType = scanner.nextLine();

        int usrYear = InputVerifier.getValidIntegerInput(
            scanner,
            "Enter oldest year for equipment: ",
            2000,
            Integer.MAX_VALUE
        );

        ResultSet rs = QueryManager.query(con, mainQuery, new String[] { usrEquipType, Integer.toString(usrYear) });
        
        System.out.println("");
        try {
            // Member has no rentals, indicate it.
            if (!rs.isBeforeFirst()) {
                System.out.println("No equipment of type \"" + usrEquipType + "\" that are after year " + usrYear);
                return;
            }

            // There are equipments, print them!
            QueryManager.printResultsAsTable(rs);
        }
        catch (SQLException err) {
            System.out.println("Error occurred when retrieving member renting info.");
        }
    }
}
