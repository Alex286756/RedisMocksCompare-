package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Mocks {
    protected int port;

    public String getCommandRunResult(String command) throws IOException {
        String response = null;
        try (Socket clientSocket = new Socket("localhost", port)) {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            String word = command + "\n";
            outputStream.write(word.getBytes());

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))) {
                response = bf.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }
}
