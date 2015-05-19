package netdb.courses.softwarestudio.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Mark on 1/17/2015.
 */
public class SaveSession {
    static final String PREF_USER_ID = "userid";
    static final String PREF_USER_PW = "userpw";
    static final String PREF_USER_COOKIE = "usercookie";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserID(Context ctx, String userID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.commit();
    }
    public static void setUserPW(Context ctx, String userPW) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PW, userPW);
        editor.commit();
    }

    public static void setUserCOOKIE(Context ctx, String userCOOKIE) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_COOKIE, userCOOKIE);
        editor.commit();
    }

    public static String getUserID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }
    public static String getUserPW(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PW, "");
    }

    public static String getUserCOOKIE(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_COOKIE, "");
    }
}
