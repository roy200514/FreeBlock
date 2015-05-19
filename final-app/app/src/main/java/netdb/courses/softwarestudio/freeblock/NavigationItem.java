package netdb.courses.softwarestudio.freeblock;

import android.graphics.drawable.Drawable;
/**
 * Created by Mark on 1/16/2015.
 */


public class NavigationItem {
    private String mText;

    public NavigationItem(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

}
