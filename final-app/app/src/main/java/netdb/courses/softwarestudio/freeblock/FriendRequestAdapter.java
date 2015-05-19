package netdb.courses.softwarestudio.freeblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import netdb.courses.softwarestudio.domain.Information;

/**
 * Created by Mark on 1/21/2015.
 */
public class FriendRequestAdapter extends ArrayAdapter<Information> {

    private ArrayList<Information> friendRequest;

    private static class ViewHolder {
        private TextView itemView;
    }

    public FriendRequestAdapter(Context context, ArrayList<Information> items) {
        super(context, 0, items);
        this.friendRequest = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.friend_request_item, parent, false);
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.request_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (friendRequest.get(position).getUserid().equals("YOU HAVE NO REQUEST")) {
            viewHolder.itemView.setText(friendRequest.get(position).getUserid());
        } else {
            viewHolder.itemView.setText(friendRequest.get(position).getUserid()+"\n("+(friendRequest.get(position).getUsername())+")");
        }

        return convertView;
    }
}
