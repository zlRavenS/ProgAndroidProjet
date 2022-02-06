package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class AddRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        loadRoomType();
    }

    public void onClickValider(View view) {
        Context that = this;

        // Récupération du token
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        // Récupération du nom de la nouvelle pièce
        EditText newNameField = (EditText) findViewById(R.id.new_name_room);
        String newName = String.valueOf(newNameField.getText());

        // Récupération du nom du type de la nouvelle pièce
        Spinner newTypeField = (Spinner) findViewById(R.id.new_type_room);
        Picture newType = (Picture) newTypeField.getSelectedItem();

        // Connexion au site pour créer une pièce
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name", newName)
                .addBodyParameter("idPicture",""+newType.getIdPicture())
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                // Retour sur l'activité précédente en récupérant la nouvelle pièce
                                Intent i = new Intent(AddRoomActivity.this, RoomsActivity.class);
                                i.putExtra("token", token);
                                startActivity(i);
                                break;
                            default:
                                // Si erreur, affichage d'un Toast
                                Toast toastError = Toast.makeText(AddRoomActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(AddRoomActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });
    }

    public void loadRoomType() {
        Context that = this;

        // Connexion au site pour récupérer les images de nos types de pièces
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/pictures?type=room")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Listage de toutes les images
                            JSONArray pictures = response.getJSONArray("pictures");

                            ArrayList<Picture> pictureList = new ArrayList<>();

                            // Pour chaque image
                            for(int iPicture = 0; iPicture < pictures.length(); ++iPicture)
                            {
                                final JSONObject picture = pictures.getJSONObject(iPicture);
                                pictureList.add(new Picture(
                                        picture.getInt("id"),
                                        picture.getString("url")
                                ));
                            }

                            // Affichage des images de pièces dans un Spinner grâce à un Adapter
                            ArrayAdapter<Picture> adapter = new ArrayAdapter<>(
                                    that,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    pictureList
                            );

                            Spinner spinnerType = findViewById(R.id.new_type_room);

                            spinnerType.setAdapter(adapter);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(that,anError.getErrorBody(),Toast.LENGTH_SHORT);
                        toastError.show();
                        anError.getErrorCode();
                    }
                });
    }
}
