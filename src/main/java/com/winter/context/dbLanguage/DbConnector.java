package com.winter.context.dbLanguage;

import com.winter.context.Context;
import com.winter.context.generatedClasses.User;
import com.winter.context.util.ClassGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
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

    public static final String NATIVE_QUERY_EXAMPLE = "native select * from user where id = 15";
    public static final String Y_QUERY_SELECT_EXAMPLE = "get user (id=1,username='dav@mail.ru',name='dav')";
    public static final String Y_QUERY_INSERT_EXAMPLE = "add user (id=1,username='test@mail.ru')";
    public static final String Y_QUERY_DELETE_EXAMPLE = "delete user (id=1)";
    public static final String Y_QUERY_UPDATE_EXAMPLE = "update user id=5 (username='ddd@mail.ru',name='dav')";


    private Statement connectToDBAndGetStatement() throws ClassNotFoundException, SQLException {
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

    public <T> T execute(String yQuery, Class<T> c) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!Context.isRunned) {
            return null;
        }
        String[] strings = yQuery.split(" ");
        String some;
        switch (strings[0]) {
            case "get":
                some = strings[2].replace("(", "");
                some = some.replace(")", "");
                String[] params = some.split(",");
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
                some = strings[2].replace("(", "");
                some = some.replace(")", "");
                String[] keyValue = some.split(",");
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
                if (addQuery(strings[1], content, values)) {
                    boolean t = true;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                } else {
                    boolean t = false;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                }
            case "delete":
                some = strings[2].replace("(", "");
                some = some.replace(")", "");
                String[] params1 = some.split(",");
                String queryParams1 = "";
                for (int i = 0; i < params1.length; i++) {
                    if (params1.length - i == 1) {
                        queryParams1 = queryParams1 + params1[i];
                        break;
                    }
                    queryParams1 = queryParams1 + params1[i] + " and ";
                }
                if (deleteQuery(strings[1], queryParams1)) {
                    boolean t = true;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                } else {
                    boolean t = false;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                }
            case "update":
                String[] conditions = strings[2].split("=");
                String key = conditions[0];
                String value = conditions[1];

                some = strings[3].replace("(", "");
                some = some.replace(")", "");
                String[] params2 = some.split(",");
                String queryParams2 = "";
                for (int i = 0; i < params2.length; i++) {
                    if (params2.length - i == 1) {
                        queryParams2 = queryParams2 + params2[i];
                        break;
                    }
                    queryParams2 = queryParams2 + params2[i] + " , ";
                }
                queryParams2 = queryParams2 + " where " + key + " = " + value;
                if (updateQuery(strings[1], queryParams2)) {
                    boolean t = true;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                } else {
                    boolean t = false;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                }
            case "native":
                yQuery = yQuery.replaceFirst("native", "");
                yQuery = yQuery.replaceFirst(" ", "");
                return nativeQuery(yQuery, c);
            default:
                return null;
        }
    }

    public <T> T execute(String yQuery, Class<T> c, Object object) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!Context.isRunned) {
            return null;
        }

        String[] strings = yQuery.split(" ");
        Field[] fields = c.getFields();
        String some;
        switch (strings[0]) {
            case "get":
                if (fields.length == 0) {
                    fields = c.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                    }
                }
                String queryParams = "";
                for (int i = 0; i < fields.length; i++) {
                    if (fields.length - i == 1) {
                        if (fields[i].get(object) instanceof String) {
                            queryParams = queryParams + fields[i].getName() + "=" + "'" + fields[i].get(object) + "'";
                        } else if (fields[i].get(object) == null) {
                            queryParams = queryParams + fields[i].getName() + " is " + fields[i].get(object);
                        } else {
                            queryParams = queryParams + fields[i].getName() + "=" + fields[i].get(object);

                        }
                    } else {
                        if (fields[i].get(object) instanceof String) {
                            queryParams = queryParams + fields[i].getName() + "=" + "'" + fields[i].get(object) + "'" + " and ";
                        } else if (fields[i].get(object) == null) {
                            queryParams = queryParams + fields[i].getName() + " is " + fields[i].get(object) + " and ";
                        } else {
                            queryParams = queryParams + fields[i].getName() + "=" + fields[i].get(object) + " and ";
                        }
                    }
                }
                return (T) getQuery(strings[1], queryParams, c);
            case "add":
                if (fields.length == 0) {
                    fields = c.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                    }
                }
                HashMap<String, String> map = new HashMap();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].get(object) instanceof String) {
                        map.put(fields[i].getName(), String.valueOf("'" + fields[i].get(object) + "'"));
                    } else {
                        map.put(fields[i].getName(), String.valueOf(fields[i].get(object)));
                    }
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
                if (addQuery(strings[1], content, values)) {
                    boolean t = true;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                } else {
                    boolean t = false;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                }
            case "delete":
                if (fields.length == 0) {
                    fields = c.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                    }
                }
                String queryParams1 = "";
                for (int i = 0; i < fields.length; i++) {
                    if (fields.length - i == 1) {
                        if (fields[i].get(object) instanceof String) {
                            queryParams1 = queryParams1 + fields[i].getName() + "=" + "'" + fields[i].get(object) + "'";
                        } else if (fields[i].get(object) == null) {
                            queryParams1 = queryParams1 + fields[i].getName() + " is " + fields[i].get(object);
                        } else {
                            queryParams1 = queryParams1 + fields[i].getName() + "=" + fields[i].get(object);
                        }
                    } else {
                        if (fields[i].get(object) instanceof String) {
                            queryParams1 = queryParams1 + fields[i].getName() + "=" + "'" + fields[i].get(object) + "'" + " and ";
                        } else if (fields[i].get(object) == null) {
                            queryParams1 = queryParams1 + fields[i].getName() + " is " + fields[i].get(object) + " and ";
                        } else {
                            queryParams1 = queryParams1 + fields[i].getName() + "=" + fields[i].get(object) + " and ";
                        }
                    }
                }
                if (deleteQuery(strings[1], queryParams1)) {
                    boolean t = true;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                } else {
                    boolean t = false;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                }
            case "update":
                String[] conditions = strings[2].split("=");
                String key = conditions[0];
                String value = conditions[1];

                some = strings[3].replace("(", "");
                some = some.replace(")", "");
                String[] params2 = some.split(",");
                String queryParams2 = "";
                for (int i = 0; i < params2.length; i++) {
                    if (params2.length - i == 1) {
                        queryParams2 = queryParams2 + params2[i];
                        break;
                    }
                    queryParams2 = queryParams2 + params2[i] + " , ";
                }
                queryParams2 = queryParams2 + " where " + key + " = " + value;
                if (updateQuery(strings[1], queryParams2)) {
                    boolean t = true;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                } else {
                    boolean t = false;
                    Optional<T> b = (Optional<T>) Optional.of(t);
                    return (T) b.get();
                }
            case "native":
                yQuery = yQuery.replaceFirst("native", "");
                yQuery = yQuery.replaceFirst(" ", "");
                return nativeQuery(yQuery, c);
            default:
                return null;
        }
    }

    private <T> T nativeQuery(String query, Class<T> c) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println(query);
        if (query.split(" ")[0].equals("select")) {
            List<T> list = new ArrayList<>();
            ResultSet resultSet = connectToDBAndGetStatement().executeQuery(query);
            Constructor<?> ctor = c.getConstructor();
            Object o;
            Field[] fields = c.getFields();
            if (fields.length == 0) {
                fields = c.getDeclaredFields();
                for (Field f :
                        fields) {
                    f.setAccessible(true);
                }
            }
            int rsSize = 0;
            while (resultSet.next()) {
                o = ctor.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].set(o, resultSet.getObject(fields[i].getName(), fields[i].getType()));
                }
                list.add((T) o);
                rsSize++;
            }
            if (rsSize == 1) {
                Object obj = list.get(0);
                return (T) obj;
            }

            return (T) list;
        } else if ((query.split(" ")[0].equals("insert")) || (query.split(" ")[0].equals("update")) || (query.split(" ")[0].equals("delete"))) {
            connectToDBAndGetStatement().executeUpdate(query);
            boolean t = true;
            Optional<T> b = (Optional<T>) Optional.of(t);
            return (T) b.get();
        } else {
            boolean t = false;
            Optional<T> b = (Optional<T>) Optional.of(t);
            return (T) b.get();
        }
    }

    private boolean deleteQuery(String tableName, String queryParams) {
        System.out.println("delete from " + tableName + " where " + queryParams);
        try {
            connectToDBAndGetStatement().executeUpdate("delete  from " + tableName + " where " + queryParams);
            return true;
        } catch (SQLException s) {
            s.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateQuery(String tableName, String queryParam) {
        System.out.println("update " + tableName + " set " + queryParam);
        try {
            connectToDBAndGetStatement().executeUpdate("update " + tableName + " set " + queryParam);
            System.out.println("updated");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean addQuery(String tableName, String content, String values) {
        System.out.println("insert into " + tableName + " " + content + " values " + values);
        try {
            connectToDBAndGetStatement().executeUpdate("insert into " + tableName + " " + content + " values " + values);
            System.out.println("Added");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private <T> T getQuery(String tableName, String queryParams, Class<T> c) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        System.out.println("select * from " + tableName + " where " + queryParams);
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
