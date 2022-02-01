package com.example.catchtheghost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private static int row,col;
    private LinearLayout parent;
    private LinearLayout[] row_layouts;
    private Button[][] buttons;
    private Button timerButton,sensorButton,captureButton,sensedButton;
    //    private TextView[][] columnHeads;
    private TextView resultText;
    private Game game;
    private Cell currentCell;
    private boolean end = false;
//    private boolean colMove = true, positionSet = false, end = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
//        Intent intent = getIntent();
//        row = intent.getIntExtra("board",1);
//        if(row==1) {
//            setContentView(R.layout.activity_play);
//        }
//        else{
//            setContentView(R.layout.activity_main);
//        }

        init();
    }

    public void init() {
        parent = (LinearLayout) findViewById(R.id.board_row_ll);

        Intent intent = getIntent();
        row = intent.getIntExtra("row", 5);
        col = intent.getIntExtra("col", 5);
//        Log.d("found row =>", "" + row);
        buttons = new Button[row][col];
        //new
//        columnHeads = new TextView[1][row+1];
        //neww shesh
        row_layouts = new LinearLayout[row];

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int buttonSize,buttonCol;
        int rowM,colM;
        if(row < 7){
            rowM = 5;
        }
        else{
            rowM = 2;
        }

        if(col < 7){
            colM = 5;
        }
        else{
            colM = 7;
        }

        buttonSize = 180 - row * rowM;
        buttonCol = 180 - col * colM;
        lp.setMargins(0, 1, 0, 1);
//        if(row == -1) {
//            buttonSize = 120;
//            lp.setMargins(60, 1, 0, 1);
//        }
//        else{
//            buttonSize = 100;
//            lp.setMargins(5, 1, 0, 1);
//        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(buttonSize, buttonCol);
        lp1.setMargins(1, 0, 1, 0);
        //newly added
//        textRow = new LinearLayout(this);
//        textRow.setLayoutParams(lp);
//        textRow.setOrientation(LinearLayout.HORIZONTAL);
//        for(int j = 0 ; j < row+1 ; j++){
//            columnHeads[0][j] = new TextView(this);
//            columnHeads[0][j].setLayoutParams(lp1);
////            buttons[0][j].setId(i*row+j);
//            if(j>0) {
//                columnHeads[0][j].setText("   " + (char) ('A' + j-1));
//            }
////            buttons[0][j].setOnClickListener(this);
////            buttons[0][j].setBackgroundResource(R.drawable.checker_box);
//            textRow.addView(columnHeads[0][j]);
//        }
//        parent.addView(textRow);
        //shesh
        for (int i = 0; i < row; i++) {
            row_layouts[i] = new LinearLayout(this);
            row_layouts[i].setLayoutParams(lp);
            row_layouts[i].setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < row; j++) {
                //newly added
//                if(j == -1){
//                    TextView textView = new TextView(this);
//                    textView.setLayoutParams(lp1);
////            buttons[0][j].setId(i*row+j);
//                    textView.setText("   "+(i+1));
////            buttons[0][j].setOnClickListener(this);
////            buttons[0][j].setBackgroundResource(R.drawable.checker_box);
//                    row_layouts[i].addView(textView);
//                }
//                //shesh
//                else {
                buttons[i][j] = new Button(this);
                buttons[i][j].setLayoutParams(lp1);
                buttons[i][j].setId(i * row + j);
                buttons[i][j].setTag(i + " " + j);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setBackgroundResource(R.drawable.checker_box);
                row_layouts[i].addView(buttons[i][j]);

            }
            parent.addView(row_layouts[i]);
        }


        resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText("Capture The Ghost");
        game = new Game(row,col);
        currentCell = null;
//        game.initGame();
//        positions = new ArrayList<>();


        timerButton = (Button) findViewById(R.id.timerButtonId);
        sensorButton = (Button) findViewById(R.id.sensorButtonId);
        captureButton = (Button) findViewById(R.id.catchButtonId);
        timerButton.setOnClickListener(this);
        sensorButton.setOnClickListener(this);
        captureButton.setOnClickListener(this);
        drawBoard();
    }

    @Override
    public void onClick(View v) {
        if(!end) {
            if(sensedButton != null){
                sensedButton.setBackgroundResource(R.drawable.checker_box);
                sensedButton = null;
            }
            if(currentCell != null) {
                int i = currentCell.getY();
                int j = currentCell.getX();
                Button button = buttons[i][j];
                button.setAlpha(1.0f);
                button.setBackgroundResource(R.drawable.checker_box);
            }
            if (v.getId() == R.id.timerButtonId) {
                currentCell = null;
                game.getGhost().makeMovement();
                game.updateTransition();
                drawBoard();
            } else if (v.getId() == R.id.sensorButtonId) {
                if (currentCell != null) {
                    game.setSensor(currentCell.getX(), currentCell.getY());
                    handleSensing(currentCell);
                    game.updateObservation();
                    drawBoard();
                    currentCell = null;
                } else {
                    sensorButton.setError("choose a cell first");
                }
            } else if (v.getId() == R.id.catchButtonId) {
                if (currentCell != null) {
                    endGame();
                } else {
                    sensorButton.setError("choose a cell first");
                }
            } else {
                String tag = (String) v.getTag();
                String[] inds = tag.split(" ");
                int r = Integer.parseInt(inds[0]);
                int c = Integer.parseInt(inds[1]);
                currentCell = new Cell(c, r);
                Button button = buttons[r][c];
                button.setAlpha(0.5f);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void handleSensing(Cell cell){
        int color = game.getSensor().detectGhost(game.getGhost().getCurrentCell());
        int i = cell.getY();
        int j = cell.getX();
        Button button = buttons[i][j];
        if(color == 1){//red
            button.setBackgroundResource(R.drawable.checker_red);
            sensedButton = button;
        }
        else if(color == 2){//orange
            button.setBackgroundResource(R.drawable.checker_orange);
            sensedButton = button;
        }
        else if(color == 3){//green
            button.setBackgroundResource(R.drawable.checker_green);
            sensedButton = button;
        }
    }

    public void endGame(){
        if(game.getGhost().getCurrentCell().equals(currentCell)){
            resultText.setText("Hurrah!! You captured the ghost at "+game.getGhost().getCurrentCell());
        }
        else{
            resultText.setText("Sorry!! you could not capture the ghost ("+game.getGhost().getCurrentCell()+")");
        }
        end = true;
        drawBoard();
    }

    public void drawBoard(){
        Cell board[][] = game.getBoard();
        for(int i = 0 ; i < row ; i++){
            for(int j = 0 ; j < col ; j++){
                Button button = buttons[i][j];
                button.setText(((Double)board[i][j].getGhostProbability()).toString());
            }
        }
    }
}