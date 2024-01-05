package org.example.golangmock;

import org.example.Mocks;
import org.example.commandsdocs.CommandDocs;
import org.example.exceptions.ReadDataException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GolangMockExecute extends Mocks {

//    protected final static int PORT = 40150;

    public GolangMockExecute() {
        super();
        System.out.println("Start on golang Redis...");
        port = 40155;
        mockName = "Golang";
    }

    public List<CommandDocs> getCommandsDocs(List<String> listOfCommands) throws IOException {
        List<CommandDocs> result = new ArrayList<>();
        for (String commandName : listOfCommands) {
            try (Socket clientSocket = new Socket("localhost", port)) {
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                String word = "command docs " + commandName + "\n";
                outputStream.write(word.getBytes());

                CommandDocs commandDocs = new CommandDocs(inputStream);
                commandDocs.readCommandDocsFromInputStream();
                result.add(commandDocs);

                // если надо записать в файл
//                try (FileWriter writer = new FileWriter("read_command.txt", true)) {
//                    BufferedWriter bufferWriter = new BufferedWriter(writer);
//                    bufferWriter.write(commandInfo.toString());
//                    bufferWriter.write("\n");
//                    bufferWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
        return result;
//        System.out.println("Считано данных о " + commandInfos.size() + " командах Редиса...");
    }


    public List<String> getListOfCommands() throws IOException {
        List<String> redisCommandsName = new ArrayList<>();
//        redisCommandsName.add("xgroup");
        try (Socket clientSocket = new Socket("localhost", port)) {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            String word = "command list\n";
            outputStream.write(word.getBytes());

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))){
                String line = bf.readLine();
                if (line == null) {
                    return null;
                }
                if (!line.startsWith("*")) {
                    throw new ReadDataException("");
                }
                int count = Integer.parseInt(line.replace("*", ""));
                for (int i = 0; i < count; i++) {
                    if (!bf.readLine().startsWith("$")) {
                        throw new ReadDataException("Ошибка во время чтения списка команд редиса...");
                    }
                    redisCommandsName.add(bf.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return redisCommandsName;
    }

//    public String getCommandRunResult(String command) throws IOException {
//        try (Socket clientSocket = new Socket("localhost", port)) {
//            InputStream inputStream = clientSocket.getInputStream();
//            OutputStream outputStream = clientSocket.getOutputStream();
//
//            String word = command + "\n";
//            outputStream.write(word.getBytes());
//
//            try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream))){
//                return bf.readLine();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

}
