import java.util.ArrayList;
import java.util.Random;

//Work needed
public class Client {
    public static void main(String[] args) throws InterruptedException {
        NetworkUtility networkUtility = new NetworkUtility("127.0.0.1", 4444);
        System.out.println("Connected to server");
        /**
         * Tasks
         */
        
        /*
        1. Receive EndDevice configuration from server
        2. Receive active client list from server
        3. for(int i=0;i<100;i++)
        4. {
        5.      Generate a random message
        6.      Assign a random receiver from active client list
        7.      if(i==20)
        8.      {
        9.            Send the message and recipient IP address to server and a special request "SHOW_ROUTE"
        10.           Display routing path, hop count and routing table of each router [You need to receive
                            all the required info from the server in response to "SHOW_ROUTE" request]
        11.     }
        12.     else
        13.     {
        14.           Simply send the message and recipient IP address to server.
        15.     }
        16.     If server can successfully send the message, client will get an acknowledgement along with hop count
                    Otherwise, client will get a failure message [dropped packet]
        17. }
        18. Report average number of hops and drop rate
        */
        EndDevice sourceDevice = (EndDevice) networkUtility.read();
        System.out.println("s => "+sourceDevice.getIpAddress());
        Random random = new Random();
        for(int i=0;i<10;i++) {
            EndDevice destinationDevice = (EndDevice) networkUtility.read();
            System.out.println("source IP " + sourceDevice.getIpAddress()+" destination IP " + destinationDevice.getIpAddress());
            String msg = "Hello " + random.nextInt(1000) + random.nextInt(1000) + " !!";
            System.out.println(sourceDevice.getDeviceID() + " => " + msg);
//            if (i == 20) {
            Packet dataPacket = new Packet(msg, "SHOW_ROUTE", sourceDevice.getIpAddress(), destinationDevice.getIpAddress());
            networkUtility.write(dataPacket);
            System.out.println("packet sent");
            Object res = networkUtility.read();
            System.out.println("response received");
            if (res instanceof ArrayList) {
                ArrayList<Router> route = (ArrayList<Router>) res;
                System.out.println("Route ");
                for(int k = 0 ; k < route.size() ; k++){
                    System.out.print(route.get(k).getRouterId()+" ");
                }
                System.out.println();
                for (int k = 0; k < route.size(); k++) {
                    route.get(k).printRoutingTable();
                }
                System.out.println("number of hops : " + (int) networkUtility.read());
            } else {
                System.out.println((String) res);
            }
//            }
//            else {
//                Packet dataPacket = new Packet(msg,"",sourceDevice.getIpAddress(),destinationDevice.getIpAddress());
//                networkUtility.write(dataPacket);
//                System.out.println("packet sent");
//                String result = (String) networkUtility.read();
//                System.out.println("response received" + result);
//                if(result.equalsIgnoreCase("packet dropped")){
//                    System.out.println(result);
//                }
//                else {
//                    System.out.println("number of hops : "+(int)networkUtility.read());
//                }
//            }
        }


    }
}