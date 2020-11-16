package com.example.lineofaction;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Game implements Serializable {


    private char board[][];
    private int pieceTable[][];
    private ArrayList<Position> blackPositions; // topmost empty position of a column
    private ArrayList<Position> whitePositions;


    private int totalBlackCheckers, totalWhiteCheckers;
    private final int ROW;
    private final int COL;
    public final int POS_INF = 99999;
    public final int NEG_INF = -99999;
    public final int DRAWN = 555555;
    final int Q3 = 3;
    final int Q4 = 4;

    int gameScore = 0;
    char playerSign = 'X';
    char aiSign = 'O';

    public Game(int r) {
        this.ROW = r;
        this.COL = r;
        totalBlackCheckers = 2 * COL - 4;
        totalWhiteCheckers = 2 * COL - 4;
    }


    public void initGame() {
        board = new char[ROW][COL];
        blackPositions = new ArrayList<>();
        whitePositions = new ArrayList<>();

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (i == 0 || i == ROW - 1) {
                    if (j != 0 && j != COL - 1) {
                        board[i][j] = playerSign;
//                        System.out.println(" i => "+i+" j => "+j);
                        blackPositions.add(new Position(j, i));
                    } else {
                        board[i][j] = '.';
                    }
                } else {
                    if (j == 0 || j == COL - 1) {
                        board[i][j] = aiSign;
                        whitePositions.add(new Position(j, i));
                    } else {
                        board[i][j] = '.';
                    }
                }
            }
        }

        if (COL == 8 && ROW == 8) {
            pieceTable = new int[][]{
                    {-80, -25, -20, -20, -20, -20, -25, -80},
                    {-25, 10, 10, 10, 10, 10, 10, -25},
                    {-20, 10, 25, 25, 25, 25, 10, -20},
                    {-20, 10, 25, 50, 50, 25, 10, -20},
                    {-20, 10, 25, 50, 50, 25, 10, -20},
                    {-20, 10, 25, 25, 25, 25, 10, -20},
                    {-25, 10, 10, 10, 10, 10, 10, -25},
                    {-80, -25, -20, -20, -20, -20, -25, -80}
            };
        } else {
            pieceTable = new int[][]{
                    {-80, -25, -20, -20, -25, -80},
                    {-25, 10, 10, 10, 10, -25},
                    {-20, 10, 50, 50, 10, -20},
                    {-20, 10, 50, 50, 10, -20},
                    {-25, 10, 10, 10, 10, -25},
                    {-80, -25, -20, -20, -25, -80}
            };
        }

    }


    public boolean validMove(Position pos, char sign) {
        int r = pos.getY();
        int c = pos.getX();
        if (board[r][c] == sign) {
            return true;
        }
        return false;
    }

    public ArrayList<Position> findSteps(char opSign, Position pos, int rowSteps, int colSteps, int d1Steps, int d2Steps) {
        int r = pos.getY();
        int c = pos.getX();
        int i;
        char sign = (opSign == playerSign) ? aiSign : playerSign;
        ArrayList<Position> positions = new ArrayList<>();
        // to the right =>
        for (i = 1; i < rowSteps && (c + i) < ROW; i++) {
            if (board[r][c + i] == opSign) {
                break;
            }
        }
        if (i == rowSteps && (c + i) < ROW) {
            if (board[r][c + i] != sign) {
                Position position = new Position(c + i, r);
                positions.add(position);
            }
        }

        // to the left <=
        for (i = 1; i < rowSteps && (c - i) > -1; i++) {
            if (board[r][c - i] == opSign) {
                break;
            }
        }
        if (i == rowSteps && (c - i) > -1) {
            if (board[r][c - i] != sign) {
                Position position = new Position(c - i, r);
                positions.add(position);
            }
        }

        // to upwards ^
        for (i = 1; i < colSteps && (r - i) > -1; i++) {
            if (board[r - i][c] == opSign) {
                break;
            }
        }
        if (i == colSteps && (r - i) > -1) {
            if (board[r - i][c] != sign) {
                Position position = new Position(c, r - i);
                positions.add(position);
            }
        }

        // to downwards \/
        for (i = 1; i < colSteps && (r + i) < ROW; i++) {
            if (board[r + i][c] == opSign) {
                break;
            }
        }
        if (i == colSteps && (r + i) < ROW) {
            if (board[r + i][c] != sign) {
                Position position = new Position(c, r + i);
                positions.add(position);
            }
        }

        // to left-up diagonal
        for (i = 1; i < d1Steps && (r - i) > -1 && (c - i) > -1; i++) {
            if (board[r - i][c - i] == opSign) {
                break;
            }
        }
        if (i == d1Steps && (r - i) > -1 && (c - i) > -1) {
            if (board[r - i][c - i] != sign) {
                Position position = new Position(c - i, r - i);
                positions.add(position);
            }
        }

        // to right-down diagonal
        for (i = 1; i < d1Steps && (r + i) < ROW && (c + i) < COL; i++) {
            if (board[r + i][c + i] == opSign) {
                break;
            }
        }
        if (i == d1Steps && (r + i) < ROW && (c + i) < COL) {
            if (board[r + i][c + i] != sign) {
                Position position = new Position(c + i, r + i);
                positions.add(position);
            }
        }

        // to left-down diagonal
        for (i = 1; i < d2Steps && (r + i) < ROW && (c - i) > -1; i++) {
            if (board[r + i][c - i] == opSign) {
                break;
            }
        }
        if (i == d2Steps && (r + i) < ROW && (c - i) > -1) {
            if (board[r + i][c - i] != sign) {
                Position position = new Position(c - i, r + i);
                positions.add(position);
            }
        }

        // to right-up diagonal
        for (i = 1; i < d2Steps && (r - i) > -1 && (c + i) < COL; i++) {
            if (board[r - i][c + i] == opSign) {
                break;
            }
        }
        if (i == d2Steps && (r - i) > -1 && (c + i) < COL) {
            if (board[r - i][c + i] != sign) {
                Position position = new Position(c + i, r - i);
                positions.add(position);
            }
        }

        return positions;
    }

    public ArrayList<Position> validatePossiblePositions(Position pos, char sign) {
        int r = pos.getY();
        int c = pos.getX();
        char opSign = (sign == playerSign) ? aiSign : playerSign;
        int stepsInRow = 0, stepsInCol = 0, stepsInD1 = 0, stepsInD2 = 0;
        for (int i = 0; i < ROW; i++) {
            if (board[i][c] != '.') {
                stepsInCol++;
            }
        }
        for (int i = 0; i < COL; i++) {
            if (board[r][i] != '.') {
                stepsInRow++;
            }
        }

        stepsInD1++;
        for (int i = 1; (c - i) > -1 && (r - i) > -1; i++) {
            if (board[r - i][c - i] != '.') {
                stepsInD1++;
            }
        }

        for (int i = 1; (c + i) < COL && (r + i) < ROW; i++) {
            if (board[r + i][c + i] != '.') {
                stepsInD1++;
            }
        }

        stepsInD2++;
        for (int i = 1; (c - i) > -1 && (r + i) < ROW; i++) {
            if (board[r + i][c - i] != '.') {
                stepsInD2++;
            }
        }

        for (int i = 1; (c + i) < COL && (r - i) > -1; i++) {
            if (board[r - i][c + i] != '.') {
                stepsInD2++;
            }
        }
//        System.out.println("steps "+stepsInCol+" "+stepsInRow+" "+stepsInD1+" "+stepsInD2);
        ArrayList<Position> positions = findSteps(opSign, pos, stepsInRow, stepsInCol, stepsInD1, stepsInD2);
//        System.out.println(positions.size());
//        System.out.println(positions);
        return positions;
    }

    public double checkerRatio(char sign){
        if(sign == playerSign) {
            return (whitePositions.size() * 1.0) / blackPositions.size();
        }
        return (blackPositions.size() * 1.0) / whitePositions.size();
    }

    public ArrayList<ArrayList<Position>> createGraph(ArrayList<Position> positions) {
        ArrayList<ArrayList<Position>> graph = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            Position position = positions.get(i);
            ArrayList<Position> adj = new ArrayList<>();
            for (int j = 0; j < positions.size(); j++) {
                if (i != j) {
                    Position position1 = positions.get(j);
                    if (position.distant(position1) < 1.80) {
                        position1.setId(j);
                        adj.add(position1);
                    }
                }
            }
            graph.add(i, adj);
        }
        return graph;
    }

    public int numberOfConnectedComponents2(ArrayList<Position> positions, char sign) {

        HashMap<Position, Integer> hashMap = new HashMap<>();
        for(int i = 0 ; i < positions.size() ; i++){
            Position position = positions.get(i);
            hashMap.put(position, i);
        }

        int cnt = 0;
        boolean [] visited = new boolean[positions.size()];
        for(int i = 0 ; i < positions.size() ; i++){
            visited[i] = false;
        }

        LinkedList<Position> queue = new LinkedList<>();
        int start = 0;

        while(true) {
            visited[start] = true;
            queue.add(positions.get(start));
            while (queue.size() != 0) {
                Position s = queue.poll();

                int sRow = s.getY();
                int sCol = s.getX();

                int r = (sRow-1)<0?0:sRow-1;
                int c = (sCol-1)<0?0:sCol-1;


//                System.out.print(r + " " + c);
                for(int i = r ; i < sRow+2 ; i++){
                    if(i < ROW){
                        for(int j = c ; j < sCol+2 ; j++){
                            if(j < COL){
                                if(board[i][j] == sign){
                                    int index = hashMap.get(new Position(j, i));
                                    if(!visited[index]) {
                                        queue.add(positions.get(index));
                                        visited[index] = true;
                                    }
                                }
                            }
                        }
                    }
                }

            }
            boolean allVisited = true;
            for(int i = 0 ; i < positions.size() ; i++){
                if(!visited[i]){
                    allVisited = false;
                    start = i;
                    break;
                }
            }
            cnt++;
            if(allVisited){
                break;
            }
        }
        return cnt;
    }

    public int evaluatePieceTable(char sign) {
        int score = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(board[i][j] == sign){
                    score += pieceTable[i][j];
                }
            }
        }
        return score;
    }

    public int minPos(ArrayList<Position> positions, boolean row){//true => row line false => column line returns the min position of positions in a column of row :3
        int min,temp;
        if(row) {
            min = ROW;
        }
        else{
            min = COL;
        }
        for(Position position : positions){
            if(row) {
                temp = position.getY();
            }
            else {
                temp = position.getX();
            }
            if(temp < min){
                min = temp;
            }
        }
        return min;
    }

    public int maxPos(ArrayList<Position> positions, boolean row){//true => row line false => column line returns the max position of positions in a column of row :3
        int max = -1,temp;
        for(Position position : positions){
            if(row) {
                temp = position.getY();
            }
            else {
                temp = position.getX();
            }
            if(temp > max){
                max = temp;
            }
        }
        return max;
    }

    public int evaluateArea(char sign){//only Length
        int minRow=ROW,maxRow=-1,minCol=COL,maxCol=-1;
        ArrayList<Position> positions = (sign==playerSign)?blackPositions:whitePositions;
        minRow = minPos(positions, true);
        maxRow = maxPos(positions, true);
        minCol = minPos(positions, false);
        maxCol = maxPos(positions, false);
        int h = (maxRow-minRow) > -1 ? (maxRow-minRow) : 0;
        int w = (maxCol-minCol) > -1 ? (maxCol-minCol) : 0;
        return h*w;
    }

    public Position evaluateCOM(ArrayList<Position> positions) {//only Width
        int rows=0,cols=0,row,col;
        for(Position position : positions){
            row = position.getY();
            col = position.getX();
            rows += row;
            cols += col;
        }
        row = rows/positions.size();
        col = cols/positions.size();
        return new Position(col, row);

    }

    //calculates density :3
    public double calculateAvgDensity(char sign){
        ArrayList <Position> positions = (sign == playerSign)?blackPositions:whitePositions;
        Position COM = evaluateCOM(positions);
        double totalDistance = 0.0,dist;
        int comRow = COM.getY();
        int comCol = COM.getX();
        for(Position position : positions){
            int row = position.getY();
            int col = position.getX();
            dist = Math.sqrt(Math.pow(row - comRow,2)+Math.pow(col - comCol,2));
            totalDistance += dist;
        }
        if(totalDistance != 0.0) {
            return positions.size() / totalDistance;
        }
        else{
            return 1.0;
        }
    }

    //around 2 distance of COM
    public int evaluateQuad(Position COM, char sign){
        int cnt = 0;
        int cRow = COM.getY(), cCol = COM.getX();
        int sRow = (cRow-2 > -1)? cRow - 2:0;
        int sCol = (cCol-2 > -1)? cCol - 2:0;
        for(int i = sRow ; i < cRow + 3 ; i++){
            if(i < ROW){
                for(int j = sCol ; j < cCol + 3 ; j++){
                    if(j < COL){
                        int Q = identifyR2Quad(new Position(j, i), sign);
                        if(Q == Q3 || Q == Q4){
                            cnt++;
                        }
                    }
                }
            }
        }
        return cnt;
    }

    public int identifyR2Quad(Position position, char sign){
        int Q = 0;
        int startRow = position.getY();
        int startCol = position.getX();
        for(int i = startRow ; i < startRow+2 ; i++){
            if(i > -1 && i < ROW) {
                for (int j = startCol; j < startCol+2; j++) {
                    if(j > -1 && j < COL){
                        if(board[i][j] == sign){
                            Q++;
                        }
                    }
                }
            }
        }
        return Q;
    }


    public int calculateGame(char sign, int currentScore){

        ArrayList<Position> sidePositions;
        ArrayList<Position> opponentPositions;
        char opsign = (sign == playerSign)?aiSign:playerSign;
        if(sign == playerSign){
            sidePositions = blackPositions;
            opponentPositions = whitePositions;
        }
        else{
            sidePositions = whitePositions;
            opponentPositions = blackPositions;
        }
        int sideConnectedComponents = this.numberOfConnectedComponents2(sidePositions, sign);
        if(sideConnectedComponents == 1){
            return (sign==playerSign)?POS_INF:NEG_INF;
        }
        int opponentsConnectedComponents = this.numberOfConnectedComponents2(opponentPositions, opsign);
        if(opponentsConnectedComponents == 1){
            return (sign==playerSign)?NEG_INF:POS_INF;
        }
        else{
            boolean drawn = true;
            for(Position position : sidePositions){
                ArrayList<Position> positions = this.validatePossiblePositions(position, this.playerSign);
                if(positions.size() > 0){
                    drawn = false;
                    break;
                }
            }
            if(drawn){
                return DRAWN;
            }
        }
        ArrayList<Position> allPositions = new ArrayList<>();
        allPositions.addAll(sidePositions);
        allPositions.addAll(opponentPositions);
        int pieceTableValue = this.evaluatePieceTable(sign) - this.evaluatePieceTable(opsign); //more
        int areaValue = this.evaluateArea(opsign) - this.evaluateArea(sign); //less
        double ratioValue = this.checkerRatio(opsign) - this.checkerRatio(sign); //less
        int connectedValue = opponentsConnectedComponents - sideConnectedComponents; //less
        Position COM = this.evaluateCOM(allPositions);
        int quadValue = this.evaluateQuad(COM, sign) - this.evaluateQuad(COM, opsign); //more
        double densityValue = this.calculateAvgDensity(sign) - this.calculateAvgDensity(opsign); //more

        double score = (2.5* connectedValue) + (1.5* ratioValue) + (2.5*quadValue) + (2.5*densityValue) + (2.5*areaValue) + (2.5*pieceTableValue);
        int c = Math.round((float) score/14);
        if(sign==(playerSign)) {
            return (currentScore + c);
        }
        else {
            return (currentScore - c);
        }
    }

    public void resetMarked(Position position, char sign){
        int row = position.getY();
        int col = position.getX();

        board[row][col] = sign;
        if(sign == playerSign){
            blackPositions.add(position);
        }
        else{
            whitePositions.add(position);
        }
    }

    public Position updatePosition(Position previousPosition, Position position, char sign){
        char opSign = (sign == playerSign)?aiSign:playerSign;
        int prevRow = previousPosition.getY();
        int prevCol = previousPosition.getX();
        int row = position.getY();
        int col = position.getX();
        Position markedPosition = null;
        // might need to update checkers' position array :3
        if(sign == playerSign){
            if(board[row][col] == opSign){
                markedPosition = position;
                whitePositions.remove(position);
            }
            blackPositions.remove(previousPosition);
            blackPositions.add(position);
        }
        else{
            if(board[row][col] == opSign){
                markedPosition = position;
                blackPositions.remove(position);
            }
            whitePositions.remove(previousPosition);
            whitePositions.add(position);
        }

        board[prevRow][prevCol] = '.';
        board[row][col] = sign;
        return markedPosition;
    }


    public int miniMax(char sign, int depth, int currentScore, int alpha, int beta){
//        System.out.println("in depth => "+depth);
        int score = calculateGame(sign, currentScore);

        if(depth < 0) return score;

        // Maximizer won the game
        if(score == POS_INF) return POS_INF;

        // Minimizer won the game
        if(score == NEG_INF) return NEG_INF;

        // Draw game
        if(score == DRAWN)  return 0;

        char opsign = (sign == playerSign)?aiSign:playerSign;
        // if this is maximizer's move
        if(sign==aiSign)//sign has been put, now evaluate for opsign
        {
            int best = NEG_INF;

            ArrayList<Position> opponentPositions = blackPositions;
            for (int i = 0 ; i < opponentPositions.size(); i++){
                Position pos = opponentPositions.get(i);
                ArrayList<Position> positions = this.validatePossiblePositions(pos,opsign);
                for(Position tentativePosition : positions) {
                    Position marked = updatePosition(pos, tentativePosition, opsign);
                    int moveScore = miniMax(opsign,depth - 1, score, alpha, beta);
                    updatePosition(tentativePosition, pos, opsign);
                    if(marked != null){
                        this.resetMarked(marked, sign);
                    }
                    if (moveScore > best) {
                        best = moveScore;
                        if(best > alpha){
                            alpha = best;
                            if(alpha >= beta){
                                return best;
                            }
                        }
                    }
                }
            }
            return best;
            // return best-depth;
        }
        // if this is minimizer's move
        else
        {
            int best = POS_INF;

            ArrayList<Position> opponentPositions = whitePositions;
            for (int i = 0 ; i < opponentPositions.size(); i++) {
                Position pos = opponentPositions.get(i);
                ArrayList<Position> positions = this.validatePossiblePositions(pos,opsign);
                for(Position tentativePosition : positions) {
                    Position marked = updatePosition(pos, tentativePosition, opsign);
                    int moveScore = miniMax(opsign,depth - 1, score, alpha, beta);
                    updatePosition(tentativePosition, pos, opsign);
                    if(marked != null){
                        this.resetMarked(marked, sign);
                    }
                    if (moveScore < best) {
                        best = moveScore;
                        if(best < beta){
                            beta = best;
                            if(alpha >= beta){
                                return best;
                            }
                        }
                    }
                }
            }
            return best;
        }

    }

    public Position[] aiTurn(int depth) {
        int best = POS_INF;
        Position[] move = new Position[2];
        char sign = aiSign;
        char opsign = playerSign;
//
        for (int i = 0 ; i < whitePositions.size(); i++) {
            Position aiPos = whitePositions.get(i);
            ArrayList<Position> aiPositions = validatePossiblePositions(aiPos, sign);
            for (Position tentativePosition : aiPositions) {
                Position marked = updatePosition(aiPos, tentativePosition, sign);
//                drawBoard();
                int moveScore = miniMax(sign, depth - 1, gameScore, NEG_INF, POS_INF);
                updatePosition(tentativePosition, aiPos, sign);
                if(marked != null){
                    this.resetMarked(marked, opsign);
                }
//                drawBoard();
//                System.out.println("movescore "+ moveScore);
                if (moveScore < best) {
                    best = moveScore;
                    move[0] = aiPos;
                    move[1] = tentativePosition;
                }
            }

        }
        if (best == POS_INF) {
            for (int i = 0 ; i < whitePositions.size(); i++) {
                Position aiPos = whitePositions.get(i);
                ArrayList<Position> aiPositions = validatePossiblePositions(aiPos, aiSign);
                for (Position tentativePosition : aiPositions) {
                    Position marked = updatePosition(aiPos, tentativePosition, sign);
                    int moveScore = miniMax(sign, depth - 1, gameScore, NEG_INF, POS_INF);
                    updatePosition(tentativePosition, aiPos, sign);
                    if(marked != null){
                        this.resetMarked(marked, playerSign);
                    }
                    if (moveScore == NEG_INF) {
                        move[0] = aiPos;
                        move[1] = tentativePosition;
                    }
                }
            }
        }
        return move;
    }


    public boolean checkResult(){

        if(gameScore == POS_INF){
//            System.out.println("player 1 wins !!");
            return true;
        }
        else if(gameScore == NEG_INF){
//            System.out.println("player 2 wins !!");
            return true;
        }
        else if(gameScore == DRAWN){
//            System.out.println("drawn !!");
            return true;
        }
        return false;
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }

    public char[][] getBoard() {
        return board;
    }

}
