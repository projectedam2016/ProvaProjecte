package com.example.alumne.provaprojecte;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

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
    }
    public class PassTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;



        PassTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String link = "http://projectedam2016.comxa.com/editapass.php";
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(LoginActivity.dades, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while(!(line = reader.readLine()).equals("")) {
                    if(line.equals("1")) {
                        return true;
                    }if(line.equals("0"))
                        return false;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
            } else {
            }
        }
    }
}
