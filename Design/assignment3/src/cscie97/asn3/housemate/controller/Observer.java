package cscie97.asn3.housemate.controller;

import cscie97.asn3.housemate.model.HouseMateModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * <h1>Observer</h1>
 * Created by: Brandon T. Wood
 * Date: Sept, 23rd 2018
 */
public class Observer {
    private HouseMateModel subject;

    /**
     * Constructs the Controller object, taking in a reference to a subject model for the controller to watch
     *      and manipulate based on rule configured in setupBaseRules method below. Implements the Observer Pattern.
     * @param subject
     */
    public Observer(HouseMateModel subject) {
        this.subject = subject;
        subject.addObserver(this);
    }

    public Observer() {
    }

    /**
     * Called by subject when changes occur, triggering us to see if changes match any of our rules.
     * @param change
     */
    public void update(String change) {
        //System.out.println("DEBUG :: Model updated :: " + change);
        // Iterate and check our list of rules.
    }
}
