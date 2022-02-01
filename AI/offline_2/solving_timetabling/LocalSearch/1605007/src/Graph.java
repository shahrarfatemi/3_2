import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Graph {

    private int numberOfVertices;
    private int numberOfEdges;
    ArrayList<Vertex> vertices;

    public Graph() {
        vertices = new ArrayList<>();
    }

    public Graph(int numberOfVertices, int numberOfEdges) {
        vertices =  new ArrayList<>();
        this.numberOfVertices = numberOfVertices;
        this.numberOfEdges = numberOfEdges;
    }

    void addVertex(int v){
        Vertex vertex = new Vertex(v);
        vertices.add(vertex);
        numberOfVertices++;
    }

    Vertex findVertex(int v){
        for (Vertex vertex : vertices) {
            if(vertex.getVertexNo() == v){
                return vertex;
            }
        }
        return null;
    }

    void addEdge(int v1, int v2){
        Vertex vertex1 = findVertex(v1);
        Vertex vertex2 = findVertex(v2);
        if((vertex1 != null) && (vertex2 != null)){
            vertex1.addToList(vertex2);
            vertex2.addToList(vertex1);
            numberOfEdges++;
        }
    }

    boolean checkGraph(){
        for(Vertex vertex : vertices){
            for(int i = 0 ; i < vertex.getAdjList().size()-1 ; i++){
                Vertex v1 = vertex.getAdjList().get(i);
                for(int j = i+1 ; j < vertex.getAdjList().size() ; j++){
                    Vertex v2 = vertex.getAdjList().get(j);
                    if(v1.getVertexNo() == v2.getVertexNo()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    double kempeChainInterchange(int source, String stuFilename){

        boolean []visited = new boolean[numberOfVertices];

        LinkedList<Vertex> queue = new LinkedList<>();
        visited[source-1] = true;
        Vertex sourceVertex = findVertex(source);
        int colour1 = sourceVertex.getColour();
        Random random = new Random();
        int secondVertex = random.nextInt(sourceVertex.getAdjList().size())%(sourceVertex.getAdjList().size());
        int colour2 = sourceVertex.getAdjList().get(secondVertex).getColour();
        queue.add(sourceVertex);
        while(!queue.isEmpty()){
            sourceVertex = queue.poll();
            int colour = (sourceVertex.getColour()==colour1)? colour2:colour1;
            sourceVertex.setColour(colour);
            Iterator<Vertex> i = sourceVertex.getAdjList().iterator();
            while(i.hasNext()){
                Vertex v = i.next();
                int index = v.getVertexNo()-1;
                if(!visited[index] && v.getColour() == colour){
                    visited[index] = true;
                    queue.add(v);
                }
            }
        }
        double penalty = 0.0;
        try{
            penalty = this.calculatePenalty(stuFilename);
//            System.out.println("penalty => "+penalty);
        }catch (Exception e){
            System.out.println("Exception => "+e.getMessage());
        }
        return penalty;
    }

    int calculateSlots(){
        ArrayList<Vertex> vertices = new ArrayList<>();
        for(int i = 0 ; i < this.getNumberOfVertices() ; i++){
            vertices.add(this.getVertices().get(i));
        }
        //sorting the graph according to the sat degree then degree
        Collections.sort(vertices);
//        System.out.println(this.getVertices());
        boolean []COLOURS = new boolean[vertices.size()+1];
        for(int i = 0 ; i < vertices.size()+1 ; i++){
            COLOURS[i] = false;
        }
        Vertex temp;
        //setting colour for first vertex in sorted array
        //taking each vertex of the sorted array
//        System.out.println(vertices);
        while(!vertices.isEmpty()){
            temp = vertices.get(0);
            for(Vertex v : temp.getAdjList()){
                //checking if the vertex has a valid colour
                if(v.getColour() != 0){
                    //setting that colour of the COLOURS array true if the vertex has a valid colour
                    COLOURS[v.getColour()] = true;
                }
            }
            int j,c = 100000;
            //for all the vertices setting the colour back to false :3
            for(j = 1 ; j < this.getNumberOfVertices()+1 ; j++){
                if(COLOURS[j] == true){
                    COLOURS[j] = false;
                }
                //if the colour is false already, trying to make that colour the valid colour of the temp vertex
                else{
                    if(j < c){
                        c = j;
                        temp.setColour(c);
                    }
                }
            }
            if(temp.getColour() != 0) {
                for (Vertex v : temp.getAdjList()) {
                    v.setSaturationDegree(v.getSaturationDegree()+1);
                }
            }
            vertices.remove(0);
            Collections.sort(vertices);
        }
        //printing colour of all the vertices
//        for(int i = 0 ; i < this.getNumberOfVertices() ; i++){
//            temp = this.getVertices().get(i);
//            System.out.println(temp.getColour());
//        }
        //using a set to distinguish the number of colours :3
        HashSet<Integer> slots = new HashSet<>();
        for(int i = 0 ; i < this.getNumberOfVertices() ; i++){
            temp = this.getVertices().get(i);
            if(temp.getColour() != 0){
                slots.add(temp.getColour());
            }
        }
//        System.out.println(slots);
//        System.out.println("slots =>"+slots.size());
        return slots.size();
    }

    int difference(int c1, int c2){
        return ((c1 - c2) < 0)?(c2 - c1):(c1 - c2);
    }

    int calculatePairWiseCost(int diff){
        int res;
        switch (diff){
            case 1: res = 16;
                break;
            case 2: res = 8;
                break;
            case 3: res = 4;
                break;
            case 4: res = 2;
                break;
            case 5: res = 1;
                break;
            default:res = 0;
        }
        return res;
    }

    double calculatePenalty(String stuFileName) throws Exception{
        File fileStudent = new File("Toronto\\"+stuFileName);

        BufferedReader brStudent = new BufferedReader(new FileReader(fileStudent));
        int totalCost = 0, numberOfStudents = 0;
        String st;
        while ((st = brStudent.readLine()) != null) {
            numberOfStudents++;
            String [] strs = st.split(" ");
            for(int i = 0 ; i < strs.length-1 ; i++){
                int v1 = Integer.parseInt(strs[i]);
                Vertex vertex1 = this.findVertex(v1);
                for(int j = i+1 ; j < strs.length ; j++){
                    int v2 = Integer.parseInt(strs[j]);
                    Vertex vertex2 = this.findVertex(v2);
                    int pairCost = calculatePairWiseCost(difference(vertex1.getColour(), vertex2.getColour()));
                    totalCost += pairCost;
                }
            }
//            System.out.println("total cost "+totalCost);
//            System.out.println(st);
        }
//        System.out.println("no of students "+numberOfStudents);
        return (totalCost*1.0)/numberOfStudents;
    }


    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "numberOfVertices=" + numberOfVertices +
                ", numberOfEdges=" + numberOfEdges +
                ", vertices=" + vertices +
                '}';
    }
}
