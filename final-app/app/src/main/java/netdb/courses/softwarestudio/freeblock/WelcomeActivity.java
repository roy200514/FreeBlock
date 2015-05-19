package netdb.courses.softwarestudio.freeblock;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import netdb.courses.softwarestudio.domain.CampaignLibrary;
import netdb.courses.softwarestudio.domain.FriendList;
import netdb.courses.softwarestudio.domain.SaveSession;
import netdb.courses.softwarestudio.domain.Schedule;
import netdb.courses.softwarestudio.domain.UserDetail;
import netdb.courses.softwarestudio.rest.RestManager;


public class WelcomeActivity extends ActionBarActivity {

    private RestManager restMgr;

    Bundle extras = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        restMgr = RestManager.getInstance(this);

        intent = new Intent(this, MainActivity.class);
        //startActivity(intent);

        // Set Header
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(this));

        //new FetchUserDetail().execute(header);

        GetUserDetail(header);

    }

    public void GetUserDetail(final Map<String, String> header) {

        restMgr.getResource(UserDetail.class, null, null, header, new RestManager.GetResourceListener<UserDetail>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, UserDetail resource) {
                if (code == 200) {
                    extras.putParcelable("userDetail", resource);
                    //Log.d("User Detail from extras: ", extras.getParcelable("userDetail").toString());
                    GetSchedule(header);
                }
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void GetSchedule(final Map<String, String> header) {

        restMgr.listResource(Schedule.class, null, header, new RestManager.ListResourceListener<Schedule>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<Schedule> resources) {
                if (code == 200) {
                    ArrayList<Schedule> list = new ArrayList<Schedule>(resources);
                    extras.putParcelableArrayList("Schedule", list);

                    GetFriendList(header);
                }
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void GetFriendList(final Map<String, String> header) {
        restMgr.listResource(FriendList.class, null, header, new RestManager.ListResourceListener<FriendList>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<FriendList> resources) {
                if (code == 200) {
                    ArrayList<FriendList> list = new ArrayList<FriendList>(resources);
                    extras.putParcelableArrayList("FriendList", list);

                    GetCampaignLibrary(header);
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    public void GetCampaignLibrary(final Map<String, String> header) {
        restMgr.getResource(CampaignLibrary.class, null, null, header, new RestManager.GetResourceListener<CampaignLibrary>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, CampaignLibrary resource) {
                if (code == 200) {
                    extras.putParcelable("CampaignLibrary", resource);

                    intent.putExtras(extras);
                    Log.d("User Detail from intent's extra: ", intent.getParcelableExtra("userDetail").toString());
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }
            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }



    private class FetchCampaignLibrary extends AsyncTask<Map<String, String>, Void, Map<String, String>> {


        @Override
        protected Map<String, String> doInBackground(Map<String, String>... header) {

            restMgr.getResource(CampaignLibrary.class, null, null, header[0], new RestManager.GetResourceListener<CampaignLibrary>() {
                @Override
                public void onResponse(int code, Map<String, String> headers, CampaignLibrary resource) {
                    if (code == 200) {
                        extras.putParcelable("CampaignLibrary", resource);
                        Toast.makeText(WelcomeActivity.this, "Fetching Data....", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onRedirect(int code, Map<String, String> headers, String url) {
                }
                @Override
                public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                    if (code == 400) {
                        Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);
            return header[0];
        }

        @Override
        protected void onPostExecute(Map<String, String> header) {
            Toast.makeText(WelcomeActivity.this, "100% and hi", Toast.LENGTH_SHORT).show();
            //Log.d("User Detail's id from extras: ", extras.getParcelable("userDetail").toString());
        }
    }

    private class FetchFriendList extends AsyncTask<Map<String, String>, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Map<String, String>... header) {
            restMgr.listResource(FriendList.class, null, header[0], new RestManager.ListResourceListener<FriendList>() {
                @Override
                public void onResponse(int code, Map<String, String> headers, List<FriendList> resources) {
                    if (code == 200) {
                        extras.putParcelableArrayList("FriendList", (ArrayList) resources);
                        //Toast.makeText(WelcomeActivity.this, "Fetching Data...", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onRedirect(int code, Map<String, String> headers, String url) {
                }

                @Override
                public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                    if (code == 400) {
                        Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);
            return header[0];
        }

        @Override
        protected void onPostExecute(Map<String, String> header) {
            Toast.makeText(WelcomeActivity.this, "75% and more...", Toast.LENGTH_SHORT).show();
            new FetchCampaignLibrary().execute(header);
        }
    }

    private class FetchSchedule extends AsyncTask<Map<String, String>, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Map<String, String>... header) {
            restMgr.listResource(Schedule.class, null, header[0], new RestManager.ListResourceListener<Schedule>() {
                @Override
                public void onResponse(int code, Map<String, String> headers, List<Schedule> resources) {
                    if (code == 200) {
                        extras.putParcelableArrayList("Schedule", (ArrayList) resources);
                        //Toast.makeText(WelcomeActivity.this, "Fetching Data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onRedirect(int code, Map<String, String> headers, String url) {

                }

                @Override
                public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                    if (code == 400) {
                        Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);
            return header[0];
        }
        @Override
        protected void onPostExecute(Map<String, String> header) {
            Toast.makeText(WelcomeActivity.this, "50% and more...", Toast.LENGTH_SHORT).show();
            new FetchFriendList().execute(header);
        }


    }

    private class FetchUserDetail extends AsyncTask<Map<String, String>, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String, String> doInBackground(Map<String, String>... header) {

            restMgr.getResource(UserDetail.class, null, null, header[0], new RestManager.GetResourceListener<UserDetail>() {
                @Override
                public void onResponse(int code, Map<String, String> headers, UserDetail resource) {
                    if (code == 200) {
                        extras.putParcelable("userDetail", resource);
                        Log.d("User Detail's id from extras: ", extras.getParcelable("userDetail").toString());
                        //Toast.makeText(WelcomeActivity.this, "Fetching Data....", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onRedirect(int code, Map<String, String> headers, String url) {
                }

                @Override
                public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                    if (code == 400) {
                        Toast.makeText(WelcomeActivity.this, "Fetching Data failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);

            return header[0];
        }

        @Override
        protected void onPostExecute(Map<String, String> header) {
            Toast.makeText(WelcomeActivity.this, "25% and more...", Toast.LENGTH_SHORT).show();

            new FetchSchedule().execute(header);
        }
    }

}
