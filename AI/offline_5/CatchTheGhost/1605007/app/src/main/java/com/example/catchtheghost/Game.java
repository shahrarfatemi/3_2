package com.example.catchtheghost;

import java.util.Scanner;

public class Game {
    private Ghost ghost;
    private Sensor sensor;
    private int ROW;
    private int COL;
    public Cell[][] board;
    private double tempProb [][];

    public Game(int ROW, int COL) {
        this.ROW = ROW;
        this.COL = COL;
        ghost = new Ghost(new Cell(COL/2,ROW/2),ROW,COL);
        sensor = new Sensor(new Cell(-1,-1),ROW,COL);
        board = new Cell[ROW][COL];
        tempProb = new double[ROW][COL];
        double inProb = 1.0/(ROW*COL);
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                board[i][j] = new Cell(j, i);
                board[i][j].initializeProbability(inProb);
            }
        }
    }

    public Ghost getGhost() {
        return ghost;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Cell[][] getBoard() {
        return board;
    }

    private double updateSensorbasedProbability(Cell cell) {
        double prob = sensor.ghostProbabilityOfCell(cell)*cell.getGhostProbability();
//        cell.setGhostProbability(prob);
        return prob;
    }


    private double updateProbability(Cell cell){
        int j1 = (cell.getX() == 0)?cell.getX():cell.getX()-1;
        int j2 = (cell.getX() == COL-1)?cell.getX():cell.getX()+1;
        int i1 = (cell.getY() == 0)?cell.getY():cell.getY()-1;
        int i2 = (cell.getY() == ROW-1)?cell.getY():cell.getY()+1;
        double sumOfProbs = 0.0;
//        int numberOfSideDirections = 0;
//        int numberOfDiagonalDirections = 0;
        double moveProb=0.0;

//        for(int i = i1 ; i <= i2 ; i++){
//            for(int j = j1 ; j <= j2 ; j++){
//                if(i != cell.getX() && j != cell.getY()){//diagonal
//                    numberOfDiagonalDirections++;
//                }
//                else if(i != cell.getX() || j != cell.getY()){//sidewise
//                    numberOfSideDirections++;
//                }
//            }
//        }
//        i ==> row , j ==> col
//        int cnt = 0;
        for(int i = i1 ; i <= i2 ; i++){
            for(int j = j1 ; j <= j2 ; j++){
                if(j == cell.getX() && i == cell.getY()){
                    moveProb = 0.008;//same position
//                    cnt++;
                }
                else if(j != cell.getX() && i != cell.getY()){
                    moveProb = 0.008;//diagonal
//                    cnt++;
                }
                else if(j != cell.getX() || i != cell.getY()){
                    moveProb = 0.24;//sidewise
//                    cnt++;
                }

                sumOfProbs += (board[i][j].getGhostProbability()*moveProb);
            }
        }
//        System.out.println("count "+cnt);
        return sumOfProbs;
//        if(this.getX() == 0){
//            if(this.getY() == 0){//(0,0)
//                sides = random.nextInt(2);
//                return (sides == 0)?new Cell(this.getX() + 1, this.getY()):
//                        new Cell(this.getX(), this.getY() + 1);
//            }
//            if(this.getY() == ROW - 1){//(0,row-1)
//                sides = random.nextInt(2);
//                return (sides == 0)?new Cell(this.getX() + 1, this.getY()):
//                        new Cell(this.getX(), this.getY() - 1);
//            }
//            else{
//                sides = random.nextInt(3);
//                switch (sides){
//                    case 0 : return new Cell(this.getX(), this.getY() - 1);//(0,row--)
//
//                    case 1 : return new Cell(this.getX() + 1, this.getY());//(1,row)
//
//                    case 2 : return new Cell(this.getX(), this.getY() + 1);//(0,row++)
//                }
//            }
//        }
//        else if(this.getX() == COLUMN - 1){
//            if(this.getY() == 0){//(col - 1,0)
//                sides = random.nextInt(2);
//                return (sides == 0)?new Cell(this.getX() - 1, this.getY()):
//                        new Cell(this.getX(), this.getY() + 1);
//            }
//            if(this.getY() == ROW - 1){//(0,row-1)
//                sides = random.nextInt(2);
//                return (sides == 0)?new Cell(this.getX() - 1, this.getY()):
//                        new Cell(this.getX(), this.getY() - 1);
//            }
//            else{
//                sides = random.nextInt(3);
//                switch (sides){
//                    case 0 : return new Cell(this.getX(), this.getY() - 1);//(col-1,row--)
//
//                    case 1 : return new Cell(this.getX() - 1, this.getY());//(col-2,row)
//
//                    case 2 : return new Cell(this.getX(), this.getY() + 1);//(col-1,row++)
//                }
//            }
//        }
//        else if(this.getY() == 0){
//            sides = random.nextInt(3);
//            switch (sides){//baame,niche,daane
//                case 0 : return new Cell(this.getX() - 1, this.getY());
//
//                case 1 : return new Cell(this.getX(), this.getY() + 1);
//
//                case 2 : return new Cell(this.getX() + 1, this.getY());
//            }
//        }
//        else if(this.getY() == ROW - 1){
//            sides = random.nextInt(3);
//            switch (sides){//baame,uporee,daane
//                case 0 : return new Cell(this.getX() - 1, this.getY());
//
//                case 1 : return new Cell(this.getX(), this.getY() -1);
//
//                case 2 : return new Cell(this.getX() + 1, this.getY());
//            }
//        }
//        else{
//            sides = random.nextInt(4);
//            switch (sides){//baame,uporee,daane,niche
//                case 0 : return new Cell(this.getX() - 1, this.getY());
//
//                case 1 : return new Cell(this.getX(), this.getY() -1);
//
//                case 2 : return new Cell(this.getX() + 1, this.getY());
//
//                case 3 : return new Cell(this.getX(), this.getY() + 1);
//            }
//        }
//        return new Cell(-1,-1);
    }



    public  void updateTransition(){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                tempProb[i][j] = updateProbability(board[i][j]);
            }
        }
//        printTempBoard();
        normalizeProbability();

    }

    public  void updateObservation(){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                tempProb[i][j] = updateSensorbasedProbability(board[i][j]);
            }
        }
        normalizeProbability();
    }


    public  void setSensor(int x, int y){
        sensor.setCell(x,y);
    }

    public  void normalizeProbability(){
        double totalProbs = 0.0;
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                totalProbs += tempProb[i][j];
            }
        }
        double prob;
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                prob = tempProb[i][j];
                board[i][j].setGhostProbability(prob/totalProbs);
            }
        }
    }

    public  void printBoard(){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                System.out.printf("%.3f\t",board[i][j].getGhostProbability());
            }
            System.out.println();
            System.out.println();
        }
    }


    public  void printTempBoard(){
        System.out.println("TEMPPPPPPP");
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++) {
                System.out.printf("%.3f\t",tempProb[i][j]);
            }
            System.out.println();
            System.out.println();
        }
    }
//
//    public  void main(String[] args) {
//        Scanner input = new Scanner(System.in);
//        ROW = 5;
//        COL = 5;
//        ghost = new Ghost(new Cell(2,2),ROW,COL);
//        sensor = new Sensor(new Cell(-1,-1),ROW,COL);
//        board = new Cell[ROW][COL];
//        tempProb = new double[ROW][COL];
//        double inProb = 1.0/(ROW*COL);
//        for(int i = 0 ; i < ROW ; i++){
//            for(int j = 0 ; j < COL ; j++) {
//                board[i][j] = new Cell(j, i);
//                board[i][j].initializeProbability(inProb);
//            }
//        }
//
//        updateProbability(new Cell(0,0));
//        updateProbability(new Cell(1,1));
//        updateProbability(new Cell(1,4));
//        int i = 0;
//        while(i < 10) {
//            printBoard();
//            int c = input.nextInt();
//            System.out.println(ghost.makeMovement());
//            updateTransition();
////            printBoard();
////            int x = input.nextInt();
////            int y = input.nextInt();
////            setSensor(x, y);
////            sensor.detectGhost(ghost.getCurrentCell());
////            updateObservation();
//            i++;
//        }
////        System.out.println("2,2 er prob "+updateProbability(board[2][2]));
////        setSensor(1,3);
////        sensor.detectGhost(ghost.getCurrentCell());
////        updateObservation();
////        System.out.println("jdnsvjlfnfjknvklaej");
////        printBoard();
////        setSensor(2,4);
////        sensor.detectGhost(ghost.getCurrentCell());
////
////        updateObservation();
////        System.out.println("jdnsvjlfnfjknvklaej");
////        printBoard();
//    }
}
