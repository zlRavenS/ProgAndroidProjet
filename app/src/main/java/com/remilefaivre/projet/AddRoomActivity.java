package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import okhttp3.Response;

public class AddRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
    }

    public void onClickValider(View view) {
        Context that = this;

        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        EditText newNameField = (EditText) findViewById(R.id.new_name_capteur);
        String newName = String.valueOf(newNameField.getText());

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name", newName)
                .addBodyParameter("idPicture","2")
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                Intent i = new Intent(AddRoomActivity.this, RoomsActivity.class);
                                i.putExtra("token", token);
                                startActivity(i);
                                break;
                            default:
                                Toast toastError = Toast.makeText(AddRoomActivity.this, "Erreur " + response.code(), Toast.LENGTH_SHORT);
                                toastError.show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toastError = Toast.makeText(AddRoomActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toastError.show();
                    }
                });
    }
}
