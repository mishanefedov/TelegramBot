package main.java;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CovidStats {
    public static String getStatsFor(String country){
        try {
            System.out.println("getting Stats");
            OkHttpClient client = new OkHttpClient();

            Request request=new Request.Builder()
                    .url("https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/stats?country=" + country)        //choose new url
                    .get()
                    .addHeader("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "b741b9beb5msh6e277c18c1c922fp10a318jsn8dbfaf1d02de")
                    .build();

            Response response=client.newCall(request).execute();
            return Stats.parse(response.body().string());
        }catch (Exception e){
            return null;
        }
    }
}
