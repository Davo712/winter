package com.winter.context.dbLanguage;

import com.winter.context.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class DbConnector {

    private Connection connection = null;
    public String dbName;
    public String username;
    public String password;
    public String hostName;

    public DbConnector(String dbName, String username, String password, String hostName) {
        this.dbName = dbName;
        this.username = username;
        this.password = password;
        this.hostName = hostName;
    }

    public static final String yQuerySelectExample = "get user (id=1,username='dav@mail.ru',name='dav')";
    public static final String yQueryInsertExample = "add user (id=1,username='test@mail.ru')";


    public Statement connectToDBAndGetStatement() throws ClassNotFoundException, SQLException {
        if (!Context.isRunned) {
            return null;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + hostName + "/" + dbName,
                username, password);
        Statement statement;
        statement = connection.createStatement();
        return statement;
    }

    public <T> T setQuery(String yQuery, Class<T> c) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!Context.isRunned) {
            return null;
        }
        String[] strings = yQuery.split(" ");
        switch (strings[0]) {
            case "get":
                String x = strings[2].replace("(", "");
                x = x.replace(")", "");
                String[] params = x.split(",");
                String queryParams = "";
                for (int i = 0; i < params.length; i++) {
                    if (params.length - i == 1) {
                        queryParams = queryParams + params[i];
                        break;
                    }
                    queryParams = queryParams + params[i] + " and ";
                }
                return getQuery(strings[1], queryParams, c);
            case "add":
                String y = strings[2].replace("(", "");
                y = y.replace(")", "");
                String[] keyValue = y.split(",");
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < keyValue.length; i++) {
                    map.put(keyValue[i].split("=")[0], (keyValue[i].split("=")[1]));
                }

                String content = "(";
                String values = "(";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    content = content + key + ",";
                    values = values + value + ",";
                }
                content = content.substring(0, content.length() - 1);
                values = values.substring(0, values.length() - 1);
                content = content + ")";
                values = values + ")";
                addQuery(strings[1], content, values);
                break;
            case "delete":
                break;
            case "update":
                break;
        }
        return null;
    }

    public void setQuery(String yQuery,Object object) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!Context.isRunned) {
            return;
        }
        String[] strings = yQuery.split(" ");
        switch (strings[0]) {
            case "add":
                String y = strings[2].replace("(", "");
                y = y.replace(")", "");
                String[] keyValue = y.split(",");
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < keyValue.length; i++) {
                    map.put(keyValue[i].split("=")[0], (keyValue[i].split("=")[1]));
                }

                String content = "(";
                String values = "(";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    content = content + key + ",";
                    values = values + value + ",";
                }
                content = content.substring(0, content.length() - 1);
                values = values.substring(0, values.length() - 1);
                content = content + ")";
                values = values + ")";
                addQuery(strings[1], content, values);
                break;
            case "delete":
                break;
            case "update":
                break;
            default:
                return;
        }
        return;
    }


    private void addQuery(String tableName, String content, String values) throws SQLException, ClassNotFoundException {
        connectToDBAndGetStatement().executeUpdate("insert into " + tableName + " " + content + " values " + values);
        System.out.println("Added");
    }

    private <T> T getQuery(String tableName, String queryParams, Class<T> c) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ResultSet resultSet = connectToDBAndGetStatement().executeQuery("select * from " + tableName + " where " + queryParams);
        Constructor<?> ctor = c.getConstructor();
        Object o = ctor.newInstance();
        Field[] fields = c.getFields();
        if (fields.length == 0) {
            fields = c.getDeclaredFields();
            for (Field f :
                    fields) {
                f.setAccessible(true);
            }
        }
        if (resultSet.next()) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(o, resultSet.getObject(fields[i].getName(), fields[i].getType()));
            }
        }
        return (T) o;
    }

}
