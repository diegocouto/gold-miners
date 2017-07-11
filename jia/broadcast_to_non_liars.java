package jason.stdlib;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import lib.EmotionalAgent;

public class broadcast_to_non_liars extends DefaultInternalAction {

  @Override public int getMinArgs() {
    return 2;
  }
  @Override public int getMaxArgs() {
    return 2;
  }

  @Override
  protected void checkArguments(Term[] args) throws JasonException {
    super.checkArguments(args);
    if (!args[0].isAtom()) {
      throw JasonException.createWrongArgument(this,"illocutionary force argument must be an atom");
    }
  }

  @Override
  public boolean canBeUsedInContext() {
    return false;
  }

  @Override
  public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    checkArguments(args);

    Term ilf = args[0];
    Term pcnt = args[1];

    EmotionalAgent agent = ((EmotionalAgent)ts.getAg());
    Message m = new Message(ilf.toString(), ts.getUserAgArch().getAgName(), null, pcnt);

    for (String agentName : ts.getUserAgArch().getRuntimeServices().getAgentsNames()) {
      if (!agent.believes(Literal.parseLiteral("liar(" + agentName + ")"), new Unifier())) {
        Message msg = new Message(m);
        msg.setReceiver(agentName);

        ts.getUserAgArch().sendMsg(msg);
      }
    }

    return true;
  }
}
