//Work needed
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Router implements Serializable {
    private int routerId;
    private int numberOfInterfaces;
    private ArrayList<IPAddress> interfaceAddresses;//list of IP address of all interfaces of the router
    private ArrayList<RoutingTableEntry> routingTable;//used to implement DVR
    private ArrayList<Integer> neighborRouterIDs;//Contains both "UP" and "DOWN" state routers
    private Boolean state;//true represents "UP" state and false is for "DOWN" state
    private Map<Integer, IPAddress> gatewayIDtoIP;
    public Router() {
        interfaceAddresses = new ArrayList<>();
        routingTable = new ArrayList<>();
        neighborRouterIDs = new ArrayList<>();

        /**
         * 80% Probability that the router is up
         */
        Random random = new Random();
        double p = random.nextDouble();
        if(p < 0.80) state = true;
        else state = false;

        numberOfInterfaces = 0;
    }

    public Router(int routerId, ArrayList<Integer> neighborRouters, ArrayList<IPAddress> interfaceAddresses, Map<Integer, IPAddress> gatewayIDtoIP) {
        this.routerId = routerId;
        this.interfaceAddresses = interfaceAddresses;
        this.neighborRouterIDs = neighborRouters;
        this.gatewayIDtoIP = gatewayIDtoIP;
        routingTable = new ArrayList<>();



        /**
         * 80% Probability that the router is up
         */
//        Random random = new Random();
//        double p = random.nextDouble();
//        if(p < 0.80) state = true;
//        else state = false;

        state = true;

        numberOfInterfaces = interfaceAddresses.size();
    }

    @Override
    public String toString() {
        String string = "";
        string += "Router ID: " + routerId + "\n" + "Interfaces: \n";
        for (int i = 0; i < numberOfInterfaces; i++) {
            string += interfaceAddresses.get(i).getString() + "\t";
        }
        string += "\n" + "Neighbors: \n";
        for(int i = 0; i < neighborRouterIDs.size(); i++) {
            string += neighborRouterIDs.get(i) + "\t";
        }
        return string;
    }



    /**
     * Initialize the distance(hop count) for each router.
     * for itself, distance=0; for any connected router with state=true, distance=1; otherwise distance=Constants.INFTY;
     */
    public void initiateRoutingTable() {
        for(Router router : NetworkLayerServer.routers) {
            double dist = 1.0;
            if (!router.getState()){
                dist = Constants.INFINITY;
            }
            int ID = router.getRouterId();
            if (ID == routerId) {
                routingTable.add(new RoutingTableEntry(ID, 0, ID));
            } else if (neighborRouterIDs.contains(ID)) {
                routingTable.add(new RoutingTableEntry(ID, dist, ID));
            } else {
                routingTable.add(new RoutingTableEntry(ID, Constants.INFINITY,-1));
            }

        }
    }

    /**
     * Delete all the routingTableEntry
     */
    public void clearRoutingTable() {
        routingTable.clear();
        routingTable = new ArrayList<>();
    }

    /**
     * Update the routing table for this router using the entries of Router neighbor
     * @param neighbor
     */
    public boolean updateRoutingTable(Router neighbor) {
        boolean isUpdated = false;
        double distance = 1;
//        for(RoutingTableEntry routingTableEntry : routingTable){
//            if(routingTableEntry.getRouterId() == neighbor.getRouterId()){
//                distance = routingTableEntry.getDistance();
//            }
//        }
        int itr = 0;
        for(RoutingTableEntry entry : routingTable) {
//            System.out.println("neighbor Id "+neighbor.getRouterId());
            if(itr<neighbor.getRoutingTable().size()) {
                RoutingTableEntry tableEntry = neighbor.getRoutingTable().get(itr++);
                if ((tableEntry.getDistance() + distance) < entry.getDistance()) {
                    entry.setDistance(tableEntry.getDistance() + distance);
                    entry.setGatewayRouterId(neighbor.getRouterId());//setting neighbour as the next hop
                    isUpdated = true;
                }
            }
        }
        return isUpdated;
    }

    public boolean sfupdateRoutingTable(Router neighbor) {
        boolean isUpdated = false;
        double distance = 1;
//        for(RoutingTableEntry routingTableEntry : routingTable){
//            if(routingTableEntry.getRouterId() == neighbor.getRouterId()){
//                distance = routingTableEntry.getDistance();
//            }
//        }
        int itr = 0;
//        System.out.println("for router : "+this.getRouterId());
        for(RoutingTableEntry entry : routingTable) {
//            System.out.println("neighbor Id "+neighbor.getRouterId());
            if(itr<neighbor.getRoutingTable().size()) {
                RoutingTableEntry tableEntry = neighbor.getRoutingTable().get(itr++);
                double dist = tableEntry.getDistance() + distance;
                if (((dist != entry.getDistance()) && (entry.getGatewayRouterId() == neighbor.getRouterId())) || //nextHop(x,y) == z && so that same distance doesn't get updated again
                        ((dist < entry.getDistance()) && //d < d(x,y)
                                (this.getRouterId() != tableEntry.getGatewayRouterId()))) { // x!= nextHop(z,y)
                    entry.setDistance(dist);
                    entry.setGatewayRouterId(neighbor.getRouterId());//setting neighbour as the next hop
                    isUpdated = true;
                }
            }
        }
//        System.out.println("updated : "+isUpdated);
//        printRoutingTable();
        return isUpdated;
    }



    /**
     * If the state was up, down it; if state was down, up it
     */
    public void revertState() {
        state = !state;
        if(state) { initiateRoutingTable(); }
        else { clearRoutingTable(); }
    }

    public int getRouterId() {
        return routerId;
    }

    public void setRouterId(int routerId) {
        this.routerId = routerId;
    }

    public int getNumberOfInterfaces() {
        return numberOfInterfaces;
    }

    public void setNumberOfInterfaces(int numberOfInterfaces) {
        this.numberOfInterfaces = numberOfInterfaces;
    }

    public ArrayList<IPAddress> getInterfaceAddresses() {
        return interfaceAddresses;
    }

    public void setInterfaceAddresses(ArrayList<IPAddress> interfaceAddresses) {
        this.interfaceAddresses = interfaceAddresses;
        numberOfInterfaces = interfaceAddresses.size();
    }

    public ArrayList<RoutingTableEntry> getRoutingTable() {
        return routingTable;
    }

    public void addRoutingTableEntry(RoutingTableEntry entry) {
        this.routingTable.add(entry);
    }

    public ArrayList<Integer> getNeighborRouterIDs() {
        return neighborRouterIDs;
    }

    public void setNeighborRouterIDs(ArrayList<Integer> neighborRouterIDs) { this.neighborRouterIDs = neighborRouterIDs; }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Map<Integer, IPAddress> getGatewayIDtoIP() { return gatewayIDtoIP; }

    public void printRoutingTable() {
        System.out.println("Router " + routerId);
        System.out.println("DestID Distance Nexthop");
        for (RoutingTableEntry routingTableEntry : routingTable) {
            System.out.println(routingTableEntry.getRouterId() + " " + routingTableEntry.getDistance() + " " + routingTableEntry.getGatewayRouterId());
        }
        System.out.println("-----------------------");
    }
    public String strRoutingTable() {
        String string = "Router" + routerId + "\n";
        string += "DestID Distance Nexthop\n";
        for (RoutingTableEntry routingTableEntry : routingTable) {
            string += routingTableEntry.getRouterId() + " " + routingTableEntry.getDistance() + " " + routingTableEntry.getGatewayRouterId() + "\n";
        }

        string += "-----------------------\n";
        return string;
    }

}
