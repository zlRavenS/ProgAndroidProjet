package com.remilefaivre.projet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class CapteurAdapter extends BaseAdapter implements ListAdapter {

    //Contexte de l'activité
    Context context;

    //Objet du layout
    int obj;
    JSONArray capteurs;

    String urlPicture;
    String name;
    String type;
    int id;
    String token;

    public CapteurAdapter(@NonNull Context context, int obj, JSONArray capteurs, String token) {
        this.context=context;
        this.obj=obj;
        this.capteurs=capteurs;
        this.token = token;
    }


    @Override
    public int getCount() {
        if(null==capteurs)
            return 0;
        else
            return capteurs.length();
    }

    @Override
    public Object getItem(int i) {
        if(null==capteurs) return null;
        else
            return capteurs.optJSONObject(i);
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

        // Récupération des différents éléments du layout
        TextView nameField = view.findViewById(R.id.texte_capteur);
        ImageView imgSensor = view.findViewById(R.id.image_capteur);
        Button deleteSensor = view.findViewById(R.id.button_deleteSensor);


        // Récupération des différents éléments du capteur donné en paramètre
        try {
            name = capteurs.getJSONObject(position).getString("name");
            urlPicture =  capteurs.getJSONObject(position).getString("picture");
            type = capteurs.getJSONObject(position).getString("type");
            id = capteurs.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Changement des éléments de notre layout
        imgSensor.setImageURI(Uri.parse(urlPicture));
        nameField.setText(name);
        deleteSensor.setTag(id);

        // Récupération de la valeur de notre capteurs à l'aide du site et de l'ID du capteur
        TextView valueField = view.findViewById(R.id.texte_value);
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-value?idSensor="+id)
            .addHeaders("Authorization","Bearer " + token)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Double value;
                    String unit;
                    try {
                        // Récupération de la valeur et de l'unité de mesure
                        value = response.getDouble("value");
                        unit =  response.getString("unit");

                        valueField.setText(""+value + ""+unit);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    anError.getErrorCode();
                }
            });

        // Téléchargement de l'image correspondant au type du capteur
        new CapteurAdapter.DownloadImageTask(imgSensor)
            .execute("https://myhouse.lesmoulinsdudev.com/"+ urlPicture);
        return view;
    }

    // Affichage et téléchargement d'une image
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


