package com.tam.todoapp;

//import android.app.FragmentManager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddEditTaskDialog.AddEditTaskDialogListener {

    private  final int REQUEST_CODE = 18;
    private int selectedIndexOfItem = -1;
    //private ArrayList<String> todoItems;
    //private ArrayAdapter<String> todoAdapter;
    private List<Task> tasksList;
    private TasksAdapter tasksAdapter;
    private ListView lvItem;
    private EditText etEditText;
    private ToDoItemsDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = ToDoItemsDbHelper.getInstance(this);
        lvItem = (ListView)findViewById(R.id.lvItems);
        populateTasks();
        lvItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Task curentTask = (Task)lvItem.getItemAtPosition(position);
                    dbHelper.deleteTask(curentTask.getId());
                    tasksList.remove(curentTask);
                    tasksAdapter.notifyDataSetChanged();
                    //populateTasks();
                    //Log.d("omg", "song");
                    return true;
                } catch (Exception ex) {
                    showMessage(ex.getMessage());
                    //Log.d("omg", "chet");
                    return false;
                }
            }
        });

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // use dialog
                try {
                    Task selectedTask = (Task)lvItem.getItemAtPosition(position);
                    selectedIndexOfItem = position;
                    showEditTaskDialog(selectedTask);
                } catch (Exception ex) {
                    showMessage(ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onFinishEditingTask() {
        this.tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishAddingTask(Task task) {
        this.tasksList.add(task);
        this.tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddTask:
            {
                showAddTaskDialog();
                return true;
            }
            default:
                return  false;
        }
    }

    private void showAddTaskDialog() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            AddEditTaskDialog taskDialog = AddEditTaskDialog.newInstance(null);
            taskDialog.setTitle("Add task");
            taskDialog.show(fm, "Todo");
        } catch (Exception ex) {
            showMessage(ex.getMessage());
        }
    }

    private void showEditTaskDialog(Task task) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            AddEditTaskDialog taskDialog = AddEditTaskDialog.newInstance(task);
            taskDialog.setTitle("Edit task");
            taskDialog.show(fm, "Todo");
        } catch (Exception ex) {
            showMessage(ex.getMessage());
        }
    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void populateTasks() {
        try {
            this.tasksList = new ArrayList<Task>();
            readItemsFromDb();
            this.tasksAdapter = new TasksAdapter(this, this.tasksList);

            lvItem.setAdapter(this.tasksAdapter);
            tasksAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            showMessage(ex.getMessage());
        }
    }

    private void readItemsFromDb() {
        try {
            this.tasksList = dbHelper.getAllTasks();
        } catch(Exception ex) {
            showMessage(ex.getMessage());
        }
    }

//    private void poputlateArrayItem() {
//        todoItems = new ArrayList<String>();
////        todoItems.add("Item 1");
////        todoItems.add("Item 2");
////        todoItems.add("Item 3");
//        readItemsFromFile();
//        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
//    }

//    private void readItemsFromFile() {
//        File fileDir = getFilesDir();
//        File file = new File(fileDir, "todo.txt");
//        try {
//            todoItems = new ArrayList<String>(FileUtils.readLines(file));
//        } catch (IOException e) {
//            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void writeItemsToFile() {
//        File fileDir = getFilesDir();
//        File file = new File(fileDir, "todo.txt");
//        try {
//            FileUtils.writeLines(file, todoItems);
//        } catch (IOException e) {
//            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }

//    public void onAddItem(View view) {
//        try {
//            //Toast.makeText(MainActivity.this, "kek", Toast.LENGTH_LONG).show();
//            //todoAdapter.add(etEditText.getText().toString());
//            Task task = new Task();
//            task.setTaskName(etEditText.getText().toString());
//            this.dbHelper.addTask(task);
//            this.tasksAdapter.add(task);
//            etEditText.setText("");
//            //writeItemsToFile();
//        } catch (Exception ex) {
//            showMessage(ex.getMessage());
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            String itemText = data.getExtras().getString("itemText");
//            Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, String.valueOf(selectedIndexOfItem), Toast.LENGTH_SHORT).show();
//            todoItems.set(selectedIndexOfItem, itemText);
//            todoAdapter.notifyDataSetChanged();
//            writeItemsToFile();
//        }
//    }

}
