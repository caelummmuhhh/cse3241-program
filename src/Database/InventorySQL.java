package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.EquipmentModel;

public class InventorySQL {
    public static void AddEquipmentRecord(Connection con, EquipmentModel item) {
        String sql = "INSERT INTO INVENTORY (ItemID, Name, Type, Size, Weight, Color, Year, WarehouseID, ManufacturerID)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, item.ItemID);
            ps.setString(2, item.Name);
            ps.setString(3, item.Type);
            ps.setString(4, item.Size);
            ps.setInt(5, item.Weight);
            ps.setString(6, item.Color);
            ps.setInt(7, item.Year);
            ps.setInt(8, item.WarehouseID);
            ps.setInt(9, item.ManufacturerID);

            ps.executeUpdate();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }

    /**
     * Retrieve member by equipment ID
     * 
     * @param con      - the connection to the database
     * @param searchId - the ID to search for
     * @return - found equipment if any
     */
    public static EquipmentModel GetEquipmentByID(Connection con, int searchId) {
        String sql = "SELECT * FROM INVENTORY WHERE ItemID = ?";
        ResultSet rs = QueryManager.query(con, sql, new String[] { String.valueOf(searchId) });
        try {
            if (!rs.isBeforeFirst()) {
                // nothing was found
                return null;
            }

            rs.next(); 
            EquipmentModel item = new EquipmentModel(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getInt(7),
                    rs.getInt(8),
                    rs.getInt(9));
            rs.close();
            return item;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Retrieving from database...");
        }
        return null;

    }

    /**
     * Retrieves the next equipment ID from the database, one that doesn't have an
     * item occupying it.
     * 
     * @param con - the connection to the database
     * @return the next equipment ID (>=0), returns (<0) if error
     */
    public static int GetNextEquipmentID(Connection con) {
        String sql = "SELECT COALESCE(MAX(ItemID), 0) + 1 AS NextID FROM INVENTORY;";
        ResultSet rs = QueryManager.query(con, sql, new String[0]);
        int itemId = -1;
        try {
            rs.next();
            itemId = rs.getInt(1);
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Retrieving from database...");
            itemId = -1;
        }
        return itemId;
    }

    public static void UpdateExistingEquipment(Connection con, EquipmentModel item) {
        String sql = "UPDATE INVENTORY\n"
                + "SET ItemID = ?, Name = ?, Type = ?, Size = ?, Weight = ?, Color = ?, Year = ?, WarehouseID = ?, ManufacturerID = ?\n"
                + "WHERE ItemID = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, item.ItemID);
            ps.setString(2, item.Name);
            ps.setString(3, item.Type);
            ps.setString(4, item.Size);
            ps.setInt(5, item.Weight);
            ps.setString(6, item.Color);
            ps.setInt(7, item.Year);
            ps.setInt(8, item.WarehouseID);
            ps.setInt(9, item.ManufacturerID);

            ps.executeUpdate();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }

    public static void DeleteEquipment(Connection con, int itemId) {
        String sql = "DELETE FROM INVENTORY WHERE ItemID = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, itemId);
            ps.executeUpdate();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    
    public static void PrintAll(Connection con) {
        String sql = "SELECT * FROM INVENTORY;";
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }
}
