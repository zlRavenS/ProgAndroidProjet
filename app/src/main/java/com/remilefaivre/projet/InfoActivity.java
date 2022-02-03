package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String id = tokenI.getStringExtra("id");

        loadCapteurs(token, id);
    }

    public void loadCapteurs(String token, String id) {
        Context that = this;

        ListView listCapteurs = findViewById(R.id.liste_capteurs);


        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensors?idRoom="+id)
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
}