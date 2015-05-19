package netdb.courses.softwarestudio.freeblock;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netdb.courses.softwarestudio.domain.CampaignLibrary;
import netdb.courses.softwarestudio.domain.FriendList;
import netdb.courses.softwarestudio.domain.SaveSession;
import netdb.courses.softwarestudio.domain.Schedule;
import netdb.courses.softwarestudio.domain.SessionUser;
import netdb.courses.softwarestudio.domain.UserDetail;
import netdb.courses.softwarestudio.rest.RestManager;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, FriendFragment.OnDeleteFriendUpdate, FilterFragment.OnFilterScheduleUpdate {

    private static final String MY_PROFILE_FRG_TAG = "MY_PROFILE_FRG_TAG";
    private static final String FILTER_FRG_TAG = "MY_FILTER_FRG_TAG";
    private static final String FRIEND_FRAGMENT_TAG = "FRIEND_FRAG";
    private static final String ABOUT_TAG = "ABOUT_TAG";

    private static final String BOOLEAN_FALSE = "BOOLEAN_FALSE";

    private static final int FETCH_ALL = 0;
    private static final int FETCH_DETAIL_ONLY = 1;
    private static final int FETCH_SCHEDULE_ONLY = 2;
    private static final int FETCH_FRIENDLIST_ONLY = 3;

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GridView mGridView;
    private DailyViewFragment dailyViewFragment;
    private FrameLayout mFrameLayout;
    private ProgressBar mProgressBar;

    private RestManager restMgr;

    private UserDetail mUserDetail;
    private ArrayList<Schedule> mSchedule;      // Complete Schedule
    private ArrayList<Schedule> fixSchedule;    // Filter Schedule
    private ArrayList<FriendList> mFriendList;
    private CampaignLibrary mCampaignLibrary;

    private MainViewAdapter mainViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Fetch Data
        restMgr = RestManager.getInstance(this);
        // Set header and Fetch data
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(this));


        //if (mUserDetail==null) Log.d("user detail's id I got: ", "NULLLLLLL");
        //else Log.d("user detail's id I got: ", mUserDetail.getUserid());

        // Handle grid_weekday GridView.
        //mainViewAdapter = new MainViewAdapter(this, mSchedule);


        // Handle toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        setTitle(Title());
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Handle Drawer Fragment
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.closeDrawer();

        // Handle Progress Bar and FrameLayout before fetching data
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_data);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);

        // Initialize restMgr
        restMgr = RestManager.getInstance(this);



        // Fetch Data *** important ***
        GetUserDetailandOther(header, 0);


        //mProgressBar.setVisibility(View.GONE);
        //mFrameLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentTransaction transaction;
        switch (position) {
            case 0:
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                setTitle(Title());
                break;
            case 1:
                Fragment myProfile_fragment = new MyProfileFragment();
                transaction = getFragmentManager().beginTransaction();
                Bundle me = new Bundle();
                me.putParcelable("myDetail", mUserDetail);
                myProfile_fragment.setArguments(me);
                transaction
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.container, myProfile_fragment, MY_PROFILE_FRG_TAG).addToBackStack(MY_PROFILE_FRG_TAG).commit();
                break;
            case 2:
                FilterFragment filterFragment = new FilterFragment();
                transaction = getFragmentManager().beginTransaction();
                Bundle sc = new Bundle();
                sc.putParcelableArrayList("mSchedule", mSchedule);
                filterFragment.setArguments(sc);
                transaction
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.container, filterFragment, FILTER_FRG_TAG).addToBackStack(FILTER_FRG_TAG).commit();
                break;
            case 3:
                // set header
                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Cookie", SaveSession.getUserCOOKIE(this));
                GetUserDetailandOther(header, FETCH_FRIENDLIST_ONLY);
                break;
            case 4:
                Fragment about_fragment = new AboutFragment();
                transaction = getFragmentManager().beginTransaction();
                transaction
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.container, about_fragment, ABOUT_TAG).addToBackStack(ABOUT_TAG).commit();
                break;
            case 5:
                Logout();
                break;
        }
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            menu.findItem(R.id.action_add_main).setVisible(false);
        }
        Log.d("back stacks on prepare: ", ""+getFragmentManager().getBackStackEntryCount());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_main:
                Intent intent = new Intent(this, SetEvent.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Schedule", mSchedule);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFilterUpdate(ArrayList<Schedule> schedules) {
        fixSchedule = schedules;
        Log.d("fixSchedule: ", ""+(fixSchedule==null));
        Fragment fragment = getFragmentManager().findFragmentByTag(FILTER_FRG_TAG);
        FragmentTransaction fT = getFragmentManager().beginTransaction();
        fT.detach(fragment).commit();


        finish();
        startActivity(getIntent());

        //mainViewAdapter = new MainViewAdapter(this, fixSchedule);
        //mGridView.setAdapter(mainViewAdapter);
    }

    @Override
    public void onDelete(ArrayList<FriendList> friendList, UserDetail userDetail) {

        mFriendList = friendList;
        mUserDetail = userDetail;

        Fragment fragment = getFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG);
        FragmentTransaction fT = getFragmentManager().beginTransaction();
        fT.detach(fragment);
        fT.commit();

        mGridView.setAdapter(mainViewAdapter);

        Fragment friend_fragment = new FriendFragment();
        Bundle bundleFriend = new Bundle();
        bundleFriend.putParcelableArrayList("friendList", mFriendList);
        bundleFriend.putParcelable("userDetail", mUserDetail);
        friend_fragment.setArguments(bundleFriend);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, friend_fragment, FRIEND_FRAGMENT_TAG).addToBackStack(FRIEND_FRAGMENT_TAG).commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            Log.d("back stack count: ", ""+fragmentManager.getBackStackEntryCount());
            if (fragmentManager.getBackStackEntryCount() == 1) {
                setTitle(Title());
            }
        }
        else {
            super.onBackPressed();
        }
    }

    public void GetUserDetailandOther(final Map<String, String> header, final int type) {

        restMgr.getResource(UserDetail.class, null, null, header, new RestManager.GetResourceListener<UserDetail>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, UserDetail resource) {
                if (code == 200) {
                    if (type == FETCH_ALL) {
                        mUserDetail = resource;
                        Log.d("mUserDetail has userID: ", mUserDetail.getUserid());
                        GetSchedule(header, FETCH_ALL);
                    } else if (type == FETCH_FRIENDLIST_ONLY) {
                        mUserDetail = resource;
                        GetFriendList(header, FETCH_FRIENDLIST_ONLY);
                    }
                }
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(MainActivity.this, "Fetch Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void GetSchedule(final Map<String, String> header, final int type) {

        restMgr.listResource(Schedule.class, null, header, new RestManager.ListResourceListener<Schedule>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<Schedule> resources) {
                if (code == 200) {
                    if (type == FETCH_ALL) {
                        ArrayList<Schedule> list = new ArrayList<Schedule>(resources);
                        mSchedule = list;
                        fixSchedule = mSchedule;
                        Log.d("mSchedule is empty? : ", "" + mSchedule.isEmpty());
                        GetFriendList(header, FETCH_ALL);
                    }
                }
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(MainActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void GetFriendList(final Map<String, String> header, final int type) {

        restMgr.listResource(FriendList.class, null, header, new RestManager.ListResourceListener<FriendList>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<FriendList> resources) {
                if (code == 200) {
                    if (type == FETCH_ALL) {
                        ArrayList<FriendList> list = new ArrayList<FriendList>(resources);
                        mFriendList = list;
                        Log.d("mFriendList is empty? : ", "" + mFriendList.isEmpty());
                        GetCampaignLibrary(header);
                    } else if (type == FETCH_FRIENDLIST_ONLY) {
                        ArrayList<FriendList> list = new ArrayList<FriendList>(resources);
                        mFriendList = list;
                        openFriendFragment();
                    }
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(MainActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void GetCampaignLibrary(final Map<String, String> header) {
        restMgr.getResource(CampaignLibrary.class, null, null, header, new RestManager.GetResourceListener<CampaignLibrary>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, CampaignLibrary resource) {
                if (code == 200) {
                    mCampaignLibrary = resource;
                    Log.d("mCampaignLibrary has : ", ""+mCampaignLibrary.getCampaignLibrary().isEmpty());
                    Toast.makeText(MainActivity.this, "Data Fetched DONE", Toast.LENGTH_SHORT).show();
                    mFrameLayout.setVisibility(View.VISIBLE);
                    //Try to new GridView After Done Fetch
                    mGridView = (GridView) findViewById(R.id.grid_weekday);
                    mainViewAdapter = new MainViewAdapter(MainActivity.this, mSchedule);
                    mGridView.setAdapter(mainViewAdapter);

                    PickDailyView();

                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }
            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(MainActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
        mProgressBar.setVisibility(View.GONE);
    }

    private void openFriendFragment() {
        FragmentTransaction transaction;
        Fragment friend_fragment = new FriendFragment();
        transaction = getFragmentManager().beginTransaction();
        Bundle bundleFriend = new Bundle();
        bundleFriend.putParcelable("userDetail", mUserDetail);
        bundleFriend.putParcelableArrayList("friendList", mFriendList);
        friend_fragment.setArguments(bundleFriend);
        transaction
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, friend_fragment, FRIEND_FRAGMENT_TAG).addToBackStack(FRIEND_FRAGMENT_TAG)
                .commit();

    }

    public void Logout() {

        // Set Header
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(this));

        restMgr.deleteResource(SessionUser.class, null, header, new RestManager.DeleteResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                if (code == 203) {
                    SaveSession.setUserCOOKIE(MainActivity.this, null);
                    Toast.makeText(MainActivity.this, "Log out SUCCESS", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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

    public String DayTitle(String EEE, int day) {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int date = Calendar.getInstance().get(Calendar.DATE);
        return ("" + (date-dayOfWeek+day+1) + " " + EEE);
    }

    public void PickDailyView() {

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position%8 == 1) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
               //     bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    dailyViewFragment.setArguments(bundleFriend);
                    bundleFriend.putInt("weekday",0);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Sun", 0));
                } else if (position%8 == 2) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
                  //  bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    bundleFriend.putInt("weekday",1);
                    dailyViewFragment.setArguments(bundleFriend);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Mon", 1));
                } else if (position%8 == 3) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
                  //  bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    bundleFriend.putInt("weekday",2);
                    dailyViewFragment.setArguments(bundleFriend);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Tue", 2));
                } else if (position%8 == 4) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
                  //  bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    bundleFriend.putInt("weekday",3);
                    dailyViewFragment.setArguments(bundleFriend);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Wed", 3));
                } else if (position%8 == 5) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
                   // bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    bundleFriend.putInt("weekday",4);
                    dailyViewFragment.setArguments(bundleFriend);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Thu", 4));
                } else if (position%8 == 6) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
                   // bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    bundleFriend.putInt("weekday",5);
                    dailyViewFragment.setArguments(bundleFriend);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Fri", 5));
                } else if (position%8 == 7) {
                    dailyViewFragment = new DailyViewFragment();
                    FragmentTransaction transaction;
                    transaction = getFragmentManager().beginTransaction();
                    Bundle bundleFriend = new Bundle();
                    bundleFriend.putParcelableArrayList("friendList", mFriendList);
                    bundleFriend.putParcelableArrayList("schedule",mSchedule);
                   // bundleFriend.putParcelableArrayList("filschedule", fixSchedule);
                    bundleFriend.putInt("weekday",6);
                    dailyViewFragment.setArguments(bundleFriend);
                    transaction
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, dailyViewFragment).addToBackStack(null).commit();
                    setTitle(DayTitle("Sat", 6));
                }
            }
        });
    }

}
