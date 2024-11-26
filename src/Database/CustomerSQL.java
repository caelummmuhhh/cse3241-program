package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerSQL {
    public static void PrintAvailableItems(Connection con) {
        String sql = "SELECT I.ItemID EquipmentID, I.Name Equipment, I.Type, I.Color, I.Year, M.Name ManufacturerName\n" +
                        "FROM INVENTORY I\n" +
                        "LEFT JOIN MANUFACTURER M ON M.ManufacturerID = I.ManufacturerID\n" +
                        "WHERE I.ItemID NOT IN (\n" +
                        "    SELECT ItemID\n" +
                        "    FROM RENTALS\n" +
                        "    WHERE EndDt >= CURRENT_DATE OR EndDt IS NULL\n" + 
                        ");";
        
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }

    public static ArrayList<Integer> GetAvailableItemsIDs(Connection con) {
        String sql = "SELECT I.ItemID EquipmentID\n" +
                        "FROM INVENTORY I\n" +
                        "LEFT JOIN MANUFACTURER M ON M.ManufacturerID = I.ManufacturerID\n" +
                        "WHERE I.ItemID NOT IN (\n" +
                        "    SELECT ItemID\n" +
                        "    FROM RENTALS\n" +
                        "    WHERE EndDt >= CURRENT_DATE OR EndDt IS NULL\n" +
                        ");";
        ResultSet rs = QueryManager.query(con, sql, new String[0]);
        ArrayList<Integer> ids = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                ids.add(id);
            }   
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return ids;
    }

    /**
     * Retrieves the next Rental ID from the database, one that doesn't have a Rental occupying it.
     * 
     * @param con - the connection to the database
     * @return - rental ID (>=0), returns (<0) if error
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
            System.out.println("ERROR Retrieving from database... aborting!");
            return -1;
        }
        return rentalId;
    }
}
