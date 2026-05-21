package org.osndevs.liteorm.models;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ModelDescriptor(String tableName, @Nullable ColumnDescriptor primaryKeyDescriptor, List<ColumnDescriptor> columnDescriptors, Model modelAnnotation) {

}
