package emp.quezy.other;

import android.content.Context;
import android.view.View;

/**
 * Created by Urban on 3. 12. 2017.
 */

public interface AnimationInterface {

    void fadeIn(View v, Context context, int resource);
    void fadeOut(View v, Context context, int resource);
}
