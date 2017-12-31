package emp.quezy.other;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Urban on 3. 12. 2017.
 */

public class MyAnimation implements AnimationInterface {

    @Override
    public void randomAnim(View v, Context context, int resource) {
        Animation fadeInAnim = AnimationUtils.loadAnimation(context, resource);
        v.startAnimation(fadeInAnim);

    }

}
