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

public class AddCapteurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_capteur);

        loadCapteurType();
    }

    public void onClickValider(View view) {
        // Récupération du token et de l'ID de la Room
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String nameRoom = tokenI.getStringExtra("nameRoom");

        // Récupération du nom du nouveau capteur
        EditText newNameField = (EditText) findViewById(R.id.new_name_room);
        String newName = String.valueOf(newNameField.getText());

        // Récupération du type du nouveau capteur
        Spinner newTypeField = (Spinner) findViewById(R.id.new_type_peripherique);
        SensorType newType = (SensorType) newTypeField.getSelectedItem();


        // Connexion au site pour créer un capteur
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/sensor-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name", newName)
                .addBodyParameter("idSensorType",""+newType.getId())
                .addBodyParameter("idRoom",""+idRoom)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                // Retour sur l'activité précédente en récupérant le nouveau capteur
                                Intent i = new Intent(AddCapteurActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);
                                i.putExtra("nameRoom", nameRoom);

                                startActivity(i);
                                break;
                            default:
                                // Si erreur, affichage d'un Toast
                                Toast toastError = Toast.makeText(AddCapteurActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(AddCapteurActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });
    }

    public void loadCapteurType() {
        Context that = this;

        // Connexion au site pour récupérer nos types de capteurs
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-types")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Listage de tous les types de capteurs
                            JSONArray types = response.getJSONArray("sensor-types");

                            // Pour chaque type de capteurs
                            ArrayList<SensorType> typeList = new ArrayList<>();

                            for(int iType = 0; iType < types.length(); ++iType)
                            {
                                final JSONObject type = types.getJSONObject(iType);
                                typeList.add(new SensorType(
                                        type.getInt("id"),
                                        type.getString("name"),
                                        type.getString("picture")
                                ));
                            }

                            // Affichage des types de capteurs dans un Spinner grâce à un Adapter
                            ArrayAdapter<SensorType> adapter = new ArrayAdapter<>(
                                    that,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    typeList
                            );

                            Spinner spinnerType = findViewById(R.id.new_type_peripherique);

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