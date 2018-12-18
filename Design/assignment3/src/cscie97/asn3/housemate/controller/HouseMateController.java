package cscie97.asn3.housemate.controller;
import cscie97.asn3.housemate.model.HouseMateModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * <h1>HouseMate Model Controller</h1>
 * Created by: Brandon T. Wood
 * Date: Oct 15th, 2018
 */
public class HouseMateController extends Observer {
    private HouseMateModel subject;
    private ArrayList<Rule> ruleList = new ArrayList<Rule>();


    /**
     * Constructs the Controller object, taking in a reference to a subject model for the controller to watch
     *      and manipulate based on rule configured in setupBaseRules method below. Implements the Observer Pattern.
     * @param subject
     */
    public HouseMateController(HouseMateModel subject) {
        this.subject = subject;
        subject.addObserver((Observer) this);
        //setupBaseRules();
        setupExternalRules();
    }


    /**
     * Sets up the base rules for the observer to follow, built in functionality.
     *
    private void setupBaseRules(){
        Rule modeFire = (String change) -> {
            System.out.println("Debug :: There has been a fire! Fire Mode on!");
        };
        ruleList.add(modeFire);
    };*/


    /**
     * Sets up the expendable rules for the observer to follow, this section is for adding additional classes from vendors or
     *      custom user classes.
     */
    private void setupExternalRules(){
        ruleList.add(new FridgeRules());
        ruleList.add(new SafetyRules());
        ruleList.add(new AvaRules());
        ruleList.add(new CameraRules());
        ruleList.add(new OvenRules());
    }


    /**
     * Called by subject when changes occur, triggering us to see if changes match any of our rules.
     * @param change
     */
    public void update(String change) {
        //System.out.println("DEBUG :: Model updated :: " + change);
        // Iterate and check our list of rules.
        boolean value;
        for(Rule rule : ruleList){
            value = rule.execute(change.toLowerCase(), subject);
            if(value){
                Date date = new Date();
                System.out.println(date + " LOG :: " + rule.getName() + " triggered by Change: " + change);
            }
        }
    }
}
