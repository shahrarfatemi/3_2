import java.io.FileReader;
import java.util.*;

public class FifteenPuzzle implements Comparable{

    static int ROW  = 4;
    static int COL = 4;
    int blankPosition;
    int cost;
    static int LEFT = -1;
    static int RIGHT = 1;
    static int UP = -2;
    static int DOWN = 2;

    static int finalBoard[][] ={{1, 2, 3, 4},{5, 6, 7, 8},{9, 10, 11, 12},{13, 14, 15, 0}};

    public FifteenPuzzle(FifteenPuzzle previous, int[][] board, int level, int blankPosition) {
        this.previous = previous;
        this.level = level;
        this.blankPosition = blankPosition;
        this.board = new int [ROW][COL];
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                this.board[i][j] = board[i][j];
            }
        }
        cost = calculateCost(finalBoard);
    }

    int priority(){
        return cost+level;
    }

    FifteenPuzzle previous;

    int board[][];

    int level = 0;

    int mod(int n){
        if(n < 0)
            return -n;
        return n;
    }

    void printBox(int board[][]){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                System.out.print(board[i][j]+"\t");
            }
            System.out.println();
        }
    }

    void printMOVE(int turn){
        if(turn == LEFT){
            System.out.println("LEFT");
        }
        else if(turn == RIGHT){
            System.out.println("RIGHT");
        }
        else if(turn == UP){
            System.out.println("UP");
        }
        else if(turn == DOWN){
            System.out.println("DOWN");
        }
    }

    int countInv(int board[][]){
        int inv = 0;
        int A[] = new int[ROW*COL];
        int cnt = 0;
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                A[cnt++] = board[i][j];
            }
        }
        for(int i = 0 ; i < ROW*COL ; i++){
            for(int j = i + 1 ; j < ROW*COL ; j++){
                if((A[i] != 0) && (A[j] != 0) && (A[i] > A[j])){
                    inv++;
                }
            }
        }
        return inv;
    }

    boolean validPosition(int pos){
        if((pos < ROW) && (pos > -1) && (pos < COL)){
            return true;
        }
        return false;
    }

    int calculateCost(int dest[][]){
        int cost = 0,row,col;
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                if(board[i][j] != 0 && board[i][j] != dest[i][j]){
                    row = (board[i][j]-1)/ROW;
                    col = (board[i][j]-1)%COL;
                    cost += (mod(i-row)+mod(j-col));
                }
            }
        }
        //System.out.println("calculating cost : "+cost);
        return cost;
    }

    FifteenPuzzle createNode(FifteenPuzzle parent, int pos, int move, int level){

        int row = pos/ROW;
        int col = pos%COL;
        System.out.println("row "+ row+" col "+col);
        int tempBoard[][] = new int [ROW][COL];
        int blank = -1;
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                tempBoard[i][j] = parent.board[i][j];
            }
        }
        //LEFT
        if(move == LEFT) {
            if (validPosition(col - 1)) {//Column left
                tempBoard[row][col] = parent.board[row][col - 1];
                tempBoard[row][col - 1] = 0;
                blank = pos-1;
                //printBox(board);
            }
        }
        //RIGHT
        else if(move == RIGHT) {
            if (validPosition(col + 1)) {//Column right
                System.out.println("col +1 "+ col+1);
                tempBoard[row][col] = parent.board[row][col + 1];
                tempBoard[row][col + 1] = 0;
                blank = pos+1;
                //printBox(board);
            }
        }
        //UP
        else if(move == UP) {
            if (validPosition(row - 1)) {//Row up
                tempBoard[row][col] = parent.board[row - 1][col];
                tempBoard[row - 1][col] = 0;
                blank = pos-ROW;
                //printBox(board);
            }
        }
        //DOWN
        else if(move == DOWN) {
            if (validPosition(row + 1)) {//Row down
                tempBoard[row][col] = parent.board[row + 1][col];
                tempBoard[row + 1][col] = 0;
                blank = pos+ROW;
                //printBox(board);
            }
        }
        if(blank < 0){
            return null;
        }
        FifteenPuzzle fifteenPuzzle = new FifteenPuzzle(parent, tempBoard, level+1, blank);

        return fifteenPuzzle;
    }

    void printAll(FifteenPuzzle fifteenPuzzle){
        if(fifteenPuzzle.previous != null){
            printAll(fifteenPuzzle.previous);
        }
        fifteenPuzzle.printBox(fifteenPuzzle.board);
        System.out.println(fifteenPuzzle);
    }

    @Override
    public String toString() {
        return "FifteenPuzzle{" +
                "cost=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("inside equals");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FifteenPuzzle that = (FifteenPuzzle) o;
        return Arrays.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        System.out.println("inside hash");
        return Arrays.hashCode(board);
    }

    boolean finishGame(int board[][], int finalBoard[][]){
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                if(board[i][j] != finalBoard[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int board[][] = new int[4][4];
        int blankPosition=-1,cont=0;
        Scanner scanner = new Scanner(System.in);
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                board[i][j] = scanner.nextInt();
                if(board[i][j] == 0){
                    blankPosition = cont;
                }
                cont++;
            }
        }

        FifteenPuzzle fifteenPuzzle = new FifteenPuzzle(null, board, 0, blankPosition);
        int inversion = fifteenPuzzle.countInv(fifteenPuzzle.board);
        boolean solvable = false;
        if((((blankPosition/ROW)%2 == 0)&&(inversion%2)!=0) || (((blankPosition/ROW)%2 != 0)&&(inversion%2)==0)){
            System.out.println("Solvable");
            solvable = true;
        }
        else{
            System.out.println("Not Solvable");
        }

        if(solvable){
            final HashSet <FifteenPuzzle> closed = new HashSet<>();
            final PriorityQueue<FifteenPuzzle> queue = new PriorityQueue<>();
            queue.add(fifteenPuzzle);
            long st = System.currentTimeMillis();
            while(!queue.isEmpty()){
                FifteenPuzzle tPuzzle = queue.poll();
                System.out.println("open list size : "+queue.size());
                if(tPuzzle.finishGame(tPuzzle.board, finalBoard)){
                    tPuzzle.printAll(tPuzzle);
                    break;
                }
                closed.add(tPuzzle);
                FifteenPuzzle L = tPuzzle.createNode(tPuzzle, tPuzzle.blankPosition, LEFT, tPuzzle.level);
                FifteenPuzzle R = tPuzzle.createNode(tPuzzle, tPuzzle.blankPosition, RIGHT, tPuzzle.level);
                FifteenPuzzle U = tPuzzle.createNode(tPuzzle, tPuzzle.blankPosition, UP, tPuzzle.level);
                FifteenPuzzle D = tPuzzle.createNode(tPuzzle, tPuzzle.blankPosition, DOWN, tPuzzle.level);
                if(L != null){
                    if(!closed.contains(L)) {
                        queue.add(L);
                    }
                }
                if(R != null){
                    if(!closed.contains(R)) {
                        queue.add(R);
                    }
                }
                if(U != null){
                    if(!closed.contains(U)) {
                        queue.add(U);
                    }
                }
                if(D != null){
                    if(!closed.contains(D)) {
                        queue.add(D);
                    }
                }

                System.out.println("open list size : "+queue.size());
            }
            long fin = System.currentTimeMillis();
            long dur = fin - st;
            System.out.println("expanded nodes : "+closed.size());
            System.out.println("duration : "+dur);
        }
    }

    @Override
    public int compareTo(Object o) {
        System.out.println("comparing");
        FifteenPuzzle that = (FifteenPuzzle)o;
        return this.priority() - that.priority();
    }
}
