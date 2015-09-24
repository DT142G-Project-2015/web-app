package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Utils {

    public static List<Map<String, Object>> toList(ResultSet rs) throws SQLException {

        List<Map<String, Object>> rows = new ArrayList<>();

        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            int numColumns = md.getColumnCount();

            Map<String, Object> currentRow = new HashMap<>();

            for (int i = 1; i <= numColumns; i++) {
                currentRow.put(md.getColumnLabel(i).toLowerCase(), rs.getObject(i));
            }
            rows.add(currentRow);
        }

        return rows;
    }
}
