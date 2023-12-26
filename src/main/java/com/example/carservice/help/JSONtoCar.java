package com.example.carservice.help;



import com.example.carservice.Entities.Car;
import com.example.carservice.dbFeatures.EmbedikaException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class JSONtoCar {
    public static Car parseToCar(File file) throws EmbedikaException {
        try {
            Object o = new JSONParser().parse(new FileReader(file));
            JSONObject j = (JSONObject) o;
            String number = (String) j.get("Number");
            String model = (String) j.get("Model");
            String color = (String) j.get("Color");
            String year = (String) j.get("Year");

            return new Car(number,model,color,Integer.parseInt(year));
        } catch (Exception e){
            e.printStackTrace();
        }
        throw new EmbedikaException("Некорректный файл. Приведите его к виду {\n" +
                "    \"Number\": \"A565AA34\",\n" +
                "    \"Model\": \"Lamba\",\n" +
                "    \"Color\": \"Orange\",\n" +
                "    \"Year\": \"1945\"\n" +
                "}");
    }
}
