package org.example;

import org.example.commandsdocs.CommandDocs;
import org.example.javamock.JavaMockExecute;

import java.io.IOException;
import java.util.List;

import static org.example.javamock.JavaMockExecute.convertCommandToLine;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Start comparing mocks of Redis!" );

        RedisExecute redisExecute = new RedisExecute();
        List<String> listOfAllCommands = redisExecute.getListOfCommands();
        List<CommandDocs> allCommandDocs = redisExecute.getCommandsDocs(listOfAllCommands);

        JavaMockExecute javaMockExecute = new JavaMockExecute();
        for (CommandDocs command : allCommandDocs) {
            String cmd = convertCommandToLine(command);
            String resultRedis = redisExecute.getCommandRunResult(cmd);
            System.out.println(resultRedis);
            String ResultJava = javaMockExecute.getCommandRunResult(cmd);
            System.out.println(ResultJava);
        }
    }
}
