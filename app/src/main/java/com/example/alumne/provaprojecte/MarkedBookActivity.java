package com.example.alumne.provaprojecte;

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
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MarkedBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static Context estat;
    ListView llistaMarcats;
    AdapterMarked adaptador;
    TextView llistabuida;
    static ArrayList<Marked> marked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marked_book);
        estat = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        llistaMarcats = (ListView) findViewById(R.id.listOwnMarked);
        marked = new ArrayList<>();
        adaptador = new AdapterMarked(this, R.layout.item_marked, R.id.marked_llibre, marked);
        llistaMarcats.setAdapter(adaptador);
        llistaMarcats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotOwnedActivity.llibre.setUsuari(marked.get(position).getIdUsuari());
                startActivity(new Intent("android.intent.action.UserActivity"));
            }
        });

        llistabuida = (TextView) findViewById(R.id.EmptyList);
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
            if (LoginActivity.user) {
                startActivity(new Intent("android.intent.action.LoginActivity"));
                finish();
            } else {
                startActivity(new Intent("android.intent.action.VisitorActivity"));
            }
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
        }else if (id == R.id.nav_marked) {
            startActivity(new Intent("android.intent.action.MarkedBookActivity"));
            finish();
        }else if (id == R.id.nav_own_marked) {
            startActivity(new Intent("android.intent.action.OwnedMarkedActivity"));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        ListTask task = new ListTask();
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListTask task = new ListTask();
        task.execute();
    }
    //La classe llegeix les dades necessaries de la base de dades per mostrar-les i per ús posterior de l'aplicació
    public class ListTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        ListTask() {
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            //Llegeix tots els llibres de l'aplicació
            try {
                String link = "http://projectedam2016.comxa.com/buscallibres.php";
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                //Aqui repasa tota la resposta de la query i la separa per fer diver-sos llibres que mostrar
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                String[] dades = result.split("'");
                marked.clear();
                //i els junta en la llista que es mostra desprès a l'aplicació
                for (int i = 0; i < dades.length; i += 5) {
                    Marked mark = new Marked();
                    mark.setTitol(dades[i]);
                    mark.setUsuari(dades[i + 1]);
                    mark.setAceptat(dades[i + 2]);
                    mark.setIdUsuari("a");
                    mark.setIdLlibre("a");
                    mark.setIdPropietari("a");
                    mark.setImatge(Base64.decode((dades[i + 4]), Base64.DEFAULT));
                    marked.add(mark);
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //Al acabar notifica el canvi de la llista al adaptador
            adaptador.notifyDataSetChanged();
        }
    }
}
