package com.example.dell.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaActionSound;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.new_note){
            Intent intent = new Intent(getApplicationContext(),EditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView =(ListView) findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.example.dell.notes", Context.MODE_PRIVATE);
        HashSet<String> set =(HashSet<String>) sharedPreferences.getStringSet("notes",null);

        if(set == null){
                notes.add("Add new note");
        }else{
            notes = new ArrayList<>(set);
        }

        notes.add("Add a note");

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                intent.putExtra("notesID",i);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDel = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDel);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext()
                                        .getSharedPreferences("com.example.dell.notes", Context.MODE_PRIVATE);

                                HashSet<String> hashSet = new HashSet<>(MainActivity.notes);

                                sharedPreferences.edit().putStringSet("notes",hashSet).apply();

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });

    }
}
