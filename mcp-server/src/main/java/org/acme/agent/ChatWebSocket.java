package org.acme.agent;


import io.quarkus.websockets.next.*;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.common.annotation.Blocking;



public class ChatWebSocket
{


  private final Bot bot;

  public ChatWebSocket(Bot bot) {
    this.bot = bot;
  }

  @OnOpen
  public String onOpen() {
    return "Hello, I am a filesystem robot, how can I help?";
  }

  @OnTextMessage
  @Blocking
  public String onMessage(String message) {
    return bot.chat(message);
  }





}
