package Database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Helpers.DataTypeHelpers;

public class RentalsSQL {
    /**
     * Create a rental WITH delivery.
     * 
     * @param con - connection to database
     * @param itemId - item to rent
     * @param memberId - id of member that is renting
     * @param startDt - start date of rental
     * @param endDt - estimated end date of rental
     * @param fee - the fee for the item rental
     * @param deliveryAddr - the address to deliver to
     * @return whether or not the rental and delivery creation was successful
     */
    public static boolean CreateRental(Connection con, int itemId, int memberId, Date startDt,
            Date endDt, int fee, String deliveryAddr) {
        String rentalSql = "INSERT INTO RENTALS (RentalID, ItemID, MemberID, StartDt, EndDt, Fee)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        String shipmentSql = "INSERT INTO SHIPMENTS (DeliveryID, RentalID, EstDeliveryDt, Status, Address, DroneID, Type, Distance)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        int rentalId = GetNextRentalID(con);
        int deliveryId = ShipmentsSQL.GetNextShipmentID(con);
        int droneId = DataTypeHelpers.getRandomInt(DronesSQL.GetListOfAllIDs(con));
        int distance = DataTypeHelpers.getRandomInt(0, 100);
        String shipmentType = "Delivery";
        String shipmenStatus = "Processing";

        try {
            con.setAutoCommit(false);
            PreparedStatement rentalPs = con.prepareStatement(rentalSql);
            rentalPs.setInt(1, rentalId);
            rentalPs.setInt(2, itemId);
            rentalPs.setInt(3, memberId);
            rentalPs.setString(4, DataTypeHelpers.convertToDateOnlyISO(startDt));
            rentalPs.setString(5, DataTypeHelpers.convertToDateOnlyISO(endDt));
            rentalPs.setInt(6, fee);

            PreparedStatement shipmentPs = con.prepareStatement(shipmentSql);
            shipmentPs.setInt(1, deliveryId);
            shipmentPs.setInt(2, rentalId);
            shipmentPs.setString(3, DataTypeHelpers.convertToDateOnlyISO(startDt));
            shipmentPs.setString(4, shipmenStatus);
            shipmentPs.setString(5, deliveryAddr);
            shipmentPs.setInt(6, droneId);
            shipmentPs.setString(7, shipmentType);
            shipmentPs.setInt(8, distance);

            rentalPs.executeUpdate();
            shipmentPs.executeUpdate();

            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (SQLException err) {
            System.out.println("Error processing rental. Aborting...");
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException err2) {
                System.out.println("Error aborting...");
                err.printStackTrace();
            }
        }
        return false;
    }


    /**
     * Making rental WITHOUT delivery
     * 
     * @param con - connection to database
     * @param itemId - item to rent
     * @param memberId - id of member that is renting
     * @param startDt - start date of rental
     * @param endDt - estimated end date of rental
     * @param fee - the fee for the item rental
     * @return whether or not the rental creation was a success
     */
    public static boolean CreateRental(Connection con, int itemId, int memberId, Date startDt,
            Date endDt, int fee) {
        String rentalSql = "INSERT INTO RENTALS (RentalID, ItemID, MemberID, StartDt, EndDt, Fee)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        int rentalId = GetNextRentalID(con);

        try {
            PreparedStatement rentalPs = con.prepareStatement(rentalSql);
            rentalPs.setInt(1, rentalId);
            rentalPs.setInt(2, itemId);
            rentalPs.setInt(3, memberId);
            rentalPs.setString(4, DataTypeHelpers.convertToDateOnlyISO(startDt));
            rentalPs.setString(5, DataTypeHelpers.convertToDateOnlyISO(endDt));
            rentalPs.setInt(6, fee);

            rentalPs.executeUpdate();
            return true;
        } catch (SQLException err) {
            System.out.println("Error processing rental. Aborting...");
        }
        return false;
    }

    /**
     * Return rental WITH pick up.
     * 
     * @param con - connection to database
     * @param memberId - id of member that the rental belongs to
     * @param rentalId - rental id to return
     * @param pickUpAddr - address where item will be picked up from
     * @return whether or not the return and pick up creation was successful
     */
    public static boolean ReturnRental(Connection con, int memberId, int rentalId, String pickUpAddr) {
        int deliveryId = ShipmentsSQL.GetNextShipmentID(con);
        int droneId = DataTypeHelpers.getRandomInt(DronesSQL.GetListOfAllIDs(con));
        int distance = DataTypeHelpers.getRandomInt(0, 100);
        Date newEndDt = new Date();
        String shipmentType = "Return";
        String shipmenStatus = "Processing";

        String updateRentalSql = "UPDATE RENTALS SET EndDt = ? WHERE RentalID = ?;";
        String shipmentSql = "INSERT INTO SHIPMENTS (DeliveryID, RentalID, EstDeliveryDt, Status, Address, DroneID, Type, Distance)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            con.setAutoCommit(false);
            PreparedStatement rentalPs = con.prepareStatement(updateRentalSql);
            rentalPs.setString(1, DataTypeHelpers.convertToDateOnlyISO(newEndDt));
            rentalPs.setInt(2, rentalId);

            PreparedStatement shipmentPs = con.prepareStatement(shipmentSql);
            shipmentPs.setInt(1, deliveryId);
            shipmentPs.setInt(2, rentalId);
            shipmentPs.setString(3, DataTypeHelpers.convertToDateOnlyISO(newEndDt));
            shipmentPs.setString(4, shipmenStatus);
            shipmentPs.setString(5, pickUpAddr);
            shipmentPs.setInt(6, droneId);
            shipmentPs.setString(7, shipmentType);
            shipmentPs.setInt(8, distance);

            rentalPs.executeUpdate();
            shipmentPs.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (SQLException err) {
            System.out.println("Error returning rental. Aborting...");
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException err2) {
                System.out.println("Error aborting...");
                err.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Return rental WITHOUT pickup.
     * 
     * @param con - connection to database
     * @param memberId - id of member that the rental belongs to
     * @param rentalId - rental id to return
     * @return whether or not the return was successful
     */
    public static boolean ReturnRental(Connection con, int memberId, int rentalId) {
        Date newEndDt = new Date();

        String updateRentalSql = "UPDATE RENTALS SET EndDt = ? WHERE RentalID = ?;";

        try {
            PreparedStatement rentalPs = con.prepareStatement(updateRentalSql);
            rentalPs.setString(1, DataTypeHelpers.convertToDateOnlyISO(newEndDt));
            rentalPs.setInt(2, rentalId);
            rentalPs.executeUpdate();
            return true;
        } catch (SQLException err) {
            System.out.println("Error returning rental. Aborting...");
        }
        return false;
    }


    public static void PrintActiveRentalEquipments(Connection con, int memberId) {
        String sql = "SELECT r.RentalID, i.Name AS Equipment, r.StartDt AS CheckoutDate\n" + //
                        "FROM RENTALS r\n" + //
                        "JOIN INVENTORY i ON r.ItemID = i.ItemID\n" + //
                        "JOIN MEMBERS m ON r.MemberID = m.MemberID\n" + //
                        "WHERE m.MemberID = ? AND r.RentalID IN (\n" + //
                        "    SELECT RentalID\n" + //
                        "    FROM SHIPMENTS\n" + //
                        "    GROUP BY RentalID\n" + //
                        "    HAVING COUNT(*) = 1\n" + //
                        ");";
        QueryManager.queryAndPrint(con, sql, new String[] { Integer.toString(memberId) });
    }

    public static ArrayList<Integer> GetActiveRentalIDs(Connection con, int memberId) {
        String sql = "SELECT r.RentalID\n" + //
                        "FROM RENTALS r\n" + //
                        "JOIN INVENTORY i ON r.ItemID = i.ItemID\n" + //
                        "JOIN MEMBERS m ON r.MemberID = m.MemberID\n" + //
                        "WHERE m.MemberID = ? AND r.RentalID IN (\n" + //
                        "    SELECT RentalID\n" + //
                        "    FROM SHIPMENTS\n" + //
                        "    GROUP BY RentalID\n" + //
                        "    HAVING COUNT(*) = 1\n" + //
                        ");";
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ResultSet rs = QueryManager.query(con, sql, new String[] { Integer.toString(memberId) });
            while (rs.next()) {
                ids.addLast(rs.getInt(1));
            }
            rs.close();
        }
        catch (SQLException err) {
            System.out.println(err.getMessage());
            return null;
        }
        return ids;
    }

    public static void PrintNotPickedUpReturnRentals(Connection con, int memberId) {
        String sql = "SELECT R.RentalID, StartDt, EndDt, I.Name EquipmentName, I.Type EquipmentType\n" + //
                        "FROM RENTALS R\n" + //
                        "JOIN INVENTORY I ON I.ItemID = R.ItemID\n" + //
                        "WHERE R.MemberID = ? AND R.RentalID NOT IN (\n" + //
                        "    SELECT RentalID\n" + //
                        "    FROM SHIPMENTS\n" + //
                        "    WHERE Type = 'Return'\n" + //
                        ");";
        QueryManager.queryAndPrint(con, sql, new String[] { Integer.toString(memberId) });
    }

    public static ArrayList<Integer> GetNotPickedUpReturnRentalIDs(Connection con, int memberId) {
        String sql = "SELECT RentalID\n" + //
                        "FROM RENTALS\n" + //
                        "WHERE MemberID = ? AND RentalID NOT IN (\n" + //
                        "    SELECT RentalID\n" + //
                        "    FROM SHIPMENTS\n" + //
                        "    WHERE Type = 'Return'\n" + //
                        ")\n";
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ResultSet rs = QueryManager.query(con, sql, new String[] { Integer.toString(memberId) });
            while (rs.next()) {
                ids.addLast(rs.getInt(1));
            }
            rs.close();
        }
        catch (SQLException err) {
            System.out.println(err.getMessage());
            return null;
        }
        return ids;
    }

    public static void PrintNotDeliveredRentals(Connection con, int memberId) {
        String sql = "SELECT R.RentalID, StartDt, EndDt, I.Name EquipmentName, I.Type EquipmentType\n" + //
                        "FROM RENTALS R\n" + //
                        "JOIN INVENTORY I ON I.ItemID = R.ItemID\n" + //
                        "WHERE R.MemberID = ? AND R.RentalID NOT IN (\n" + //
                        "    SELECT RentalID\n" + //
                        "    FROM SHIPMENTS\n" + //
                        ");";
        QueryManager.queryAndPrint(con, sql, new String[] { Integer.toString(memberId) });
    }

    public static ArrayList<Integer> GetNotDeliveredRentalIDs(Connection con, int memberId) {
        String sql = "SELECT RentalID\n" + //
                        "FROM RENTALS\n" + //
                        "WHERE MemberID = ? AND RentalID NOT IN (\n" + //
                        "    SELECT RentalID\n" + //
                        "    FROM SHIPMENTS\n" + //
                        ");\n";
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ResultSet rs = QueryManager.query(con, sql, new String[] { Integer.toString(memberId) });
            while (rs.next()) {
                ids.addLast(rs.getInt(1));
            }
            rs.close();
        }
        catch (SQLException err) {
            System.out.println(err.getMessage());
            return null;
        }
        return ids;
    }

    /**
     * Retrieves the next rental ID from the database, one that doesn't have a
     * rental occupying it.
     * 
     * @param con - the connection to the database
     * @return the next rental ID (>=0), returns (<0) if error
     */
    public static int GetNextRentalID(Connection con) {
        String sql = "SELECT COALESCE(MAX(RentalID), 0) + 1 AS NextID FROM RENTALS;";
        ResultSet rs = QueryManager.query(con, sql, new String[0]);
        int rentalId = -1;
        try {
            rs.next();
            rentalId = rs.getInt(1);
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Retrieving from database...");
            return -1;
        }
        return rentalId;
    }
}
