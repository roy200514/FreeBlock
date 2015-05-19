package netdb.courses.softwarestudio.freeblock;


import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.domain.FriendList;
import netdb.courses.softwarestudio.domain.Schedule;


/**
 * A simple {@link Fragment} subclass.
 */


public class DailyViewFragment extends Fragment {
    private static final String FILSCHEDULE = "filschedule";
    private static final String WEEKDAY = "weekday";
    private static final String FRIEND_LIST = "friendList";
    private static final String SCHEDUEL = "schedule";
    private GridView DailyTimeScope;
    private DailyTimeAdapter dailyTimeAdapter;
    private List<Schedule> mSchedule;
    private List<FriendList> mFriendList;
    private List<Schedule> filschedule;
    private ArrayList<Schedule> tmpfils;
    private int dayinfo;
    private ArrayList<Schedule> scheduleList;
    private DailyViewDetailFragment dailyViewDetailFragment = new DailyViewDetailFragment();
    public DailyViewFragment() {
        // Required empty public constructor
    }

   /* public static DailyViewFragment newInstance(List<FriendList> friendList){
        DailyViewFragment fragment = new DailyViewFragment();
        Bundle args = new Bundle();
        ArrayList<FriendList> list = new ArrayList<FriendList>(friendList);
        args.putParcelableArrayList(FRIEND_LIST, list);
        fragment.setArguments(args);
        return fragment;}*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set has menu
        setHasOptionsMenu(true);

        if (getArguments() != null) {

            ArrayList<FriendList> list;

            int info;
            list = getArguments().getParcelableArrayList(FRIEND_LIST);
            scheduleList = getArguments().getParcelableArrayList(SCHEDUEL);
            info = getArguments().getInt(WEEKDAY);
           // tmpfils = getArguments().getParcelableArrayList(FILSCHEDULE);
            mFriendList = list;
            mSchedule = scheduleList;
            dayinfo = info;
           // filschedule = tmpfils;


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_daily_view, container, false);
        DailyTimeScope =(GridView)view.findViewById(R.id.daily_time_scope);

        dailyTimeAdapter = new DailyTimeAdapter(getActivity(),mFriendList,mSchedule, dayinfo);

        DailyTimeScope.setAdapter(dailyTimeAdapter);
        click();
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_day, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addEvent();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void addEvent() {
        Bundle eventBundle = new Bundle();
        ArrayList<Schedule> pass = new ArrayList<Schedule>(mSchedule);

        eventBundle.putParcelableArrayList("Schedule",pass);

        Intent intent = new Intent(getActivity(), SetEvent.class);
        intent.putExtras(eventBundle);
        startActivity(intent);
    }

    private void click (){

        DailyTimeScope.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction;
                ArrayList<Schedule>tmpSchedule = new ArrayList<Schedule>(mSchedule);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",position);
                bundle.putInt("DayInfo",dayinfo);

                bundle.putParcelableArrayList("filschedule",scheduleList);
                dailyViewDetailFragment.setArguments(bundle);
                transaction = getFragmentManager().beginTransaction();
                transaction
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.container,dailyViewDetailFragment).addToBackStack(null).commit();
            }
        });
    }
}
