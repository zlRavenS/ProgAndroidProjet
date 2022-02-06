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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

public class PeripheriqueAdapter extends BaseAdapter implements ListAdapter {

    //Contexte de l'activité
    Context context;

    //Objet du layout
    int obj;
    JSONArray devices;

    String urlPicture;
    String name;
    String type;
    int id;
    int status;


    public PeripheriqueAdapter(@NonNull Context context, int obj, JSONArray devices) {
        this.context=context;
        this.obj=obj;
        this.devices = devices;
    }


    @Override
    public int getCount() {
        if(null== devices)
            return 0;
        else
            return devices.length();
    }

    @Override
    public Object getItem(int i) {
        if(null== devices) return null;
        else
            return devices.optJSONObject(i);
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

        TextView nameField = view.findViewById(R.id.texte_nom_peripherique);
        ImageView imgDevice = view.findViewById(R.id.img_peripherique);
        Button deleteDevice = view.findViewById(R.id.button_supprimer_peripherique);
        Switch stateDevice = view.findViewById(R.id.device_state);


        try {
            name = devices.getJSONObject(position).getString("name");
            urlPicture =  devices.getJSONObject(position).getString("picture");
            type = devices.getJSONObject(position).getString("type");
            id = devices.getJSONObject(position).getInt("id");
            status = devices.getJSONObject(position).getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nameField.setText(name);
        deleteDevice.setTag(id);

        if(status==1){
            stateDevice.setChecked(true);
        }else{
            stateDevice.setChecked(false);
        }
        stateDevice.setTag(id);

        new PeripheriqueAdapter.DownloadImageTask(imgDevice)
                .execute("https://myhouse.lesmoulinsdudev.com/"+ urlPicture);
        return view;
    }

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


