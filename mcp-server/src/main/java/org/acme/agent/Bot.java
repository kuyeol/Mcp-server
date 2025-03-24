package org.acme.agent;


import jakarta.enterprise.context.SessionScoped;

@SessionScoped
public interface Bot
{
  String chat(String msg);


}
