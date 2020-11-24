package main.java;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URLEncoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Bot {
    public static Map<String, String> responses;
    public static void main(String[] args){
        responses=null;
        connect();
    }
    public static void connect(){
        try {
            OkHttpClient client= new OkHttpClient();
            String getMeUrl = URLEncoder.encode("https://api.telegram.org/bot1330605061:AAFT_h8sVStfF59UAZd45gt9_T9lymySz1c/getMe", StandardCharsets.UTF_8.toString());
            Request request = new Request.Builder().url("https://api.telegram.org/bot1330605061:AAFT_h8sVStfF59UAZd45gt9_T9lymySz1c/getMe").get().build();
            System.out.println(request);
            System.out.println("/////");
            Response response= client.newCall(request).execute();
            //client.newCall(request).execute();
            System.out.println(response.body().string());
            long offset=0;
            while (true){
                Thread.sleep(1000);
                request=new Request.Builder().url("https://api.telegram.org/bot1141854084:AAGAJfcep4cSTIMm5gZsQA8RBJR_PY3po3w/getUpdates?offset=" + offset + "&allowed_updates=message").get().build();
                response=client.newCall(request).execute();
                String responseString = response.body().string();

                Object object = new JSONParser().parse(responseString);
                System.out.println(object);
                JSONObject jsonObject = (JSONObject)object;
                System.out.println("jsOb: "+jsonObject);
                JSONArray jsonArray=(JSONArray)jsonObject.get("result");
                System.out.println("jsAr: "+jsonArray);

                if (jsonArray.size()!=0)offset=setOffset(jsonArray);
                for (Object message:jsonArray){
                    sendResponse(((JSONObject)message).get("message"));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void sendResponse(Object message){
        System.out.println("Respondong to message :"+message);

        try {
            OkHttpClient client=new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.telegram.org/bot1141854084:AAGAJfcep4cSTIMm5gZsQA8RBJR_PY3po3w/sendMessage"+
                            "?chat_id="+getChatId(message)+
                            "&text="+getResponseMessage(((JSONObject)message).get("text").toString()).replaceAll(" ","%20"))
                    .post(null)
                    .build();
            Response response=client.newCall(request).execute();
            System.out.println(response.body().string());
        }catch (Exception e){

        }
    }

    public static String getChatId(Object message){
        return ((JSONObject)((JSONObject)message).get("chat")).get("id").toString();
    }

    public static String getResponseMessage(String message) {
        try {
            String responseMessage="";
            System.out.println("generating response to :"+message);
            if (message.equals("/start"))
                responseMessage="Type the country ";
            else
                responseMessage= CovidStats.getStatsFor(message);
            return responseMessage;
        }catch (Exception e){
            return null;
        }
    }
    public static long setOffset(JSONArray jsonArray){
        return Long.parseLong(((JSONObject)jsonArray.get(0)).get("update_id").toString()) + jsonArray.size();
    }
}
