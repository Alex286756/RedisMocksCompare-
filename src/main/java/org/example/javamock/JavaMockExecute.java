package org.example.javamock;

import org.example.Mocks;
import org.example.commandsdocs.CommandDocs;
import redis.clients.jedis.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import static redis.clients.jedis.Protocol.Command;

public class JavaMockExecute extends Mocks {

//    private final static int PORT = 40152;
//    JavaMock javaMock;

    public JavaMockExecute() {
        super();
        port = 40152;
        mockName = "Java";
        try {
            javaMock = new JavaMock(port);
            System.out.println("Start on Java...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertCommandToLine(CommandDocs command) {
        StringBuilder result = new StringBuilder();
        result.append(command.getCommandName()).append(" ");
//        if (command.getParameters().get())
        return result.toString();
    }

//    @Override
//    public String getCommandRunResult(String command) {
//        String response = "";
//        Protocol.Command command1 = Command.valueOf(command.trim().toUpperCase());
//        Object result = javaMock.getClient().sendCommand(command1);
//        response = new String((byte[])result);
//        return response;
//    }
}
