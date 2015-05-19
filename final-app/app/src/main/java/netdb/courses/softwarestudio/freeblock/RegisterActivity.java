package netdb.courses.softwarestudio.freeblock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netdb.courses.softwarestudio.domain.User;
import netdb.courses.softwarestudio.rest.RestManager;
;

public class RegisterActivity extends Activity {

    /**
     * AddressCollection for AutoCompleteTextView. Create UI lifecycle helper.
     */
    private List<String> AddressCollection = new ArrayList<String>();
    private RestManager restMgr;
    // UI references.
    private EditText mUserID;
    private EditText mPasswordView;
    private EditText mNameView;
    private EditText mPhoneView;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        restMgr = RestManager.getInstance(this);
        // Set up the register form.
        mUserID = (EditText) findViewById(R.id.userID);

        mPasswordView = (EditText) findViewById(R.id.password);
        mNameView = (EditText) findViewById(R.id.name);
        mPhoneView = (EditText) findViewById(R.id.phone);
        mPhoneView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mUserIDRegisterBtn = (Button) findViewById(R.id.register_button);
        Button DEBUG = (Button) findViewById(R.id.tmp);
        DEBUG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mUserIDRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister() {

        // Reset errors.
        mUserID.setError(null);
        mPasswordView.setError(null);
        mNameView.setError(null);
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        String userID = mUserID.getText().toString();
        String password = mPasswordView.getText().toString();
        String name = mNameView.getText().toString();
        String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid userID address.
        if (TextUtils.isEmpty(userID)) {
            mUserID.setError(getString(R.string.error_field_required));
            focusView = mUserID;
            cancel = true;
        } else if (!isValid(userID)) {
            mUserID.setError(getString(R.string.error_invalid_id_or_pw));
            focusView = mUserID;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_id_or_pw));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user name.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!isValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_id_or_pw));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid user phone.
        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            UserRegisterTask(userID, password, name, phone);
        }
    }

    private boolean isPhoneValid(String phone) {
        if (!phone.substring(0, 2).equals("09")) {
            return false;
        } else if (phone.length()!=10) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValid(String id_or_pw) {
        return !id_or_pw.contains(" ") && id_or_pw.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void UserRegisterTask(final String userID, final String password, final String name, final String phone) {

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set Header
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json");
        User user = new User(userID, password, name, phone);

        restMgr.postResource(User.class, user, header, new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                if (code == 201) {
                    Toast.makeText(RegisterActivity.this, "Register SUCCESS, Log in Now", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {}

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 409) {
                    Toast.makeText(RegisterActivity.this, "Register FAILED, account duplicate", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
                if (code == 500) {
                    Toast.makeText(RegisterActivity.this, "Server BOOM", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            }
        }, null);

    }

}



