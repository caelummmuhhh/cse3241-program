package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


public class QueryManager {
    public static ResultSet query(Connection con, String sql, String[] params) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            return query(con, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet query(Connection con, PreparedStatement sql) {
        try {
        	ResultSet rs = sql.executeQuery();
        	rs.close();
        	sql.close();
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void queryAndPrint(Connection con, String sql, String[] params) {
        try {
            ResultSet rs = query(con, sql, params);
            printResultsAsTable(rs);
            rs.close();    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printResultsAsTable(ResultSet rs) {
        int COL_WHITE_SPACE = 4;

        try {
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();

            // Calculate column size, which is max(string length) + COL_WHITE_SPACE for each column
            ArrayList<Integer> columnSizes = new ArrayList<Integer>();
            ArrayList<String> columnNames = new ArrayList<String>();
        	for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                columnNames.add(colName);
                columnSizes.add(colName.length() + COL_WHITE_SPACE);
            }
            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
                    row.add(columnValue);
                    if (columnValue != null && columnSizes.get(i-1) < columnValue.length() + COL_WHITE_SPACE) {
                        columnSizes.set(i-1, columnValue.length() + COL_WHITE_SPACE);
                    }
        		}
                rows.add(row);
            }

            // Actually print it out
            int tableWidth = 0; // to print out header margin
        	for (int i = 0; i < columnCount; i++) {
                tableWidth += columnSizes.get(i);
        		System.out.print(String.format("%-" + columnSizes.get(i) +"s", columnNames.get(i)));
        	}
			System.out.print("\n");
            System.out.println(new String(new char[tableWidth]).replace("\0", "="));
            for (ArrayList<String> row : rows) {
        		for (int i = 0; i < columnCount; i++) {
                    System.out.print(String.format("%-" + columnSizes.get(i) +"s", row.get(i)));
        		}
    			System.out.print("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
