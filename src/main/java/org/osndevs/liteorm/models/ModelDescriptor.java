package org.osndevs.liteorm.models;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

public class ModelDescriptor<T> {
    public final String tableName;
    public final @Nullable ColumnDescriptor primaryKeyDescriptor;
    public final List<ColumnDescriptor> columnDescriptors;
    public final Model modelAnnotation;
    public final List<Class<?>> types;
    public final Constructor<T> constructor;
    public final Class<T> clazz;

    public ModelDescriptor(
        Class<T> clazz,
        String tableName,
        @Nullable ColumnDescriptor primaryKeyDescriptor,
        List<ColumnDescriptor> columnDescriptors,
        Model modelAnnotation
    ) {
        this.clazz = clazz;
        this.tableName = tableName;
        this.primaryKeyDescriptor = primaryKeyDescriptor;
        this.columnDescriptors = columnDescriptors;
        this.modelAnnotation = modelAnnotation;
        this.types = new LinkedList<>();

        for (var descriptor : columnDescriptors) {
            this.types.add(descriptor.field().getType());
        }

        Class<?>[] paramTypes = this.types.toArray(new Class<?>[0]);
        Constructor<T> constructor;

        try {
            constructor = clazz.getConstructor(paramTypes);
        }
        catch (NoSuchMethodException _) {
            constructor = null;
        }

        this.constructor = constructor;
    }
}
