package emp.quezy.helper;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Urban on 3. 12. 2017.
 */

public class HelperMethods {


    public static void showToast(Activity myActivity, String message) {
        Toast.makeText(myActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}
