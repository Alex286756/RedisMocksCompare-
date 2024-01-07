package org.example.pythonmock;

import org.example.Mocks;
import org.example.commandsdocs.CommandDocs;
import org.example.javamock.JavaMock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.io.IOException;

public class PythonMockExecute extends Mocks {

    public PythonMockExecute() {
        super();
        port = 40153;
//        port = 6379;
        mockName = "Python";
        try {
            javaMock = new JavaMock(port);
            System.out.println("Start on Python...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getPyCommandRunResult(String command) throws IOException {
        String response = "";
        try (Socket clientSocket = new Socket("localhost", port)) {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            String word = command + "\n";
            outputStream.write(word.getBytes());

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))) {
                while (bf.ready() || response.isEmpty()) {
                    response += bf.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(mockName + ": " + command + " -> " + response);
    }
}
