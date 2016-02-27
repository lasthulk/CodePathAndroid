package com.tam.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    private  final int REQUEST_CODE = 18;
    private int selectedIndexOfItem = -1;
    private ArrayList<String> todoItems;
    //private ArrayList<Task> todoItems;
    //private ArrayAdapter<String> todoAdapter;
    private ArrayAdapter<String> todoAdapter;
    private ListView lvItem;
    private EditText etEditText;
    private ToDoItemsDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        poputlateArrayItem();
        dbHelper = ToDoItemsDbHelper.getInstance(this);
        lvItem = (ListView)findViewById(R.id.lvItems);
        lvItem.setAdapter(todoAdapter);
        etEditText =(EditText)findViewById(R.id.etEditText);
        lvItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                todoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedText = lvItem.getItemAtPosition(position).toString();
                selectedIndexOfItem = position;
                //Toast.makeText(MainActivity.this, "Cur Index: " + String.valueOf(selectedIndexOfItem), Toast.LENGTH_LONG).show();
                // open form edit
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("itemText", selectedText);
                intent.putExtra("itemPosition", position);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemText = data.getExtras().getString("itemText");
//            Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, String.valueOf(selectedIndexOfItem), Toast.LENGTH_SHORT).show();
            todoItems.set(selectedIndexOfItem, itemText);
            todoAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void poputlateArrayItem() {
        todoItems = new ArrayList<String>();
//        todoItems.add("Item 1");
//        todoItems.add("Item 2");
//        todoItems.add("Item 3");
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onAddItem(View view) {
        //Toast.makeText(MainActivity.this, "kek", Toast.LENGTH_LONG).show();
        todoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }
}
