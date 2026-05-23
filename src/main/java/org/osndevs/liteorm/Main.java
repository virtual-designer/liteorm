package org.osndevs.liteorm;

import org.osndevs.liteorm.database.Database;

public class Main {
    static void main() throws Exception {
        try (final var database = Database.createGlobalInstance("postgresql://postgres:root@localhost:5432/liteorm")) {
            final var users = database
                    .select(User.class)
                    .where("id", ">", 5)
                    .and()
                    .where("id", "<", 8)
                    .getAll();

            for (final var user : users) {
                System.out.println("User #" + user.getId() + ": @" + user.getUsername());
            }
        }
    }
}
