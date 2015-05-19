package netdb.courses.softwarestudio.freeblock;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import netdb.courses.softwarestudio.domain.UserDetail;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    private static final String USER_DETAIL = "myDetail";

    private UserDetail mDetail;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProfileFragment.
     */
    public static MyProfileFragment newInstance(UserDetail mDetail) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_DETAIL, mDetail);
        fragment.setArguments(args);
        return fragment;
    }

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDetail = getArguments().getParcelable(USER_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set main grid to be gone.
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.GONE);

        // set title
        getActivity().setTitle("My Profile");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        view.setBackgroundColor(Color.WHITE);

        TextView myID = (TextView) view.findViewById(R.id.my_detail_id);
        myID.setText(mDetail.getUserid());
        myID.setTextColor(getResources().getColor(R.color.myCharactersColor));

        TextView myName = (TextView) view.findViewById(R.id.my_detail_name);
        myName.setText(mDetail.getUsername());
        myName.setTextColor(getResources().getColor(R.color.myCharactersColor));

        TextView myPhone = (TextView) view.findViewById(R.id.my_detail_phone);
        myPhone.setText(mDetail.getPhonenumber());
        myPhone.setTextColor(getResources().getColor(R.color.myCharactersColor));
        view.setBackgroundColor(Color.WHITE);

        return view;
    }

    @Override
    public void onPause() {
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.VISIBLE);
        super.onPause();
    }


}
