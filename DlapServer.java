import java.net.*;
import java.io.*;
import java.util.*;

public class DlapServer {

    static final String FILE_NAME = "logs.txt";

    public static void main(String[] args) throws Exception {
        int port = 8888;
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Server bound to port: " + port);
            while (true) {
                try (Socket socket = ss.accept()) {
                    System.out.println("Connected to client: " + socket.getInetAddress());

                    BufferedReader headerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

                    String header = headerReader.readLine();
                    if (header == null) continue;

                    StringTokenizer strk = new StringTokenizer(header, " ");
                    String command = strk.nextToken().toUpperCase();

                    if (command.equals("COUNT")) {
                        analyzeCount(headerWriter);
                    } 
                    else if (command.equals("DISTRIBUTION")) {
                        analyzeDistribution(headerWriter);
                    }
                    else {
                        headerWriter.write("ERROR INVALID_COMMAND\n");
                        headerWriter.flush();
                    }

                } catch (Exception e) {
                    System.err.println("Interaction error: " + e.getMessage());
                }
            }
        }
    }

    static void analyzeCount(BufferedWriter headerWriter) throws Exception {
        int count = 0;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(FILE_NAME))) {
            while (fileReader.readLine() != null) {
                count++;
            }
        }
        headerWriter.write("OK \n");
        headerWriter.flush();
        headerWriter.write("TOTAL=" + count);
        headerWriter.flush();
    }

    static void analyzeDistribution(BufferedWriter headerWriter) throws Exception {
        ArrayList<String> statusCodes = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length >= 2) {
                    statusCodes.add(parts[parts.length - 2]);
                }
            }
        }

        ArrayList<String> counted = new ArrayList<>();
        StringBuilder response = new StringBuilder();
        for (String code : statusCodes) {
            if (!counted.contains(code)) {
                int count = 0;
                for (String c : statusCodes) {
                    if (c.equals(code)) count++;
                }
                response.append(code).append("=").append(count).append("\n");
                counted.add(code);
            }
        }

        headerWriter.write("OK \n");
        headerWriter.flush();
        headerWriter.write(response.toString());
        headerWriter.flush();
    }
}
