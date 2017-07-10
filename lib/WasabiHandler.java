package lib;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.*;

public class WasabiHandler implements Runnable {
  private static WasabiHandler instance = null;

  private Map<String, String> agentsAffectiveState;
  private DatagramSocket socket;

  private static final String HOST_NAME = "localhost";
  private static final int SEND_PORT = 42425;
  private static final int RECEIVE_PORT = 42424;
  private static final int DATAGRAM_SIZE = 4096;
  private static final int SLEEP_TIME = 1000;

  private WasabiHandler() {}

  public static WasabiHandler getInstance() {
    if (instance == null) {
      instance = new WasabiHandler();
    }

    return instance;
  }

  protected static Map<String, String> parse(String response) {
    Map<String, String> temp = new HashMap<String, String>();
    String agentName, emotionName, currentHighestEmotion;
    double currentHighestEmotionValue;

    Pattern agentsPattern = Pattern.compile("ID\\d+=(\\w+)\\s*\\(((?:\\s*[a-z]*=\\d+.?\\d*\\s*)*)");
    Pattern emotionsPattern = Pattern.compile("(\\w*)=(\\d*\\.?\\d*)");
    Matcher agentsMatcher = agentsPattern.matcher(response);
    Matcher emotionsMatcher;

    while (agentsMatcher.find()) {
      agentName = agentsMatcher.group(1);

      currentHighestEmotion = "";
      currentHighestEmotionValue = 0.0;

      emotionsMatcher = emotionsPattern.matcher(agentsMatcher.group(2));

      while (emotionsMatcher.find()) {
        if(Double.parseDouble(emotionsMatcher.group(2)) > currentHighestEmotionValue)
          currentHighestEmotion = emotionsMatcher.group(1);
      }

      if (currentHighestEmotion != "") {
        temp.put(agentName, currentHighestEmotion);
      }
    }

    return temp;
  }

  public void run() {
    try {
      socket = new DatagramSocket(RECEIVE_PORT);
    } catch (SocketException se) {
      System.err.println("Couldn't create the DatagramSocket.");
    }

    while (!Thread.currentThread().isInterrupted()) {
      try {
        getUpdatedAffectiveStates();
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        socket.close();
      }
    }
  }

  private void getUpdatedAffectiveStates() {
    byte[] receivedData = new byte[DATAGRAM_SIZE];
    DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

    try {
      socket.receive(receivePacket);
    } catch (IOException se) {
      System.err.println("Couldn't receive new data.");
    }

    agentsAffectiveState = parse(new String(receivePacket.getData()));
  }

  public String getAffectiveState(String agentName) {
    return agentsAffectiveState.get(agentName);
  }

  public void sendImpulse(String agentName, int impulse) {
    String action = "1&IMPULSE&" + agentName + "&" + Integer.toString(impulse);
    sendAction(action);
  }

  public void sendDominance(String agentName, int dominance) {
    String action = "1&DOMINANCE&" + agentName + "&" + Integer.toString(dominance);
    sendAction(action);
  }

  private void sendAction(String action) {
    byte[] sendData = action.getBytes();;

    try {
      InetAddress address = InetAddress.getByName(HOST_NAME);
      DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, SEND_PORT);

      socket.send(packet);
    } catch (Exception se) {
      System.err.println("Couldn't send last action to WASABI.");
    }
  }
}
