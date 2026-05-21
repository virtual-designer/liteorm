package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.models.ColumnDescriptor;
import org.osndevs.liteorm.models.ColumnType;

public record InsertQueryValue(int index, ColumnDescriptor columnDescriptor, Object value) {
    public ColumnType getType() {
        return columnDescriptor.type();
    }
}
