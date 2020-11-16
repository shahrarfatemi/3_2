

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class ServerThread implements Runnable {

    NetworkUtility networkUtility;
    EndDevice endDevice;
    ArrayList<Router> routeInfo = null;
    int hopCount = 0;
    ServerThread(NetworkUtility networkUtility, EndDevice endDevice) {
        this.networkUtility = networkUtility;
        this.endDevice = endDevice;
        System.out.println("Server Ready for client " + NetworkLayerServer.clientCount);
        NetworkLayerServer.clientCount++;
        new Thread(this).start();
    }

    @Override
    public void run() {
        int totalHopCount = 0;
        int totalDropCount = 0;
        /**
         * Synchronize actions with client.
         */
        
        /*
        Tasks:
        1. Upon receiving a packet and recipient, call deliverPacket(packet)
        2. If the packet contains "SHOW_ROUTE" request, then fetch the required information
                and send back to client
        3. Either send acknowledgement with number of hops or send failure message back to client
        */
        networkUtility.write(endDevice);
        Random random = new Random();
        for(int i = 0 ; i < 10 ; i++){
            int ran = random.nextInt(NetworkLayerServer.endDevices.size());
            EndDevice device = NetworkLayerServer.endDevices.get(ran);
//            if(device == null){
//                System.out.println("ami null "+ran);
//            }
            while(device.getDeviceID() == endDevice.getDeviceID()){
                ran = random.nextInt(NetworkLayerServer.endDevices.size());

//                System.out.println("while e ran "+ran);
                device = NetworkLayerServer.endDevices.get(ran);
            }
//            System.out.println("ran "+ran);
            networkUtility.write(device);
//            System.out.println("i "+i);
            Packet packet = (Packet) networkUtility.read();
//            System.out.println("Packet received "+ packet.getMessage());
            if(deliverPacket(packet)) {
                if (packet.getSpecialMessage().equalsIgnoreCase("SHOW_ROUTE")) {
                    if(routeInfo!= null){
                        networkUtility.write(routeInfo);
                        networkUtility.write(hopCount);
                    }
                } else {
                    networkUtility.write("sending successful");
                    networkUtility.write(hopCount);
                }

            }
            else {
                totalDropCount++;
                networkUtility.write("packet dropped");
            }
            totalHopCount += hopCount;
            routeInfo = null;
            hopCount = 0;
        }


        System.out.println("For Device => "+ endDevice.getDeviceID()+
                " avg hopCount "+(totalHopCount*1.0)/100.0+" droprate "+(totalDropCount*1.0/100.0));


//        try {
//            FileWriter csvWriter = new FileWriter("report.csv",true);
//            csvWriter.append(""+(endDevice.getDeviceID()+1));
//            csvWriter.append(",");
//            csvWriter.append(""+(totalHopCount*1.0)/100.0);
//            csvWriter.append(",");
//            csvWriter.append(""+(totalDropCount*1.0)/100.0);
//            csvWriter.append("\n");
//            csvWriter.flush();
//            csvWriter.close();
////            Thread.sleep(5000);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }


    }


    public Boolean deliverPacket(Packet p) {

        
        /*
        1. Find the router s which has an interface
                such that the interface and source end device have same network address.
        2. Find the router d which has an interface
                such that the interface and destination end device have same network address.
        3. Implement forwarding, i.e., s forwards to its gateway router x considering d as the destination.
                similarly, x forwards to the next gateway router y considering d as the destination,
                and eventually the packet reaches to destination router d.

            3(a) If, while forwarding, any gateway x, found from routingTable of router r is in down state[x.state==FALSE]
                    (i) Drop packet
                    (ii) Update the entry with distance Constants.INFTY
                    (iii) Block NetworkLayerServer.stateChanger.t
                    (iv) Apply DVR starting from router r.
                    (v) Resume NetworkLayerServer.stateChanger.t

            3(b) If, while forwarding, a router x receives the packet from router y,
                    but routingTableEntry shows Constants.INFTY distance from x to y,
                    (i) Update the entry with distance 1
                    (ii) Block NetworkLayerServer.stateChanger.t
                    (iii) Apply DVR starting from router x.
                    (iv) Resume NetworkLayerServer.stateChanger.t

        4. If 3(a) occurs at any stage, packet will be dropped,
            otherwise successfully sent to the destination router
        */

        IPAddress sourceIP = p.getSourceIP();
        EndDevice sourceDevice = NetworkLayerServer.endDeviceMap.get(sourceIP);
//        System.out.println(NetworkLayerServer.endDeviceMap);
        System.out.println("source IP : "+sourceIP);
        Integer sourceRouterID = NetworkLayerServer.interfacetoRouterID.get(sourceDevice.getGateway());
        Router sRouter = NetworkLayerServer.routerMap.get(sourceRouterID);
        IPAddress destinationIP = p.getDestinationIP();
        EndDevice destinationDevice = NetworkLayerServer.endDeviceMap.get(destinationIP);
        Integer destinationRouterID = NetworkLayerServer.interfacetoRouterID.get(destinationDevice.getGateway());
        Router dRouter = NetworkLayerServer.routerMap.get(destinationRouterID);
        if((sRouter != null)&&(dRouter != null)){
            System.out.println("Source : "+sRouter.getRouterId() +" Destination : "+dRouter.getRouterId());
            int dRouterIndex = -1,itr=0;
            for(RoutingTableEntry entry : sRouter.getRoutingTable()){
                if(entry.getRouterId() == dRouter.getRouterId()){
                    dRouterIndex = itr;
                }
                itr++;
            }
            if(sRouter.getState() && dRouterIndex > -1) {
                ArrayList<Router> route = new ArrayList<>();
                Router temp = sRouter;
                route.add(temp);
                while (true) {
                    if (temp.getRouterId() == dRouter.getRouterId()) {
                        hopCount++;
                        routeInfo = route;
                        break;
                    }
                    RoutingTableEntry entry = temp.getRoutingTable().get(dRouterIndex);
                    int gatewayID = entry.getGatewayRouterId();
//                    System.out.println("gatewayRouter : " + gatewayID);
                    if (gatewayID == -1 || hopCount > Constants.INFINITY) {
                        routeInfo = null;
                        return false;
                    }
                    else {
                        Router temp1 = NetworkLayerServer.routerMap.get(gatewayID);
                        if (temp1.getState()) {
                            hopCount++;
                            for (RoutingTableEntry tableEntry : temp1.getRoutingTable()) {// for 3(b)
                                if (tableEntry.getGatewayRouterId() == temp.getRouterId()) {
                                    if (tableEntry.getDistance() >= Constants.INFINITY) {
                                        tableEntry.setDistance(1);
//                                        System.out.println("stopping state changer");
//                                        synchronized (NetworkLayerServer.stateChanger.msg) {
//                                            RouterStateChanger.islocked = true;
                                        blockAndRunDVR(temp1.getRouterId());
//                                            RouterStateChanger.islocked = false;
//                                            RouterStateChanger.msg.notify();
//                                        }
                                    }
                                }
                            }
                            temp = temp1;
                            route.add(temp);
                        }
                        else {//for 3(a)
//                            System.out.println("router off"+temp1.getRouterId());
                            entry.setDistance(Constants.INFINITY);
//                            System.out.println("stopping state changer");
                            blockAndRunDVR(temp.getRouterId());
                            routeInfo = null;
                            return false;
                        }
                    }
                }
            }
            else {
                return false;
            }
        }
        else {
            routeInfo = null;
            return false;
        }
        return true;
    }

    private void blockAndRunDVR(int startingRouterID){
        synchronized (NetworkLayerServer.stateChanger.msg) {
            RouterStateChanger.islocked = true;
//            NetworkLayerServer.simpleDVR(startingRouterID);
            NetworkLayerServer.DVR(startingRouterID);
            RouterStateChanger.islocked = false;
            RouterStateChanger.msg.notify();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
}
