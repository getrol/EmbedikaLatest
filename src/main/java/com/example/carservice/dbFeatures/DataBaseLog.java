package com.example.carservice.dbFeatures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.carservice.Entities.Log;

public class DataBaseLog {
    private final Statement statement;

    public DataBaseLog(Statement statement) {
        this.statement = statement;
    }

    public void writeLog(Log log){
        try {
            statement.execute(String.format("INSERT INTO 'Logs' ('UserCode', 'Request', 'RequestData') " +
                    "VALUES ('%s', '%s', '%s'); ",
                    log.getUserId(),
                    log.getRequest(),
                    log.getRequestData()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Log[] getAllLogs() throws EmbedikaException {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Logs");
            ArrayList<Log> arrayList = new ArrayList<>();

            while (resultSet.next()) {
                String ident = resultSet.getString("UserCode");
                String request = resultSet.getString("Request");
                String requestData = resultSet.getString("RequestData");

                arrayList.add(new Log(ident,request, requestData));
            }
            if (arrayList.isEmpty()) {
                throw new EmbedikaException("База логов машин полностью пуста.");
            }
            return arrayList.toArray(new Log[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void deleteAllLogs() throws SQLException {
        statement.execute("DELETE FROM Logs");
    }


    public Log[] findLogsWithAttributesAND(HashMap<String, String> attributes) throws EmbedikaException {
        try {
            StringBuilder stringBuilder = new StringBuilder("Select * FROM Log WHERE ");
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                stringBuilder.append(String.format("%s = '%s' AND ", entry.getKey(), entry.getValue()));
            }
            stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length() - 1);
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            ArrayList<Log> arrayList = new ArrayList<>();

            while (resultSet.next()) {
                String ident = resultSet.getString("UserCode");
                String request = resultSet.getString("Request");
                String requestData = resultSet.getString("RequestData");

                arrayList.add(new Log(ident,request, requestData));
            }
            if (arrayList.isEmpty()){
                throw new EmbedikaException("По заданным атрибутам не найдена ни одна запись");
            }
            return arrayList.toArray(new Log[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Log[] findLogsWithAttributesOR(HashMap<String, String> attributes) throws EmbedikaException {
        try {
            StringBuilder stringBuilder = new StringBuilder("Select * FROM Log WHERE ");
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                stringBuilder.append(String.format("%s = '%s' OR ", entry.getKey(), entry.getValue()));
            }
            stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length() - 1);
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            ArrayList<Log> arrayList = new ArrayList<>();

            while (resultSet.next()) {
                String ident = resultSet.getString("UserCode");
                String request = resultSet.getString("Request");
                String requestData = resultSet.getString("RequestData");

                arrayList.add(new Log(ident,request, requestData));
            }
            if (arrayList.isEmpty()){
                throw new EmbedikaException("По заданным атрибутам не найдена ни одна запись");
            }
            return arrayList.toArray(new Log[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
