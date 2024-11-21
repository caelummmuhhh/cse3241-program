package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Helpers.DataTypeHelpers;
import Models.MemberModel;

public class MembersSQL {
    public static void AddMemberRecord(Connection con, MemberModel member) {
        try {
            String sql = "INSERT INTO MEMBERS (MemberID, FName, LName, Address, PhoneNumber, Email, MembershipStartDt, MemberStatus)"
                    + "VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, member.MemberID);
            ps.setString(2, member.FName);
            ps.setString(3, member.LName);
            ps.setString(4, member.Address);
            ps.setString(5, member.PhoneNumber);
            ps.setString(6, member.Email);
            ps.setString(7, DataTypeHelpers.convertToDateOnlyISO(member.MembershipStartDt));
            ps.setString(8, member.MembershipStatus);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieve member by member ID
     * 
     * @param con - the connection to the database
     * @param searchId - the ID to search for
     * @return - found member if any
     */
    public static MemberModel GetMemberByID(Connection con, int searchId) {
        String sql = "SELECT * FROM MEMBERS WHERE MemberID = ?";
        ResultSet rs = QueryManager.query(con, sql, new String[] {String.valueOf(searchId)});
        try {
            rs.next();
            MemberModel member = new MemberModel(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                DataTypeHelpers.parseDateString(rs.getString(7)),
                rs.getString(8)
            );
            rs.close();
            return member;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Retrieving from database...");
        }
        return null;
    }

    /**
     * Retrieves the next member ID from the database, one that doesn't have a member occupying it.
     * 
     * @param con - the connection to the database
     * @return the next member ID (>=0), returns (<0) if error
     */
    public static int GetNextMemberID(Connection con) {
        String sql = "SELECT COALESCE(MAX(MemberID), 0) + 1 AS NextID FROM MEMBERS;";
        ResultSet rs = QueryManager.query(con, sql, new String[0]);
        int memberId = -1;
        try {
            rs.next();
            memberId = rs.getInt(1);
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Retrieving from database...");
            return -1;
        }
        return memberId;
    }

    public static void UpdateExistingMember(Connection con, MemberModel member) {
        try {
            String sql = "UPDATE MEMBERS " +
                         "SET FName = ?, LName = ?, Address = ?, PhoneNumber = ?, Email = ? " +
                         "WHERE MemberID = ?; ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, member.FName);
            ps.setString(2, member.LName);
            ps.setString(3, member.Address);
            ps.setString(4, member.PhoneNumber);
            ps.setString(5, member.Email);
            ps.setInt(6, member.MemberID);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}
