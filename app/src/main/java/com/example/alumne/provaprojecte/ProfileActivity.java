package com.example.alumne.provaprojecte;

import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nom, correu, cognom1, cognom2, data,llistabuida;
    ListView llistallibres;
    NewAdapter adaptador;
    static ArrayList<Llibre> llibres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nom = (TextView) findViewById(R.id.NomTextData);
        correu = (TextView) findViewById(R.id.CorreuTextData);
        llistallibres = (ListView) findViewById(R.id.listView2);
        llistabuida=(TextView)findViewById(R.id.EmptyList);
        llibres = new ArrayList<>();
        adaptador = new NewAdapter(this, R.layout.item_list, R.id.llibre_name, llibres);
        llistallibres.setAdapter(adaptador);
        llistallibres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OwnedActivity.id = llibres.get(position).getId();
                startActivity(new Intent("android.intent.action.OwnedActivity"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent("android.intent.action.SettingsActivity"));
            return true;
        } else if (id == R.id.action_logout) {
            startActivity(new Intent("android.intent.action.LoginActivity"));
            finish();
            ((Activity) MainActivity.estat).finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            finish();
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent("android.intent.action.ProfileActivity"));
            finish();
        } else if (id == R.id.nav_add) {
            startActivity(new Intent("android.intent.action.AddActivity"));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProfileTask profileTask = new ProfileTask();
        profileTask.execute();
        ListTask listTask = new ListTask();
        listTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProfileTask profileTask = new ProfileTask();
        profileTask.execute();
        ListTask listTask = new ListTask();
        listTask.execute();

    }

    public class ProfileTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;

        ProfileTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                String link = "http://projectedam2016.comxa.com/buscarusuari.php";
                String id = LoginActivity.dades.get(0);
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                dades = result.split(" ");
                return true;
            } catch (Exception e) {
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            nom.setText("  " + dades[0]);
            correu.setText("  " + dades[1]);
            adaptador.notifyDataSetChanged();

        }

    }

    public class ListTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        ListTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                String link = "http://projectedam2016.comxa.com/buscallibresusuari.php";
                String id = LoginActivity.dades.get(0);
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                llibres.clear();
                result = sb.toString();
                String[] dades = result.split(" ");
                for (int i = 0; i < dades.length; i += 3) {
                    Llibre llibre = new Llibre();
                    llibre.setNom(dades[i]);
                    llibre.setAutor(dades[i + 1]);
                    llibre.setId(dades[i + 2]);
                    llibres.add(llibre);
                }
                return true;
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            adaptador.notifyDataSetChanged();
            if(llibres.isEmpty()){
                llistallibres.setVisibility(View.GONE);
                llistabuida.setVisibility(View.VISIBLE);
            }else{
                llistallibres.setVisibility(View.VISIBLE);
                llistabuida.setVisibility(View.GONE);
            }
        }
    }

}
