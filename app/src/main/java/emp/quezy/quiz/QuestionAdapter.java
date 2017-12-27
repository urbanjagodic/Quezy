package emp.quezy.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import emp.quezy.R;


public class QuestionAdapter extends ArrayAdapter<String> {

    private static class ViewHolder {
        TextView QuestionText;
    }

    public QuestionAdapter(Context context, ArrayList<String> questions) {
        super(context, 0, questions);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String value = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
            viewHolder.QuestionText = convertView.findViewById(R.id.QuestionText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.QuestionText.setText(value);

        return convertView;
    }
}
