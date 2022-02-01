import jdk.nashorn.internal.ir.IdentNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class Vertex implements Comparable<Vertex>{
    Position position;
    boolean assigned;
    ArrayList<Integer> domain;
    ArrayList<Integer> prevDomain;
    int domainSize;
    int maxForwardDegree;

    public Vertex(int size, Position position) {
        this.position = position;
        this.maxForwardDegree = 2*size - 1;//new
        this.domain = new ArrayList<>();
        for(int i = 0 ; i < size ; i++){
            domain.add(i+1);
        }
        this.assigned = false;
        this.domainSize = size;
        this.prevDomain = new ArrayList<>();
        for(Integer integer : domain){
            prevDomain.add(integer);
        }
    }

    public void updatePrevDomain(){
        prevDomain.clear();
        prevDomain.addAll(domain);
    }

    public void resetDomain(){
        domain.clear();
        domain.addAll(prevDomain);
        domainSize = domain.size();
    }

    boolean removeValue(Integer item){
        if(domainSize > 0) {
            if (domain.remove(item)) {
//                prevDomain.remove(item);
                domainSize--;
                return true;
            }
        }
        return false;
    }

    boolean addValue(Integer item){
        if(!domain.contains(item)) {
            domain.add(item);
//            prevDomain.add(item);
            domainSize++;
            return true;
        }
        return false;
    }

    boolean resetValue(){
        if (isAssigned()) {
            resetDomain();
            setAssigned(false);
            return true;
        }
        return false;
    }

    boolean setValue(Integer val){
        if (!isAssigned()) {
            updatePrevDomain();
            domain.clear();
            domain.add(val);
            domainSize = 1;
            setAssigned(true);
            return true;
        }
        return false;
    }

    //new
    boolean decreaseMaxForwardDegree(){
        if(maxForwardDegree > 0){
            maxForwardDegree--;
            return true;
        }
        return false;
    }

    void increaseMaxForwardDegree(){
            maxForwardDegree++;
    }
    //


    public int getMaxForwardDegree() {
        return maxForwardDegree;
    }

    public int getDomainSize() {
        return domainSize;
    }

    public void setDomainSize(int domainSize) {
        this.domainSize = domainSize;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public ArrayList<Integer> getDomain() {
        return domain;
    }

    public void setDomain(ArrayList<Integer> domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return this.position.equals(vertex.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this .getPosition(),this.getDomainSize());
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "position=" + position +
                ", assigned=" + assigned +
                "\n, domain=" + domain +
                ", domainSize=" + domainSize +
                "}\n";
    }


    @Override
    public int compareTo(Vertex o) {
//        if(this.getDomainSize() == o.getDomainSize()){
////            System.out.println("comparing forwardDegree 1)"+this.getPosition()+"2)"+o.getPosition()+"  "+this.getMaxForwardDegree()+" "+o.getMaxForwardDegree());
//            return Integer.compare(this.getMaxForwardDegree(), o.getMaxForwardDegree());//brelaz
//        }
        return Integer.compare(this.getDomainSize(),o.getDomainSize());//sdf
//        return Integer.compare(o.getMaxForwardDegree(),this.maxForwardDegree);//max
//        return Integer.compare(this.getMaxForwardDegree(), o.getMaxForwardDegree());//min
//        return Double.compare((this.getDomainSize()*1.0)/this.getMaxForwardDegree(),
//                o.getDomainSize()*1.0/o.getMaxForwardDegree());/domddeg
    }

}
