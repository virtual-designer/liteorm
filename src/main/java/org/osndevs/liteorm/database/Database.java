package org.osndevs.liteorm.database;

import org.osndevs.liteorm.sql.InsertQuery;
import org.osndevs.liteorm.sql.Query;
import org.osndevs.liteorm.sql.QueryBuilder;
import org.osndevs.liteorm.sql.SelectQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements AutoCloseable {
    private static Database globalInstance;
    private static QueryBuilder queryBuilder;
    private final Connection connection;

    public Database(String url) throws SQLException, URISyntaxException {
        URI uri = new URI(url);
        String userInfo = uri.getUserInfo();
        int firstColonIndex = userInfo.indexOf(':');
        String username = userInfo;
        String password = "";

        if (firstColonIndex >= 0) {
            username = userInfo.substring(0, firstColonIndex);
            password = userInfo.substring(Math.min(firstColonIndex + 1, userInfo.length()));
        }

        String finalUrl = "jdbc:" + uri.getScheme() + "://" + uri.getHost();

        if (uri.getPort() >= 0) {
            finalUrl += ":" + uri.getPort();
        }

        if (uri.getPath() != null && !uri.getPath().isEmpty()) {
            finalUrl += uri.getPath();
        }

        if (uri.getQuery() != null && !uri.getQuery().isEmpty()) {
            finalUrl += "?" + uri.getQuery();
        }

        System.out.println(finalUrl);
        connection = DriverManager.getConnection(finalUrl, username, password);
    }

    public static Database createGlobalInstance(String url) throws SQLException, URISyntaxException {
        globalInstance = new Database(url);
        return globalInstance;
    }

    public static Database getGlobalInstance() {
        return globalInstance;
    }

    public QueryBuilder getQueryBuilder() {
        if (queryBuilder == null) {
            queryBuilder = new QueryBuilder(this);
        }

        return queryBuilder;
    }

    public Connection getConnection() {
        return connection;
    }

    @SafeVarargs
    public final <T> InsertQuery<T> insert(Class<T> clazz, T... models) {
        return getQueryBuilder().buildInsertStatement(clazz, models);
    }

    public <T> SelectQuery<T> select(Class<T> clazz) {
        return getQueryBuilder().buildSelectStatement(clazz);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public void logQuery(Query<?> query) {
        final var sql = query.toSQL();
        System.out.printf("LiteORM: QUERY: %s | PARAMETERS: [", sql);
        var initialIteration = true;

        for (final var boundedValue : query.getBoundedValues()) {
            if (!initialIteration) {
                System.out.print(", ");
            }
            else {
                initialIteration = false;
            }

            System.out.printf("p%d=(%s)", boundedValue.index(), boundedValue.value().getClass().getName());
            System.out.print(boundedValue.value());
        }

        System.out.print("]\n");
    }
}
