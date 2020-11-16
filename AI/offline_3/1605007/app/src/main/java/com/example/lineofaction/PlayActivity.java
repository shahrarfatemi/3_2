package com.example.lineofaction;

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

    private static int size,player;
    private LinearLayout parent;
    private LinearLayout textRow;
    private LinearLayout[] row_layouts;
    private Button[][] buttons;
    private TextView[][] columnHeads;
    private TextView durationText;
    private Game game;
    private boolean playerMove = true, positionSet = false, end = false;
    private ArrayList<Position> positions;
    private Position prevPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
//        Intent intent = getIntent();
//        size = intent.getIntExtra("board",1);
//        if(size==1) {
//            setContentView(R.layout.activity_play);
//        }
//        else{
//            setContentView(R.layout.activity_main);
//        }

        init();
    }

    public void init(){
        parent = (LinearLayout) findViewById(R.id.board_row_ll);

        Intent intent = getIntent();
        size = intent.getIntExtra("board",1);
        size = (size == 1) ? 6 : 8;
        Log.d("found size =>", "" + size);
        player = intent.getIntExtra("opponent",1);//1 => ai 2 => opponent
        buttons = new Button[size][size];
        //new
        columnHeads = new TextView[1][size+1];
        //neww shesh
        row_layouts = new LinearLayout[size];

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int buttonSize;
        if(size == 6) {
            buttonSize = 120;
            lp.setMargins(60, 1, 0, 1);
        }
        else{
            buttonSize = 100;
            lp.setMargins(5, 1, 0, 1);
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(buttonSize, buttonSize);
        lp1.setMargins(1,0,1,0);
        //newly added
        textRow = new LinearLayout(this);
        textRow.setLayoutParams(lp);
        textRow.setOrientation(LinearLayout.HORIZONTAL);
        for(int j = 0 ; j < size+1 ; j++){
            columnHeads[0][j] = new TextView(this);
            columnHeads[0][j].setLayoutParams(lp1);
//            buttons[0][j].setId(i*size+j);
            if(j>0) {
                columnHeads[0][j].setText("   " + (char) ('A' + j-1));
            }
//            buttons[0][j].setOnClickListener(this);
//            buttons[0][j].setBackgroundResource(R.drawable.checker_box);
            textRow.addView(columnHeads[0][j]);
        }
        parent.addView(textRow);
        //shesh
        for (int i = 0 ; i < size ; i++){
            row_layouts[i] = new LinearLayout(this);
            row_layouts[i].setLayoutParams(lp);
            row_layouts[i].setOrientation(LinearLayout.HORIZONTAL);
            for(int j = -1 ; j < size ; j++){
                //newly added
                if(j == -1){
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(lp1);
//            buttons[0][j].setId(i*size+j);
                    textView.setText("   "+(i+1));
//            buttons[0][j].setOnClickListener(this);
//            buttons[0][j].setBackgroundResource(R.drawable.checker_box);
                    row_layouts[i].addView(textView);
                }
                //shesh
                else {
                    buttons[i][j] = new Button(this);
                    buttons[i][j].setLayoutParams(lp1);
                    buttons[i][j].setId(i * size + j);
                    buttons[i][j].setTag(i + " " + j);
                    buttons[i][j].setOnClickListener(this);
                    buttons[i][j].setBackgroundResource(R.drawable.checker_box);
                    row_layouts[i].addView(buttons[i][j]);
                }
            }

            parent.addView(row_layouts[i]);
        }


        durationText = (TextView) findViewById(R.id.durationText);
        if(player == 1){
            durationText.setText("Playing against AI");
        }
        else{
            durationText.setText("Two Player Mode");
        }
        game = new Game(size);
        game.initGame();
        positions = new ArrayList<>();
        drawBoard();
    }

    public void chooseButton(Position position, int r, int c, char sign){
        Button button = buttons[r][c];
        button.setAlpha(0.5f);
        prevPosition = position;
        positions = game.validatePossiblePositions(position, sign);
        for (int i = 0; i < positions.size(); i++) {
            Position p = positions.get(i);
            Button button1 = buttons[p.getY()][p.getX()];
            button1.setAlpha(0.5f);
        }
        positionSet = true;
    }

    public boolean updateGame(Position position, char sign, boolean move){//returns true if ends :3
        game.updatePosition( prevPosition, position, sign);
        game.gameScore = game.calculateGame(sign, game.gameScore);
        if(game.checkResult()){//game ends er code
            endGame();
            return true;
        }
        playerMove = move;
        drawBoard();
        return false;
    }

    @Override
    public void onClick(View v) {
        if(!end) {
            String tag = (String) v.getTag();
            String[] inds = tag.split(" ");
            int r = Integer.parseInt(inds[0]);
            int c = Integer.parseInt(inds[1]);
            Position position = new Position(c, r);
            if (playerMove) {
                if (!positionSet) {
                    if (game.validMove(position, game.playerSign)) {
                        chooseButton(position, r, c, game.playerSign);
                    }
                } else {
                    if (positions.contains(position)) {
                        if (updateGame(position, game.playerSign, false)) {
                            return;
                        }
                        if (player == 1) {
                            doAI();
                        } else if (player == 2) {
                            durationText.setText("Player 2's Turn");
                        }
                    }
                    positionSet = false;
                    uncheckButtons();
                }
            } else {
                if (player == 2) {
                    //last e true kore dite hobe :3
                    if (!positionSet) {
                        if (game.validMove(position, game.aiSign)) {
                            chooseButton(position, r, c, game.aiSign);
                        }
                    } else {
                        if (positions.contains(position)) {
                            if (updateGame(position, game.aiSign, true)) {
                                return;
                            }
                            durationText.setText("Player 1's Turn");
                        }
                        positionSet = false;
                        uncheckButtons();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void doAI(){
        long start = System.currentTimeMillis();
        Position[] aiMove = game.aiTurn(3);
        double duration = ((System.currentTimeMillis() - start)*1.0/1000);
//        durationText.setText("           Time taken "+ duration + " seconds");
        Log.d("duration",""+duration);
        if(game.validMove(aiMove[0],game.aiSign)) {
            game.updatePosition(aiMove[0], aiMove[1], game.aiSign);//0 for ai
            drawBoard();
//            main.drawBoard();
            game.gameScore = game.calculateGame(game.aiSign, game.gameScore);
//            main.errorCheck();
//                    //check result
            if(game.checkResult()){//game ends er code
                endGame();
            }
//                    numberOfTurns += 2;
        }
        playerMove = true;
    }

    public void uncheckButtons(){
        for(int i = 0 ; i < game.getROW() ; i++){
            for(int j = 0 ; j < game.getCOL() ; j++){
                Button button = buttons[i][j];
                button.setAlpha(1.0f);
            }
        }
    }

    public void endGame(){
        String msg = "";
        if(game.gameScore == game.POS_INF){
            msg = "player 1 wins !!";
        }
        else if(game.gameScore == game.NEG_INF){
            msg = "player 2 wins !!";
        }
        else if(game.gameScore == game.DRAWN){
            msg = "drawn !!";
        }
//        Log.d("button : ","button pressed"+board+" "+player);
        durationText.setText(msg);
        end = true;
        drawBoard();
    }

    public void drawBoard(){
        for(int i = 0 ; i < game.getROW() ; i++){
            for(int j = 0 ; j < game.getCOL() ; j++){
                Button button = buttons[i][j];
                if(game.getBoard()[i][j] == game.playerSign){
                    button.setBackgroundResource(R.drawable.black);
                }
                else if(game.getBoard()[i][j] == game.aiSign){
                    button.setBackgroundResource(R.drawable.white);
                }
                else{
                    button.setBackgroundResource(R.drawable.checker_box);
                }
            }
        }
        uncheckButtons();
    }
}