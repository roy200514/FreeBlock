package netdb.courses.softwarestudio.freeblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import netdb.courses.softwarestudio.domain.Schedule;

/**
 * Created by Mark on 1/18/2015.
 */
public class MainViewAdapter extends BaseAdapter {
    private Context mContext;
    private String[] Mark = new String[200];
    private List<Schedule> Schedule;
    private int count = 0 ;

    public MainViewAdapter(Context context, List<Schedule> schedule) {

        this.Schedule = schedule;
        this.mContext = context;

        // Handle days
        for (int i=0; i<Mark.length; i++) {
            if (i%8 == 0 && i != 0) {
                Mark[i] = "" + (i/8-1) + ":00\n\t~\n" + "" + i/8 + ":00";
            }
        }

        // Handle Headers
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int startDay = date - dayOfWeek + 1;
        Mark[1] = "Sun\n" + (startDay);
        Mark[2] = "Mon\n" + (startDay+1);
        Mark[3] = "Tue\n" + (startDay+2);
        Mark[4] = "Wed\n" + (startDay+3);
        Mark[5] = "Thu\n" + (startDay+4);
        Mark[6] = "Fri\n" + (startDay+5);
        Mark[7] = "Sat\n" + (startDay+6);

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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mView;
        TextView mTextView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            mView = inflater.inflate(R.layout.week_day_item, null);
        } else {
            mView = convertView;
        }



        mTextView = (TextView) mView.findViewById(R.id.grid_weekday_item);
        mTextView.setText(Mark[position]);
        // to show the day and the time
        if(position/8 == 0 || position%8 == 0){

            mView.setBackgroundResource(R.drawable.item_background);
        }
        else{

            for (int j = 0 ; j < Schedule.size() ; j++){

                if (Schedule.get(j).getSchedule().get(position%8-1).get(position/8-1) == null){

                }
                else {
                    count ++;
                }
            }

            if(count == 0 )mView.setBackgroundResource(R.drawable.item_background);
            else if(count == 1) mView.setBackgroundResource(R.drawable.mainview_item_1);
            else if (count == 2)mView.setBackgroundResource(R.drawable.mainview_item_2);
            else if (count == 3)mView.setBackgroundResource(R.drawable.mainview_item_3);
            else if (count == 4)mView.setBackgroundResource(R.drawable.mainview_item_4);
            else if (count == 5)mView.setBackgroundResource(R.drawable.mainview_item_5);
        }
            count = 0 ;

        return mView;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.Schedule = schedule;
    }
}
