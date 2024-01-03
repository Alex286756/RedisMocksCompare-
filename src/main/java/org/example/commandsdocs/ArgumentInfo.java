package org.example.commandsdocs;

import org.example.exceptions.ReadDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentInfo {
    private final Map<ArgumentFields, String> argument = new HashMap<>();
    private final Map<FlagsType, String> flags = new HashMap<>();
    private final List<ArgumentInfo> argumentValue = new ArrayList<>();

    public void readArgument(BufferedReader bf) throws ReadDataException, IOException {
        String line = bf.readLine();
        if (line == null) {
            return;
        }
        if (!line.startsWith("*")) {
            throw new ReadDataException("");
        }
        int count = Integer.parseInt(line.replace("*", ""));
        for (int i = 0; i < count / 2; i++) {
            if (!bf.readLine().startsWith("$")) {
                throw new ReadDataException("");
            }
            String name = bf.readLine();
            if ("flags".equals(name)) {
                readFlags(bf);
                continue;
            }
            if ("arguments".equals(name)) {
                String line2 = bf.readLine();
                int count2 = Integer.parseInt(line2.replace("*", ""));
                for (int i2 = 0; i2 < count2; i2++) {
                    ArgumentInfo newArgument = new ArgumentInfo();
                    newArgument.readArgument(bf);
                    argumentValue.add(newArgument);
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
                    throw new ReadDataException("");
            }
            argument.put(ArgumentFields.valueOf(name.toUpperCase()), value);
        }
    }


    public void readFlags(BufferedReader bf) throws ReadDataException, IOException {
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
            flags.put(FlagsType.valueOf(flagLine.toUpperCase()), flagLine);
        }
    }

    public String getArgument(ArgumentFields field) {
        if ("flags".equals(field.name())) {
            return getFlags();
        }
        return argument.get(field);
    }

    private String getFlags() {
        StringBuilder result = new StringBuilder();
        for (FlagsType ft : flags.keySet()) {
            result.append("\t").append(ft.name()).append(" -> ").append(flags.get(ft)).append("\n");
        }
        return result.toString();
    }

    public Map<ArgumentFields, String> getArgument() {
        return argument;
    }

    public List<ArgumentInfo> getArgumentValue() {
        return argumentValue;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Argument's fields: \n");
        for (ArgumentFields key: argument.keySet()) {
            result.append("\t\t").append(key.toString()).append(" = ").append(argument.get(key)).append("\n");
        }
        if (flags.size() != 0) {
            result.append("\tFlags of argument: \n");
            for (FlagsType key : flags.keySet()) {
                result.append("\t\t").append(key.toString()).append(" -> ").append(flags.get(key)).append("\n");
            }
        }

        for (ArgumentInfo key : argumentValue) {
            result.append("\t\t\t").append(key.toString()).append("\n");
        }
        return result.toString();
    }
}
