package com.example.alumne.provaprojecte;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Context estat;
    private Calendar calendar;
    private EditText dateView, isbn, title, author;
    private int year, month, day;
    Button registra;
    private AddTask addTask = null;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    ImageView imgView;
    byte[] imatge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
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
        dateView = (EditText) findViewById(R.id.publishdate);
        isbn = (EditText) findViewById(R.id.isbn);
        title = (EditText) findViewById(R.id.title);
        author = (EditText) findViewById(R.id.author);
        registra = (Button) findViewById(R.id.register_form_button);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
        imgView = (ImageView) findViewById(R.id.imgView);
        dateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setDate(v);
                return false;
            }
        });
        registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campNo();
            }
        });
        imatge=null;
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
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                //Conversio a bytearray
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ((BitmapDrawable) imgView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
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
            ModifyActivity.context.add(MainActivity.estat);
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = new Intent("android.intent.action.ProfileActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
        } else if (id == R.id.nav_add) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent = new Intent("android.intent.action.AddActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    private void campNo() {
        if (addTask != null) {
            return;
        }

        // Reset errors.
        title.setError(null);
        isbn.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError(getString(R.string.error_field_required));
            focusView = title;
            cancel = true;
        }
        // Check for a valid email address.

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            addTask = new AddTask();
            addTask.execute();
            finish();
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
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class AddTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;
        String isbnt = isbn.getText().toString();
        String namet = title.getText().toString();
        String authort = author.getText().toString();
        String date = dateView.getText().toString();
        String image;

        AddTask() {
            if (imatge!=null)
                image = Base64.encodeToString(imatge, Base64.DEFAULT);
            else {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap imagen= ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.icona,null)).getBitmap();
                imagen.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imatge = stream.toByteArray();
                image= Base64.encodeToString(imatge,Base64.DEFAULT);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String link = "http://projectedam2016.comxa.com/creallibre.php";
                String id = LoginActivity.dades;
                String data = URLEncoder.encode("idbook", "UTF-8") + "=" + URLEncoder.encode(isbnt, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namet, "UTF-8");
                data += "&" + URLEncoder.encode("author", "UTF-8") + "=" + URLEncoder.encode(authort, "UTF-8");
                data += "&" + URLEncoder.encode("publdate", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("iduser", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
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
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

    }

}
