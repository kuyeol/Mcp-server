package org.acme.tutorial;

import dev.langchain4j.agent.tool.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ToolSupport
{


    @Tool("Current Date and Time")
    public String currentDate() {

        LocalDateTime     now       = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("''yy년 MM월 dd일 E요일 HH시 mm분 ss초", Locale.KOREAN);

        return now.format(formatter);
    }


    @Tool("Current user name")
    public User userName() {
        User user = new User("Ung");
        return user;
    }


    record User(String userName)
    {
    }




}
