import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {
    Socket socket;
    Thread th;
    int threadNo;
    static String root = "G:\\3-2\\ComputerNetworks\\Sessional\\Offline\\root";

    public ServerThread(Socket socket,int threadNo) {
        this.socket = socket;
        this.threadNo = threadNo;
        this.th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        try {
            File file = new File("index.html");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            StringBuilder tempSb = new StringBuilder();
            String line;
            String currDirectory = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }

            String content;
            //System.out.println(content);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pr = new PrintWriter(socket.getOutputStream());
            System.out.println("waiting");
            String input = in.readLine();
            System.out.println("server input : "+input);

            // String content = "<html>Hello</html>";
            if (input != null) {
                if (input.startsWith("GET")) {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("log.txt", true));
                    PrintWriter pw = new PrintWriter(bufferedWriter);
                    StringTokenizer tokens = new StringTokenizer(input, " ");
                    tokens.nextToken();
                    String token = tokens.nextToken();
                    String finalToken = token.replace("%20", " ");
                    System.out.println(" middle token : " + finalToken);
                    File file1 = new File(finalToken);
                    System.out.println("file/dir name : " + file1.getName());


                    if (file1.isDirectory()) {
                        currDirectory = finalToken;
                        String arr[] = file1.list();

                        int n = arr.length;

                        for (int i = 0; i < n; i++) {
                            System.out.println(arr[i]);
                            tempSb.append("\t\t<a href=\"" + currDirectory + "/" + arr[i] + "\">  " + arr[i] + "</a> <br>");
                            tempSb.append("\n");
                        }
                        System.out.println("No of entries in this directory " + n);
                        tempSb.append("\t</body>");
                        tempSb.append("\n");
                        tempSb.append("</html>");
                        tempSb.append("\n");
                        content = sb.toString() + tempSb.toString();
                        System.out.println(content);

                        pr.write("HTTP/1.1 200 OK\r\n");
                        pr.write("Server: Java HTTP Server: 1.0\r\n");
                        pr.write("Date: " + new Date() + "\r\n");
                        pr.write("Content-Type: text/html\r\n");
                        pr.write("Content-Length: " + content.length() + "\r\n");
                        pr.write("\r\n");
                        pr.write(content);
                        pr.flush();

                        pw.println("Thread# " + threadNo);
                        pw.println("HTTP Request: " + input);
                        pw.println("HTTP Status Code: 200 OK");
                        pw.println("HTTP Mime Type: " + Files.probeContentType(file1.toPath()));
                        pw.flush();

                    } else if (file1.isFile()) {
                        System.out.println("should download");
                        tempSb.append("\t\t<a href=\"" + currDirectory + "/" + file1.getName() + "\">  " + file1.getName() + "</a> <br>");
                        tempSb.append("\n");
                        tempSb.append("\t</body>");
                        tempSb.append("\n");
                        tempSb.append("</html>");
                        tempSb.append("\n");
                        content = sb.toString() + tempSb.toString();
                        System.out.println(content);

                        pr.write("HTTP/1.1 200 OK\r\n");
                        pr.write("Server: Java HTTP Server: 1.0\r\n");
                        pr.write("Date: " + new Date() + "\r\n");
                        pr.write("Content-Length: " + file1.length() + "\r\n");
                        pr.write("Content-Type: application/x-force-download\r\n");
                        pr.write("\r\n");
                        //pr.write(content);
                        pr.flush();

                        pw.println("Thread# " + threadNo);
                        pw.println("HTTP Request: " + input);
                        pw.println("HTTP Status Code: 200 OK");
                        pw.println("HTTP Mime Type: " + Files.probeContentType(file1.toPath()));
                        pw.flush();

                        int count;
                        byte[] buffer = new byte[1024];

                        OutputStream out = socket.getOutputStream();
                        BufferedInputStream inp = new BufferedInputStream(new FileInputStream(file1));
                        while ((count = inp.read(buffer)) > 0) {
                            out.write(buffer, 0, count);
                            out.flush();
                        }

                    } else {
                        System.out.println("File not found");
                        tempSb.append("<h1> Error : 404 not found </h1>");
                        tempSb.append("\n");
                        tempSb.append("\t</body>");
                        tempSb.append("\n");
                        tempSb.append("</html>");
                        tempSb.append("\n");
                        content = sb.toString() + tempSb.toString();
                        System.out.println(content);

                        pr.write("HTTP/1.1 404 Not Found\r\n");
                        pr.write("Server: Java HTTP Server: 1.0\r\n");
                        pr.write("Date: " + new Date() + "\r\n");
                        pr.write("Content-Length: " + content.length() + "\r\n");
                        pr.write("Content-Type: text/html\r\n");
                        pr.write("\r\n");
                        pr.write(content);
                        pr.flush();

                        pw.println("Thread# " + threadNo);
                        pw.println("HTTP Request: " + input);
                        pw.println("HTTP Status Code: 404 Not Found");
                        pw.flush();
                    }

                    pw.close();
                    bufferedWriter.close();


                }
                else if(input.startsWith("UPLOAD")){
//                    System.out.println("up e dhukseeee");
                    String [] tokens = input.split(" ");
//                    String token = tokens[1];
//                    System.out.println("token : "+token);
                    int count;
                    count = tokens.length;
                    StringBuilder sb1 = new StringBuilder();
                    for (int i = 1; i < count - 1; i++) {
                        sb1.append(tokens[i]);
                        sb1.append(" ");
                    }
                    sb1.append(tokens[count - 1]);
                    String fileName = sb1.toString();
                    System.out.println("file name in server : " + fileName);
                    System.out.println("input " + input);
                    String filePath = root+"/"+fileName;

                    byte[] buffer = new byte[1024];
                    OutputStream fo = new FileOutputStream(filePath);
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    while ((count = is.read(buffer)) > 0) {
//                        System.out.println(buffer.toString());
                        fo.write(buffer, 0, count);
//                        fo.flush();
                    }
                    fo.close();
                }

                else if(input.startsWith("POST")){
                    String [] tokens = input.split(" ");
                    String token = tokens[1];
                    int count;
                    count = tokens.length;
                    StringBuilder sb1 = new StringBuilder();
                    for (int i = 1; i < count - 1; i++) {
                        sb1.append(tokens[i]);
                        sb1.append(" ");
                    }
                    sb1.append(tokens[count - 1]);
                    String fileName = sb1.toString();
                    System.out.println("file name in server : " + fileName);
                    System.out.println("input " + input);
                    System.out.println("post request not handled");
                }

                else{
                    System.out.println(input);
                }
            }
            socket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
