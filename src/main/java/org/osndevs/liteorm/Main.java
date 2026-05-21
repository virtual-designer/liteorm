package org.osndevs.liteorm;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.utils.DatabaseUtils;

public class Main {
    static void main() throws Exception {
        System.out.println("Table: " + DatabaseUtils.getTableNameFromModel(User.class));

        try (final var database = Database.createGlobalInstance("postgresql://postgres:root@localhost:5432/liteorm")) {
            final var query = database.select(User.class);
            final var users = database.select(User.class).getAll();

            for (final var user : users) {
                System.out.println("User #" + user.getId() + ": @" + user.getUsername());
            }
        }
    }
}
