package org.osndevs.liteorm;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.utils.DatabaseUtils;

public class Main {
    static void main() throws Exception {
        System.out.println("Table: " + DatabaseUtils.getTableNameFromModel(User.class));

        try (final var database = Database.createGlobalInstance("postgresql://postgres:root@localhost:5432/liteorm")) {
            User user1 = new User();
            user1.setName("Admin User 4");
            user1.setUsername("root4");
            user1.setPassword("1234");
            user1.setAdministrator(true);
            user1.save();
        }
    }
}
