package com.ass.sd2550project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner mSpinnerCards;
    Button mBtnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinnerCards = findViewById(R.id.spinner_card_count);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cards = mSpinnerCards.getSelectedItem().toString();
                String s = cards.substring(0, cards.length() - 6);
                int cardCount = Integer.valueOf(s);
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("CardCount", cardCount);
                startActivity(i);
            }
        });
    }
}
