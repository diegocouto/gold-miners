package lib;

import jason.asSemantics.Agent;
import jason.asSemantics.Intention;
import jason.asSyntax.Literal;
import jason.RevisionFailedException;

import java.util.List;

public class EmotionalAgent extends Agent {
  private static WasabiHandler wasabiHandler = WasabiHandler.getInstance();
  private String currentAffectiveState;

  @Override
  public List<Literal>[] brf(Literal beliefToAdd, Literal beliefToDel,  Intention i) throws RevisionFailedException {
    String affectiveState = wasabiHandler.getAffectiveState(ts.getUserAgArch().getAgName());

    if (affectiveState != null && !affectiveState.equals(currentAffectiveState)) {
      if (currentAffectiveState != null) {
        brf(Literal.parseLiteral(affectiveState), Literal.parseLiteral(currentAffectiveState), Intention.EmptyInt, false);
      } else {
        brf(Literal.parseLiteral(affectiveState), null, Intention.EmptyInt, false);
      }

      currentAffectiveState = affectiveState;
    }

    return brf(beliefToAdd, beliefToDel, i, false);
  }

  public void triggerImpulse(int impulse) {
    wasabiHandler.sendImpulse(ts.getUserAgArch().getAgName(), impulse);
  }

  public void triggerImpulse(int impulse, int dominance) {
    wasabiHandler.sendImpulse(ts.getUserAgArch().getAgName(), impulse);
    wasabiHandler.sendDominance(ts.getUserAgArch().getAgName(), dominance);
  }
}
