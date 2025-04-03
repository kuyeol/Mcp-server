package org.acme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NewCli
{

    public static String exeCli="git commit -a -m"+ "ms";

    public static void main(String[] args) {
        // executeCommand(exeCli);
        execute2Command(exeCli, exeCli);
    }
    public static void executeCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void execute2Command(String unixCommand, String windowsCommand) {
        ProcessBuilder processBuilder;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", windowsCommand);
        } else {
            processBuilder = new ProcessBuilder("bash", "-c", unixCommand);
        }

        try {
            Process        process = processBuilder.start();
            BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String         line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
