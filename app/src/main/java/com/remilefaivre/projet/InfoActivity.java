package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import okhttp3.Response;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");

        loadCapteurs(token, idRoom);
        loadPeripheriques(token, idRoom);

    }



    public void onClickAjouterCapteur(View view) {
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");

        Intent i = new Intent(InfoActivity.this, AddCapteurActivity.class);
        i.putExtra("token", token);
        i.putExtra("idRoom", ""+idRoom);
        startActivity(i);
    }

    public void onClickSupprimerCapteur(View view) {
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");

        int idSensor = (int) view.getTag();

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/sensor-delete")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idSensor",""+idSensor)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                Intent i = new Intent(InfoActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);
                                startActivity(i);
                                break;
                            default:
                                Toast toastError = Toast.makeText(InfoActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(InfoActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });


    }

    public void loadCapteurs(String token, String idRoom) {
        Context that = this;

        ListView listCapteurs = findViewById(R.id.liste_capteurs);


        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensors?idRoom="+idRoom)
                .addHeaders("Authorization","Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray capteurs = response.getJSONArray("sensors");

                            CapteurAdapter capteurAdapter = new CapteurAdapter(that,R.layout.capteur_item, capteurs, token);
                            listCapteurs.setAdapter(capteurAdapter);

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





    public void loadPeripheriques(String token, String idRoom) {
        Context that = this;

        ListView listPeripheriques = findViewById(R.id.liste_peripheriques);


        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/devices?idRoom=" + idRoom)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray peripheriques = response.getJSONArray("devices");

                            PeripheriqueAdapter peripheriqueAdapter = new PeripheriqueAdapter(that, R.layout.peripherique_item, peripheriques);
                            listPeripheriques.setAdapter(peripheriqueAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(that, anError.getErrorBody(), Toast.LENGTH_SHORT);
                        toastError.show();
                        anError.getErrorCode();
                    }
                });
    }

    public void onClickAjouterPeripherique(View view) {
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");

        Intent i = new Intent(InfoActivity.this, AddPeripheriqueActivity.class);
        i.putExtra("token", token);
        i.putExtra("idRoom", ""+idRoom);
        startActivity(i);
    }

    public void onClickSupprimerPeripherique(View view) {
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");

        int idDevice = (int) view.getTag();

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/sensor-delete")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idSensor",""+idDevice)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                Intent i = new Intent(InfoActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);
                                startActivity(i);
                                break;
                            default:
                                Toast toastError = Toast.makeText(InfoActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(InfoActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });


    }

    public void onClickEtat(View view){
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");

        int idDevice = (int) view.getTag();

        /*Button button = view.findViewById(R.id.button_etat_peripherique);
        String status = (String) button.getText();
        int statu;

        if(status=="1"){
            statu = 0;
        }else {
            statu = 1;
        }
        */


        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-status")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idDevice",""+idDevice)
                .addBodyParameter("idDevice",""+0)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                Intent i = new Intent(InfoActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);
                                startActivity(i);
                                break;
                            default:
                                Toast toastError = Toast.makeText(InfoActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(InfoActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });
    }
}