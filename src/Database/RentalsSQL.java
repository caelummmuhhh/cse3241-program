package Database;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Helpers.DataTypeHelpers;

public class RentalsSQL {
    public static void CreateRental(Connection con, int itemId, int memberId, Date startDt,
            Date endDt, int fee, String deliveryAddr) {
        String rentalSql = "INSERT INTO RENTALS (RentalID, ItemID, MemberID, StartDt, EndDt, Fee)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        String shipmentSql = "INSERT INTO SHIPMENTS (DeliveryID, RentalID, EstDeliveryDt, Status, Address, DroneID, Type, Distance)\n"
                + //
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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
