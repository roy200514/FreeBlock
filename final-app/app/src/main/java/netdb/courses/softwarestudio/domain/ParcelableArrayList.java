package netdb.courses.softwarestudio.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Mark on 1/19/2015.
 */
public class ParcelableArrayList extends ArrayList<String> implements Parcelable {

    private static final long serialVersionUID = -8516873361351845306L;

    public ParcelableArrayList(){
        super();
    }

    protected ParcelableArrayList(Parcel in) {
        in.readList(this, String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this);
    }

    public static final Parcelable.Creator<ParcelableArrayList> CREATOR = new Parcelable.Creator<ParcelableArrayList>() {
        @Override
        public ParcelableArrayList createFromParcel(Parcel in) {
            return new ParcelableArrayList(in);
        }
        @Override
        public ParcelableArrayList[] newArray(int size) {
            return new ParcelableArrayList[size];
        }
    };

}