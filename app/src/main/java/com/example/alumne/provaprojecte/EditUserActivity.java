package com.example.alumne.provaprojecte;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText user,surname1,surname2,email,date,old,current,repeat;
    private Calendar calendar;
    private int year, month, day;
    static String[] dadesdata;
    Button edit, pass;
    private PassTask passTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user=(EditText)findViewById(R.id.user);
        surname1=(EditText)findViewById(R.id.surname1);
        surname2=(EditText)findViewById(R.id.surname2);
        email=(EditText)findViewById(R.id.email);
        date=(EditText)findViewById(R.id.date);
        old=(EditText)findViewById(R.id.oldpassword);
        current=(EditText)findViewById(R.id.password);
        repeat=(EditText)findViewById(R.id.repeat_password);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setDate(v);
                return false;
            }
        });
        calendar = Calendar.getInstance();
        BookTask bookTask=new BookTask();
        bookTask.execute();
        edit=(Button)findViewById(R.id.botoeditar);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTask editTask=new EditTask();
                editTask.execute();
                finish();
            }
        });
        pass=(Button)findViewById(R.id.changepass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }
    public void setDate(View view) {
        showDialog(999);
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
        date.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent=null;
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
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
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent=new Intent("android.intent.action.LoginActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(ProfileActivity.estat);
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
            ModifyActivity.intent=new Intent("android.intent.action.MainActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(ProfileActivity.estat);
            ModifyActivity.context.add(MainActivity.estat);
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent=new Intent("android.intent.action.ProfileActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(ProfileActivity.estat);
        } else if (id == R.id.nav_add) {
            startActivity(new Intent("android.intent.action.ModifyActivity"));
            ModifyActivity.intent=new Intent("android.intent.action.AddActivity");
            ModifyActivity.context.clear();
            ModifyActivity.context.add(this);
            ModifyActivity.context.add(ProfileActivity.estat);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void attemptLogin() {
        if (passTask != null) {
            return;
        }

        // Reset errors.
        old.setError(null);
        current.setError(null);
        repeat.setError(null);

        // Store values at the time of the login attempt.
        String oldpass = old.getText().toString();
        String currentpass = current.getText().toString();
        String repeatpassword = repeat.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(oldpass)) {
            old.setError(getString(R.string.error_field_required));
            focusView = old;
            cancel = true;
        }
        if (!currentpass.equals(repeatpassword)) {
            repeat.setError(getString(R.string.repeat_password));
            focusView=repeat;
            cancel=true;
        }
        if (TextUtils.isEmpty(currentpass)) {
            repeat.setError(getString(R.string.error_field_required));
            focusView = repeat;
            cancel = true;
        }
        if (TextUtils.isEmpty(repeatpassword)) {
            repeat.setError(getString(R.string.error_field_required));
            focusView = repeat;
            cancel = true;
        }
        if(!TextUtils.isEmpty(currentpass)&&currentpass.length()<=4){
            current.setError(getString(R.string.error_invalid_password));
            focusView = current;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            passTask = new PassTask();
            passTask.execute();
        }
    }

    public class BookTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades ;

        BookTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                String link = "http://projectedam2016.comxa.com/buscarusuari.php";
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
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                dades= result.split("'");
                dadesdata = dades[4].split("-");
                return true;
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            setTitle(dades[0]);
            user.setText(dades[0]);
            email.setText(dades[1]);
            surname1.setText(dades[2]);
            surname2.setText(dades[3]);
            day = Integer.parseInt(dadesdata[2]);
            month = Integer.parseInt(dadesdata[1]);
            year = Integer.parseInt(dadesdata[0]);
            showDate(year, month + 1, day);
        }
    }

    public class EditTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;
        String nom=user.getText().toString();
        String correu=email.getText().toString();
        String cognom1=surname1.getText().toString();
        String cognom2=surname2.getText().toString();
        String datas=date.getText().toString();

        EditTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String link = "http://projectedam2016.comxa.com/editarusuari.php";
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(nom, "UTF-8");
                data += "&" + URLEncoder.encode("surname1", "UTF-8") + "=" + URLEncoder.encode(cognom1, "UTF-8");
                data += "&" + URLEncoder.encode("surname2", "UTF-8") + "=" + URLEncoder.encode(cognom2, "UTF-8");
                data += "&" + URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(correu, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datas, "UTF-8");
                data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(LoginActivity.dades, "UTF-8");

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
    }
    public class PassTask extends AsyncTask<Void, Void, Boolean> {
        String result;
        String[] dades;
        String oldpass=old.getText().toString();
        String pass=current.getText().toString();


        PassTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String link = "http://projectedam2016.comxa.com/editapass.php";
                String data = URLEncoder.encode("oldpass", "UTF-8") + "=" + URLEncoder.encode(oldpass, "UTF-8");
                data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(LoginActivity.dades, "UTF-8");
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
            passTask = null;
            if (success) {
                finish();
            } else {
                old.setError(getString(R.string.error_incorrect_password));
                old.requestFocus();
            }
        }
    }
}
