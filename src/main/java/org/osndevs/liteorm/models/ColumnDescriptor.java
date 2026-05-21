package org.osndevs.liteorm.models;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public record ColumnDescriptor(String name, @NotNull ColumnType type, Field field, Column columnAnnotation, Map<Class<?>, Annotation> otherAnnotations) {
}
