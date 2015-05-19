package netdb.courses.softwarestudio.freeblock;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import netdb.courses.softwarestudio.domain.Schedule;
import netdb.courses.softwarestudio.freeblock.R;

public class PickerAdapter extends BaseAdapter {

    private Context mContext;
    private int day;
    private List<Schedule> previousSchedule;
    private ArrayList<Integer> timesData;

    public PickerAdapter(Context c ,List<Schedule> preSchedule) {
        this.timesData = new ArrayList<Integer>(84);
        this.previousSchedule = preSchedule;
        setTimesData(this.timesData);
        mContext = c;

        refreshTimes();
    }

    public void setTimesData(ArrayList<Integer> timesData) {

        for(int i = 0;i != timesData.size();i++){
                timesData.set(i, ((i%7+1)*10000) + (i/7*100));
            }
        this.timesData = timesData;
        }


    public ArrayList<Integer>getTimesData(){
        return this.timesData;
    }

    public int getCount() {
        return times.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        TextView dayView;


        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.time_item, null);
        }
        else{

            v= convertView;
        }
        dayView = (TextView)v.findViewById(R.id.time);

        if(previousSchedule.get(0).getSchedule().get(day).get(position)==null)
        {
            dayView.setText(times[position]);
        }
        else {

            v.setBackgroundResource(R.drawable.time_picked);
           v.setClickable(true);
        }


        return v;
    }

    public void refreshTimes()
    {

        // clear Data
        timesData.clear();

        times = new String[24];
        // populate times
        for ( int i = 0 ; i < times.length ; i++){
            times[i] = "\t"+ i + ":00\n\t~\n\t"+""+(i+1)+":00";
        }

    }


    public void setDay (int day){
        this.day = day;
    }
    // references to our items
    public String[] times;
}