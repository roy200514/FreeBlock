package netdb.courses.softwarestudio.freeblock;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import netdb.courses.softwarestudio.domain.Schedule;

/**
 * Created by frank on 2015/1/21.
 */
public class DailyViewDetailAdapter extends BaseAdapter {

    private List<Schedule> Schedule;
    private Context mContext;
    private int time;
    private int day;
    private String[] EventString;


    public DailyViewDetailAdapter(Context c,List<Schedule> schedule,int time, int day){
        mContext = c;
        EventString = new String[5];
        Schedule = schedule;
        this.time = time;
        this.day = day;

        EventDetail();
    }


    @Override
    public int getCount() {
        return EventString.length;
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
        TextView EventView;

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.event_detail_item, null);
        }
        else{
            v= convertView;
        }

        EventView = (TextView)v.findViewById(R.id.detail_item);
        EventView.setText(EventString[position]);
        EventView.setHeight(200);
        EventView.setTextSize(30);

        return v;
    }



    public void EventDetail(){
            String Event;
            String Person;
        //System.out.println(Schedule.get(0).getSchedule().get(1));
        //System.out.println(Schedule.get(0).getSchedule().get(1).get(0));


            for ( int i = 0 ; i < Schedule.size(); i ++){
               Event =  Schedule.get(i).getSchedule().get(day).get(time);
                //System.out.println("i = "+i +" day = "+ day+" time = "+time);
                //System.out.println("Event = "+Event);
               if (Event == null) {
                   Event = "Free!!!";
               }

               Person = Schedule.get(i).getUsername();
               EventString[i] = ""+Person+" : "+ Event;

            }
    }


}
