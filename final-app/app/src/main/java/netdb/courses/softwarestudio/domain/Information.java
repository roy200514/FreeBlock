package netdb.courses.softwarestudio.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mark on 1/21/2015.
 */
public class Information implements Parcelable {
    private String userid;
    private String username;

    public Information() {
        // required empty constructor
    }

    public Information(String userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    // in order to use Parcel
    public static final Parcelable.Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel source) {
            return new Information(source);
        }
        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(username);
    }

    private Information(Parcel in) {
        this.userid = in.readString();
        this.username = in.readString();
    }
}
