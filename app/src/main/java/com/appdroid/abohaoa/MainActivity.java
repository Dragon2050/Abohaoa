package com.appdroid.abohaoa;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.TimeZoneFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;*/

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

import Util.Utils;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.CurrentCondition;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private static ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private RelativeLayout relativeLayout;

    Weather weather=new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cityName=(TextView) findViewById(R.id.cityText);
        //iconView= (ImageView) findViewById(R.id.thumbnailIcon);
        temp=(TextView) findViewById(R.id.tempText);
        description= (TextView) findViewById(R.id.cloudText);
        humidity=(TextView) findViewById(R.id.humidText);
        pressure=(TextView) findViewById(R.id.pressureText);
        wind=(TextView) findViewById(R.id.windText);
        sunrise=(TextView) findViewById(R.id.riseText);
        sunset=(TextView) findViewById(R.id.setText);
        updated=(TextView) findViewById(R.id.updateText);
        relativeLayout=(RelativeLayout) findViewById(R.id.content_main);

        CityPreference cityPreference=new CityPreference(MainActivity.this);


        renderWeatherData(cityPreference.getCity());
    }

    public void renderWeatherData(String city){

        WeatherTask weatherTask=new WeatherTask();
        if(weatherTask!=null)
            weatherTask.execute(new String[]{city+"&units=metric"});
        else
            showInputDialog();


    }

    /*private static class DownloadImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {

            return downloadImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
        }

        public static Bitmap downloadImage(String text) {
            try {

                //InputStream is = (InputStream) new URL(Utils.ICON_URL+code+".png").getContent();
                if(text.equals("Clear")){

                    InputStream is = (InputStream) new URL("http://images.clipartpanda.com/rain-clipart-RcGyE4Epi.png").getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();
                    return d;

                }
                else if(text.equals("Clouds")){

                    InputStream is = (InputStream) new URL("http://images.clipartpanda.com/happy-cloud-clipart-cloud-md.png").getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();
                    return d;

                }
                else if(text.equals("Haze")){

                    InputStream is = (InputStream) new URL("http://cdn.xl.thumbs.canstockphoto.com/canstock0809554.jpg").getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();
                    return d;

                }
                else if(text.equals("Mist")){

                    InputStream is = (InputStream) new URL("http://cdn.xl.thumbs.canstockphoto.com/canstock0809554.jpg").getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();
                    return d;

                }
                else{
                    return null;
                }


            } catch (Exception e) {
                return null;
            }
        }


    }*/


    private class WeatherTask extends AsyncTask<String, Void, Weather>{

        @Override
        protected Weather doInBackground(String... params) {

            String data= ((new WeatherHttpClient()).getWeatherData(params[0]));

            System.out.println("Mehedee");
            System.out.println("Alam");
            System.out.println("Anon");
            System.out.println("Abir");
            System.out.println("Naim");
            System.out.println("Jahri");
            System.out.println("Mazhar");
            System.out.println("Zahid");

            System.out.println("Boss"+data);

            try{

                weather= JSONWeatherParser.getWeather(data);

                weather.iconData=weather.currentCondition.getIcon();

                //Log.v("Data:",weather.currentCondition.getDescription());

                //new DownloadImageAsyncTask().execute(weather.currentCondition.getCondition());
                return  weather;


            }catch (NullPointerException e){

                return null;

            }

        }

        @Override
        protected void onPostExecute(Weather weather) {

            try{

                super.onPostExecute(weather);

                //cityName.setText(weather.place.getCity()+","+weather.place.getCountry());
                cityName.setText(convertCity(weather.place.getCity())+","+convertCity(weather.place.getCountry()));

                //Conversion of time

                java.text.DateFormat df= java.text.DateFormat.getTimeInstance();

                String sunriseDate= df.format(new Date(weather.place.getSunrise()*1000));
                String sunriseDateW=convertNumber(sunriseDate);
                String sunsetDate= df.format(new Date(weather.place.getSunset()*1000));
                String sunsetDateW=convertNumber(sunsetDate);
                String updateDate= df.format(new Date(weather.place.getLastupdate()*1000));
                String updateDateW=convertNumber(updateDate);

                sunrise.setText("????????????????????????: "+"????????? "+sunriseDateW+" ???????????????");
                sunset.setText("???????????????????????????: "+"????????????????????? "+sunsetDateW+" ???????????????");
                updated.setText("????????? ????????????????????????: "+updateDateW);

                //conversion of temperature begins here;

                DecimalFormat decimalFormat=new DecimalFormat("#.#");
                String tempString = decimalFormat.format(weather.currentCondition.getTemperature());
                String tempStringW= convertNumber(tempString);
                temp.setText(""+tempStringW+"????????");
                //temp.setText(""+"-???.???"+"????????");

                //Conversion of humidity  Starts here
                String humidityString=Float.toString(weather.currentCondition.getHumidity());
                String humidityStringW=convertNumber(humidityString);
                humidity.setText("???????????????: "+humidityStringW+"%");

                //Conversion of pressure starts here
                String pressureString=Float.toString(weather.currentCondition.getPressure());
                String pressureStringW= convertNumber(pressureString);
                pressure.setText("?????????: "+pressureStringW+" ????????????????????????");

                //Conversion of wind starts here
                String windString=Float.toString(weather.wind.getSpeed());
                String windStringW= convertNumber(windString);
                wind.setText("???????????????: "+windStringW+"??????/?????????");


                convertCondition(weather.currentCondition.getCondition());

                System.out.println(weather.currentCondition.getCondition());

            }catch (NullPointerException e){

                Toast.makeText(getApplicationContext(),"????????? ??????????????? ????????? ??????", Toast.LENGTH_LONG).show();
                showInputDialog();

            }


        }

        public String convertNumber(String text){
            int len,i;
            char[] temporary=new char[15];
            String newString;
            len=text.length();

            for(i=0;i<len;i++){

                if(text.charAt(i)==':')
                    temporary[i]=':';
                if(text.charAt(i)=='.')
                    temporary[i]='.';
                if(text.charAt(i)=='-')
                    temporary[i]='-';
                if(text.charAt(i)=='0')
                    temporary[i]='???';
                if(text.charAt(i)=='1')
                    temporary[i]='???';
                if(text.charAt(i)=='2')
                    temporary[i]='???';
                if(text.charAt(i)=='3')
                    temporary[i]='???';
                if(text.charAt(i)=='4')
                    temporary[i]='???';
                if(text.charAt(i)=='5')
                    temporary[i]='???';
                if(text.charAt(i)=='6')
                    temporary[i]='???';
                if(text.charAt(i)=='7')
                    temporary[i]='???';
                if(text.charAt(i)=='8')
                    temporary[i]='???';
                if(text.charAt(i)=='9')
                    temporary[i]='???';
            }
            newString=String.valueOf(temporary);

            return  newString;

        }

        public String convertCity(String text){

            String convertCountry="????????????????????????";
            String convertCity=null;

            if(text.equals("BD")){

                return convertCountry;

            }
            else if(text.equals("Comilla")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Barguna")){
                convertCity="??????????????????";
                return convertCity;
            }
            else if(text.equals("Barisal")){
                convertCity="??????????????????";
                return convertCity;
            }
            else if(text.equals("Bhola")){
                convertCity="????????????";
                return convertCity;
            }
            else if(text.equals("Jhalokati")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Patuakhali")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Pirojpur")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Bandarban")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Brahmanbaria")){
                convertCity="????????????????????????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Chandpur")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Coxs Bazar")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Chittagong")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Feni")){
                convertCity="????????????";
                return convertCity;
            }
            else if(text.equals("Khagrachhari")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Lakshmipur")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Noakhali")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Rangamati")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Dhaka")){
                convertCity="????????????";
                return convertCity;
            }
            else if(text.equals("Faridpur")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Gazipur")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Gopalganj")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Kishoreganj")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Madaripur")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Manikganj")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Munshiganj")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Narayanganj")){
                convertCity="?????????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Narsingdi")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Rajbari")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Shariatpur")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Tangail")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Bagerhat")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Chuadanga")){
                convertCity="?????????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Jessore")){
                convertCity="????????????";
                return convertCity;
            }
            else if(text.equals("Jhenaidah")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Khulna")){
                convertCity="???????????????";
                return convertCity;
            }
            else if(text.equals("Kushtia")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Magura")){
                convertCity="??????????????????";
                return convertCity;
            }
            else if(text.equals("Meherpur")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Narail")){
                convertCity="??????????????????";
                return convertCity;
            }
            else if(text.equals("Satkhira")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Jamalpur")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Mymensingh")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Netrokona")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Sherpur")){
                convertCity="??????????????????";
                return convertCity;
            }
            else if(text.equals("Bogra")){
                convertCity="??????????????????";
                return convertCity;
            }
            else if(text.equals("Joypurhat")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Naogaon")){
                convertCity="???????????????";
                return convertCity;
            }
            else if(text.equals("Natore")){
                convertCity="???????????????";
                return convertCity;
            }
            else if(text.equals("Chapai Nawabganj")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Rajshahi")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Sirajganj")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Dinajpur")){
                convertCity="????????????????????????";
                return convertCity;
            }
            else if(text.equals("Gaibandha")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Kurigram")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Lalmonirhat")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Nilphamari")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Panchagarh")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Rangpur")){
                convertCity="???????????????";
                return convertCity;
            }
            else if(text.equals("Thakurgaon")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Habiganj")){
                convertCity="?????????????????????";
                return convertCity;
            }
            else if(text.equals("Moulvibazar")){
                convertCity="??????????????????????????????";
                return convertCity;
            }
            else if(text.equals("Sunamganj")){
                convertCity="???????????????????????????";
                return convertCity;
            }
            else if(text.equals("Sylhet")){
                convertCity="???????????????";
                return convertCity;
            }
            else if(text.equals("Pabna")){
                convertCity="???????????????";
                return convertCity;
            }
            else
                return text;


        }

        public void convertCondition(String text){
            if(text.equals("Clear")){

                description.setText("????????????: "+"????????????????????????");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else if(text.equals("Haze")){

                description.setText("????????????: "+"??????????????????");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else if(text.equals("Cloudy")||text.equals("Clouds")){

                description.setText("????????????: "+"???????????????");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else if(text.equals("Mist")){

                description.setText("????????????: "+"???????????????????????????????????????");
                // relativeLayout.setBackgroundColor(Color.parseColor("#rrggbb"));
                // relativeLayout.setBackgroundColor(Color.parseColor("#DD5044"));
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));


            }
            else if(text.equals("Rain")){

                description.setText("????????????: "+"??????????????????");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else{
                description.setText("????????????: "+text);
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));
            }

        }


    }

    private void showInputDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("????????? ????????????????????????");

        final EditText cityInput= new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("e.g.: Comilla,BD");
        builder.setView(cityInput);
        builder.setPositiveButton("??????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CityPreference cityPreference=new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());

                String newCity=cityPreference.getCity();
                renderWeatherData(newCity);
            }
        });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_cityId) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}
