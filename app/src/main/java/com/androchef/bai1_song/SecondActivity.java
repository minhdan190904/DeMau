package com.androchef.bai1_song;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    EditText etName, etSinger, etTime;
    Button btnBack, btnAdd;
    private static final int UPDATE_CODE = 365;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();

        btnBack.setOnClickListener(view -> {
            finish();
        });

        btnAdd.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String singer = etSinger.getText().toString();
            String time = etTime.getText().toString();
            if (name.isEmpty() || singer.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if(btnAdd.getText().toString() == "ADD"){
                Song song = new Song();
                song.setName(name);
                song.setSinger(singer);
                song.setTime(Double.parseDouble(time));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result_song", song);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                Song song = new Song();
                song.setId(id);
                song.setName(name);
                song.setSinger(singer);
                song.setTime(Double.parseDouble(time));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result_song", song);
                setResult(UPDATE_CODE, resultIntent);
                finish();
            }
        });

        Song song = (Song) getIntent().getSerializableExtra("selected_song");

        if (song != null) {
            etName.setText(song.getName());
            etTime.setText(song.getTime().toString());
            etSinger.setText(song.getSinger());
            btnAdd.setText("UPDATE");
            id = song.getId();
        }

    }

    private void initView() {
        etName = findViewById(R.id.etSongName);
        etSinger = findViewById(R.id.etSinger);
        etTime = findViewById(R.id.etTime);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
    }
}