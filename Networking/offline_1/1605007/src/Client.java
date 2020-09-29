import java.net.Socket;

public class Client {
    Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) {
        try {
            new ClientThread();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
