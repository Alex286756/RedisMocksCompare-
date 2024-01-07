package org.example;

import org.example.commandsdocs.CommandDocs;
import org.example.golangmock.GolangMockExecute;
import org.example.javamock.JavaMockExecute;
import org.example.pythonmock.PythonMockExecute;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.resps.CommandDocument;
import redis.clients.jedis.resps.CommandInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.javamock.JavaMockExecute.convertCommandToLine;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Start comparing mocks of Redis!" );
        List<String> tests = new ArrayList<>();
        tests.add("ping");
        tests.add("set asd dsa");
        tests.add("get asd");
        tests.add("keys *");
        tests.add("del asd");
//        String testCommand = "ping";
//        String testCommand = "set asd dsa";
//        String testCommand = "get asd";
//        String testCommand = "keys *";
//        String testCommand = "shutdown";

        RedisExecute redisExecute = new RedisExecute();
        JavaMockExecute javaMockExecute = new JavaMockExecute();
        PythonMockExecute pythonMockExecute = new PythonMockExecute();
        GolangMockExecute golangMockExecute = new GolangMockExecute();

        for (String testCommand: tests) {
            redisExecute.runCommand(testCommand);
            javaMockExecute.runCommand(testCommand);
            pythonMockExecute.getPyCommandRunResult(testCommand);
            golangMockExecute.getCommandRunResult(testCommand);
            System.out.println();
        }

//        List<String> listOfAllCommands = redisExecute.getListOfCommands();
//        List<CommandDocs> allCommandDocs = redisExecute.getCommandsDocs(listOfAllCommands);
//        Map<String, CommandDocument> allCommandDocs = redisExecute.getCommandsDocs(listOfAllCommands);
//        Map<String, CommandInfo> allCommandsInfos = redisExecute.getCommandsInfos(listOfAllCommands);

//        for (int i = 0; i < 5; i++) {
//            CommandDocs command = allCommandDocs.get(i);

//        for (String cmd : allCommandDocs.keySet()) {
//            if ("save ".equals(cmd) || "multi".equals(cmd) || "monitor".equals(cmd) || "quit".equals(cmd)
//            || "reset".equals(cmd) || "shutdown".equals(cmd)) {
//                continue;
//            }
//            if ("client".equals(cmd) || "memory".equals(cmd) || "xinfo".equals(cmd) || "module".equals(cmd)
//                    || "xgroup".equals(cmd) || "cluster".equals(cmd) || "command".equals(cmd) || "function".equals(cmd)
//                    || "acl".equals(cmd) || "config".equals(cmd) || "latency".equals(cmd) || "slowlog".equals(cmd)
//                    || "pubsub".equals(cmd) || "script".equals(cmd) || "object".equals(cmd)) {
//                continue;
//            }
//            if (cmd.contains("|")) {
//                continue;
//            }
//
//            redisExecute.runCommand(cmd);
//            javaMockExecute.runCommand(cmd);
//            pythonMockExecute.runCommand(cmd);
//            System.out.println("\n\n");
//        }

    }
}
