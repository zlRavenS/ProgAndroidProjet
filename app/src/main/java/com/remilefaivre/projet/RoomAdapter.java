package com.remilefaivre.projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

public class RoomAdapter extends BaseAdapter implements ListAdapter {

    //Contexte de l'activité
    Context context;

    //Objet du layout
    int obj;

    JSONArray room;

    String name;
    String urlPicture;
    int idRoom;

    public RoomAdapter(@NonNull Context context, int obj, JSONArray room) {
        this.context=context;
        this.obj=obj;
        this.room=room;
    }

    @Override
    public int getCount() {
        if(null==room)
            return 0;
        else
            return room.length();
    }

    @Override
    public Object getItem(int i) {
        if(null==room) return null;
        else
            return room.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(obj, null, false);

        //getting the view elements of the list from the view
        TextView roomName = view.findViewById(R.id.texte_room);
        ImageView roomPicture = view.findViewById(R.id.image_room);

        //adding values to the list item
        try {
            name = room.getJSONObject(position).getString("name");
            urlPicture = room.getJSONObject(position).getString("url");
            idRoom = room.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        roomName.setText(name);
        //roomPicture

        //finally returning the view
        return view;
    }
}
