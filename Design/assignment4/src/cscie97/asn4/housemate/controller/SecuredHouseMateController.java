package cscie97.asn4.housemate.controller;

import cscie97.asn4.housemate.entitlement.AccessToken;
import cscie97.asn4.housemate.entitlement.HouseMateEntitler;
import cscie97.asn4.housemate.model.HouseMateModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * <h1>HouseMate Model Controller</h1>
 * Created by: Brandon T. Wood
 * Date: Oct 15th, 2018
 */
public class SecuredHouseMateController extends Observer {
    private HouseMateModel subject;
    private ArrayList<Rule> ruleList = new ArrayList<Rule>();
    private HouseMateEntitler titler = HouseMateEntitler.getInstance();
    private AccessToken token;

    /**
     * Constructs the Controller object, taking in a reference to a subject model for the controller to watch
     *      and manipulate based on rule configured in setupBaseRules method below. Implements the Observer Pattern.
     * @param subject
     */
    public SecuredHouseMateController(HouseMateModel subject) {
        this.subject = subject;
        subject.addObserver((Observer) this);
        // Hard coded admin values.
        this.token = titler.login("Admin", "trustNo1");
        setupExternalRules();
    }


    /**
     * Sets up the expendable rules for the observer to follow, this section is for adding additional classes from vendors or
     *      custom user classes.
     */
    private void setupExternalRules(){
        ruleList.add(new FridgeRules());
        ruleList.add(new SafetyRules());
        // Change to ava rules for voice-prints, new constructor.
        ruleList.add(new AvaRules(titler, token));
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
        // Check admin perms before completing this.
        if (titler.checkToken(token)) {
            for (Rule rule : ruleList) {
                value = rule.execute(change.toLowerCase(), subject);
                if (value) {
                    Date date = new Date();
                    System.out.println(date + " LOG :: " + rule.getName() + " triggered by Change: " + change);
                }
            }
        }
    }
}
