package com.michaell.alavedra.monitorupao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.michaell.alavedra.monitorupao.utils.RecyclerViewMenuAdapter;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar mToolBar = findViewById(R.id.myToolBar);
        RecyclerView rvMenuItems = findViewById(R.id.rvMenuItems);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Menú de opciones");
        rvMenuItems.setLayoutManager(new LinearLayoutManager(this));
        rvMenuItems.setAdapter(new RecyclerViewMenuAdapter(this, getIntent().getStringExtra("id_alumno")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itmInformacionMenu:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
