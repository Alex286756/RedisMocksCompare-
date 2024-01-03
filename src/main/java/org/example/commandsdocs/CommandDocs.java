package org.example.commandsdocs;

import org.example.exceptions.ReadDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDocs {
    private String commandName = "";
    private Map<String, String> parameters;
    private List<ArgumentInfo> arguments = new ArrayList<>();
    private final Map<String, String> history = new HashMap<>();
    private final List<String> docsFlags = new ArrayList<>();
    private final List<CommandDocs> subCommands = new ArrayList<>();

    private final InputStream in;

    public CommandDocs(InputStream in) {
        this.in = in;
    }

    private String message() {
        return "command docs " + commandName + ": ошибка чтения данных из Редиса";
    }

    public void readCommandDocsFromInputStream() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))){
            readCommandName(bufferedReader);
            readCommandParameters(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readCommandName(BufferedReader bf) {
        try {
            String line = bf.readLine();
            if (line == null) {
                commandName = "";
                return;
            }
            if (!"*2".equals(line)) {
                throw new ReadDataException("Ошибка чтения данных из Редиса");
            }
            if (!bf.readLine().startsWith("$")) {
                throw new ReadDataException("Ошибка чтения данных из Редиса");
            }
            commandName = bf.readLine();
        } catch (IOException e) {
            throw new ReadDataException("Ошибка во время чтения имени команды (command docs)...");
        }
    }

    public void readSubCommandName(BufferedReader bf) throws IOException {
        bf.readLine();
        commandName = bf.readLine();
    }

    private void readCommandParameters(BufferedReader bf) {
        try {
            parameters = new HashMap<>();
            String line = bf.readLine();
            if (line == null) {
                return;
            }
            if (!line.startsWith("*")) {
                throw new ReadDataException(message());
            }
            int count = Integer.parseInt(line.replace("*", ""));
            for (int i = 0; i < count / 2; i++) {
                if (!bf.readLine().startsWith("$")) {
                    throw new ReadDataException(message());
                }
                String name = bf.readLine();
                if ("doc_flags".equals(name)) {
                    readDocsFlags(bf);
                    continue;
                }
                if ("history".equals(name)) {
                    readCommandHistory(bf);
                    continue;
                }
                if ("arguments".equals(name)) {
                    readCommandArguments(bf);
                    break;
                }
                if ("subcommands".equals(name)) {
                    String line2 = bf.readLine();
                    int count2 = Integer.parseInt(line2.replace("*", ""));
                    for (int i2 = 0; i2 < count2 / 2; i2++) {
                        CommandDocs newSubCommand = new CommandDocs(in);
                        newSubCommand.readSubCommandName(bf);
                        newSubCommand.readCommandParameters(bf);
                        subCommands.add(newSubCommand);
                    }
                    continue;
                }
                String newLine = bf.readLine();
                String value = "";
                switch (newLine.charAt(0)) {
                    case '$':
                        value = bf.readLine();
                        break;
                    case ':':
                        value = newLine.replace(":", "");
                        break;
                    default:
                        throw new ReadDataException(message());
                }
                parameters.put(name, value);
            }
        } catch (IOException e) {
            throw new ReadDataException("Ошибка во время чтения параметров команды (command docs " + commandName +")...");
        }
    }


    private void readCommandArguments(BufferedReader bf) {
        try {
            arguments = new ArrayList<>();
            String line = bf.readLine();
            if (line == null) {
                return;
            }
            if (!line.startsWith("*")) {
                throw new ReadDataException(message());
            }
            int count = Integer.parseInt(line.replace("*", ""));
            for (int i = 0; i < count; i++) {
                ArgumentInfo argumentInfo = new ArgumentInfo();
                argumentInfo.readArgument(bf);
                arguments.add(argumentInfo);
            }
        } catch (ReadDataException e) {
            throw new ReadDataException("Ошибка во время чтения аргумента команды (command docs " + commandName +")...");
        } catch (IOException e) {
            throw new ReadDataException("Ошибка во время чтения аргументов команды (command docs " + commandName +")...");
        }
    }


    private void readCommandHistory(BufferedReader bf) {
        try {
            String line = bf.readLine();
            if (line == null) {
                return;
            }
            if (!line.startsWith("*")) {
                throw new ReadDataException(message());
            }
            int count = Integer.parseInt(line.replace("*", ""));
            for (int i = 0; i < count; i++) {
                bf.readLine();
                if (!bf.readLine().startsWith("$")) {
                    throw new ReadDataException(message());
                }
                String name = bf.readLine();
                bf.readLine();
                history.put(name, bf.readLine());
            }
        } catch (IOException e) {
            throw new ReadDataException("Ошибка во время чтения параметров команды (command docs " + commandName +")...");
        }
    }

    public void readDocsFlags(BufferedReader bf) throws ReadDataException, IOException {
        String line = bf.readLine();
        if (line == null) {
            return;
        }
        if (!line.startsWith("*")) {
            throw new ReadDataException("");
        }
        int count = Integer.parseInt(line.replace("*", ""));
        for (int i = 0; i < count; i++) {
            String flagLine = bf.readLine().replace("+", "");
            docsFlags.add(flagLine);
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public List<ArgumentInfo> getArguments() {
        return arguments;
    }

    public Map<String, String> getHistory() {
        return history;
    }

    public List<String> getDocsFlags() {
        return docsFlags;
    }

    public List<CommandDocs> getSubCommands() {
        return subCommands;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Command name : ").append(commandName).append("\n");
        result.append("Parameters: \n");
        for (String key: parameters.keySet()) {
            result.append("\t").append(key).append(" = ").append(parameters.get(key)).append("\n");
        }
        if (docsFlags.size() != 0) {
            result.append("Docs flags: \n");
            for (String key : docsFlags) {
                result.append("\t").append(key).append("\n");
            }
        }
        if (history.size() != 0) {
            result.append("History: \n");
            for (String key : history.keySet()) {
                result.append("\t").append(key).append(": ").append(history.get(key)).append("\n");
            }
        }
        result.append("Arguments: \n");
        for (ArgumentInfo argument : arguments) {
            result.append("\t").append(argument);
        }
        if (subCommands.size() != 0) {
            result.append("Subcommands: \n");
            for (CommandDocs key : subCommands) {
                result.append("\t").append(key).append("\n");
            }
        }
        return result.toString();
    }
}
