package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.models.ColumnType;

public abstract class QueryWithConditions<T extends QueryWithConditions<?>> extends Query<T> {
    protected boolean whereAdded = false;

    public QueryWithConditions(Database database) {
        super(database);
    }

    public T where(String left, String operator, Object right, ColumnType type) {
        if (!whereAdded) {
            sql += " where ";
            whereAdded = true;
        }

        sql += " " + left + " " + operator + " ? ";
        boundedValues.add(new BoundedValue(boundedValueIndex++, type, right));
        return returnThis();
    }

    public T where(String left, String operator, Integer right) {
        return where(left, operator, right, ColumnType.INTEGER);
    }

    public T where(String left, String operator, String right) {
        return where(left, operator, right, ColumnType.STRING);
    }

    public T where(String left, String operator, StringBuilder right) {
        return where(left, operator, right.toString(), ColumnType.STRING);
    }

    public T where(String left, String operator, Boolean right) {
        return where(left, operator, right, ColumnType.BOOLEAN);
    }

    public T where(String left, String operator, Long right) {
        return where(left, operator, right, ColumnType.LONG);
    }

    public T and() {
        sql += " and ";
        return returnThis();
    }

    public T or() {
        sql += " or ";
        return returnThis();
    }

    public T limit(long limit) {
        sql += " limit ? ";
        boundedValues.add(new BoundedValue(boundedValueIndex++, ColumnType.LONG, limit));
        return returnThis();
    }

    public T limit(long offset, long limit) {
        sql += " limit ?, ? ";
        boundedValues.add(new BoundedValue(boundedValueIndex++, ColumnType.LONG, offset));
        boundedValues.add(new BoundedValue(boundedValueIndex++, ColumnType.LONG, limit));
        return returnThis();
    }
}
