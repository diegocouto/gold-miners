package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import mining.WorldModel;

public class get_random_location extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        try {
            WorldModel model = WorldModel.get();
            Location l = model.getFreePos();

            return un.unifies(terms[0], new NumberTermImpl(l.x)) &&
                   un.unifies(terms[1], new NumberTermImpl(l.y));
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
