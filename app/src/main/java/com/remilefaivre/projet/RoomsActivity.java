package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class RoomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext());

        setContentView(R.layout.activity_rooms);

        //Réupération du token via l'intent
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        loadRooms(token);

    }
    public void onClickItem(View view) {
        //récupération de l'id de la pièce
        int idRoom = (int) view.getTag();

        //récupération du nom de la pièce
        TextView nameRoom = view.findViewById(R.id.texte_room);
        String titreRoom = nameRoom.getText().toString();

        //récupération du token
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        //on passe à l'affichage des infos de la pièce en passant les 3 paramètres précédents
        Intent i = new Intent(RoomsActivity.this, InfoActivity.class);
        i.putExtra("token", token);
        i.putExtra("idRoom", ""+idRoom);
        i.putExtra("nameRoom", titreRoom);

        startActivity(i);
    }
    public void onClickAjouter(View view) {
        //récupération du token
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        //on passe à la page de création de pièce
        Intent i = new Intent(RoomsActivity.this, AddRoomActivity.class);
        i.putExtra("token", token);
        startActivity(i);
    }
    public void onClickSupprimer(View view) {
        //on récupère le token
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        //on récupère l'id de la room
        int idRoom = (int) view.getTag();

        //on se connecte au site pour supprimer une room
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-delete")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idRoom",""+idRoom)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                //on reload la page afin d'actualiser la liste des pièces
                                Intent i = new Intent(RoomsActivity.this, RoomsActivity.class);
                                i.putExtra("token", token);
                                startActivity(i);
                                break;
                            default:
                                Toast toastError = Toast.makeText(RoomsActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(RoomsActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });


    }

    public void loadRooms(String token) {
        //Pour conserver le contexte de l'activité
        Context that = this;

        ListView listRooms = findViewById(R.id.liste_rooms);

        //on se connecte au site pour charger la liste des rooms
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/rooms")
                .addHeaders("Authorization","Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rooms = response.getJSONArray("rooms");

                            // Affiche les pièces dans le ListView à partir du modèle room_item.xml
                            RoomAdapter roomAdapter = new RoomAdapter(that,R.layout.room_item, rooms);
                            listRooms.setAdapter(roomAdapter);

                        } catch (JSONException e) {
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