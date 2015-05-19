package netdb.courses.softwarestudio.freeblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by frank on 2015/1/16.
 */
public class WeekdayAdapter extends BaseAdapter {
    Context mContext;
    public WeekdayAdapter (Context c) {

      mContext = c;
        refreshweek();
    }
    @Override
    public int getCount() {
        return weekday.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.week_item, null);
        }
        else {
            v = convertView;
        }
        TextView weekView = (TextView)v.findViewById(R.id.week);
        weekView.setText(weekday[position]);

        return v;
    }

    public void refreshweek(){
        weekday = new String[7];
        for (int i =  0; i < weekday.length; i++){
            if (i==0) weekday[i] ="\n\tMon";
            else if (i==1) weekday[i] ="\n\tTue";
            else if (i==2) weekday[i] ="\n\tWed";
            else if (i==3) weekday[i] ="\n\tThu";
            else if (i==4) weekday[i] ="\n\tFri";
            else if (i==5) weekday[i] ="\n\tSat";
            else if (i==6) weekday[i] ="\n\tSun";

        }
    }
    String[] weekday;


}
