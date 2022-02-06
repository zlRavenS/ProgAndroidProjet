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

public class AddPeripheriqueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_peripherique);

        loadDeviceType();
    }

    public void onClickValider(View view) {

        // Récupération du token et de l'ID de la Room
        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String idRoom = intent.getStringExtra("idRoom");

        // Récupération du nom du nouveau périphérique
        EditText newNameField = (EditText) findViewById(R.id.new_name_text);
        String newName = String.valueOf(newNameField.getText());

        // Récupération du type du nouveau périphérique
        Spinner newTypeField = (Spinner) findViewById(R.id.new_type_spinner);
        DeviceType newType = (DeviceType) newTypeField.getSelectedItem();

        // Connexion au site pour créer un périphérique
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name", newName)
                .addBodyParameter("idDeviceType",""+newType.getId())
                .addBodyParameter("idRoom",""+idRoom)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                // Retour sur l'activité précédente en récupérant le nouveau périphérique
                                Intent i = new Intent(AddPeripheriqueActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);

                                startActivity(i);
                                break;
                            default:
                                // Si erreur, affichage d'un Toast
                                Toast toastError = Toast.makeText(AddPeripheriqueActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(AddPeripheriqueActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });
    }

    public void loadDeviceType() {
        Context that = this;

        // Connexion au site pour récupérer nos types de périphériques
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/device-types")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Listage de tous les types de périphériques
                            JSONArray deviceTypes = response.getJSONArray("device-types");

                            ArrayList<DeviceType> typeList = new ArrayList<>();

                            // Pour chaque type de périphériques
                            for(int iType = 0; iType < deviceTypes.length(); ++iType)
                            {
                                final JSONObject type = deviceTypes.getJSONObject(iType);
                                typeList.add(new DeviceType(
                                        type.getInt("id"),
                                        type.getString("name"),
                                        type.getString("picture")
                                ));
                            }

                            // Affichage des types de périphériques dans un Spinner grâce à un Adapter
                            ArrayAdapter<DeviceType> adapter = new ArrayAdapter<>(
                                    that,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    typeList
                            );

                            Spinner spinnerType = findViewById(R.id.new_type_spinner);

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