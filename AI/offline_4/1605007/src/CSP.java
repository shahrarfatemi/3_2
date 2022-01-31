import javafx.geometry.Pos;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class CSP {
    int [][] board;
    final int ROW;
    final int COL;
    final int POS_INF = 99999;
    final int NEG_INF = -99999;
    final int GO_ON = 0;
    private int nodes = 0;
    private int backtracks = 0;
    boolean chooseForwardDeg = false;
    Vertex [][] vertices;
    ArrayList<Vertex> priorityQueue;

    public static HashMap<Integer, ArrayList<Position>> valueMap = new HashMap<>();

    public CSP(String fileName, boolean chooseForwardDeg) throws IOException {
        this.chooseForwardDeg = chooseForwardDeg;
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int k = Integer.parseInt(br.readLine());
        this.ROW = k;
        this.COL = k;
        board = new int[ROW][COL];
        vertices = new Vertex[ROW][COL];
        String str;
        k = 0;
        System.out.println("size "+ROW);
        //queue
        priorityQueue = new ArrayList<>();
        while ((str = br.readLine()) != null){
            String [] vals = str.split(", ");
//            System.out.println(vals);
            for(int i = 0 ; i < COL ; i++){
                int val = Integer.parseInt(vals[i]);
                board[k][i] = val;
                vertices[k][i] = new Vertex(ROW, new Position(k,i));
                priorityQueue.add(vertices[k][i]);
            }
            k++;
        }




        initValueMap();
        initVertices();

    }

    public void initValueMap(){
        for(int i = 0 ; i < ROW ; i++){
            ArrayList<Position> positions = new ArrayList<>();
            valueMap.put(i+1,positions);
        }
    }

    public void initVertices(){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                if(board[i][j] != 0){
                    Vertex vertex = vertices[i][j];
                    if(vertex.setValue(board[i][j])){
                        updateDomains(vertex);
                        priorityQueue.remove(vertex);
                        valueMap.get(board[i][j]).add(vertex.getPosition());
                    }
                    else{
                        System.out.println("could not assign value to "+vertex);
                    }
                }
            }
        }
    }

    public ArrayList<Position> setVertex(Position position, int value){//new
        int row = position.getRow();
        int col = position.getColumn();
        Vertex vertex = vertices[row][col];
        if(vertex.setValue(value)){
            board[row][col] = value;
            ArrayList<Position> positions = updateDomains(vertex);
            valueMap.get(board[row][col]).add(vertex.getPosition());
            return positions;//new
        }
        return null;//new
    }

    public boolean resetVertex(Position position, ArrayList<Position> positions){//new
        int row = position.getRow();
        int col = position.getColumn();
        Vertex vertex = vertices[row][col];
        if(vertex.resetValue()){
            resetDomains(vertex, positions);//new
            valueMap.get(board[row][col]).remove(vertex.getPosition());
            board[row][col] = 0;
            return true;
        }
        return false;
    }



    public void printVertices(){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                System.out.println(vertices[i][j]);
            }
        }
    }

    public void printValueMap(){
        System.out.println(valueMap);
    }

    public int evaluateBoard(){
        int count = 0;
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                if(vertices[i][j].domainSize == 0){
                    return NEG_INF;
                }
                else if(vertices[i][j].getDomainSize() == 1 && vertices[i][j].isAssigned()){
                    count++;
                }
            }
        }
        if(count == ROW*COL){
            return POS_INF;
        }
        return GO_ON;
    }

    public boolean checkNeighbours(Vertex vertex){
        int row = vertex.getPosition().getRow();
        int col = vertex.getPosition().getColumn();
        for(int j = 0 ; j < COL ; j++){
            if(j != col){
                if(vertices[row][j].getDomainSize() <= 0){
                    backtracks++;
                    return false;
                }
            }
        }
        for(int i = 0 ; i < ROW ; i++){
            if(i != row){
                if(vertices[i][col].getDomainSize() <= 0){
                    backtracks++;
                    return false;
                }
            }
        }
        return true;
    }

    private boolean assMAC(Vertex vertex, HashMap<Vertex, ArrayList<Position>> hashMap){
        ArrayList<Integer> domains = new ArrayList<>();//new
        for(int i = 0 ; i < vertex.getDomain().size() ; i++) {//new
            int v = vertex.getDomain().get(i);
            domains.add(v);//new
        }

        for(Integer integer : domains) {//new
            ArrayList<Position> positions = setVertex(vertex.getPosition(), integer);//new
//            System.out.println("setting vertex "+vertex);
            if(checkNeighbours(vertex)) {
                hashMap.put(vertex, positions);
                return true;
            }
            backtracks++;
            resetVertex(vertex.getPosition(), positions);
//            System.out.println("resetting vertex "+vertex);
        }
        return false;
    }

    private void assMAC2(HashMap<Vertex, ArrayList<Position>> hashMap){
        if(hashMap.size() != 0) {
            for(HashMap.Entry<Vertex, ArrayList<Position>> entry : hashMap.entrySet()){
                ArrayList<Position> positions = entry.getValue();
                if(positions != null){
                    resetVertex(entry.getKey().getPosition(), positions);
                }
            }
        }
    }

    public boolean checkNeighbours2(Vertex vertex){
        int row = vertex.getPosition().getRow();
        int col = vertex.getPosition().getColumn();
        HashMap<Vertex, ArrayList<Position>> hashMap = new HashMap<>();
        boolean res = checkNeighbours(vertex);
        if(res){
            for(int j = 0 ; j < COL ; j++){
                if(j != col && !vertices[row][j].isAssigned()){
                    //baki part
                    res = assMAC(vertices[row][j], hashMap);
                    if(!res){
                        //assMAC2();
                        // return
                        return false;
                    }
                }
            }
            for(int i = 0 ; i < ROW ; i++){
                if(i != row && !vertices[i][col].isAssigned()){
                    res = assMAC(vertices[i][col], hashMap);
                    if(!res){
                        //assMAC2();
                        // return
                        return false;
                    }
                }
            }
        }
        else{
            return res;
        }
        System.out.println(hashMap);
        assMAC2(hashMap);
        return true;
    }

    public void resetDomains(Vertex vertex, ArrayList<Position> list){
        int row = vertex.getPosition().getRow();
        int col = vertex.getPosition().getColumn();
        int value = board[row][col];
        //new
        if(list != null) {
            for (Position position : list) {
                int r = position.getRow();
                int c = position.getColumn();
                vertices[r][c].addValue(value);
            }
        }
        //
        if(chooseForwardDeg) {
            for (int j = 0; j < COL; j++) {
                if (j != col && !vertices[row][j].isAssigned()) {
                    vertices[row][j].increaseMaxForwardDegree();
                }
            }
            for (int i = 0; i < ROW; i++) {
                if (i != row && !vertices[i][col].isAssigned()) {
                    vertices[i][col].increaseMaxForwardDegree();
                }
            }
        }
        //
    }

    public ArrayList<Position> updateDomains(Vertex vertex){
        ArrayList<Position> list = new ArrayList<>();//new
        int row = vertex.getPosition().getRow();
        int col = vertex.getPosition().getColumn();
        int value = board[row][col];
        for(int j = 0 ; j < COL ; j++){
            if(j != col && vertices[row][j].removeValue(value)){//new
                list.add(new Position(row, j));
            }
            //new
            if(chooseForwardDeg && !vertices[row][j].isAssigned()) {
                vertices[row][j].decreaseMaxForwardDegree();
            }
            //
        }
        for(int i = 0 ; i < ROW ; i++){
            if(i != row && vertices[i][col].removeValue(value)){//new
                list.add(new Position(i, col));
            }
            //new
            if(chooseForwardDeg && !vertices[i][col].isAssigned()) {
                vertices[i][col].decreaseMaxForwardDegree();
            }
            //
        }
        return list;//new
    }

    public void showNeighbours(Vertex vertex){
        int row = vertex.getPosition().getRow();
        int col = vertex.getPosition().getColumn();
        for(int j = 0 ; j < COL ; j++){
            if(j != col){
                System.out.println(vertices[row][j]);
            }
        }
        for(int i = 0 ; i < ROW ; i++){
            if(i != row){
                System.out.println(vertices[i][col]);
            }
        }
    }

    public int BT() {
        //new
//        if(nodes > 10){
//            return 1;
//        }
        //
        if(priorityQueue.isEmpty()){
            return POS_INF;
        }
        Collections.sort(priorityQueue);
        Vertex vertex = priorityQueue.remove(0);
//        priorityQueue.remove(0);

        ArrayList<Integer> domains = new ArrayList<>();//new
        for(int i = 0 ; i < vertex.getDomain().size() ; i++) {//new
            int v = vertex.getDomain().get(i);
            domains.add(v);//new
        }

        //
        for(Integer integer : domains) {//new
            ArrayList<Position> positions = setVertex(vertex.getPosition(), integer);//new
//            System.out.println("setting vertex "+vertex);
            nodes++;
            if(checkNeighbours(vertex)) {
                int res = BT();
                if(res == POS_INF){
                    return POS_INF;
                }
            }
            backtracks++;
            resetVertex(vertex.getPosition(), positions);
//            System.out.println("resetting vertex "+vertex);
        }

        priorityQueue.add(vertex);
        return NEG_INF;
    }

    public int backTrack(){
        nodes++;
        //new
//        if(nodes > 10){
//            return 1;
//        }
        //
        if(priorityQueue.isEmpty()){
            return POS_INF;
        }
        Collections.sort(priorityQueue);
        Vertex vertex = priorityQueue.remove(0);
//        priorityQueue.remove(0);

        ArrayList<Integer> domains = new ArrayList<>();//new
        for(int i = 0 ; i < vertex.getDomain().size() ; i++) {//new
            int v = vertex.getDomain().get(i);
            domains.add(v);//new
        }

        //
        for(Integer integer : domains) {//new
            ArrayList<Position> positions = setVertex(vertex.getPosition(), integer);//new
//            System.out.println("setting vertex "+vertex);
            if(checkNeighbours2(vertex)) {
                int res = backTrack();
                if(res == POS_INF){
                    return POS_INF;
                }
            }
            backtracks++;
            resetVertex(vertex.getPosition(), positions);
//            System.out.println("resetting vertex "+vertex);
        }

        priorityQueue.add(vertex);
        return NEG_INF;
    }

    public int forwardChecking(){
//        Vertex vertex = priorityQueue.get(10);
//        System.out.println("in fc");
//        showNeighbours(vertex);
//        printBoard();
//        ArrayList<Position> positions = setVertex(vertex.getPosition(), vertex.getDomain().get(0));
//        System.out.println(vertex);
//        System.out.println("showing neighbours");
//        showNeighbours(vertex);
//        printBoard();
//        if(positions != null) {
//            boolean res = resetVertex(vertex.getPosition(), positions);
//            System.out.println("result of resetting "+res);
//        }
//        System.out.println(vertex);
//        System.out.println("showing neighbours again");
//        showNeighbours(vertex);
//        printBoard();
//        return 1;

        nodes++;
        //new
//        if(nodes > 10){
//            return 1;
//        }
        //
        if(priorityQueue.isEmpty()){
            return POS_INF;
        }
        Collections.sort(priorityQueue);
        Vertex vertex = priorityQueue.remove(0);
//        priorityQueue.remove(0);

        ArrayList<Integer> domains = new ArrayList<>();//new
        for(int i = 0 ; i < vertex.getDomain().size() ; i++) {//new
            int v = vertex.getDomain().get(i);
            domains.add(v);//new
        }

        //
        for(Integer integer : domains) {//new
            ArrayList<Position> positions = setVertex(vertex.getPosition(), integer);//new
//            System.out.println("setting vertex "+vertex);
            if(checkNeighbours(vertex)) {
                int res = forwardChecking();
                if(res == POS_INF){
                    return POS_INF;
                }
            }
//            backtracks++;
            resetVertex(vertex.getPosition(), positions);
//            System.out.println("resetting vertex "+vertex);
        }

        priorityQueue.add(vertex);
        return NEG_INF;
//        while(!priorityQueue.isEmpty()){
//            System.out.println(priorityQueue.remove(0));
//        }

//        System.out.println(list);
//        return 1;
    }

    public void printBoard(){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                System.out.print("\t"+board[i][j]);
            }
            System.out.println();
        }
    }

    public int getBacktracks() {
        return backtracks;
    }

    public boolean isChooseForwardDeg() {
        return chooseForwardDeg;
    }

    public void setChooseForwardDeg(boolean chooseForwardDeg) {
        this.chooseForwardDeg = chooseForwardDeg;
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }
}
