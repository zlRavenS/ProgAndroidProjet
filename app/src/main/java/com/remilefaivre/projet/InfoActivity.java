package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

                            CapteurAdapter capteurAdapter = new CapteurAdapter(that,R.layout.capteur_item, capteurs);
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



    public void loadCapteursValue(String token, int idSensor) {

        TextView valueField = (TextView) findViewById(R.id.texte_value);
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-value?idSensor="+idSensor)
                .addHeaders("Authorization","Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray values = response.getJSONArray("value");
                            String value;
                            String unit;
                            try {
                                value = values.getJSONObject(0).getString("value");
                                unit =  values.getJSONObject(0).getString("unit");
                                valueField.setText(value + unit);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getErrorCode();
                    }
                });
    }

}