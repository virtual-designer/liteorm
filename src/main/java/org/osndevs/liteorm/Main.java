package org.osndevs.liteorm;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.utils.DatabaseUtils;

public class Main {
    static void main() throws Exception {
        System.out.println("Table: " + DatabaseUtils.getTableNameFromModel(User.class));

        try (final var database = new Database("postgresql://postgres:root@localhost:5432/liteorm")) {
            User user1 = new User();

            user1.setName("Admin User 2");
            user1.setUsername("root2");
            user1.setPassword("1234");
            user1.setAdministrator(true);

            User user2 = new User();

            user2.setName("Admin User 3");
            user2.setUsername("root3");
            user2.setPassword("1234");
            user2.setAdministrator(true);

            database.getQueryBuilder().insert(user1, user2);
        }
    }
}
