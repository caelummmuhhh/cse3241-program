package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import Helpers.DataTypeHelpers;

public class ShipmentsSQL {
    /**
     * Retrieves the next delivery ID from the database, one that doesn't have a delivery occupying it.
     * 
     * @param con - the connection to the database
     * @return the next delivery ID (>=0), returns (<0) if error
     */
    public static int GetNextShipmentID(Connection con) {
        String sql = "SELECT COALESCE(MAX(DeliveryID), 0) + 1 AS NextID FROM SHIPMENTS;";
        ResultSet rs = QueryManager.query(con, sql, new String[0]);
        int shipmentId = -1;
        try {
            rs.next();
            shipmentId = rs.getInt(1);
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Retrieving from database...");
            return -1;
        }
        return shipmentId;
    }

    public static boolean CreateShipment(Connection con, int rentalId, Date deliveryDt, String shipmentType, String address) {
        String sql = "INSERT INTO SHIPMENTS (DeliveryID, RentalID, EstDeliveryDt, Status, Address, DroneID, Type, Distance)\n"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        int deliveryId = ShipmentsSQL.GetNextShipmentID(con);
        int droneId = DataTypeHelpers.getRandomInt(DronesSQL.GetListOfAllIDs(con));
        int distance = DataTypeHelpers.getRandomInt(0, 100);
        String shipmenStatus = "Processing";

        try {
            PreparedStatement shipmentPs = con.prepareStatement(sql);
            shipmentPs.setInt(1, deliveryId);
            shipmentPs.setInt(2, rentalId);
            shipmentPs.setString(3, DataTypeHelpers.convertToDateOnlyISO(deliveryDt));
            shipmentPs.setString(4, shipmenStatus);
            shipmentPs.setString(5, address);
            shipmentPs.setInt(6, droneId);
            shipmentPs.setString(7, shipmentType);
            shipmentPs.setInt(8, distance);

            shipmentPs.executeUpdate();
            return true;
        } catch (SQLException err) {
            System.out.println("Error creating shipment. Aborting...");
        }
        return false;
    }
}
