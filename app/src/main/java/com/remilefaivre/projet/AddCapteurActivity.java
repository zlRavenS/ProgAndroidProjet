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

        loadSensorType();
    }

    public void onClickValider(View view) {

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String idRoom = intent.getStringExtra("idRoom");

        EditText newNameField = (EditText) findViewById(R.id.new_name_text);
        String newName = String.valueOf(newNameField.getText());

        Spinner newTypeField = (Spinner) findViewById(R.id.new_type_spinner);
        SensorType newType = (SensorType) newTypeField.getSelectedItem();

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/sensor-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name", newName)
                .addBodyParameter("idPicture","2")
                .addBodyParameter("idSensorType",""+newType.getId())
                .addBodyParameter("idRoom",""+idRoom)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                Intent i = new Intent(AddCapteurActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);

                                startActivity(i);
                                break;
                            default:
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

    public void loadSensorType() {
        Context that = this;

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-types")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray sensorTypes  = response.getJSONArray("sensor-types");

                            ArrayList<SensorType> typeList = new ArrayList<>();
                            // Pour chaque pizza
                            for(int iType = 0; iType < sensorTypes.length(); ++iType)
                            {
                                final JSONObject type = sensorTypes.getJSONObject(iType);
                                typeList.add(new SensorType(
                                        type.getInt("id"),
                                        type.getString("name"),
                                        type.getString("picture")
                                ));
                            }

                            ArrayAdapter<SensorType> adapter = new ArrayAdapter<>(
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