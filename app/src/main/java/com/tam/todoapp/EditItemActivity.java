package com.tam.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText txtInput;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemText = getIntent().getStringExtra("itemText");
        int itemPosition = getIntent().getIntExtra("itemPosition", 0);
        txtInput = (EditText)findViewById(R.id.txtInput);
        txtInput.setText(itemText);
        txtInput.requestFocus();
        txtInput.setSelection(txtInput.getText().length());
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(EditItemActivity.this, "con", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("itemText", txtInput.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}
