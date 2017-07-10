package mining;

import jason.environment.grid.Location;
import java.util.Random;

public class GoldGenerator implements Runnable {

  WorldModel model;
  WorldView view;

  protected Random random = new Random();
  private static final int SLEEP_TIME = 1000;

  public GoldGenerator(WorldModel newModel, WorldView newView) {
    model = newModel;
    view = newView;
  }

  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        generateRandomGold();
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void generateRandomGold() {
    Location l = model.getFreePos();

    model.add(WorldModel.GOLD, l.y, l.x);
    model.setInitialNbGolds(model.getInitialNbGolds()+1);
    view.update(l.y, l.x);
    view.udpateCollectedGolds();
  }
}
