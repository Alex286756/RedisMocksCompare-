package org.example;

import org.example.javamock.JavaMock;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Mocks {
    protected int port;
    protected JavaMock javaMock;
    protected String mockName;

    public String getCommandRunResult(String command) {
        String[] splitOfCommand = command.split(" ");
        String response = "";
        Command commandName;
        Object result;
        if (splitOfCommand.length > 1) {
            commandName = Command.valueOf(splitOfCommand[0].trim().toUpperCase());
            String[] args = new String[splitOfCommand.length - 1];
            System.arraycopy(splitOfCommand, 1, args, 0, splitOfCommand.length - 1);
            result = javaMock.getClient().sendCommand(commandName, args);
        } else {
            commandName = Command.valueOf(command.trim().toUpperCase());
            result = javaMock.getClient().sendCommand(commandName);
        }
        response = new String((byte[])result);
        return response;
    }

    public void runCommand(String cmd) {
        try {
            String result = getCommandRunResult(cmd);
            System.out.println(mockName + ": " + cmd + " -> " + result);
        } catch (IllegalArgumentException e) {
            System.out.println(mockName + ": команда " + cmd + " не поддерживается.");
        } catch (JedisDataException e) {
            System.out.println(mockName + ": " + cmd + " -> " + e.getMessage());
        } catch (Exception e) {
            System.out.println(mockName + ": " + cmd + " = " + e.getMessage());
        }
    }
//    public String getCommandRunResult(String command) throws IOException {
//        String response = "";
//        try (Socket clientSocket = new Socket("localhost", port)) {
//            InputStream inputStream = clientSocket.getInputStream();
//            OutputStream outputStream = clientSocket.getOutputStream();
//
//            String word = command + "\n";
//            outputStream.write(word.getBytes());
//
//            try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))) {
////                response = bf.readLine();
//                while (bf.ready() || response.isEmpty()) {
//                    response += bf.readLine();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return response;
//    }

//    public String getOneCommand(String command) throws IOException {
//        try (Socket clientSocket = new Socket("localhost", port)) {
//            InputStream inputStream = clientSocket.getInputStream();
//            OutputStream outputStream = clientSocket.getOutputStream();
//
//            outputStream.write(command.getBytes());
//
//            try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))){
////                String result = "trying";
////                if (bf.ready()) {
//                    long count = bf.lines().count();
//                    return String.valueOf(count);
////                }
////                return result;
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
