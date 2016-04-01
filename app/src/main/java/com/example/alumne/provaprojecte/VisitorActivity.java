package com.example.alumne.provaprojecte;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VisitorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        Button buttonok=(Button)findViewById(R.id.buttonOK);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
}
