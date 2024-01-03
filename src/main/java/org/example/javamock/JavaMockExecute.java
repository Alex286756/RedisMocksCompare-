package org.example.javamock;

import org.example.Mocks;
import org.example.commandsdocs.CommandDocs;

import java.io.IOException;

public class JavaMockExecute extends Mocks {

//    private final static int PORT = 40152;
//    JavaMock javaMock;

    public JavaMockExecute() {
        super();
        port = 40152;
//        try {
//            this.javaMock = new JavaMock(port);
            System.out.println("Start on Java...");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static String convertCommandToLine(CommandDocs command) {
        StringBuilder result = new StringBuilder();
        result.append(command.getCommandName()).append(" ");
//        if (command.getParameters().get())
        return result.toString();
    }

}
