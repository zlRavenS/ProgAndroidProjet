package com.remilefaivre.projet;

import android.content.Context;
import android.net.Uri;
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

public class CapteurAdapter extends BaseAdapter implements ListAdapter {

    //Contexte de l'activité
    Context context;

    //Objet du layout
    int obj;
    int resource;

    JSONArray capteurs;

    String urlPicture;
    String name;
    String type;
    int id;

    public CapteurAdapter(@NonNull Context context, int obj, JSONArray capteurs) {
        this.context=context;
        this.obj=obj;
        this.capteurs=capteurs;
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

        TextView nameField = view.findViewById(R.id.texte_capteur);
        TextView typeField = view.findViewById(R.id.type_capteur);
        ImageView imgCapteur = view.findViewById(R.id.image_capteur);


        try {
            name = capteurs.getJSONObject(position).getString("name");
            urlPicture =  capteurs.getJSONObject(position).getString("picture");
            type = capteurs.getJSONObject(position).getString("type");
            id = capteurs.getJSONObject(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgCapteur.setImageURI(Uri.parse(urlPicture));
        nameField.setText(name);
        typeField.setText(type);
        return view;
    }

}
