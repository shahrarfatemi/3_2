package com.example.catchtheghost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spinnerRow,spinnerCol;
    private int row = 0,col = 0;
    private Button playButton;
    private TextView chooseRowText,chooseColText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseRowText = findViewById(R.id.rowText);
        spinnerRow = findViewById(R.id.rowChooseId);
        chooseColText = findViewById(R.id.colText);
        spinnerCol = findViewById(R.id.colChooseId);
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        String[] rows = getResources().getStringArray(R.array.row_options);
        ArrayAdapter <String> adapterRow = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, rows);
        adapterRow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter <String> adapterCol = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, rows);
        adapterCol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRow.setAdapter(adapterRow);
        spinnerRow.setOnItemSelectedListener(this);
        spinnerCol.setAdapter(adapterRow);
        spinnerCol.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.rowChooseId){
            row = Integer.parseInt(getResources().getStringArray(R.array.row_options)[position]);
            Log.d("row : ","pos"+position);
        }
        if(parent.getId() == R.id.colChooseId){
            col = Integer.parseInt(getResources().getStringArray(R.array.row_options)[position]);
            Log.d("col : ","pos"+position);
        }
//        Toast.makeText(this, "items selected!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.playButton){
            if(row == 0){
                chooseRowText.setError("row size is not set!!");
            }
            else if(col == 0){
                chooseRowText.setError("column size is not set!!");
            }
            else{
                Intent intent =  new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra("row",row);
                intent.putExtra("col",col);
                startActivity(intent);
//                finish();
            }
        }
    }
}