package com.example.alumne.provaprojecte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

public class OwnedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView titol, autor, any, isbn;
    static Llibre llibre;
    public static String id;
    public static Context estat;
    Button edita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        estat=this;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        titol = (TextView) findViewById(R.id.TitolTextData);
        autor = (TextView) findViewById(R.id.AutorTextData);
        any = (TextView) findViewById(R.id.AnyTextData);
        isbn = (TextView) findViewById(R.id.ISBNTextData);
        llibre = new Llibre();
        edita=(Button)findViewById(R.id.button);
        edita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.EditBookActivity"));
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
    protected void onStart() {
        super.onStart();
        BookTask bookTask = new BookTask();
        bookTask.execute();

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

    public class BookTask extends AsyncTask<Void, Void, Boolean> {
        String result;

        BookTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                String link = "http://projectedam2016.comxa.com/buscallibrescodi.php";
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
                String[] dades = result.split(" ");
                llibre.setNom(dades[0]);
                llibre.setAutor(dades[1]);
                llibre.setIsbn(dades[3]);
                String[] dadesdata=dades[2].split("-");
                llibre.setAny(dadesdata[2]+"/"+dadesdata[1]+"/"+dadesdata[0]);
                return true;
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            setTitle(llibre.getNom());
            titol.setText("  " + llibre.getNom());
            autor.setText("  " + llibre.getAutor());
            any.setText("  " + llibre.getAny());
            isbn.setText("  " + llibre.getIsbn());
        }

    }

}
