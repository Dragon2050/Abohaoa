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

                sunrise.setText("সূর্যোদয়: "+"ভোর "+sunriseDateW+" মিনিট");
                sunset.setText("সূর্যাস্ত: "+"সন্ধ্যা "+sunsetDateW+" মিনিট");
                updated.setText("শেষ পরিবর্তন: "+updateDateW);

                //conversion of temperature begins here;

                DecimalFormat decimalFormat=new DecimalFormat("#.#");
                String tempString = decimalFormat.format(weather.currentCondition.getTemperature());
                String tempStringW= convertNumber(tempString);
                temp.setText(""+tempStringW+"°সে");
                //temp.setText(""+"-৬.২"+"°সে");

                //Conversion of humidity  Starts here
                String humidityString=Float.toString(weather.currentCondition.getHumidity());
                String humidityStringW=convertNumber(humidityString);
                humidity.setText("শৈত্য: "+humidityStringW+"%");

                //Conversion of pressure starts here
                String pressureString=Float.toString(weather.currentCondition.getPressure());
                String pressureStringW= convertNumber(pressureString);
                pressure.setText("চাপ: "+pressureStringW+" প্যাসকেল");

                //Conversion of wind starts here
                String windString=Float.toString(weather.wind.getSpeed());
                String windStringW= convertNumber(windString);
                wind.setText("বাতাস: "+windStringW+"মি/সেঃ");


                convertCondition(weather.currentCondition.getCondition());

                System.out.println(weather.currentCondition.getCondition());

            }catch (NullPointerException e){

                Toast.makeText(getApplicationContext(),"শহর পাওয়া যায় নি", Toast.LENGTH_LONG).show();
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
                    temporary[i]='০';
                if(text.charAt(i)=='1')
                    temporary[i]='১';
                if(text.charAt(i)=='2')
                    temporary[i]='২';
                if(text.charAt(i)=='3')
                    temporary[i]='৩';
                if(text.charAt(i)=='4')
                    temporary[i]='৪';
                if(text.charAt(i)=='5')
                    temporary[i]='৫';
                if(text.charAt(i)=='6')
                    temporary[i]='৬';
                if(text.charAt(i)=='7')
                    temporary[i]='৭';
                if(text.charAt(i)=='8')
                    temporary[i]='৮';
                if(text.charAt(i)=='9')
                    temporary[i]='৯';
            }
            newString=String.valueOf(temporary);

            return  newString;

        }

        public String convertCity(String text){

            String convertCountry="বাংলাদেশ";
            String convertCity=null;

            if(text.equals("BD")){

                return convertCountry;

            }
            else if(text.equals("Comilla")){
                convertCity="কুমিল্লা";
                return convertCity;
            }
            else if(text.equals("Barguna")){
                convertCity="বরগুনা";
                return convertCity;
            }
            else if(text.equals("Barisal")){
                convertCity="বরিশাল";
                return convertCity;
            }
            else if(text.equals("Bhola")){
                convertCity="ভোলা";
                return convertCity;
            }
            else if(text.equals("Jhalokati")){
                convertCity="ঝালকাঠি";
                return convertCity;
            }
            else if(text.equals("Patuakhali")){
                convertCity="পটুয়াখালী";
                return convertCity;
            }
            else if(text.equals("Pirojpur")){
                convertCity="পিরোজপুর";
                return convertCity;
            }
            else if(text.equals("Bandarban")){
                convertCity="বান্দরবান";
                return convertCity;
            }
            else if(text.equals("Brahmanbaria")){
                convertCity="ব্রাহ্মণবাড়িয়া";
                return convertCity;
            }
            else if(text.equals("Chandpur")){
                convertCity="চাঁদপুর";
                return convertCity;
            }
            else if(text.equals("Coxs Bazar")){
                convertCity="কক্সবাজার";
                return convertCity;
            }
            else if(text.equals("Chittagong")){
                convertCity="চট্টগ্রাম";
                return convertCity;
            }
            else if(text.equals("Feni")){
                convertCity="ফেনী";
                return convertCity;
            }
            else if(text.equals("Khagrachhari")){
                convertCity="খাগড়াছড়ি";
                return convertCity;
            }
            else if(text.equals("Lakshmipur")){
                convertCity="লক্ষ্মীপুর";
                return convertCity;
            }
            else if(text.equals("Noakhali")){
                convertCity="নোয়াখালী";
                return convertCity;
            }
            else if(text.equals("Rangamati")){
                convertCity="রাঙ্গামাটি";
                return convertCity;
            }
            else if(text.equals("Dhaka")){
                convertCity="ঢাকা";
                return convertCity;
            }
            else if(text.equals("Faridpur")){
                convertCity="ফরিদপুর";
                return convertCity;
            }
            else if(text.equals("Gazipur")){
                convertCity="গাজীপুর";
                return convertCity;
            }
            else if(text.equals("Gopalganj")){
                convertCity="গোপালগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Kishoreganj")){
                convertCity="কিশোরগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Madaripur")){
                convertCity="মাদারীপুর";
                return convertCity;
            }
            else if(text.equals("Manikganj")){
                convertCity="মানিকগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Munshiganj")){
                convertCity="মুন্সীগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Narayanganj")){
                convertCity="নারায়ণগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Narsingdi")){
                convertCity="নরসিংদী";
                return convertCity;
            }
            else if(text.equals("Rajbari")){
                convertCity="রাজবাড়ী";
                return convertCity;
            }
            else if(text.equals("Shariatpur")){
                convertCity="শরীয়তপুর";
                return convertCity;
            }
            else if(text.equals("Tangail")){
                convertCity="টাঙ্গাইল";
                return convertCity;
            }
            else if(text.equals("Bagerhat")){
                convertCity="বাগেরহাট";
                return convertCity;
            }
            else if(text.equals("Chuadanga")){
                convertCity="চুয়াডাঙ্গা";
                return convertCity;
            }
            else if(text.equals("Jessore")){
                convertCity="যশোর";
                return convertCity;
            }
            else if(text.equals("Jhenaidah")){
                convertCity="ঝিনাইদহ";
                return convertCity;
            }
            else if(text.equals("Khulna")){
                convertCity="খুলনা";
                return convertCity;
            }
            else if(text.equals("Kushtia")){
                convertCity="কুষ্টিয়া";
                return convertCity;
            }
            else if(text.equals("Magura")){
                convertCity="মাগুরা";
                return convertCity;
            }
            else if(text.equals("Meherpur")){
                convertCity="মেহেরপুর";
                return convertCity;
            }
            else if(text.equals("Narail")){
                convertCity="নড়াইল";
                return convertCity;
            }
            else if(text.equals("Satkhira")){
                convertCity="সাতক্ষিরা";
                return convertCity;
            }
            else if(text.equals("Jamalpur")){
                convertCity="জামালপুর";
                return convertCity;
            }
            else if(text.equals("Mymensingh")){
                convertCity="ময়মনসিংহ";
                return convertCity;
            }
            else if(text.equals("Netrokona")){
                convertCity="নেত্রকোনা";
                return convertCity;
            }
            else if(text.equals("Sherpur")){
                convertCity="শেরপুর";
                return convertCity;
            }
            else if(text.equals("Bogra")){
                convertCity="বগুড়া";
                return convertCity;
            }
            else if(text.equals("Joypurhat")){
                convertCity="জয়পুরহাট";
                return convertCity;
            }
            else if(text.equals("Naogaon")){
                convertCity="নওগাঁ";
                return convertCity;
            }
            else if(text.equals("Natore")){
                convertCity="নাটোর";
                return convertCity;
            }
            else if(text.equals("Chapai Nawabganj")){
                convertCity="নওয়াবগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Rajshahi")){
                convertCity="রাজশাহী";
                return convertCity;
            }
            else if(text.equals("Sirajganj")){
                convertCity="সিরাজগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Dinajpur")){
                convertCity="দিনাজপুর";
                return convertCity;
            }
            else if(text.equals("Gaibandha")){
                convertCity="গাইবান্ধা";
                return convertCity;
            }
            else if(text.equals("Kurigram")){
                convertCity="কুড়িগ্রাম";
                return convertCity;
            }
            else if(text.equals("Lalmonirhat")){
                convertCity="লালমনিরহাট";
                return convertCity;
            }
            else if(text.equals("Nilphamari")){
                convertCity="নীলফামারী";
                return convertCity;
            }
            else if(text.equals("Panchagarh")){
                convertCity="পঞ্চগড়";
                return convertCity;
            }
            else if(text.equals("Rangpur")){
                convertCity="রংপুর";
                return convertCity;
            }
            else if(text.equals("Thakurgaon")){
                convertCity="ঠাকুরগাঁও";
                return convertCity;
            }
            else if(text.equals("Habiganj")){
                convertCity="হবিগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Moulvibazar")){
                convertCity="মৌলভীবাজার";
                return convertCity;
            }
            else if(text.equals("Sunamganj")){
                convertCity="সুনামগঞ্জ";
                return convertCity;
            }
            else if(text.equals("Sylhet")){
                convertCity="সিলেট";
                return convertCity;
            }
            else if(text.equals("Pabna")){
                convertCity="পাবনা";
                return convertCity;
            }
            else
                return text;


        }

        public void convertCondition(String text){
            if(text.equals("Clear")){

                description.setText("আকাশ: "+"পরিষ্কার");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else if(text.equals("Haze")){

                description.setText("আকাশ: "+"আবছায়া");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else if(text.equals("Cloudy")||text.equals("Clouds")){

                description.setText("আকাশ: "+"মেঘলা");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else if(text.equals("Mist")){

                description.setText("আকাশ: "+"কুয়াশাচ্ছন্ন");
                // relativeLayout.setBackgroundColor(Color.parseColor("#rrggbb"));
                // relativeLayout.setBackgroundColor(Color.parseColor("#DD5044"));
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));


            }
            else if(text.equals("Rain")){

                description.setText("আকাশ: "+"বৃষ্টি");
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));

            }
            else{
                description.setText("আকাশ: "+text);
                relativeLayout.setBackgroundDrawable( getResources().getDrawable(R.drawable.demo3));
            }

        }


    }

    private void showInputDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("শহর পরিবর্তন");

        final EditText cityInput= new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("e.g.: Comilla,BD");
        builder.setView(cityInput);
        builder.setPositiveButton("সাবমিট", new DialogInterface.OnClickListener() {
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
