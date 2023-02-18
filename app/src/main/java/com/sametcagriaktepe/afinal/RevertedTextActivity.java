package com.sametcagriaktepe.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RevertedTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverted_texts);

        TextView reversedText = findViewById(R.id.reversedText);

        String text = getIntent().getStringExtra("keyname");
        reversedText.setText(text);


    }
}