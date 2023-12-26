package com.example.carservice.controllers;

import com.example.carservice.Entities.Car;
import com.example.carservice.dbFeatures.DataBaseCar;
import com.example.carservice.dbFeatures.SqliteDBController;
import com.example.carservice.help.AdminHelper;
import com.example.carservice.help.CarCreatingHelpClass;
import com.example.carservice.help.SqlQueryFormer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

@Controller
public class ServiceController {

    private final SqliteDBController sqliteDBController;

    public ServiceController() {
        this.sqliteDBController = SqliteDBController.getInstance();
    }

    @GetMapping("/")
    public String service() {
        return "mainService";
    }

    @GetMapping("/car/addService")
    public String carCreateService() {
        return "addService";
    }

    @GetMapping("/car/searchService")
    public String carSearchService() {
        return "searchService";
    }

    @GetMapping("/car/deleteService")
    public String carDeleteService() {
        return "deleteService";
    }

    @GetMapping("/car/helpService")
    public String carHelpService() {
        return "helpService";
    }


    @PostMapping("car/create")
    public String carCreate(@RequestParam String button, @RequestParam String carNumber, @RequestParam String
            carModel, @RequestParam String carColor, @RequestParam String carYear, Model model) {
        switch (button) {
            case "Добавить машину вручную" -> {
                try {
                    sqliteDBController.getDataBaseCar().writeCar(CarCreatingHelpClass.checkAndCreateCar(carNumber, carModel, carColor, carYear));
                    model.addAttribute("result", "Машина была добавлена успешно");
                    return "addService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "addService";
                }
            }
            case "Добавить машину с из JSON файла" -> {
//                try {
//                    Car car = JSONtoCar.parseToCar(new File("Filename.json"));
//                    sqliteDBController.getDataBaseCar().writeCar(CarCreatingHelpClass.checkAndCreateCar(car.getIdentificationNumber(),
//                            car.getModel(),car.getColor(),String.valueOf(car.getBornYear())));
//                    model.addAttribute("result", "Машина была добавлена успешно");
//                    return "addService";
//                } catch (Exception e) {
//                    model.addAttribute("result", e.getMessage());
//                    return "addService";
//                }
            }
        }
        return "addService";
    }

    @PostMapping("car/createJSON")
    public String carCreateJSON() {
        return "mainService";
    }

    @RequestMapping("car/search")
    public String carSearch(@RequestParam String button, @RequestParam String carNumber, @RequestParam String
            carModel,
                            @RequestParam String carColor, @RequestParam String carYear, Model model) {
        switch (button) {
            case "Найти 'И'" -> {
                try {
                    Car[] cars = sqliteDBController.getDataBaseCar().operationWithAttributes(SqlQueryFormer.getHashMapForm(
                            carNumber, carModel, carColor, carYear), DataBaseCar.OPERATION_SEARCH, DataBaseCar.OPERATION_AND
                    );
                    model.addAttribute("result", CarCreatingHelpClass.stringFromCarMassiveWithBorder(cars, 15));
                    return "searchService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "searchService";
                }
            }
            case "Найти 'ИЛИ'" -> {
                try {
                    Car[] cars = sqliteDBController.getDataBaseCar().operationWithAttributes(SqlQueryFormer.getHashMapForm(
                            carNumber, carModel, carColor, carYear), DataBaseCar.OPERATION_SEARCH, DataBaseCar.OPERATION_OR
                    );
                    model.addAttribute("result", CarCreatingHelpClass.stringFromCarMassiveWithBorder(cars, 15));
                    return "searchService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "searchService";
                }
            }
            case "Вывести всю базу" -> {
                try {
                    Car[] cars = sqliteDBController.getDataBaseCar().findAllCars();
                    model.addAttribute("result", CarCreatingHelpClass.createStringFromCarMassive(cars));
                    return "searchService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "searchService";
                }
            }
        }

        return "searchService";
    }


    @PostMapping("car/delete")
    public String carDelete(@RequestParam String button, @RequestParam String carNumber, @RequestParam String
            carModel,
                            @RequestParam String carColor, @RequestParam String carYear, Model model) {
        if (button.equals("Удалить машину 'И'")) {
            try {
                Car[] cars = sqliteDBController.getDataBaseCar().operationWithAttributes(SqlQueryFormer.getHashMapForm(
                        carNumber, carModel, carColor, carYear), DataBaseCar.OPERATION_DELETE, DataBaseCar.OPERATION_AND
                );
                model.addAttribute("result", CarCreatingHelpClass.createStringNumbersFromCarMassive(cars));
                return "deleteService";
            } catch (Exception e) {
                model.addAttribute("result", e.getMessage());
                return "deleteService";
            }
        } else if (button.equals("Удалить машину 'ИЛИ'")) {
            try {
                Car[] cars = sqliteDBController.getDataBaseCar().operationWithAttributes(SqlQueryFormer.getHashMapForm(
                        carNumber, carModel, carColor, carYear), DataBaseCar.OPERATION_DELETE, DataBaseCar.OPERATION_OR
                );
                model.addAttribute("result", CarCreatingHelpClass.createStringNumbersFromCarMassive(cars));
                return "deleteService";
            } catch (Exception e) {
                model.addAttribute("result", e.getMessage());
                return "deleteService";
            }
        }
        return "deleteService";

    }

    @RequestMapping("car/helpService")
    public String carSearch(@RequestParam String button, Model model) {
        AdminHelper adminHelper = new AdminHelper(SqliteDBController.getInstance());
        switch (button) {
            case "Добавить 5 случайных машин" -> {
                try {
                    adminHelper.writeFewCars();
                    model.addAttribute("result", new String[]{"Машины успешно добавлены"});
                    return "helpService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "helpService";
                }
            }
            case "Удалить все машины" -> {
                try {
                    adminHelper.deleteCarBase();
                    model.addAttribute("result", new String[]{"База машин успешно удалена"});
                    return "helpService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "helpService";
                }
            }
            case "Вывести все логи" -> {
                try {
                    model.addAttribute("result", adminHelper.getLogs());
                    return "helpService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "helpService";
                }
            }
            case "Удалить логи" -> {
                try {
                    adminHelper.deleteLogs();
                    model.addAttribute("result", new String[]{"База логов успешно удалена"});
                    return "helpService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "helpService";
                }
            }
            case "Статистика по базе" -> {
                try {
                    model.addAttribute("result", adminHelper.getDBStatistics());
                    return "helpService";
                } catch (Exception e) {
                    model.addAttribute("result", e.getMessage());
                    return "helpService";
                }
            }
        }

        return "helpService";
    }


}
