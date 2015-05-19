package netdb.courses.softwarestudio.freeblock;

import android.app.Activity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netdb.courses.softwarestudio.domain.FriendList;
import netdb.courses.softwarestudio.domain.Information;
import netdb.courses.softwarestudio.domain.SaveSession;
import netdb.courses.softwarestudio.domain.UserDetail;
import netdb.courses.softwarestudio.rest.RestManager;


public class FriendFragment extends Fragment {
    OnDeleteFriendUpdate mCallback;

    // the fragment initialization parameters.
    private static final String FRIEND_LIST = "friendList";
    private static final String USER_DETAIL = "userDetail";

    private List<FriendList> mFriendList;
    private UserDetail mUserDetail;

    private TextView friendRequestTitle;
    private ListView friendRequestView;
    private TextView friendListTitle;
    private ListView friendListView;

    private RestManager restMgr;

    private List<String> toDeleteFriends = new ArrayList<String>();

    private List<Information> toHandleRequests = new ArrayList<Information>();

    private Boolean friend_menu;
    private Boolean request_menu;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param friendList Parameter 1.
     * @return A new instance of fragment FriendFragment.
     */
    public static FriendFragment newInstance(List<FriendList> friendList, UserDetail userDetail) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_DETAIL, userDetail);
        ArrayList<FriendList> list = new ArrayList<FriendList>(friendList);
        args.putParcelableArrayList(FRIEND_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendFragment() {
        // Required empty public constructor
    }

    public interface OnDeleteFriendUpdate {
        public void onDelete(ArrayList<FriendList> friendList, UserDetail userDetail);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // make sure the container activity has implemented the callback
        try {
            friend_menu = false;
            request_menu = false;
            mCallback = (OnDeleteFriendUpdate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnDeleteFriendUpdate");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set has menu
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            //mUserDetail = getArguments().getParcelable(USER_DETAIL);
            ArrayList<FriendList> list;
            list = getArguments().getParcelableArrayList(FRIEND_LIST);
            mFriendList = list;
            mUserDetail = getArguments().getParcelable(USER_DETAIL);
        }

        restMgr = RestManager.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_friend, container, false);

        // set main grid to be gone.
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.GONE);

        // set title
        getActivity().setTitle("Friend List");

        // set request title
        friendRequestTitle = (TextView) fragmentView.findViewById(R.id.fragment_friend_request_title);

        // set friendRequest
        friendRequestView = (ListView) fragmentView.findViewById(R.id.fragment_friend_request);
        ArrayList<Information> arrayOfRequest = new ArrayList<Information>(mUserDetail.getFriendrequest());
        if (arrayOfRequest.isEmpty()) {
            // if request is empty, set up "NO REQUEST"
            Information tmp = new Information("YOU HAVE NO REQUEST", "");
            arrayOfRequest.add(tmp);
            friendRequestView.setAdapter(new FriendRequestAdapter(getActivity(), arrayOfRequest));
        }

        friendRequestView.setAdapter(new FriendRequestAdapter(getActivity(), arrayOfRequest));
        friendRequestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mUserDetail.getFriendrequest().isEmpty()) {
                    Toast.makeText(getActivity(), "What do you want to do?", Toast.LENGTH_SHORT).show();
                } else {
                    handleRequest(mUserDetail.getFriendrequest().get(position), view);
                }
            }
        });

        // set Friend List Title
        friendListTitle = (TextView) fragmentView.findViewById(R.id.fragment_friend_list_title);


        // set up friend list and Call Adapter, set onclick listener
        ArrayList<FriendList> arrayOfFriend = new ArrayList<FriendList>(mFriendList);
        if (arrayOfFriend.isEmpty()) {
            arrayOfFriend.add(new FriendList("YOU HAVE NO FRIEND", 0));
        }
        Log.d("arrayOfFriend[0] user ID : ", arrayOfFriend.get(0).getUserid() );
        friendListView = (ListView) fragmentView.findViewById(R.id.fragment_friend_list);
        friendListView.setAdapter(new FriendListAdapter(getActivity(), arrayOfFriend));

        // check friend detail
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mFriendList.isEmpty()) {
                    Toast.makeText(getActivity(), "What do you want to do?", Toast.LENGTH_SHORT).show();
                } else {
                    Fragment friend_detail = new FriendProfileFragment();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelable("friendDetail", mFriendList.get(position));
                    Log.d("Friend detail from bundle empt : ", "" + bundleFriend.getParcelable("friendDetail").toString());
                    friend_detail.setArguments(bundleFriend);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, friend_detail).addToBackStack(null).commit();
                }
            }
        });

        //final List<String> toDeleteFriends = new ArrayList<String>(deletedUserID);

        // long press to delete
        friendListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mFriendList.isEmpty()) {
                    Toast.makeText(getActivity(), "What do you want to do?", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    setupDeletedString(mFriendList.get(position), view);
                    return true;
                }
            }
        });

        // Inflate the layout for this fragment
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
        inflater.inflate(R.menu.menu_friend_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_friend:
                Log.d("add friend is pressed : ", "true");
                addFriendPhase1();
                return true;
            case R.id.action_delete_friend:
                Log.d("delete is pressed : ", "true");
                deleteFriendPhase1();
                return true;
            case R.id.action_add_request:
                Log.d("add request is pressed : ", "true");
                acceptRequestPhase1();
                return true;
            case R.id.action_delete_request:
                Log.d("delete request is pressed : ", "true");
                deleteRequestPhase1();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!friend_menu && !request_menu) {
            // delete friend not selected
            menu.findItem(R.id.action_delete_friend).setVisible(false);
            menu.findItem(R.id.action_add_friend).setVisible(true);
            menu.findItem(R.id.action_add_request).setVisible(false);
            menu.findItem(R.id.action_delete_request).setVisible(false);
        } else if (friend_menu && !request_menu) {
            // can delete friend only display one trash can
            menu.findItem(R.id.action_delete_friend).setVisible(true);
            menu.findItem(R.id.action_add_friend).setVisible(false);
            menu.findItem(R.id.action_add_request).setVisible(false);
            menu.findItem(R.id.action_delete_request).setVisible(false);
        } else if (request_menu && !friend_menu) {
            menu.findItem(R.id.action_delete_request).setVisible(true);
            menu.findItem(R.id.action_add_request).setVisible(true);
            menu.findItem(R.id.action_add_friend).setVisible(false);
            menu.findItem(R.id.action_delete_friend).setVisible(false);
        } else {
            menu.findItem(R.id.action_delete_friend).setVisible(false);
            menu.findItem(R.id.action_add_friend).setVisible(false);
            menu.findItem(R.id.action_add_request).setVisible(false);
            menu.findItem(R.id.action_delete_request).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    private void handleRequest(Information info, View view) {

        if (toHandleRequests.contains(info)) {
            toHandleRequests.remove(info);
            view.setBackgroundColor(Color.WHITE);
        } else {
            toHandleRequests.add(info);
            view.setBackgroundColor(getResources().getColor(R.color.mainColor1));
        }

        request_menu = !toHandleRequests.isEmpty();

        getActivity().invalidateOptionsMenu();

    }

    private void setupDeletedString(FriendList friendList, View view) {

        // set up deletedString
        if ( toDeleteFriends.contains(friendList.getUserid())) {
            toDeleteFriends.remove(friendList.getUserid());
            view.setBackgroundColor(Color.WHITE);
        } else {
            toDeleteFriends.add(friendList.getUserid());
            view.setBackgroundColor(getResources().getColor(R.color.myPrimaryDarkColor));
        }

        friend_menu = !toDeleteFriends.isEmpty();

        getActivity().invalidateOptionsMenu();

    }

    private void addFriendPhase1() {
        AddFriendFragment fragment = new AddFriendFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, fragment, "SEARCH").addToBackStack("SEARCH")
                .commit();

    }

    private void acceptRequestPhase1() {
        // set header
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));

        acceptRequestPhase2(header, toHandleRequests, toHandleRequests.size());
    }
    private void acceptRequestPhase2(final Map<String, String> header, final List<Information> tmp, final int tmp_size) {
        final int tmp_dfs = tmp_size - 1;

        FriendList toAdd = new FriendList(tmp.get(tmp_dfs).getUserid(), 1);
        Log.d("RequestID toAdd:", toAdd.getUserid());

        restMgr.postResource(FriendList.class, toAdd, header, new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                Log.d("user deleted :", "true");
                if (tmp_dfs > 0) {
                    acceptRequestPhase2(header, tmp, tmp_dfs);
                } else {
                    acceptRequestPhase3();
                }
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

            }
        }, null);

    }

    private void acceptRequestPhase3() {
        // set Header
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));

        restMgr.listResource(FriendList.class, null, header, new RestManager.ListResourceListener<FriendList>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<FriendList> resources) {
                if (code == 200) {
                    // set Header
                    Map<String, String> header = new HashMap<>();
                    header.put("Accept", "application/json");
                    header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));
                    acceptRequestPhase4(resources, header);
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(getActivity(), "Fetching FriendList failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    private void acceptRequestPhase4(final List<FriendList> list, Map<String, String> header) {

        restMgr.getResource(UserDetail.class, null, null, header, new RestManager.GetResourceListener<UserDetail>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, UserDetail resource) {
                ArrayList<FriendList> tmpList = new ArrayList<FriendList>(list);
                mCallback.onDelete(tmpList, resource);
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

            }
        }, null);
    }







    private void deleteRequestPhase1() {

        // set header
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));

        Log.d("RequestID:", toHandleRequests.get(0).getUserid());
        deleteRequestPhase2(header, toHandleRequests, toHandleRequests.size());


    }
    private void deleteRequestPhase2(final Map<String, String> header, final List<Information> tmp, final int tmp_size) {
        final int tmp_dfs = tmp_size - 1;

        FriendList toDelete = new FriendList(tmp.get(tmp_dfs).getUserid(), 0);
        Log.d("RequestID toDelete:", toDelete.getUserid());

        restMgr.postResource(FriendList.class, toDelete, header, new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                Log.d("user deleted :", "true");
                if (tmp_dfs > 0) {
                    deleteRequestPhase2(header, tmp, tmp_dfs);
                } else {
                    deleteRequestPhase3();
                }
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

            }
        }, null);

    }
    private void deleteRequestPhase3() {
        // set Header
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));

        restMgr.listResource(FriendList.class, null, header, new RestManager.ListResourceListener<FriendList>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<FriendList> resources) {
                if (code == 200) {
                    // set Header
                    Map<String, String> header = new HashMap<>();
                    header.put("Accept", "application/json");
                    header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));
                    deleteRequestPhase4(resources, header);
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(getActivity(), "Fetching FriendList failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }
    private void deleteRequestPhase4(final List<FriendList> list, Map<String, String> header) {

        restMgr.getResource(UserDetail.class, null, null, header, new RestManager.GetResourceListener<UserDetail>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, UserDetail resource) {
                ArrayList<FriendList> tmpList = new ArrayList<FriendList>(list);
                mCallback.onDelete(tmpList, resource);
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

            }
        }, null);
    }







    private void deleteFriendPhase1() {

        // set header
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));

        deleteFriendPhase2(header, toDeleteFriends, toDeleteFriends.size());
    }
    private void deleteFriendPhase2(final Map<String, String> header, final List<String> tmp, final int tmp_size) {
        final int tmp_dfs = tmp_size - 1;


        // delete data
        restMgr.deleteResource(FriendList.class, tmp.get(tmp_dfs), header, new RestManager.DeleteResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                Log.d("user deleted :", "true");
                if (tmp_dfs > 0) {
                    deleteFriendPhase2(header, tmp, tmp_dfs);
                } else {
                    deleteFriendPhase3();
                }

            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }
            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
            }
        }, null);
    }
    public void deleteFriendPhase3() {

        // set Header
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(getActivity()));

        restMgr.listResource(FriendList.class, null, header, new RestManager.ListResourceListener<FriendList>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<FriendList> resources) {
                if (code == 200) {
                    ArrayList<FriendList> list = new ArrayList<FriendList>(resources);
                    mCallback.onDelete(list, mUserDetail);
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(getActivity(), "Fetching FriendList failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }
}
