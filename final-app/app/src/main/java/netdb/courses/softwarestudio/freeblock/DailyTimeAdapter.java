package netdb.courses.softwarestudio.freeblock;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.domain.FriendList;
import netdb.courses.softwarestudio.domain.Schedule;

/**
 * Created by frank on 2015/1/18.
 */
public class DailyTimeAdapter extends BaseAdapter {
    private Context mContext;
    private List<FriendList> mFriendList;
    private List<Schedule> mSchedule;
    private int dayinfo;
    public DailyTimeAdapter(Context c, List<FriendList> mFriendList,List<Schedule> mSchedule, int dayinfo) {


        this.dayinfo = dayinfo;
        this.mFriendList = mFriendList;
        this.mSchedule = mSchedule;

        System.out.println(dayinfo);
        mContext = c;

        refreshTimes();
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
        dayView.setText(times[position]);

        dayView.setHeight(118);

        if(position%6 == 0 ){
           // v.setVisibility(View.VISIBLE);
            v.setBackgroundResource(R.drawable.item_background);
        }
        else{
            if (position%6 > mSchedule.size()){
                //v.setVisibility(View.INVISIBLE);
                v.setBackgroundResource(R.drawable.item_background);
            }
            else{
                if(position%6 == 1) {
                    //v.setBackgroundResource(R.drawable.user_red);

                    if ( mSchedule.get(position%6-1).getSchedule().get(dayinfo).get(position/6) == null){
                       // v.setVisibility(View.INVISIBLE);
                        v.setBackgroundResource(R.drawable.item_background);
                    }
                    else {
                       // v.setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.user_red);
                    }

                }
                else if(position%6 == 2) {
                    //v.setBackgroundResource(R.drawable.user_brown);

                    if ( mSchedule.get(position%6-1).getSchedule().get(dayinfo).get(position/6) == null){
                       // v.setVisibility(View.INVISIBLE);
                        v.setBackgroundResource(R.drawable.item_background);
                    }
                    else {
                       // v.setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.user_brown);
                    }
                }
                else if(position%6 == 3){
                   // v.setBackgroundResource(R.drawable.user_yellow);

                    if ( mSchedule.get(position%6-1).getSchedule().get(dayinfo).get(position/6) == null){
                      //  v.setVisibility(View.INVISIBLE);
                        v.setBackgroundResource(R.drawable.item_background);
                    }
                    else {
                       // v.setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.user_yellow);
                    }
                }
                else if(position%6 == 4) {
                   // v.setBackgroundResource(R.drawable.user_blue);

                    if ( mSchedule.get(position%6-1).getSchedule().get(dayinfo).get(position/6)== null){
                       // v.setVisibility(View.INVISIBLE);
                        v.setBackgroundResource(R.drawable.item_background);
                    }
                    else {
                      //  v.setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.user_blue);
                    }

                }
                else if(position%6 == 5) {
                  //  v.setBackgroundResource(R.drawable.user_green);

                    if ( mSchedule.get(position%6-1).getSchedule().get(dayinfo).get(position/6) == null){
                      //  v.setVisibility(View.INVISIBLE);
                        v.setBackgroundResource(R.drawable.item_background);
                    }
                    else {
                      //  v.setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.user_green);
                    }


                }
                else if (position%6 == 0 ){
                    v.setBackgroundResource(R.drawable.item_background);
                    v.setVisibility(View.VISIBLE);
                }

            }
            }





        return v;
    }

    public void refreshTimes()
    {
        times = new String[144];
        // populate times
        for ( int i = 0 ; i < times.length ; i++){
            if(i%6 == 0)
            times[i] = "\t\t"+ i/6 + ":00\n\t\t~\n\t\t"+""+((i/6)+1)+":00";
        }

    }



    // references to our items
    public String[] times;
}
