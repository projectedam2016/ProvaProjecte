package com.example.alumne.provaprojecte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ModifyActivity extends Activity {
    public static Intent intent;
    public static ArrayList<Context> context=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Button buttonok=(Button)findViewById(R.id.buttonOK);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Context c:context) {
                    ((Activity)c).finish();
                    startActivity(intent);
                }
                finish();
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

}
