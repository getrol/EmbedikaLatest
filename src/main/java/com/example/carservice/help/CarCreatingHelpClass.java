package com.example.carservice.help;


import com.example.carservice.Entities.Car;
import com.example.carservice.dbFeatures.EmbedikaException;

import java.util.ArrayList;
import java.util.Random;

public class CarCreatingHelpClass {
    static char[] goodNumbers = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}; //10
    static char[] goodWords = new char[]{'A', 'B', 'E', 'K', 'M', 'H', 'O', 'P', 'C', 'T', 'Y', 'X',
            'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'}; // 12+12

    static String[] models = new String[]{"Niva", "Mazda", "Porch", "Morgan", "Mini", "Jaguar"};
    static String[] colors = new String[]{"Blue", "Black", "Pink", "Green", "Yellow", "Red"};

    public static Car checkAndCreateCar(String number, String model, String color, String year) throws EmbedikaException {
        checkNumber(number.toUpperCase());
        checkModel(model);
        checkColor(color);
        checkYear(year);
        return new Car(number.toUpperCase(), model, color, Integer.parseInt(year));
    }

    public static void checkModel(String model) throws EmbedikaException {
        if (model.isEmpty()) {
            throw new EmbedikaException("Не введено поле модели");
        }
        if (model.toCharArray().length > 30) {
            throw new EmbedikaException("Не бывает такого длинного названия модели");
        }
    }
    public static void checkColor(String color) throws EmbedikaException {
        if (color.isEmpty()) {
            throw new EmbedikaException("Цвет не введен");
        }
        if (color.toCharArray().length > 30) {
            throw new EmbedikaException("Не бывает такого длинного названия модели");
        }
    }

    public static void checkNumber(String number) throws EmbedikaException {

        char[] chars = number.toCharArray();
        if (number.isEmpty()) {
            throw new EmbedikaException("Строка номера пуста");
        }
        if (chars.length < 8 || chars.length > 9) {
            throw new EmbedikaException("Идентификационный номер слишком короткий или слишком длинный");
        }
        char[] charsNumbers = (chars.length == 8) ?
                new char[]{chars[1], chars[2], chars[3], chars[6], chars[7]} :
                new char[]{chars[1], chars[2], chars[3], chars[6], chars[7], chars[8]};

        for (char charsNumber : charsNumbers) {
            boolean found = false;
            for (char goodNumber : goodNumbers) {
                if (charsNumber == goodNumber) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new EmbedikaException("Неверный формат номера. Используйте шаблон A111AA12");
            }

        }

        char [] charsWords = {chars[0], chars[4], chars[5]};
        for (char charsWord : charsWords) {
            boolean found = false;
            for (char goodWord : goodWords) {
                if (charsWord == goodWord) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new EmbedikaException("Неверный формат номера. Используйте шаблон A111AA12");
            }

        }

    }

    public static void checkYear(String yearS) throws EmbedikaException {
        int i;
        if (yearS.isEmpty()) {throw new EmbedikaException("Поле ввода года пусто");}
        try {
            i = Integer.parseInt(yearS);
        } catch (Exception e) {
            throw new EmbedikaException("Неверный формат года, не используйте буквы!");
        }
        if (i < 1500 || i > 2023) {
            throw new EmbedikaException("Не может быть машины с таким годом. Введите дату от 1500 до 2023");
        }
    }

    public static String createNumber() {
        char[] futureNumber = new char[8];
        Random random = new Random();

        futureNumber[0] = goodWords[random.nextInt(0, 12)];
        futureNumber[1] = goodNumbers[random.nextInt(0, 10)];
        futureNumber[2] = goodNumbers[random.nextInt(0, 10)];
        futureNumber[3] = goodNumbers[random.nextInt(0, 10)];
        futureNumber[4] = goodWords[random.nextInt(0, 12)];
        futureNumber[5] = goodWords[random.nextInt(0, 12)];
        futureNumber[6] = goodNumbers[random.nextInt(0, 10)];
        futureNumber[7] = goodNumbers[random.nextInt(0, 10)];

        return String.valueOf(futureNumber);
    }

    public static String createModel() {
        return models[new Random().nextInt(0, models.length)];
    }

    public static String createColor() {
        return colors[new Random().nextInt(0, colors.length)];
    }

    public static int createYear() {
        return new Random().nextInt(1500, 2024);
    }

    public static Car createCar() {
        return new Car(createNumber(), createModel(), createColor(), createYear());
    }

    public static String[] stringFromCarMassiveWithBorder(Car [] cars, int border){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Найдены следующие результаты:");
        if (cars.length>border){
            arrayList.add("(Найдено слишком много совпадений. Показаны первые "+border+" результатов)");
            for (int i = 0; i < border; i++) {
                Car car = cars[i];
                arrayList.add(String.format("Машина: %d, Номер: %s, Марка: %s, Цвет: %s, Год: %d", i, car.getIdentificationNumber(),
                        car.getModel(), car.getColor(), car.getBornYear()));
            }
        } else {
            for (int i = 0; i < cars.length; i++) {
                Car car = cars[i];
                arrayList.add(String.format("Машина: %d, Номер: %s, Марка: %s, Цвет: %s, Год: %d", i, car.getIdentificationNumber(),
                        car.getModel(), car.getColor(), car.getBornYear()));
            }
        }
        return arrayList.toArray(new String[0]);

    }

    public static String[] createStringNumbersFromCarMassive(Car [] cars){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Были успешно удалены следующие машины:");
        if (cars.length>20){
            arrayList.add("(Удаленных машин слишком много, показаны первые 20 результатов)");
            for (int i = 0; i < 20; i++) {
                Car car = cars[i];
                arrayList.add(String.format("Машина: %d, Номер: %s", i+1, car.getIdentificationNumber()));
            }
        } else {
            for (int i = 0; i < cars.length; i++) {
                Car car = cars[i];
                arrayList.add(String.format("Машина: %d, Номер: %s", i+1, car.getIdentificationNumber()));
            }
        }
        return arrayList.toArray(new String[0]);
    }

    public static String[] createStringFromCarMassive(Car [] cars){
        return stringFromCarMassiveWithBorder(cars, cars.length);
    }



}

