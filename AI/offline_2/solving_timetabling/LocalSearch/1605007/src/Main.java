import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Main {

    Graph createGraph(String crsFileName, String stuFileName) throws Exception{
        File file = new File("Toronto\\"+crsFileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        File fileStudent = new File("Toronto\\"+stuFileName);

        BufferedReader brStudent = new BufferedReader(new FileReader(fileStudent));

        Graph graph = new Graph();

        String st;
        while ((st = br.readLine()) != null) {
            String [] strs = st.split(" ");
            int courseNo = Integer.parseInt(strs[0]);
            graph.addVertex(courseNo);
//            System.out.println(strs[0]);
        }


        while ((st = brStudent.readLine()) != null) {
            String [] strs = st.split(" ");
            for(int i = 0 ; i < strs.length-1 ; i++){
                int v1 = Integer.parseInt(strs[i]);
                for(int j = i+1 ; j < strs.length ; j++){
                    int v2 = Integer.parseInt(strs[j]);
                    graph.addEdge(v1, v2);
                }
            }
//            System.out.println(st);
        }
        return graph;
    }

    void printResult(int slots, double penalty, double minPenalty, double duration){
        System.out.println("Time slots => "+slots);
        System.out.println("Penalty => "+penalty);
        System.out.println("Minimized Penalty => "+minPenalty);
        System.out.println("Duation => "+duration);
    }


    public static void main(String[] args) throws Exception{

        long progStart = System.currentTimeMillis();
        String []crsFileNames = {"car-s-91.crs","car-f-92.crs","kfu-s-93.crs","tre-s-92.crs","yor-f-83.crs"};
        String []stuFileNames = {"car-s-91.stu","car-f-92.stu","kfu-s-93.stu","tre-s-92.stu","yor-f-83.stu"};
        Main main = new Main();
        Graph []graphs = new Graph[5];
//        for(int k = 0 ; k < 5 ; k++) {
//            System.out.println(graphs[k]);
//        }

        for(int k = 0 ; k < 5 ; k++) {
            graphs[k] = main.createGraph(crsFileNames[k], stuFileNames[k]);
        }



        for(int k = 0 ; k < 5 ; k++) {
            int slots = graphs[k].calculateSlots();
            double penalty = graphs[k].calculatePenalty(stuFileNames[k]);
            Random random = new Random();
            double min = 10000.0, pen;
            long start = System.currentTimeMillis();
            System.out.println("Running for the Coursefile : "+crsFileNames[k]);
            for (int i = 0; i < 5000; i++) {
                int s = random.nextInt(graphs[k].getNumberOfVertices() - 1) + 1;
                if (graphs[k].getVertices().get(s - 1).getAdjList().size() > 0) {
                    pen = graphs[k].kempeChainInterchange(s, stuFileNames[k]);
                    if (pen < min) {
                        min = pen;
                    }
                }
            }
            long end = System.currentTimeMillis();
            double duration = (end - start) * 1.0 / 1000;
            main.printResult(slots, penalty, min, duration);
        }

        long progEnd = System.currentTimeMillis();
        System.out.println("program duration => "+(progEnd - progStart) * 1.0 / 1000);

    }
}



