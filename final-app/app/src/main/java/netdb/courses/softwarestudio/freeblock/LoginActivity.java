package netdb.courses.softwarestudio.freeblock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netdb.courses.softwarestudio.domain.SaveSession;
import netdb.courses.softwarestudio.domain.SessionUser;
import netdb.courses.softwarestudio.rest.RestManager;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * AddressCollection for AutoCompleteTextView. Create UI lifecycle helper.
     */
    private List<String> AddressCollection = new ArrayList<String>();
    private RestManager restMgr;
    //private StatusCallback statusCallback = new Sessionn.Status

    // UI references.
    private AutoCompleteTextView mUserID;
    private EditText mPassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log Cookie
        Log.d("CookieonCreate = ", SaveSession.getUserCOOKIE(this));

        setContentView(R.layout.activity_login);

        restMgr = RestManager.getInstance(this);

        // Set up the login form.
        mUserID = (AutoCompleteTextView) findViewById(R.id.userID);
        populateAutoComplete();

        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mUserSignInButton = (Button) findViewById(R.id.sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mUserRegisterButton = (Button) findViewById(R.id.register_button);
        mUserRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Attempt Auto Login
        showProgress(true);
        if (SaveSession.getUserCOOKIE(this) != null) attemptAutoLogin();

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mUserID.setError(null);
        mPassword.setError(null);
        // Store values at the time of the login attempt.
        String userID = mUserID.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_id_or_pw));
            focusView = mPassword;
            cancel = true;
        }

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            UserLoginTask(userID, password);
        }
    }

    public void attemptRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean isValid(String id_or_pw) {
        return id_or_pw.length() > 3;
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

    private void populateAutoComplete() {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, AddressCollection);

        mUserID.setAdapter(adapter);
    }

    /**
     * Represents a login/registration task used to authenticate the user.
     */
    public void UserLoginTask(final String userID, final String password) {

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set Header
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json");

        // Set Session
        SessionUser user = new SessionUser(userID, password);
        restMgr.postResource(SessionUser.class, user, header, new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                if (code == 201) {
                    // Save Cookie
                    SaveSession.setUserCOOKIE(LoginActivity.this, headers.get("Set-Cookie"));
                    // Log in success
                    Toast.makeText(LoginActivity.this, "Log in SUCCESS", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {}

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if (code == 400) {
                    Toast.makeText(LoginActivity.this, "Log in FAILED", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
                if (code == 500) {
                    Toast.makeText(LoginActivity.this, "Server BOOM", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            }
        }, null);
    }

    public void attemptAutoLogin() {

        // Set header
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json");
        header.put("Cookie", SaveSession.getUserCOOKIE(this));

        // Use Cookie Login
        restMgr.postResource(SessionUser.class, null, header, new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                Toast.makeText(LoginActivity.this, "Log in SUCCESS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                Log.d("Cookie ERROR", "COOKIE WRONG~~~~~~");
                showProgress(false);
            }
        }, null);
    }
}



