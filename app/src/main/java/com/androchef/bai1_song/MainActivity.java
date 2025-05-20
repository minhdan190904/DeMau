package com.androchef.bai1_song;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListAdapter listAdapter;
    private FloatingActionButton btnAdd;
    private ListView listView;
    private SqlHelper sqlHelper;
    private List<Song> songs;
    private static final int UPDATE_CODE = 365;
    private EditText etSearch;


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Song song = (Song) data.getSerializableExtra("result_song");
                        // Dùng user object
                        if (song != null) {
                            sqlHelper.insertSong(song);
                            updateUI();
                        }
                    }
                } else if(result.getResultCode() == UPDATE_CODE){
                    Intent data = result.getData();
                    if (data != null) {
                        Song song = (Song) data.getSerializableExtra("result_song");
                        // Dùng user object
                        if (song != null) {
                            sqlHelper.updateSong(song);
                            updateUI();
                        }
                    }
                }
            }
    );

    public void updateUI() {
        songs = sqlHelper.getAllSongOrder();
        songs.sort(Comparator.comparing(Song::getTime));
        listAdapter.updateList(songs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            launcher.launch(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listAdapter.searchSong(etSearch.getText().toString(), songs);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Song song = songs.get(position);
            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.pop_up_menu_song, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.update) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("selected_song", song);
                    launcher.launch(intent);
                    return true;
                } else if (itemId == R.id.delete) {
                    sqlHelper.deleteSong(song.getId());
                    updateUI();
                    return true;
                }
                return false;

            });
            popup.show();
            return true;
        });

    }

    private void init() {
        btnAdd = findViewById(R.id.btnAdd);
        etSearch = findViewById(R.id.etSearch);
        listView = findViewById(R.id.listView);
        sqlHelper = new SqlHelper(MainActivity.this);
        songs = sqlHelper.getAllSongOrder();
        songs.sort(Comparator.comparing(Song::getTime));
        listAdapter = new ListAdapter(MainActivity.this, songs);
        listView.setAdapter(listAdapter);
    }
}