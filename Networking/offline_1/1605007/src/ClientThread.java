import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientThread implements Runnable{
    Socket socket;
    Thread th;
    public ClientThread() {
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        try{
            while(true) {
                this.socket = new Socket("localhost", 6789);;
                Scanner scanner = new Scanner(System.in);
                System.out.println("Press 1 to exit");
                System.out.println("Enter request : ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("1")) {
                    socket.close();
                    return;
                }
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                if (input.startsWith("GET")) {
//                System.out.println("get request handled in browser!!");
                    pw.write(input + "\n");
                    pw.flush();
                } else if (input.startsWith("UPLOAD")) {
                    int count;
                    byte[] buffer = new byte[2];
                    String[] tokens = input.split(" ");
                    System.out.println(tokens[1]);
                    count = tokens.length;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < count - 1; i++) {
                        sb.append(tokens[i]);
                        sb.append(" ");
                    }
                    sb.append(tokens[count - 1]);
                    String token = sb.toString();
                    File file = new File(token);
                    System.out.println("file name in client : " + file.getName());
                    System.out.println("input " + input);

                    if (file.isFile()) {
                        pw.write("UPLOAD " + file.getName() + "\n");
                        pw.flush();
                        BufferedInputStream inp = new BufferedInputStream(new FileInputStream(file));
                        BufferedOutputStream out = new BufferedOutputStream(os);
                        while ((count = inp.read(buffer)) > 0) {
//                            System.out.println(buffer.toString());
                            out.write(buffer, 0, count);
                            out.flush();
                        }
//                out.flush();
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                System.out.println(in.readLine());
                        out.close();
                    } else {
                        System.out.println("file doesn't exist!!");
                        pw.write("Trying to upload with an invalid file name ->" + file.getName() + "\n");
                        pw.flush();
                    }
                }

//            BufferedOutputStream out = new BufferedOutputStream(fos);
//            byte[] buffer = new byte[1024];
//            int count;
//            InputStream in = socket.getInputStream();
//            while((count=in.read(buffer)) >0){
//                fos.write(buffer);
//            }
//            fos.close();
                socket.close();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
