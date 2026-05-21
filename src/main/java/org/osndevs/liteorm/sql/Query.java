package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Query {
    protected Database database;
    protected String sql;

    public Query(Database database) {
        this.database = database;
    }

    public abstract PreparedStatement toStatement() throws SQLException;

    public void execute() throws SQLException {
        try (PreparedStatement statement = this.toStatement()) {
            statement.execute();
        }
    }

    public String toSQL() {
        return this.sql + ";";
    }
}
