package netdb.courses.softwarestudio.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import netdb.courses.softwarestudio.rest.model.Resource;

public class CampaignLibrary extends Resource implements Parcelable {
    List<String> library = new ArrayList<String>();

    public CampaignLibrary() {
    }

    public CampaignLibrary(List<String> library) {
        this.library = library;
    }

    public static String getCollectionName() {
        return "campaignlibrary";
    }

    public void setCampaignLibrary(List<String> library) {
        this.library = library;
    }

    public List<String> getCampaignLibrary() {
        return this.library;
    }


    // in order to use Parcel

    public static final Parcelable.Creator<CampaignLibrary> CREATOR = new Parcelable.Creator<CampaignLibrary>() {
        @Override
        public CampaignLibrary createFromParcel(Parcel source) {
            return new CampaignLibrary(source);
        }

        @Override
        public CampaignLibrary[] newArray(int size) {
            return new CampaignLibrary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(library);
    }

    private CampaignLibrary(Parcel in) {
        in.readStringList(library);
    }
}
