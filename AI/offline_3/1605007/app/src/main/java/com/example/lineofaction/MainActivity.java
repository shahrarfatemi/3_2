package com.example.lineofaction;

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

    private Spinner spinnerBoard,spinnerOpponent;
    private int board = 0,player = 0;
    private Button playButton;
    private TextView chooseBoardText, chooseOpponentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseBoardText = findViewById(R.id.chooseBoardText);
        chooseOpponentText = findViewById(R.id.chooseOpponentText);
        spinnerBoard = findViewById(R.id.boardChooseId);
        spinnerOpponent = findViewById(R.id.opponentChooseId);
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        String[] boards = getResources().getStringArray(R.array.board_size);
        String[] opponents = getResources().getStringArray(R.array.player_type);
        ArrayAdapter <String> adapterBoard = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, boards);
        adapterBoard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter <String> adapterOpponent = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, opponents);
        adapterBoard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBoard.setAdapter(adapterBoard);
        spinnerBoard.setOnItemSelectedListener(this);
        spinnerOpponent.setAdapter(adapterOpponent);
        spinnerOpponent.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.boardChooseId){
            board = position+1;
            Log.d("board : ","pos"+position);
        }
        else if(parent.getId() == R.id.opponentChooseId){
            player = position+1;
            Log.d("opponent : ","pos"+position);
        }
        Toast.makeText(this, "items selected!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.playButton){
            if(board == 0){
                chooseBoardText.setError("board size is not set!!");
            }
            else if(player == 0){
                chooseOpponentText.setError("opponent type is not set!!");
            }
            else{
                Log.d("button : ","button pressed"+board+" "+player);
                Intent intent =  new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra("board",board);
                intent.putExtra("opponent",player);
                startActivity(intent);
//                finish();
            }
        }
    }
}