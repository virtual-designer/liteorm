package org.osndevs.liteorm.models;

import org.osndevs.liteorm.utils.DatabaseUtils;

import java.util.List;

public abstract class ActiveRecord {
    private Model modelAnnotation;
    private List<Column> columnAnnotations;

    public Model getModelAnnotation() {
        if (modelAnnotation == null) {
            modelAnnotation = DatabaseUtils.getModelAnnotation(this.getClass());
        }

        return modelAnnotation;
    }
}
