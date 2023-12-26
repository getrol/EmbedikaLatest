package com.example.carservice.dbFeatures;


import com.example.carservice.Entities.Car;
import com.example.carservice.Entities.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBaseCar {
    public static final int OPERATION_AND = 5;
    public static final int OPERATION_OR = 4;
    public static final int OPERATION_SEARCH = 0;
    public static final int OPERATION_DELETE = 1;

    private final Statement statement;
    private final DataBaseLog dataBaseLog;
    private final Connection connection;


    public DataBaseCar(Connection connection) {
        try {
            this.connection = connection;
            this.statement = connection.createStatement();
            this.dataBaseLog = new DataBaseLog(connection.createStatement());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public DataBaseLog getDataBaseLog() {
        return dataBaseLog;
    }

    public void writeCar(Car car) throws EmbedikaException {
        if (!isExist(car.getIdentificationNumber())) {
            try {
                String executeString = String.format("INSERT INTO 'Cars' ('Number', 'Model', 'Color', 'Year') " +
                                "VALUES ('%s', '%s', '%s', '%d'); ",
                        car.getIdentificationNumber(),
                        car.getModel(),
                        car.getColor(),
                        car.getBornYear());
                statement.execute(executeString);
                dataBaseLog.writeLog(new Log("Insert car with ID: " + car.getIdentificationNumber()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new EmbedikaException("Машина с таким номером уже существует");
        }
    }

    public void deleteCar(String identificationNumber) throws EmbedikaException {
        if (isExist(identificationNumber)) {
            try {
                String executeString = String.format("DELETE FROM Cars WHERE Number = '%s'", identificationNumber);
                statement.execute(executeString);
                dataBaseLog.writeLog(new Log("Delete car with ID: " + identificationNumber));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new EmbedikaException("Машины с таким номером не существует");
        }

    }


    public Car[] findAllCars() throws EmbedikaException {
        try {
            String executeString = "SELECT * FROM Cars";
            ResultSet resultSet = statement.executeQuery(executeString);
            ArrayList<Car> arrayList = new ArrayList<>();

            while (resultSet.next()) {
                String ident = resultSet.getString("Number");
                String model = resultSet.getString("Model");
                String color = resultSet.getString("Color");
                int year = resultSet.getInt("Year");

                arrayList.add(new Car(ident, model, color, year));
            }
            dataBaseLog.writeLog(new Log("Select all Cars"));
            if (arrayList.isEmpty()) {
                throw new EmbedikaException("База данных машин полностью пуста.");
            }

            return arrayList.toArray(new Car[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Car[] operationWithAttributes(HashMap<String, String> attributes, int operationType, int operationCode) throws EmbedikaException, SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Select * FROM Cars WHERE ");
        if (operationCode == OPERATION_AND) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                stringBuilder.append(String.format("%s = '%s' AND ", entry.getKey(), entry.getValue()));

            }
            stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length() - 1);
        } else if (operationCode == OPERATION_OR) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                stringBuilder.append(String.format("%s = '%s' OR ", entry.getKey(), entry.getValue()));
            }
            stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length() - 1);
        }
        ArrayList<Car> arrayList = findCarsInDB(stringBuilder.toString());

        if (arrayList.isEmpty()) {
            throw new EmbedikaException("По заданным атрибутам не найдена ни одна запись");
        }

        if (operationType == OPERATION_SEARCH) {
            dataBaseLog.writeLog(new Log("Searching cars with attributes"));
        } else if (operationType == OPERATION_DELETE) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM Cars WHERE ");
            if (operationCode == OPERATION_AND) {
                for (Map.Entry<String, String> entry : attributes.entrySet()) {
                    stringBuilder.append(String.format("%s = '%s' AND ", entry.getKey(), entry.getValue()));
                }
                stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length() - 1);
            }
            if (operationCode == OPERATION_OR) {
                for (Map.Entry<String, String> entry : attributes.entrySet()) {
                    stringBuilder.append(String.format("%s = '%s' OR ", entry.getKey(), entry.getValue()));
                }
                stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length() - 1);
            }
            statement.execute(stringBuilder.toString());
            dataBaseLog.writeLog(new Log("Deleting cars with attributes"));
        }
        return arrayList.toArray(new Car[0]);
        }

        private ArrayList<Car> findCarsInDB (String query) throws SQLException {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Car> arrayList = new ArrayList<>();
            while (resultSet.next()) {
                String ident = resultSet.getString("Number");
                String model = resultSet.getString("Model");
                String color = resultSet.getString("Color");
                int year = resultSet.getInt("Year");
                arrayList.add(new Car(ident, model, color, year));
            }
            return arrayList;
        }

        public void deleteAllTable () throws SQLException {
            statement.execute("DELETE FROM Cars");
        }

        private boolean isExist (String identification){
            String s = null;
            try {
                ResultSet resultSet = connection.createStatement().executeQuery(String.format("Select * FROM Cars WHERE Number = '%s'", identification.toUpperCase()));
                s = resultSet.getString("Number");
            } catch (SQLException e) {
            }
            return s != null;

        }

    }
