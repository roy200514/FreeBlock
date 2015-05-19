package netdb.courses.softwarestudio.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.rest.model.Resource;

/**
 * Created by Mark on 1/19/2015.
 */
public class Schedule extends Resource implements Parcelable {

    private String username;
    private List<ParcelableArrayList> schedule = new ArrayList<ParcelableArrayList>();

    public Schedule() {
    }

    public Schedule(String username, List<ParcelableArrayList> schedule) {
        this.username = username;
        this.schedule = schedule;
    }

    public static String getCollectionName(){
        return "schedules";
    }

    public List<ParcelableArrayList> getSchedule() {
        return this.schedule;
    }

    public void setSchedule(List<ParcelableArrayList> schedule) {
        this.schedule = schedule;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // in order to use Parcel
    public static final Parcelable.Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }
        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeTypedList(schedule);
    }

    private Schedule(Parcel in) {
        this.username = in.readString();
        in.readTypedList(this.schedule, ParcelableArrayList.CREATOR);
    }
}