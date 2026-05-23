package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class Query<T> {
    protected Database database;
    protected String sql;
    protected List<BoundedValue> boundedValues;
    protected int boundedValueIndex = 1;

    public Query(Database database) {
        this.database = database;
    }

    public void execute() throws SQLException {
        database.logQuery(this);

        try (PreparedStatement statement = this.toStatement()) {
            statement.execute();
        }
    }

    public List<BoundedValue> getBoundedValues() {
        return boundedValues;
    }

    public String toSQL() {
        return this.sql + ";";
    }

    @SuppressWarnings("unchecked")
    protected T returnThis() {
        return (T) this;
    }

    public T append(String sql) {
        this.sql += sql;
        return returnThis();
    }

    public final PreparedStatement toStatement() throws SQLException {
        // noinspection SqlSourceToSinkFlow
        PreparedStatement statement = database.getConnection().prepareStatement(sql + ";");

        for (BoundedValue boundValue : boundedValues) {
            switch (boundValue.type()) {
                case STRING, TEXT -> statement.setString(boundValue.index(), (String) boundValue.value());
                case INTEGER -> statement.setInt(boundValue.index(), (Integer) boundValue.value());
                case LONG -> statement.setLong(boundValue.index(), (Long) boundValue.value());
                case BOOLEAN -> statement.setBoolean(boundValue.index(), (Boolean) boundValue.value());
                case DEFAULT -> throw new RuntimeException("Encountered DEFAULT type when building prepared statement");
            }
        }

        return statement;
    }

}
