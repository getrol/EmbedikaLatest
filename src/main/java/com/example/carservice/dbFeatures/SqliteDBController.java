package com.example.carservice.dbFeatures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteDBController {
    private static volatile SqliteDBController sqliteDBController;
    private boolean isInitialized = false;
    private Statement statement = null;
    private DataBaseCar dataBaseCar;

    private SqliteDBController() {
    }

    //Используем синглтон. Мы не можем допустить создания более 1 контроллера базы данных.
    public static SqliteDBController getInstance() {
        if (sqliteDBController == null) {
            synchronized (SqliteDBController.class) {
                if (sqliteDBController == null) {
                    sqliteDBController = new SqliteDBController();
                    sqliteDBController.init();
                }
            }
        }
        return sqliteDBController;
    }

    public DataBaseCar getDataBaseCar() {
        return dataBaseCar;
    }


    private void init() {
        if (!isInitialized) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:EmbedikaDB.s3db");
                statement = connection.createStatement();
                dataBaseCar = new DataBaseCar(connection);

                checkForDB();
                isInitialized = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Проверяем созданы ли указанные таблицы и создаем, если нет.
    private void checkForDB() throws SQLException {
        statement.execute("CREATE TABLE if not exists 'Cars' ("+
                "'Number' text, 'Model' text, 'Color' text, 'Year' INT);");


        statement.execute("CREATE TABLE if not exists 'Logs' (" +
                "'UserCode' text, 'Request' text, 'RequestData' text);");

    }
}
