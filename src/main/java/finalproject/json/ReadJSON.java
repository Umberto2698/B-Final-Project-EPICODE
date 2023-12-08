package finalproject.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadJSON {
    static JSONParser jsonParser = new JSONParser();

    public static void readJson(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            JSONArray donationCenterList = (JSONArray) obj.get("dati");
            donationCenterList.forEach(center -> parseDonationCenterObject((JSONObject) center));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseDonationCenterObject(JSONObject donationCenter) {
        String name = (String) donationCenter.get("azienda");
        System.out.println(name);
    }
}


