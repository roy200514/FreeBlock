package netdb.courses.softwarestudio.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.rest.model.Resource;

/**
 * Created by Mark on 1/19/2015.
 */
public class FriendList extends Resource implements Parcelable {

    private String userid;
    private List<String> friendlist = new ArrayList<String>();
    private List<Campaign> schedule = new ArrayList<Campaign>();
    private String username;
    private String phonenumber;
    private List<Information> friendrequest = new ArrayList<Information>();
    private int isaccept;

    public FriendList() {
    }

    public FriendList(String userid, int isaccept) {
        this.userid = userid;
        this.isaccept = isaccept;
    }

    public FriendList(String userid) {
        this.userid = userid;
    }

    public static String getCollectionName() {
        return "friends";
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

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public List<String> getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(List<String> friendlist) {
        this.friendlist = friendlist;
    }

    public List<Campaign> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Campaign> schedule) {
        this.schedule = schedule;
    }

    public List<Information> getFriendrequest() {
        return friendrequest;
    }

    public void setFriendrequest(List<Information> friendrequest) {
        this.friendrequest = friendrequest;
    }

    public void setIsaccept(int isaccept) {
        this.isaccept = isaccept;
    }

    public int getIsaccept() {
        return isaccept;
    }



    // in order to use Parcel
    public static final Parcelable.Creator<FriendList> CREATOR = new Creator<FriendList>() {
        @Override
        public FriendList createFromParcel(Parcel source) {
            return new FriendList(source);
        }

        @Override
        public FriendList[] newArray(int size) {
            return new FriendList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeList(friendlist);
        dest.writeTypedList(schedule);
        dest.writeString(username);
        dest.writeString(phonenumber);
        dest.writeList(friendrequest);
        dest.writeInt(isaccept);
    }

    private FriendList(Parcel in) {
        this.userid = in.readString();
        in.readStringList(this.friendlist);
        in.readTypedList(this.schedule, Campaign.CREATOR);
        this.username = in.readString();
        this.phonenumber = in.readString();
        in.readTypedList(this.friendrequest, Information.CREATOR);
        this.isaccept = in.readInt();
    }
}
