package netdb.courses.softwarestudio.freeblock;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.domain.Schedule;


/**
 * A simple {@link Fragment} subclass.
 */

public class DailyViewDetailFragment extends Fragment {
    private static final String POSITION = "Position";
    private static final String DAYINFO = "DayInfo";
    private static final String FILSCHEDULE = "filschedule";

    private DailyViewDetailAdapter dailyViewDetailAdapter;
    private GridView EventDetail;
    private List<Schedule> schedules;
    private int tmp;
    private int day_tmp;
    public DailyViewDetailFragment() {
        // Required empty public constructor
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            tmp = getArguments().getInt(POSITION);
            day_tmp = getArguments().getInt(DAYINFO);
            schedules = getArguments().getParcelableArrayList(FILSCHEDULE);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        dailyViewDetailAdapter = new DailyViewDetailAdapter(getActivity(),schedules,tmp/6, day_tmp);
        View view = inflater.inflate(R.layout.fragment_daily_view_detail, container, false);



        EventDetail = (GridView)view.findViewById(R.id.EventDetail);
        EventDetail.setAdapter(dailyViewDetailAdapter);


        // Inflate the layout for this fragment
       return view;
    }


}
