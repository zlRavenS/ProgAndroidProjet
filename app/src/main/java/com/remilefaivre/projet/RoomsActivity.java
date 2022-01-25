package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        loadRooms();
        loadRoomPicture();
    }

    public void loadRooms() {
        //Pour conserver le contexte de l'activité
        Context that = this;

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/rooms")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Récupération du tableau de flavour
                        JSONArray rooms = response.getJSONArray("rooms");

                        //Liste dans laquelle seront stockés les "parfums" de pizzas
                        ArrayList<Room> roomList = new ArrayList<>();

                        //Pour chaque pizza
                        for(int iRoom = 0; iRoom < rooms.length(); iRoom++) {

                            //On récupère les données de la pizza
                            final JSONObject room = rooms.getJSONObject(iRoom);

                            //On ajoute les données à la liste des parfums
                            roomList.add(new Room(
                                    room.getString("name"),
                                    room.getInt("idPicture")));
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
    }

    public void loadRoomPicture() {
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
    }
}