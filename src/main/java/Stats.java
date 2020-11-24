package main.java;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stats {
    String country,province;
    int cases, dead, recovered;

    public Stats(String country, String province, int cases,int dead,int recovered){
        this.cases=cases;
        this.country=country;
        this.recovered=recovered;
        this.dead=dead;
        this.province=province;
    }
    public static String parse(String string)throws ParseException{
        System.out.println("parsing :"+string);
        JSONObject jsonObject = (JSONObject)new JSONParser().parse(string);
        if (jsonObject.get("message").toString().startsWith("Country not found."))
            return "Type a walid country";
        JSONObject jsonData = (JSONObject) jsonObject.get("data");
        JSONArray jsonArray = (JSONArray) jsonData.get("covid19Stats");

        StringBuilder response = new StringBuilder();

        for (int i = 0;i<jsonArray.size();i++){
            response.append(getStatsForCountry(jsonArray.get(i)));
        }
        return getCountry(jsonArray.get(0))+": "+response.toString();
    }
    public static String getStatsForCountry(Object data){
        try {
            String province = ((JSONObject)data).get("province").toString();
            String confirmed = ((JSONObject)data).get("confirmed").toString();
            String deaths = ((JSONObject)data).get("deaths").toString();
            String recovered = ((JSONObject)data).get("recovered").toString();

            String res="";
            if (!province.equals(""))res=res+"("+province+")";
            res = res + " confirmed: "+confirmed + ", dead: "+deaths+", recovered: "+ recovered +"%0D%0A";
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String getCountry(Object data){
        return ((JSONObject)data).get("country").toString();
    }
}
