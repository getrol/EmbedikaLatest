package com.example.carservice.help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.carservice.Entities.*;
import com.example.carservice.dbFeatures.*;

public class AdminHelper {
    SqliteDBController sqliteDBController;

    public AdminHelper(SqliteDBController sqliteDBController) {
        this.sqliteDBController = SqliteDBController.getInstance();
    }

    private Car[] getRandomCars(int amount) {
        Car[] cars = new Car[amount];
        for (int i = 0; i < amount; i++) {
            cars[i] = CarCreatingHelpClass.createCar();
        }
        return cars;
    }


    public void writeFewCars() throws EmbedikaException {
        Car[] cars = getRandomCars(5);
        for (int i = 0; i < 5; i++) {
            sqliteDBController.getDataBaseCar().writeCar(cars[i]);
        }
    }


    public void deleteCarBase() throws SQLException {
        sqliteDBController.getDataBaseCar().deleteAllTable();
    }


    public String[] getLogs() throws EmbedikaException {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Логи:");
        Log[] logs = sqliteDBController.getDataBaseCar().getDataBaseLog().getAllLogs();
        for (Log log : logs) {
            arrayList.add(String.format("Пользователь: %s, Произвел запрос: %s,\n в: %s\n",
                    log.getUserId(), log.getRequest(), log.getRequestData()));
        }

        return arrayList.toArray(new String[0]);
    }


    public void deleteLogs() throws SQLException {
        sqliteDBController.getDataBaseCar().getDataBaseLog().deleteAllLogs();
    }

    public String[] getDBStatistics() throws EmbedikaException {
        Car[] cars = sqliteDBController.getDataBaseCar().findAllCars();
        Log[] logs = sqliteDBController.getDataBaseCar().getDataBaseLog().getAllLogs();
        String[] results = new String[6];
        results[0] = String.format("Всего машин в базе на данный момент: %d", cars.length);
        Result rM = getStatAmount(cars,"model");
        Result rC = getStatAmount(cars,"color");
        Result rY = getStatAmount(cars,"year");
        results[1] = String.format("Больше всего машин модели: %s. Их насчитывается: %d", rM.key(), rM.value());
        results[2] = String.format("Больше всего машин цвета: %s. Их насчитывается: %d", rC.key(), rC.value());
        results[3] = String.format("Больше всего машин года: %s. Их насчитывается: %d", rY.key(), rY.value());
        results[4] = String.format("Первый лог датирован: %s. Он содержал запрос: %s", logs[0].getRequestData(), logs[0].getRequest());
        results[5] = String.format("Первый лог датирован: %s. Он содержал запрос: %s", logs[0].getRequestData(), logs[0].getRequest());
        return results;
    }

    public Result getStatAmount(Car[] cars, String key) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        switch (key) {
            case "model" -> {
                for (Car car : cars) {
                    String value = car.getModel();
                    if (!hashMap.containsKey(value)) {
                        hashMap.put(value, 1);
                    } else {
                        hashMap.put(value, hashMap.get(value) + 1);
                    }
                }
            }
            case "color" -> {
                for (Car car : cars) {
                    String value = car.getColor();
                    if (!hashMap.containsKey(value)) {
                        hashMap.put(value, 1);
                    } else {
                        hashMap.put(value, hashMap.get(value) + 1);
                    }
                }
            }
            case "year" -> {
                for (Car car : cars) {
                    String value = String.valueOf(car.getBornYear());
                    if (!hashMap.containsKey(value)) {
                        hashMap.put(value, 1);
                    } else {
                        hashMap.put(value, hashMap.get(value) + 1);
                    }
                }
            }
        }
        Integer max = 0;
        String maxKey = "";
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() >= max) {
                max = entry.getValue();
                maxKey = entry.getKey();
            }
        }
        return new Result(maxKey,max);
    }
}
