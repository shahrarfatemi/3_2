import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public CSP csp;
    void init(){
        try {
            csp = new CSP("d-10-01.txt.txt",false);//true for brelaz,comment compare function for others
        }catch (Exception e){
            System.out.println(e);
        }
        csp.printBoard();
//        csp.printVertices();
//        csp.printValueMap();
//        System.out.println("queue size "+csp.priorityQueue.size()+"   "+csp.priorityQueue);
        int res = csp.forwardChecking();
        System.out.println("nodes" + " " + csp.getNodes()+"  backs  "+csp.getBacktracks());
        csp.printBoard();
//        csp.printValueMap();
//        csp.printVertices();
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        System.out.println("END");
    }
}
