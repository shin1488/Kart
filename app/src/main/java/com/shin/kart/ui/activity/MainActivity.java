package com.shin.kart.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shin.kart.R;
import com.shin.kart.data.KartApi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button getButton;
    public EditText editText;
    public String userData;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        getButton = (Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.edittext);

        getButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == getButton) {
            Thread thread = new Thread() {
                public void run() {
                    KartApi api = new KartApi(editText.getText().toString(), context);
                    api.main();
                    userData = api.responseBody;
                    Log.d("응애", userData);
                }
            };
            thread.start();
        }
    }
}