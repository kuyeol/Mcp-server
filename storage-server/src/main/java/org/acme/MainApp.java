package org.acme;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

@QuarkusMain
public class MainApp implements QuarkusApplication
{

    @Inject
    CommandLine.IFactory factory;

    @Inject
    GreetingService greet;

    @Override
    public int run(String... args) {
        String shell = "ls";
        return new CommandLine(new CommandApp(greet), factory).execute(shell);
    }

    public static void main(String... args) {
       String shell = "ls";

        Quarkus.run(MainApp.class, shell);
    }




}
