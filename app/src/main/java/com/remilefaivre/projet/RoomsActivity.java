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

        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        loadRoomPicture(token);
        loadRooms(token);
        //picture?type=

    }
    public void onClickItem(View view) {
        int idRoom = (int) view.getTag();

        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        Intent i = new Intent(RoomsActivity.this, InfoActivity.class);
        i.putExtra("token", token);
        i.putExtra("idRoom", ""+idRoom);
        startActivity(i);
    }
    public void onClickAjouter(View view) {
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        Intent i = new Intent(RoomsActivity.this, AddRoomActivity.class);
        i.putExtra("token", token);
        startActivity(i);
    }
    public void onClickSupprimer(View view) {
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        int idRoom = (int) view.getTag();
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-delete")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idRoom",""+idRoom)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
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


        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/rooms")
                .addHeaders("Authorization","Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rooms = response.getJSONArray("rooms");

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

    public void loadRoomPicture(String token) {
        //Pour conserver le contexte de l'activité
        Context that = this;

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/pictures?type=room")
                .addHeaders("Authorization","Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray pictures = response.getJSONArray("pictures");

                            ArrayList<Picture> pictureList = new ArrayList<>();

                            for (int iPicture = 0; iPicture < pictures.length(); iPicture++) {

                                final JSONObject picture = pictures.getJSONObject(iPicture);

                                //pictureList.add(new Picture(
                                 //       picture.getInt("id"),
                                  //      picture.getString("url")));
                            }
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

}