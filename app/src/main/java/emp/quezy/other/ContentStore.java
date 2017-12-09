package emp.quezy.other;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Urban on 8. 12. 2017.
 */

public class ContentStore {

    public static String prefsName = "myprefrences";
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor myEditor;


    public static void initialize(Activity myActivity) {

        myPrefs = myActivity.getApplicationContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        myEditor = myPrefs.edit();

    }

    public static SharedPreferences getMyPrefrences() {
        return myPrefs;
    }

    public static void storeData(Object value, String key) {

        if (value instanceof Integer) {
            myEditor.putInt(key, (int) value);
        } else if (value instanceof String) {
            myEditor.putString(key, value + "");
        } else if (value instanceof Boolean) {
            myEditor.putBoolean(key, (boolean)value);
        }
        myEditor.commit();
    }


    public static void removeValue(String key) {
        myEditor.remove(key);
        myEditor.commit();
    }

    public static void clearData() {
        myEditor.clear();
        myEditor.commit();
    }
}
