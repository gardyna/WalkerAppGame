package com.ru.andr.walkinggame;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;

public class UserListAdapter extends BaseAdapter {


    private ArrayList<Player> userList = new ArrayList<Player>();
    private Context context;

    UserListAdapter(Context c){
        this.context = c;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    public void setData(ArrayList<Player> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }
    public void clear(){
        this.userList.clear();
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int number) {
        return this.userList.get(number);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_room, null);
        }
        if (userList != null && userList.size()>0) {
            ImageView status_online = (ImageView) convertView.findViewById(R.id.status_online);
            ImageView status_offline = (ImageView) convertView.findViewById(R.id.status_offline);
            TextView userName = (TextView) convertView.findViewById(R.id.item_username);
            Player user = userList.get(position);
            if(user.isStatusOnline()){
                status_online.setVisibility(View.VISIBLE);
                status_offline.setVisibility(View.GONE);
            }else{
                status_online.setVisibility(View.GONE);
                status_offline.setVisibility(View.VISIBLE);
            }
            userName.setText(user.getName());
        }
        return convertView;
    }

}