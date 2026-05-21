package org.osndevs.liteorm.models;

public enum ColumnType {
    DEFAULT("[default]"),
    INTEGER,
    LONG("bigint"),
    STRING("varchar"),
    TEXT,
    BOOLEAN;

    final String sqlType;

    ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    ColumnType() {
        this.sqlType = name().toLowerCase();
    }

    @Override
    public String toString() {
        return sqlType;
    }
}
