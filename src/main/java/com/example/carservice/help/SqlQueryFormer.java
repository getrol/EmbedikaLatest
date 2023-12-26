package com.example.carservice.help;

import com.example.carservice.dbFeatures.EmbedikaException;

import java.util.HashMap;

public class SqlQueryFormer {
    public static HashMap<String, String> getHashMapForm(String number, String model,
                                                         String color, String year) throws EmbedikaException {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!number.isEmpty()) {
            CarCreatingHelpClass.checkNumber(number);
            hashMap.put("Number", number);
        }
        if (!model.isEmpty()) {
            CarCreatingHelpClass.checkModel(model);
            hashMap.put("Model", model);
        }
        if (!color.isEmpty()) {
            CarCreatingHelpClass.checkColor(color);
            hashMap.put("Color", color);
        }
        if (!year.isEmpty()) {
            CarCreatingHelpClass.checkYear(year);
            hashMap.put("Year", year);
        }
        if (hashMap.isEmpty()) {
            throw new EmbedikaException("Необходимо заполнить хотя бы 1 критерий для поиска");
        } else {
            return hashMap;
        }
    }

}
