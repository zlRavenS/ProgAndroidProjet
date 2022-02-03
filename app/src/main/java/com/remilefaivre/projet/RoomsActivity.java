package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Intent i = getIntent();
        String userToken = i.getStringExtra("tokenID");

        loadRooms(userToken);
        //loadRoomPicture(userToken);
    }

    public void loadRooms(String tokenId) {
        //Pour conserver le contexte de l'activité
        Context that = this;

        ListView listRooms = findViewById(R.id.liste_rooms);

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/rooms")
                .addHeaders("Authorization","Bearer " + tokenId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Récupération du tableau de commandes
                            JSONArray orders = response.getJSONArray("rooms");

                            //Création d'un adaptateur permettant d'afficher les commandes dans un listView
                            RoomAdapter adapter = new RoomAdapter(that,R.layout.room_item,orders);

                            //Mise en place de l'adaptateur dans le spinner
                            listRooms.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(that,anError.getErrorBody(),Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });
    }

    /*public void loadRoomPicture(String tokenId) {
        //Pour conserver le contexte de l'activité
        Context that = this;

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/pictures")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Récupération du tableau de flavour
                        JSONArray pictures = response.getJSONArray("pictures");

                        //Liste dans laquelle seront stockés les "parfums" de pizzas
                        ArrayList<Picture> pictureList = new ArrayList<>();

                        //Pour chaque pizza
                        for(int iPicture = 0; iPicture < pictures.length(); iPicture++) {

                            //On récupère les données de la pizza
                            final JSONObject picture = pictures.getJSONObject(iPicture);

                            //On ajoute les données à la liste des parfums
                            pictureList.add(new Picture(
                                    picture.getInt("id"),
                                    picture.getString("url")));
                        }

                        // TODO Affichage dans le Layout

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {

                }
            });
    }*/
}