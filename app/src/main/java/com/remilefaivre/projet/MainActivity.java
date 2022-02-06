package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickRegister(View view) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void onClickLogging(View view) {

        final EditText emailField = (EditText) findViewById(R.id.txt_mail);
        String email = emailField.getText().toString();

        final EditText passwordField = (EditText) findViewById(R.id.txt_password);
        String pwd = passwordField.getText().toString();

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/auth")
            .addBodyParameter("login", email)
            .addBodyParameter("password", pwd)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String token = response.getString("token");

                        Intent i = new Intent(MainActivity.this, RoomsActivity.class);
                        i.putExtra("token", token);
                        startActivity(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    Toast toast = Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_SHORT);
                    toast.show();
                    anError.getErrorCode();
                }
            });

    }
}