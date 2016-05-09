package com.example.alumne.provaprojecte;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class NotOwnedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView titol, autor,any, isbn;
    ImageView imageView;
    static Llibre llibre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_owned);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=(ImageView)findViewById(R.id.imageBook);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        titol=(TextView)findViewById(R.id.TitolTextData);
        autor=(TextView)findViewById(R.id.AutorTextData);
        any=(TextView)findViewById(R.id.AnyTextData);
        isbn= (TextView)findViewById(R.id.ISBNTextData);
        llibre=new Llibre();
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
        BookTask bookTask=new BookTask();
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
            if(LoginActivity.user) {
                startActivity(new Intent("android.intent.action.ProfileActivity"));
                finish();
            }else{
                startActivity(new Intent("android.intent.action.VisitorActivity"));}
        } else if(id==R.id.nav_add){
            if(LoginActivity.user) {
                startActivity(new Intent("android.intent.action.AddActivity"));
                finish();
            }
            else{
                startActivity(new Intent("android.intent.action.VisitorActivity"));}
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//Aquesta classe permet la lectura de les dades del llibre
    public class BookTask extends AsyncTask<Void, Void, Boolean> {
        String result;

        BookTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Agafa la id del llibre de l'activitat anterior i cerca a la base de dades per recollir les dades

            try {

                String link = "http://projectedam2016.comxa.com/buscallibrescodi.php";
                String id=MainActivity.idllibre;
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

                //Llegeix unicament el llibre amb aquella id i passa les dades per fer-les objecte
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                String[] dades = result.split("'");
                llibre.setNom(dades[0]);
                llibre.setAutor(dades[1]);
                llibre.setIsbn(dades[3]);
                String[] dadesdata=dades[2].split("-");
                llibre.setAny(dadesdata[2] + "/" + dadesdata[1] + "/" + dadesdata[0]);
                llibre.setImatge(Base64.decode((dades[4]), Base64.DEFAULT));
                return true;
            } catch (Exception e) {
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
                //Amb les dades de la base de dades omple els camps per donar informació del llibre
            setTitle(llibre.getNom());
            titol.setText("  " + llibre.getNom());
            autor.setText("  " +llibre.getAutor());
            any.setText("  " +llibre.getAny());
            isbn.setText("  " +llibre.getIsbn());
            imageView.setImageBitmap(BitmapFactory
                    .decodeByteArray(llibre.getImatge(),0,llibre.getImatge().length));

        }

    }

}
