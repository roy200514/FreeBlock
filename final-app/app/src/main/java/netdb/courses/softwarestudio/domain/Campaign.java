package netdb.courses.softwarestudio.domain;

import android.os.Parcel;
import android.os.Parcelable;

import netdb.courses.softwarestudio.rest.model.Resource;

public class Campaign extends Resource implements Parcelable {
	String campaignname;
	String time;

	public Campaign(String campaignname, String time) {
		this.campaignname = campaignname;
		this.time = time;
	}

	public String getCampaignname() {
		return campaignname;
	}

	public void setCampaignname(String campaignname) {
		this.campaignname = campaignname;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

    public static String getCollectionName(){
        return "campaigns";
    }


    // in order to use Parcel
    public static final Parcelable.Creator<Campaign> CREATOR = new Creator<Campaign>() {
        @Override
        public Campaign createFromParcel(Parcel source) {
            return new Campaign(source);
        }

        @Override
        public Campaign[] newArray(int size) {
            return new Campaign[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(campaignname);
        dest.writeString(time);
    }

    private Campaign(Parcel in) {
        this.campaignname = in.readString();
        this.time = in.readString();
    }
}