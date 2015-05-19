package netdb.courses.softwarestudio.freeblock;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.domain.FriendList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendProfileFragment extends Fragment {

    private static final String FRIEND_DETAIL = "friendDetail";

    private FriendList mFriend;

    public static FriendProfileFragment newInstance(FriendList friend) {
        FriendProfileFragment fragment = new FriendProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(FRIEND_DETAIL, friend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFriend = getArguments().getParcelable(FRIEND_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);

        TextView friendID = (TextView) view.findViewById(R.id.friend_detail_id);
        friendID.setText(mFriend.getUserid());

        TextView friendName = (TextView) view.findViewById(R.id.friend_detail_name);
        friendName.setText(mFriend.getUsername());

        TextView friendPhone = (TextView) view.findViewById(R.id.friend_detail_phone);
        friendPhone.setText(mFriend.getPhonenumber());
        view.setBackgroundColor(Color.WHITE);

        return view;
    }


}
