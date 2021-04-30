package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Anon on 12/15/2016.
 */

public class CityPreference {

    SharedPreferences prefs;
    public CityPreference(Activity activity){
        prefs=activity.getPreferences(Activity.MODE_PRIVATE);

    }

    public String getCity(){

        return  prefs.getString("city","Comilla,BD");
    }

    public void setCity(String city){

        prefs.edit().putString("city",city).commit();
    }
}

