package mining;

import jason.environment.grid.Location;
import java.util.Random;

public class GoldGenerator implements Runnable {

  WorldModel model;
  WorldView view;

  protected Random random = new Random();
  private static final int SLEEP_TIME = 2000;

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
    Location l = getFreePos();

    model.add(WorldModel.GOLD, l.y, l.x);
    model.setInitialNbGolds(model.getInitialNbGolds()+1);
    view.update(l.y, l.x);
    view.udpateCollectedGolds();
  }

  public Location getFreePos() {
    for (int i=0; i<(model.getWidth() * model.getHeight() * 5); i++) {
      int x = random.nextInt(model.getWidth());
      int y = random.nextInt(model.getHeight());
      Location l = new Location(x, y);
      if (model.isFree(l)) {
        return l;
      }
    }
    return null;
  }
}
