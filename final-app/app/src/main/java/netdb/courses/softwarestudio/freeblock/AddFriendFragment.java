package netdb.courses.softwarestudio.freeblock;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import netdb.courses.softwarestudio.domain.FriendList;
import netdb.courses.softwarestudio.domain.SaveSession;
import netdb.courses.softwarestudio.rest.RestManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendFragment extends Fragment {

    private EditText search;
    private Button searchBtn;

    private RestManager restMgr;

    public AddFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_add_friend, container, false);

        restMgr = RestManager.getInstance(getActivity());
        getActivity().setTitle("Add Friend");

        search = (EditText) fragmentView.findViewById(R.id.search_id);

        searchBtn = (Button) fragmentView.findViewById(R.id.start_search_btn);
        searchBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Header
                Map<String, String> header = new HashMap<>();
                header.put("Content-type", "application/json");
                header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));
                FriendList friend = new FriendList(search.getText().toString(), 2);
                restMgr.postResource(FriendList.class, friend, header, new RestManager.PostResourceListener() {
                    @Override
                    public void onResponse(int code, Map<String, String> headers) {
                        if (code == 201) {
                            Fragment fragment = getFragmentManager().findFragmentByTag("SEARCH");
                            final FragmentTransaction fT = getFragmentManager().beginTransaction();
                            fT.detach(fragment);
                            getActivity().setTitle(Title());
                            fT.commit();
                        }
                    }

                    @Override
                    public void onRedirect(int code, Map<String, String> headers, String url) {

                    }

                    @Override
                    public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                        Toast.makeText(getActivity(), "No Such User", Toast.LENGTH_SHORT).show();
                    }
                }, null);
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

    public String Title() {
        String WeekTitle;
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        Calendar c = Calendar.getInstance();
        String month_name = month.format(c.getTime());
        int date = c.get(Calendar.DATE);
        int dayofWeek = c.get(Calendar.DAY_OF_WEEK);
        int endday = date - dayofWeek + 7;
        WeekTitle = endday - 6 + "~" + endday + " " + month_name;
        return WeekTitle;
    }


}
