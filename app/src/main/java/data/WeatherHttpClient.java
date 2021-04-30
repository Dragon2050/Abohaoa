package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

/**
 * Created by Anon on 12/15/2016.
 */

public class WeatherHttpClient {

    public String getWeatherData(String place){
        HttpURLConnection connection=null;
        InputStream inputStream=null;

        try{
            connection=(HttpURLConnection)(new URL(Utils.BASE_URL+place+"&appid=23c98ca7ba6ddd3a12d0dcb75b7141d0")).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.connect();

            //Read the response
            StringBuffer stringBuffer=new StringBuffer();
            inputStream=connection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;

            while((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line+"\r\n");
            }
            inputStream.close();
            connection.disconnect();
            return stringBuffer.toString();
        } catch (IOException e){
            e.printStackTrace();


        }
        return null;
    }

}

