package netdb.courses.softwarestudio.freeblock;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


import netdb.courses.softwarestudio.domain.Campaign;
import netdb.courses.softwarestudio.domain.SaveSession;
import netdb.courses.softwarestudio.domain.Schedule;
import netdb.courses.softwarestudio.rest.RestManager;

import static netdb.courses.softwarestudio.freeblock.R.drawable.item_background_focused;
import static netdb.courses.softwarestudio.freeblock.R.drawable.weekday_click;
import static netdb.courses.softwarestudio.freeblock.R.layout.activity_set_event;

public class SetEvent extends ActionBarActivity {
    private final static String SCHEDUEL = "Schedule";
    private final static int START_DRAGGING = 1;
    private final static int STOP_DRAGGING = 0;
    private int i; // decide which picker
    private Campaign campaign;
    private int count = 0;
    private TextView DoneScope;
    private EditText EditEvent;
    private Button DoneBtn;
    private int status;
    //adapter
    private PickerAdapter adapter1;
    private PickerAdapter adapter2;
    private PickerAdapter adapter3;
    private PickerAdapter adapter4;
    private PickerAdapter adapter5;
    private PickerAdapter adapter6;
    private PickerAdapter adapter7;
    private ArrayList<Integer> pickerData;
    private StringBuffer DataString;
    public GridView weekpicker;
    public GridView picker[];
    public WeekdayAdapter wAdapter;
    public Integer Data;
    public HashMap<String,String> headers;
    private RestManager rManager;
    private ArrayList<Schedule> mScheduel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_set_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // initiailize
        i = 0;

        Intent intent = getIntent();
        mScheduel = intent.getParcelableArrayListExtra(SCHEDUEL);

        EditEvent = (EditText) findViewById(R.id.editText);
        EditEvent.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if( actionId ==R.id.ChangeKeyBoard || actionId== EditorInfo.IME_NULL){
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    return true;
                }
                return false;
            }
        });
        DataString = new StringBuffer();
        DoneBtn = (Button) findViewById(R.id.DoneBtn);
        DoneBtn.setHeight(75);
        DoneScope = (TextView) findViewById(R.id.Done);
        DoneScope.setHeight(75);

        adapter1 = new PickerAdapter(this,mScheduel);
        adapter2 = new PickerAdapter(this,mScheduel);
        adapter3 = new PickerAdapter(this,mScheduel);
        adapter4 = new PickerAdapter(this,mScheduel);
        adapter5 = new PickerAdapter(this,mScheduel);
        adapter6 = new PickerAdapter(this,mScheduel);
        adapter7 = new PickerAdapter(this,mScheduel);

        rManager = RestManager.getInstance(this);
        wAdapter = new WeekdayAdapter(this);
        setPicker();
        weekpicker = (GridView) findViewById(R.id.weekday);
        weekpicker.setAdapter(wAdapter);
        this.pickerData = new ArrayList<Integer>(84);
        // Set header and Fetch data
        headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Cookie", SaveSession.getUserCOOKIE(this));
        setPriviousTime();
        PickData(i);
        SetDragAction();
        PickDay();

    }

    private void setPicker() {

        picker = new GridView[7];

        picker[0] = (GridView) findViewById(R.id.picker1);
        adapter1.setDay(1);
        picker[0].setAdapter(adapter1);
        picker[1] = (GridView) findViewById(R.id.picker2);
        adapter2.setDay(2);
        picker[1].setAdapter(adapter2);
        picker[2] = (GridView) findViewById(R.id.picker3);
        adapter3.setDay(3);
        picker[2].setAdapter(adapter3);
        picker[3] = (GridView) findViewById(R.id.picker4);
        adapter4.setDay(4);
        picker[3].setAdapter(adapter4);
        picker[4] = (GridView) findViewById(R.id.picker5);
        adapter5.setDay(5);
        picker[4].setAdapter(adapter5);
        picker[5] = (GridView) findViewById(R.id.picker6);
        adapter6.setDay(6);
        picker[5].setAdapter(adapter6);
        picker[6] = (GridView) findViewById(R.id.picker7);
        adapter7.setDay(0);
        picker[6].setAdapter(adapter7);

        picker[1].setVisibility(View.INVISIBLE);
        picker[2].setVisibility(View.INVISIBLE);
        picker[3].setVisibility(View.INVISIBLE);
        picker[4].setVisibility(View.INVISIBLE);
        picker[5].setVisibility(View.INVISIBLE);
        picker[6].setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void SetDragAction() {
        DoneBtn.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int DragHeight;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    status = START_DRAGGING;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    status = STOP_DRAGGING;
                    // System.out.println("Bottom" + DoneScope.getBottom());
                    // 1629 is the number of the bottom of the screen
                    // 75 is the defalut heigh of the DoneBtn
                    // the comparesion here is for automatically done the drag
                    DragHeight = DoneScope.getBottom();
                    if (DragHeight > 500) {
                        while (DragHeight != 1629) {
                            //  System.out.println("DragHeight : " + DragHeight);
                            DragHeight += 1;
                            DoneBtn.setHeight(DragHeight);
                            DoneScope.setHeight(DragHeight);
                        }
                        if (DragHeight == 1629) {
                            SendData();
                        }
                    } else {
                        while (DragHeight != 75) {
                            // System.out.println("DragHeight : " + DragHeight);
                            DragHeight--;
                            DoneBtn.setHeight(75);
                            DoneScope.setHeight(75);
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (status == START_DRAGGING) {
                        System.out.println("Dragging");
                        DragHeight = (int) event.getRawY() - 1;
                        if (DragHeight < 136) {
                            DragHeight = 136;
                        }
                        DoneBtn.setHeight(DragHeight);
                        DoneScope.setHeight(DragHeight);
                        DoneBtn.invalidate();
                    }
                }
                return true;
            }
        });
    }

    private void SendData() {

        if(EditEvent.getText().length()==0 || pickerData.isEmpty() || pickerData == null){
            Toast.makeText(this, "Bulid Failed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SetEvent.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            for (int i = 0; i < pickerData.size(); i++){
                DataString.append(pickerData.get(i).toString());
            }
            campaign = new Campaign(EditEvent.getText().toString(),DataString.toString());

            rManager.postResource(Campaign.class, campaign, headers, new RestManager.PostResourceListener() {

                @Override
                public void onResponse(int code, Map<String, String> headers) {
                    if (code == 201) {
                        Toast.makeText(SetEvent.this, "Send Data Success.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SetEvent.this, MainActivity.class);
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



    }

    private void PickDay() {

        weekpicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = position;
               // view.setBackgroundResource(R.drawable.weekday_click);

                for (int j = 0; j < weekpicker.getCount(); j++) {
                    if (j == position) {
                        picker[j].setVisibility(View.VISIBLE);
                    } else {
                        picker[j].setVisibility(View.INVISIBLE);
                    }
                }



                PickData(i);
            }
        });
    }

    private void PickData(int i) {

        Data = (i + 1) * 10000;
        picker[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Data = Data + position * 100;
                if (!pickerData.contains(Data)) {
                    pickerData.add(count, Data);
                    view.setBackgroundResource(R.drawable.item_background_focused);
                }
                else{
                    view.setBackgroundResource(R.drawable.item_background);
                    pickerData.remove(Data);
                }
                Data = Data - position * 100;

            }

        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SetEvent.this);
                    return false;
                }

            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public void setPriviousTime(){

    }
}

