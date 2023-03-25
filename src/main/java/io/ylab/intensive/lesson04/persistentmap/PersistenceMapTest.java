package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceMapTest {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();
        // Create and test first map
        PersistentMap persistentMap = new PersistentMapImpl(dataSource);
        System.out.println("FIRST MAP TEST:");
        persistentMap.init("first_map");
        persistentMap.put(null, null);
        persistentMap.put("Russia", "Moscow");
        persistentMap.put("USA", "Washington");
        persistentMap.put("GB", "London");
        System.out.println(persistentMap.containsKey("Russia"));
        System.out.println(persistentMap.containsKey("Belorussia"));
        try {
            System.out.println(persistentMap.get("USA"));
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(persistentMap.getKeys());
        try {
            System.out.println(persistentMap.get("Belorussia"));
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        }
        persistentMap.remove("GB");
        System.out.println(persistentMap.containsKey("GB"));
        System.out.println(persistentMap.getKeys());
        persistentMap.clear();
        System.out.println(persistentMap.getKeys());
        // Create and test second map
        System.out.println("SECOND_MAP_TEST:");
        PersistentMap persistentMap2 = new PersistentMapImpl(dataSource);
        persistentMap2.init("second_map");
        persistentMap2.put("Russia", "Moscow");
        persistentMap2.put("USA", "Washington");
        persistentMap2.put("GB", "London");
        System.out.println(persistentMap2.containsKey("Russia"));
        System.out.println(persistentMap2.containsKey("Belorussia"));
        try {
            System.out.println(persistentMap2.get("USA"));
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(persistentMap2.getKeys());
        persistentMap2.remove("USA");
        System.out.println(persistentMap2.containsKey("GB"));
        System.out.println(persistentMap2.getKeys());
    }

    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }
}
