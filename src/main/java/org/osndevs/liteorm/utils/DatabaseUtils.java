package org.osndevs.liteorm.utils;

import org.jetbrains.annotations.NotNull;
import org.osndevs.liteorm.models.Model;

public class DatabaseUtils {
    @NotNull
    public static Model getModelAnnotation(@NotNull Class<?> model) {
        Model modelAnnotation = model.getAnnotation(Model.class);

        if (modelAnnotation == null) {
            throw new UnsupportedOperationException("Invalid model class: is it annotated with '@Model'?");
        }

        return modelAnnotation;
    }

    @NotNull
    public static String getTableNameFromModel(@NotNull Class<?> model) {
        Model modelAnnotation = getModelAnnotation(model);
        String tableName = modelAnnotation.tableName();

        if (!tableName.isEmpty()) {
            return tableName;
        }

        return model.getSimpleName().toLowerCase() + "s";
    }
}
