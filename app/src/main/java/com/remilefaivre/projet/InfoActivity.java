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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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

        // Récupération des infos de la RoomActivity
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String titreRoom = tokenI.getStringExtra("nameRoom");

        // Changement du nom de la pièce dans le layout
        TextView titre = findViewById(R.id.roomName);
        titre.setText(titreRoom);

        loadCapteurs(token, idRoom);
        loadPeripheriques(token, idRoom);

    }



    public void onClickAjouterCapteur(View view) {
        // Récupération du token et de l'ID de la pièce ainsi que de son nom
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String titreRoom = tokenI.getStringExtra("nameRoom");

        // Lancement de l'activité AddCapteurActivity et donne en extra le token et l'ID de la pièce
        Intent i = new Intent(InfoActivity.this, AddCapteurActivity.class);
        i.putExtra("token", token);
        i.putExtra("idRoom", ""+idRoom);
        i.putExtra("nameRoom", titreRoom);
        startActivity(i);
    }

    public void onClickSupprimerCapteur(View view) {
        // Récupération du token et de l'ID de la pièce ainsi que de son nom
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String titreRoom = tokenI.getStringExtra("nameRoom");

        // Création de l'ID du capteur afin que le capteur sélectionné soit supprimé
        int idSensor = (int) view.getTag();

        // Connexion au site afin de supprimer notre capteur à partir de son ID récupéré précédemment
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
                                i.putExtra("nameRoom", titreRoom);
                                startActivity(i);
                                break;
                            default:
                                // S'il n'a pas été supprimé, affiche un Toast
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

        // Récupère le ListView du layout où seront affichés les capteurs
        ListView listCapteurs = findViewById(R.id.liste_capteurs);

        // Connexion au site afin de récupérer les capteurs associés à l'ID de notre pièce sélectionnée précédemment
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensors?idRoom="+idRoom)
                .addHeaders("Authorization","Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray capteurs = response.getJSONArray("sensors");

                            // Affiche les capteurs dans le ListView à parti du modèle capteur_item.xml
                            CapteurAdapter capteurAdapter = new CapteurAdapter(that,R.layout.capteur_item, capteurs, token);
                            listCapteurs.setAdapter(capteurAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Si erreur, renvoie un Toast
                        Toast toastError = Toast.makeText(that,anError.getErrorBody(),Toast.LENGTH_SHORT);
                        toastError.show();
                        anError.getErrorCode();
                    }
                });
    }



    public void loadPeripheriques(String token, String idRoom) {
        Context that = this;

        // Récupère le ListView du layout où seront affichés les périphériques
        ListView listPeripheriques = findViewById(R.id.liste_peripheriques);

        // Connexion au site afin de récupérer les périphériques associés à l'ID de notre pièce sélectionnée précédemment
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/devices?idRoom=" + idRoom)
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray peripheriques = response.getJSONArray("devices");

                            // Affiche les périphériques dans le ListView à parti du modèle peripherique_item.xml
                            PeripheriqueAdapter peripheriqueAdapter = new PeripheriqueAdapter(that, R.layout.peripherique_item, peripheriques);
                            listPeripheriques.setAdapter(peripheriqueAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Si erreur, renvoie un Toast
                        Toast toastError = Toast.makeText(that, anError.getErrorBody(), Toast.LENGTH_SHORT);
                        toastError.show();
                        anError.getErrorCode();
                    }
                });
    }

    public void onClickAjouterPeripherique(View view) {
        // Récupération du token et de l'ID de la pièce
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String titreRoom = tokenI.getStringExtra("nameRoom");

        // Lancement de l'activité AddPeripheriqueActivity et donne en extra le token et l'ID de la pièce
        Intent i = new Intent(InfoActivity.this, AddPeripheriqueActivity.class);
        i.putExtra("token", token);
        i.putExtra("idRoom", ""+idRoom);
        i.putExtra("nameRoom", titreRoom);
        startActivity(i);
    }

    public void onClickSupprimerPeripherique(View view) {
        // Récupération du token et de l'ID de la pièce
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String titreRoom = tokenI.getStringExtra("nameRoom");

        // Création de l'ID du périphérique afin que le périphérique sélectionné soit supprimé
        int idDevice = (int) view.getTag();

        // Connexion au site afin de supprimer notre périphérique à partir de son ID récupéré précédemment
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-delete")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idDevice",""+idDevice)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                // Actualisation de notre Activité pour afficher la suppression de notre périphérique
                                Intent i = new Intent(InfoActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);
                                i.putExtra("nameRoom", titreRoom);
                                startActivity(i);
                                break;
                            default:
                                // S'il n'a pas été supprimé, affiche un Toast
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
        // Récupération du token et de l'ID de la pièce ainsi que de son nom
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String idRoom = tokenI.getStringExtra("idRoom");
        String titreRoom = tokenI.getStringExtra("nameRoom");

        // Création de l'ID du périphérique afin que l'état du périphérique sélectionné soit modifié
        int idDevice = (int) view.getTag();

        // Récupération de l'état du Switch
        boolean on = ((Switch) view).isChecked();
        int etat;
        if(on) {
            etat = 1;
        } else {
            etat = 0;
        }

        // Connexion au site afin de modifier l'état de notre périphérique à partir de son ID récupéré précédemment
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-status")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("idDevice",""+idDevice)
                .addBodyParameter("status",""+etat)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                // Actualisation de notre Activité pour afficher l'état de notre périphérique
                                Intent i = new Intent(InfoActivity.this, InfoActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("idRoom", idRoom);
                                i.putExtra("nameRoom", titreRoom);
                                startActivity(i);
                                break;
                            default:
                                // S'il n'a pas été modifié, affiche un Toast
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


    public void onClickReturnRooms(View view) {
        // Récupération du token
        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");

        // Retour sur l'activité précédente en récupérant les pièces déjà crées pour notre compte
        Intent i = new Intent(InfoActivity.this, RoomsActivity.class);
        i.putExtra("token", token);
        startActivity(i);
    }
}