package netdb.courses.softwarestudio.freeblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Mark on 1/18/2015.
 */
public class WeekHeaderAdapter extends BaseAdapter {
    private Context context;
    private String[] Mark = new String[175];

    public WeekHeaderAdapter(Context context) {
        this.context = context;
        HandleString();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View mView;

        if (convertView == null) {
            //mView = new View(context);
            mView = inflater.inflate(R.layout.week_view_item, null);

        } else {
            mView = convertView;
        }

        TextView mWeekHeader = (TextView) mView.findViewById(R.id.week_header);
        mWeekHeader.setText(Mark[position]);
        return mView;
    }

    @Override
    public int getCount() {
        return Mark.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void HandleString() {
        Mark[0] = "Sun";
        Mark[1] = "Mon";
        Mark[2] = "Tue";
        Mark[3] = "Wed";
        Mark[4] = "Thu";
        Mark[5] = "Fri";
        Mark[6] = "Sat";
    }
}
