package com.remilefaivre.projet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class PeripheriqueAdapter extends BaseAdapter implements ListAdapter {

    //Contexte de l'activité
    Context context;

    //Objet du layout
    int obj;
    int resource;
    JSONArray peripheriques;

    String urlPicture;
    String name;
    String type;
    int id;
    int status;


    public PeripheriqueAdapter(@NonNull Context context, int obj, JSONArray peripheriques) {
        this.context=context;
        this.obj=obj;
        this.peripheriques=peripheriques;
    }


    @Override
    public int getCount() {
        if(null==peripheriques)
            return 0;
        else
            return peripheriques.length();
    }

    @Override
    public Object getItem(int i) {
        if(null==peripheriques) return null;
        else
            return peripheriques.optJSONObject(i);
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
        TextView typeField = view.findViewById(R.id.texte_type_peripherique);
        ImageView imgDevice = view.findViewById(R.id.img_peripherique);
        Button deleteDevice = view.findViewById(R.id.button_supprimer_peripherique);
        Switch stateDevice = view.findViewById(R.id.device_state);


        try {
            name = peripheriques.getJSONObject(position).getString("name");
            urlPicture =  peripheriques.getJSONObject(position).getString("picture");
            type = peripheriques.getJSONObject(position).getString("type");
            id = peripheriques.getJSONObject(position).getInt("id");
            status = peripheriques.getJSONObject(position).getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //imgDevice.setImageURI(Uri.parse(urlPicture));
        nameField.setText(name);
        typeField.setText(type);
        deleteDevice.setTag(id);
        if(status==1){
            stateDevice.setChecked(true);
        }else{
            stateDevice.setChecked(false);
        }
        stateDevice.setTag(id);

        new PeripheriqueAdapter.DownloadImageTask(imgDevice)
                .execute("https://myhouse.lesmoulinsdudev.com/"+urlPicture);
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


