package org.acme;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command
public class CommandApp implements Runnable
{

    @CommandLine.Option(names = { "-n", "--name" }, description = "Who will we greet?", defaultValue = "World")
    String name;

    @Inject
    CommandLine.IFactory cliFactory;

    @CommandLine.Parameters
    private List<String> args;

    private final GreetingService greet;

    public CommandApp(GreetingService greet) {
        this.greet = greet;
    }

    @Override
    public void run() {
        greet.sayHello(name);

    }




}



@Dependent
class GreetingService
{

    void sayHello(String name) {
        System.out.println("Hello " + name + "!");
    }




}