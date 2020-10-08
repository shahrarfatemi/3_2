import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable{
    private int vertexNo;
    private List<Vertex> adjList;
    private int colour;
    private int degree;
    private int saturationDegree;

    public Vertex(int vertexNo) {
        adjList = new ArrayList<>();
        this.vertexNo = vertexNo;
        this.colour = 0;
        this.degree = 0;
        this.saturationDegree = 0;
    }

    public Vertex(int vertexNo, int colour) {
        adjList = new ArrayList<>();
        this.vertexNo = vertexNo;
        this.colour = colour;
        this.degree = 0;
        this.saturationDegree = 0;
    }

    boolean hasInList(Vertex v){
        if(adjList.contains(v)){
            return true;
        }
        return false;
    }

    void addToList(Vertex v){
        if(hasInList(v)){
            return;
        }
        this.adjList.add(v);
        this.degree++;
    }

    public int getVertexNo() {
        return vertexNo;
    }

    public void setVertexNo(int vertexNo) {
        this.vertexNo = vertexNo;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getSaturationDegree() {
        return saturationDegree;
    }

    public void setSaturationDegree(int saturationDegree) {
        this.saturationDegree = saturationDegree;
    }

    public List<Vertex> getAdjList() {
        return adjList;
    }

    public void setAdjList(List<Vertex> adjList) {
        this.adjList = adjList;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        String str = "";
        for(Vertex vertex : adjList){
            str += vertex.getVertexNo()+" ";
        }
        return "Vertex{" +
                "vertexNo=" + vertexNo +
                ", adjList=" + str +
                ", colour=" + colour +
                ", degree=" + degree +
                ", saturationDegree=" + saturationDegree +
                '}'+"\n";
    }

    @Override
    public int compareTo(Object o) {
        Vertex v = (Vertex) o;
        if(this.getSaturationDegree() == v.getSaturationDegree()){
//            System.out.println("in compare function");
            return -(this.getDegree() - v.getDegree());
        }
        return -(this.getSaturationDegree() - v.getSaturationDegree());
    }
}
