package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.models.ColumnType;
import org.osndevs.liteorm.models.ModelDescriptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery<M> extends QueryWithConditions<SelectQuery<M>> {
    Class<M> clazz;
    ModelDescriptor<M> descriptor;

    public SelectQuery(Database database, Class<M> clazz) {
        super(database);
        this.clazz = clazz;
        boundedValues = new ArrayList<>();
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

    public List<M> getAll() throws SQLException {
        return getAllInternal(Long.MAX_VALUE);
    }

    public M getFirst() throws SQLException {
        final var list = getAllInternal(1);
        return list.isEmpty() ? null : list.getFirst();
    }

    private List<M> getAllInternal(long maxCount) throws SQLException {
        List<M> results = new ArrayList<>();
        database.logQuery(this);

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
}
