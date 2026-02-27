import java.net.*;
import java.io.*;
import java.util.*;

public class DlapServer {
    public static void main(String[] args) throws Exception {
        int port = 8888; // Selecting a specific port for passive opening 

        // 1. Create ServerSocket in try-with-resources 
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("DLAP Server listening on port " + port + "...");

            while (true) {
                try (Socket socket = ss.accept()) {
                    // 3. Establish I/O streams for the connection handle
                    BufferedReader headerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

                    // 4. Read Header
                    String header = headerReader.readLine(); 
                    if (header == null) continue;

                    StringTokenizer strk = new StringTokenizer(header, " ");
                    String command = strk.nextToken();
                    String fileName = strk.nextToken();

                    // 5. Processing (Remote Analysis Logic) 
                    if (command.equals("COUNT")) {
                        long count = performRemoteCount(fileName); 
                        
                        // Send Success Reply Header
                        headerWriter.write("OK " + count + "\n");
                        headerWriter.flush();
                    } 
                    else if (command.equals("DIST")) {
                        byte[] distributionData = performRemoteDist(fileName);
                        
                        // Send Reply Header with Metadata (Size) 
                        headerWriter.write("OK " + distributionData.length + "\n");
                        headerWriter.flush();
                        
                        // Send the Delivery (Body)
                        dataOut.write(distributionData);
                        dataOut.flush();
                    } 
                    else {
                        headerWriter.write("ERROR INVALID_COMMAND\n");
                        headerWriter.flush();
                    }
                } catch (Exception e) {
                    System.err.println("Interaction error: " + e.getMessage());
                } 
                // Context and state cleared automatically when inner try-block ends 
            }
        }
    }
