package netdb.courses.softwarestudio.freeblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import netdb.courses.softwarestudio.domain.FriendList;

/**
 * Created by Mark on 1/20/2015.
 */
public class FriendListAdapter extends ArrayAdapter<FriendList> {

    private ArrayList<FriendList> mFriendList;

    private static class ViewHolder {
        private TextView itemView;
    }

    public FriendListAdapter(Context context, ArrayList<FriendList> items) {
        super(context, 0, items);
        this.mFriendList = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.friend_list_item, parent, false);
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.friend_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mFriendList.get(position).getUserid().equals("YOU HAVE NO FRIEND")) {
            viewHolder.itemView.setText(mFriendList.get(position).getUserid());
        } else {
            viewHolder.itemView.setText(mFriendList.get(position).getUserid());
        }

        return convertView;
    }
}
