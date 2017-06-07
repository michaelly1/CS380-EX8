//Michael Ly, CS380

import java.io.*;
import java.net.*;

public class WebServer {

    public static void main(String[] args)
    {
        try{
            //server running on port 8080
            ServerSocket serversocket = new ServerSocket(8080);

            while(!serversocket.isClosed()) {
                Socket socket = serversocket.accept();

                //multithreaded server
                Runnable runserver = () -> {
                    try {
                        InputStream is = socket.getInputStream();
                        OutputStream os = socket.getOutputStream();

                        //gets subfolder/file name
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        PrintWriter pw = new PrintWriter(os, true);

                        //Http request
                        String bRead = br.readLine();
                        String path = "www" +bRead.split(" ")[1];
                        File htmlfile = new File(path);

                        //checks if html page/file exists HTML response
                        //possibly need to fix formatting from printWriter?
                        if(htmlfile.exists()) {
                            System.out.println("200");
                            BufferedReader fbr = new BufferedReader(new FileReader(htmlfile));

                            pw.println("HTTP/1.1 200 OK\n" +
                                    "Content-type: text/html\n" +
                                    "Content-length: " + htmlfile.length());

                            String tempRead;
                            while((tempRead = fbr.readLine()) != null) {
                                pw.println(tempRead + "\n" );
                            }
                            fbr.close();
                            pw.close();
                        }
                        else if(!htmlfile.equals("www/hello.html")){

                            System.out.println("404");
                            File file404 = new File("www/404.html");
                            BufferedReader temp = new BufferedReader(new FileReader(file404));

                            //can only get it to print in very weird format/nonhtml format, need to fix
                            pw.println();
                            pw.println("HTTP/1.1 404 Not Found\n" +
                                    "Content-type: text/html\n" +
                                    "Content-length: "+ htmlfile.length());

                            String tempRead;
                            while((tempRead = temp.readLine()) != null) {
                                pw.println(tempRead + "\n" );
                            }
                            temp.close();
                            pw.close();
                        }
                    }
                    catch(Exception e2) {
                        e2.printStackTrace();
                    }
                };

                Thread webserver = new Thread(runserver);
                webserver.start();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}


