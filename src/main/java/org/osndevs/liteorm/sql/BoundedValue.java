package org.osndevs.liteorm.sql;

import org.osndevs.liteorm.models.ColumnType;

public record BoundedValue(int index, ColumnType type, Object value) {
}
