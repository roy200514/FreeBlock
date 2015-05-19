package netdb.courses.softwarestudio.freeblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import netdb.courses.softwarestudio.domain.Schedule;

/**
 * Created by Mark on 1/21/2015.
 */
public class FilterListAdapter extends ArrayAdapter<Schedule> {

    private ArrayList<Schedule> adapterSchedule;

    private static class ViewHolder {
        private TextView itemView;
    }

    public FilterListAdapter(Context context, ArrayList<Schedule> items) {
        super(context, 0, items);
        this.adapterSchedule = items;
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

        if (adapterSchedule.get(position).getUsername().equals("YOU HAVE NO FRIEND")) {
            viewHolder.itemView.setText(adapterSchedule.get(position).getUsername());
        } else {
            viewHolder.itemView.setText(adapterSchedule.get(position).getUsername());
        }

        return convertView;
    }
}
