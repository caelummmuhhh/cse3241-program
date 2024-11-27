package Database;

import java.sql.Connection;
import java.sql.ResultSet;

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
}
