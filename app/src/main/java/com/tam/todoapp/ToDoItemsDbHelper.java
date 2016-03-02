package com.tam.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toan on 2/28/2016.
 */
public class ToDoItemsDbHelper extends SQLiteOpenHelper {
    private static ToDoItemsDbHelper curInstance;
    private static final String DATABASE_NAME = "todoItemsDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASKS = "tasks";

    private static final String TAG = "Todo";

    public static synchronized ToDoItemsDbHelper getInstance(Context context) {
        if (curInstance == null) {
            curInstance = new ToDoItemsDbHelper(context.getApplicationContext());
        }
        return curInstance;
    }

    private ToDoItemsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ("
                + Task.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Task.COL_TASKNAME + " TEXT, "
                + Task.COL_STATUS + " TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setStatus(cursor.getString(cursor.getColumnIndex(Task.COL_STATUS)));
                    task.setTaskName(cursor.getString(cursor.getColumnIndex(Task.COL_TASKNAME)));
                    task.setId(cursor.getInt(cursor.getColumnIndex(Task.COL_ID)));
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while trying to get all tasks. Error: " + ex.getMessage());
        } finally {
            if (cursor != null && cursor.isClosed() == false) {
                cursor.close();
            }
        }
        return tasks;
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Task.COL_TASKNAME, task.getTaskName());
            values.put(Task.COL_STATUS, task.getStatus());
            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "Error while trying to add task. Error: " + ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String whereClause = "id = ?";
            ContentValues values = new ContentValues();
            values.put(Task.COL_TASKNAME, task.getTaskName());
            values.put(Task.COL_STATUS, task.getStatus());
            String[] whereArgs = new String[]{String.valueOf(task.getId())};
            db.update(TABLE_TASKS, values, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "Error while trying to update task. Error: " + ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String whereClause = "id = ?";
            String[] whereArgs = new String[]{String.valueOf(taskId)};
            db.delete(TABLE_TASKS, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "Error while trying to delete task. Error: " + ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}
