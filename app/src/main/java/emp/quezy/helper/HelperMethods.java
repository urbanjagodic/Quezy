package emp.quezy.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import emp.quezy.other.ContentStore;

/**
 * Created by Urban on 3. 12. 2017.
 */

public class HelperMethods extends AppCompatActivity {


    public static void showToast(Activity myActivity, String message) {
        Toast.makeText(myActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void createDialog(Activity myActivity, String title, String message, String okButton, String cancelButton,
                                    final DialogReturnCommand myCommand) {

        AlertDialog.Builder myBuilder;
        myBuilder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                    new AlertDialog.Builder(myActivity, android.R.style.Theme_Material_Dialog_Alert) : new AlertDialog.Builder(myActivity);

        myBuilder.setTitle(title).setMessage(message)
                .setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myCommand.finishIt();
                    }
                })
                .setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // blank
                    }
                })
                .show();
    }

    public static void killApp(Activity myActivity) {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        myActivity.finish();
    }

    public static void playSound(Activity myActivity, int resource) {
        try {
            MediaPlayer myPlayer = MediaPlayer.create(myActivity, resource);
            myPlayer.start();
        } catch (Exception e) {
            Log.d("exception", e.getMessage() + "");
        }
    }

    public static boolean checkInternetConnection(Activity myActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager)  myActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String unescapeChars(String input) {

        if (input.contains("&amp;")) {
            input = input.replace("&amp;", "&");
        }
        if (input.contains("amp;")) {
            input = input.replace("amp;", "&");
        }
        if (input.contains("&quot;")) {
            input = input.replace("&quot;", "\"");
        }
        if (input.contains("quot;")) {
            input = input.replace("quot;", "\"");
        }
        if (input.contains("&#039;")) {
            input = input.replace("&#039;", "\'");
        }
        if (input.contains("#039;")) {
            input = input.replace("#039;", "\'");
        }
        if (input.contains("rsquo;")) {
            input = input.replace("rsquo;", "’");
        }
        if (input.contains("ouml;")) {
            input = input.replace("ouml;", "ö");
        }
        return input;
    }


    public static boolean turnOnSound(Activity myActivity) {
        ContentStore.initialize(myActivity);
        SharedPreferences myPrefs = ContentStore.getMyPrefrences();
        return myPrefs.getBoolean("soundControl", false);
    }

    public static int randomPosition(int num) {
        return new Random().nextInt(num);
    }
}
