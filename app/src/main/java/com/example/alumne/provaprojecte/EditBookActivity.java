package com.example.alumne.provaprojecte;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

public class EditBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText isbn, titol, autor, data;
    static Llibre llibre;
    private Calendar calendar;
    private int year, month, day;
    private static int RESULT_LOAD_IMG = 1;
    static String[] dadesdata;
    ImageView image;
    String imgDecodableString;
    byte[] imatge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        isbn = (EditText) findViewById(R.id.ISBNText);
        titol = (EditText) findViewById(R.id.TitolText);
        autor = (EditText) findViewById(R.id.AutorText);
        data = (EditText) findViewById(R.id.AnyText);
        calendar = Calendar.getInstance();
        llibre = new Llibre();
        data.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setDate(v);
                return false;
            }
        });
        image = (ImageView) findViewById(R.id.imageEdit);
        Button edita = (Button) findViewById(R.id.editbookbutton);
        edita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTask editTask = new EditTask();
                editTask.execute();
            }
        });
        BookTask bookTask = new BookTask();
        bookTask.execute();
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                image.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                //Conversio a bytearray
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ((BitmapDrawable) image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imatge = stream.toByteArray();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = null;
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        data.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = new Intent("android.intent.action.LoginActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(OwnedActivity.estat);
            ModifyActivity.context.add(MainActivity.estat);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_search) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = new Intent("android.intent.action.MainActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(OwnedActivity.estat);
            ModifyActivity.context.add(MainActivity.estat);

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = new Intent("android.intent.action.ProfileActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(OwnedActivity.estat);
        } else if (id == R.id.nav_add) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = new Intent("android.intent.action.AddActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(OwnedActivity.estat);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Aquesta classe llista les dades del llibre a canviar
    public class BookTask extends AsyncTask<Void, Void, Boolean> {
        String result;


        BookTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Aqui es pasa el codi per obtenir el llibre

            try {
                
                String link = "http://projectedam2016.comxa.com/buscallibrescodi.php";
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(OwnedActivity.id, "UTF-8");
                System.out.println(OwnedActivity.id);
                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                //A partir d'aqui es llegeix la resposta del php
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                //Es separen les strings de resposta en per crear l'objecte de tipus llibre
                String[] dades = result.split("'");
                llibre.setNom(dades[0]);
                llibre.setAutor(dades[1]);
                llibre.setIsbn(dades[3]);
                dadesdata = dades[2].split("-");
                llibre.setAny(dadesdata[2] + "/" + dadesdata[1] + "/" + dadesdata[0]);
                llibre.setImatge(Base64.decode((dades[4]), Base64.DEFAULT));
                return true;
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //Un cop acabat  omple els EditText i el titol del activity amb les seves dades
            setTitle(llibre.getNom());
            titol.setText(llibre.getNom());
            autor.setText(llibre.getAutor());
            day = Integer.parseInt(dadesdata[2]);
            month = Integer.parseInt(dadesdata[1]);
            year = Integer.parseInt(dadesdata[0]);
            isbn.setText(llibre.getIsbn());
            showDate(year, month + 1, day);
            image.setImageBitmap(BitmapFactory
                    .decodeByteArray(llibre.getImatge(), 0, llibre.getImatge().length));
        }
    }
    //Aquesta classe permet editar els llibres
    public class EditTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;
        //Es guarden els textos dels EditText com a l'activitar de creació
        String isbnt = isbn.getText().toString();
        String namet = titol.getText().toString();
        String authort = autor.getText().toString();
        String date = data.getText().toString();
        String imageb;

        EditTask() {
            //Com aqui no pot no haver-hi una imatge directament es passa a String per poder retornar-la al php
            imageb = Base64.encodeToString(imatge, Base64.DEFAULT);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Es passa el parametre que edentifica el llibre per poder modificar-lo amb les dades que hi hagi a l'activity en aquell moment

            try {
                String link = "http://projectedam2016.comxa.com/editarllibre.php";
                String data = URLEncoder.encode("isbn", "UTF-8") + "=" + URLEncoder.encode(isbnt, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namet, "UTF-8");
                data += "&" + URLEncoder.encode("author", "UTF-8") + "=" + URLEncoder.encode(authort, "UTF-8");
                data += "&" + URLEncoder.encode("publdate", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("idbook", "UTF-8") + "=" + URLEncoder.encode(OwnedActivity.id, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data + "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imageb, "UTF-8"));
                wr.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            finish();
        }

    }
}
