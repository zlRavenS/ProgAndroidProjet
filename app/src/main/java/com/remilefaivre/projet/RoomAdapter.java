package com.remilefaivre.projet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

public class RoomAdapter extends BaseAdapter implements ListAdapter {

    //Contexte de l'activité
    Context context;

    //Objet du layout
    int obj;

    JSONArray rooms;

    String urlPicture;
    String name;
    int id;

    public RoomAdapter(@NonNull Context context, int obj, JSONArray rooms) {
        this.context=context;
        this.obj=obj;
        this.rooms = rooms;
    }


    @Override
    public int getCount() {
        if(null== rooms)
            return 0;
        else
            return rooms.length();
    }
    @Override
    public Object getItem(int i) {
        if(null== rooms) return null;
        else
            return rooms.optJSONObject(i);
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(obj, null, false);

        TextView textRoom = view.findViewById(R.id.texte_room);
        ImageView imgRoom = view.findViewById(R.id.image_room);
        Button deleteRoom = view.findViewById(R.id.delete_room);
        ConstraintLayout myView = view.findViewById(R.id.my_view);

        try {
            name = rooms.getJSONObject(position).getString("name");
            urlPicture =  rooms.getJSONObject(position).getString("picture");
            id = rooms.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textRoom.setText(name);
        deleteRoom.setTag(id);
        myView.setTag(id);

        new DownloadImageTask(imgRoom)
                .execute("https://myhouse.lesmoulinsdudev.com/"+urlPicture);
        return view;
    }

    // show The Image in a ImageView
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
