package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AndroidNetworking.initialize(this);
    }

    public void register(View view) {
        //Réupération des informations du formulaire
        final EditText champNom = (EditText) findViewById(R.id.txt_name);
        String name = champNom.getText().toString();
        final EditText champEmail = (EditText) findViewById(R.id.txt_mail);
        String email = champEmail.getText().toString();
        final EditText champMdp = (EditText) findViewById(R.id.txt_password);
        String mdp = champMdp.getText().toString();

        //Connexion au site pour l'enregistrement d'un compte
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/register")
                .addBodyParameter("name", name)
                .addBodyParameter("login", email)
                .addBodyParameter("password", mdp)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                //si le compte se créer, alors on retourne à la page de connexion
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
                                break;
                            default:
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //si erreur lors de la connexion au site alors on affiche un message d'erreur
                        Toast toast = Toast.makeText(RegisterActivity.this, "Erreur", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }
}