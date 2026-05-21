package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.models.ColumnType;
import org.osndevs.liteorm.models.ModelDescriptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery<T> extends Query {
    Class<T> clazz;
    ModelDescriptor<T> descriptor;

    public SelectQuery(Database database, Class<T> clazz) {
        super(database);
        this.clazz = clazz;
        descriptor = database.getQueryBuilder().getDescriptor(clazz);
        sql = "SELECT ";

        StringBuilder cols = new StringBuilder();

        for (var columnDescriptor : descriptor.columnDescriptors) {
            if (!cols.isEmpty()) {
                cols.append(", ");
            }

            cols.append(columnDescriptor.name());
        }

        sql += cols + " FROM " + descriptor.tableName;
    }

    public List<T> getAll() throws SQLException {
        return getAllInternal(Long.MAX_VALUE);
    }

    public T getFirst() throws SQLException {
        final var list = getAllInternal(1);
        return list.isEmpty() ? null : list.getFirst();
    }

    private List<T> getAllInternal(long maxCount) throws SQLException {
        List<T> results = new ArrayList<>();

        try (var statement = toStatement()) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (descriptor.constructor == null) {
                    throw new RuntimeException("No initializer constructor is defined for this model");
                }

                long count = 0;

                while (resultSet.next() && count < maxCount) {
                    Object[] args = new Object[descriptor.columnDescriptors.size()];
                    int index = 0;

                    for (var columnDescriptor : descriptor.columnDescriptors) {
                        Object value = resultSet.getObject(columnDescriptor.name());

                        if (value instanceof Integer && columnDescriptor.type() == ColumnType.LONG) {
                            value = ((Integer) value).longValue();
                        }

                        args[index++] = value;
                    }

                    try {
                        results.add(descriptor.constructor.newInstance(args));
                    }
                    catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }

                    count++;
                }
            }
        }

        return results;
    }

    @Override
    public PreparedStatement toStatement() throws SQLException {
        // noinspection SqlSourceToSinkFlow
        return database.getConnection().prepareStatement(sql);
    }
}
