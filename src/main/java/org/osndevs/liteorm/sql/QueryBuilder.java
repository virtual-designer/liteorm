package org.osndevs.liteorm.sql;

import org.jetbrains.annotations.NotNull;
import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.models.*;
import org.osndevs.liteorm.utils.DatabaseUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
    Database database;
    Map<Class<?>, ModelDescriptor<?>> modelDescriptorMap = new HashMap<>();

    public QueryBuilder(Database database) {
        this.database = database;
    }

    public String getTableName(Class<?> modelClass) {
        return getDescriptor(modelClass).tableName;
    }

    @NotNull
    protected <T> ModelDescriptor<T> cacheDescriptor(Class<T> klass) {
        List<ColumnDescriptor> columnDescriptorList = new ArrayList<>();
        Field[] fields = klass.getDeclaredFields();
        ColumnDescriptor primaryKeyDescriptor = null;

        for (Field field : fields) {
            Column columnAnnotation = field.getAnnotation(Column.class);

            if (columnAnnotation == null) {
                continue;
            }

            String name = columnAnnotation.actualName();

            if (name.isEmpty()) {
                name = field.getName();
                name = name.substring(0, 1).toLowerCase() +
                        name.substring(1).replaceAll("([A-Z])", "_$1").toLowerCase();
            }

            ColumnType type = columnAnnotation.type();

            if (type == ColumnType.DEFAULT) {
                type = getColumnType(field);
            }

            Map<Class<?>, Annotation> otherAnnotations = new HashMap<>();
            PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);

            if (primaryKeyAnnotation != null) {
                otherAnnotations.put(PrimaryKey.class, primaryKeyAnnotation);
            }

            final var descriptor = new ColumnDescriptor(name, type, field, columnAnnotation, otherAnnotations);
            columnDescriptorList.add(descriptor);

            if (primaryKeyAnnotation != null) {
                if (primaryKeyDescriptor != null) {
                    throw new RuntimeException("Multiple primary keys are not allowed");
                }

                primaryKeyDescriptor = descriptor;
            }
        }

        Model modelAnnotation = DatabaseUtils.getModelAnnotation(klass);
        ModelDescriptor<T> descriptor = new ModelDescriptor<>(klass, DatabaseUtils.getTableNameFromModel(klass), primaryKeyDescriptor, columnDescriptorList, modelAnnotation);
        modelDescriptorMap.put(klass, descriptor);
        return descriptor;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    protected <T> ModelDescriptor<T> getDescriptor(Class<T> modelClass) {
        ModelDescriptor<?> descriptor = modelDescriptorMap.get(modelClass);

        if (descriptor == null) {
            descriptor = cacheDescriptor(modelClass);
        }

        return (ModelDescriptor<T>) descriptor;
    }

    protected ColumnType getColumnType(Field field) {
        Type type = field.getType();

        if (type == Integer.class || type == int.class) {
            return ColumnType.INTEGER;
        }
        else if (type == Long.class || type == long.class) {
            return ColumnType.LONG;
        }
        else if (type == Boolean.class || type == boolean.class) {
            return ColumnType.BOOLEAN;
        }
        else if (type == String.class) {
            return ColumnType.STRING;
        }

        throw new RuntimeException("Invalid column type");
    }

    @SafeVarargs
    public final <T> InsertQuery<T> buildInsertStatement(Class<T> clazz, T... models) {
        return new InsertQuery<>(database, clazz, models);
    }

    public <T> SelectQuery<T> buildSelectStatement(Class<T> clazz) {
        return new SelectQuery<>(database, clazz);
    }
}
