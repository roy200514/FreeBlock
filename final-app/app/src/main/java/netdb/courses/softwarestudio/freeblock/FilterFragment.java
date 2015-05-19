package netdb.courses.softwarestudio.freeblock;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import netdb.courses.softwarestudio.domain.Information;
import netdb.courses.softwarestudio.domain.Schedule;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    OnFilterScheduleUpdate mCallback;

    private ArrayList<Schedule> completeSchedule;
    private ArrayList<Schedule> fixSchedule = new ArrayList<Schedule>();

    private TextView filterTitle;
    private ListView friendList;

    private Boolean[] check = new Boolean[5];


    public FilterFragment() {
        // Required empty public constructor
    }

    public interface OnFilterScheduleUpdate {
        public void onFilterUpdate(ArrayList<Schedule> schedules);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // make sure the container activity has implemented the callback
        try {
            mCallback = (OnFilterScheduleUpdate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnFilterScheduleUpdate");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set has menu
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            //mUserDetail = getArguments().getParcelable(USER_DETAIL);
            ArrayList<Schedule> list;
            list = getArguments().getParcelableArrayList("mSchedule");
            Log.d("list: ", list.get(0).getSchedule().toString());
            completeSchedule = list;
        }


        for (int i=0; i<5; i++) {
            fixSchedule.add(null);
            check[i] = false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_filter, container, false);

        // set title
        getActivity().setTitle("Filter");

        // set main grid to be gone.
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.GONE);



        filterTitle = (TextView) fragmentView.findViewById(R.id.fragment_filter_title);

        friendList = (ListView) fragmentView.findViewById(R.id.fragment_friend_schedule);
        ArrayList<Schedule> arrayOfSchedule = new ArrayList<Schedule>(completeSchedule);
        if (arrayOfSchedule.isEmpty()) {
            arrayOfSchedule.add(new Schedule("YOU HAVE NO FRIEND", null));
        }
        friendList.setAdapter(new FilterListAdapter(getActivity(), arrayOfSchedule));

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectSchedule(completeSchedule.get(position), view);
            }
        });

        return fragmentView;
    }

    @Override
    public void onPause() {
        // set main grid to be visible.
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.VISIBLE);

        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_done:
                doneSelectSchedule();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void doneSelectSchedule() {
        mCallback.onFilterUpdate(fixSchedule);
    }

    private void selectSchedule(Schedule schedule, View view) {
        if (fixSchedule.contains(schedule)) {
            Log.d("FIX CONTAIN:", fixSchedule.get(fixSchedule.indexOf(schedule)).getSchedule().toString());
            fixSchedule.remove(schedule);
            view.setBackgroundColor(Color.WHITE);
        } else {
            fixSchedule.add(schedule);
            view.setBackgroundColor(getResources().getColor(R.color.myPrimaryDarkColor));
        }
    }
}
