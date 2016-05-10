package com.example.alumne.provaprojecte;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DeleteBookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);
        Button buttonok=(Button)findViewById(R.id.buttonOK);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteTask deleteTask=new DeleteTask();
                deleteTask.execute();
            }
        });
        Button buttonlog=(Button)findViewById(R.id.buttonLog);
        buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //Aquesta classe connecta amb la base de dades i borra el llibre
    public class DeleteTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;

        DeleteTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Aquest metode passa la id del llibre per borrar el llibre
            try {
                String link = "http://projectedam2016.comxa.com/borrallibre.php";
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(OwnedActivity.id, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            //Si connecta amb la base de dades acaba l'activity junt amb la pàgina anterior on es veien les dades del llibre
            if (success) {
                ((Activity)OwnedActivity.estat).finish();
                finish();
            } else {
            }
        }
    }
}
