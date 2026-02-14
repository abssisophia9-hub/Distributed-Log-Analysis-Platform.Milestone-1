// imports
import java.net.*;
import java.util.*;
import java.io.*;

// class
public class DlapClient {

    public static void main(String[] args) throws Exception {

        String command = args[0];
        String fileName = args[1];

        // open connection
        try (Socket socket = new Socket("localhost", 80)) {

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
            BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));

            if (command.equals("c")) {

                String header = "COUNT" + " " + fileName + "\n";
                headerWriter.write(header);
                headerWriter.flush();

                String response = headerReader.readLine();

                if (response.equals("Error")) {
                    System.out.println("Sorry an error has occurred");
                } else {
                    StringTokenizer strk = new StringTokenizer(response, " ");
                    String status = strk.nextToken();

                    if (status.equals("Done")) {
                        int result = Integer.parseInt(strk.nextToken());
                        System.out.println(
                                "The result of the count service requested is as follows: "
                                        + result
                        );
                    }
                }

            } else if (command.equals("cd")) {

                String header = "CODE DISTRIBUTION" + " " + fileName + "\n";
                headerWriter.write(header);
                headerWriter.flush();

                String response = headerReader.readLine();

                if (response.equals("Error")) {
                    System.out.println("Sorry an error has occurred");
                } else {
                    StringTokenizer strk = new StringTokenizer(response, " ");
                    String status = strk.nextToken();

                    if (status.equals("Done")) {
                        int size = Integer.parseInt(strk.nextToken());

                        DataInputStream dataIn = new DataInputStream(in);
                        byte[] space = new byte[size];
                        dataIn.readFully(space);

                        System.out.println(
                                "The code distribution is as follows: "
                                        + Arrays.toString(space)
                        );
                    }
                }

            } else {
                System.out.println("Sorry you are connected to the wrong service");
            }
        }
    }
}
