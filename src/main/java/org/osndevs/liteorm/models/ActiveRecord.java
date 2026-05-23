package org.osndevs.liteorm.models;

import org.osndevs.liteorm.database.Database;
import org.osndevs.liteorm.utils.DatabaseUtils;

import java.sql.SQLException;
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

    @SuppressWarnings("unchecked")
    public void save(Database database) throws SQLException {
        database.insert((Class<Object>) (Object) this.getClass(), this).execute();
    }

    public void save() throws SQLException {
        save(Database.getGlobalInstance());
    }
}
