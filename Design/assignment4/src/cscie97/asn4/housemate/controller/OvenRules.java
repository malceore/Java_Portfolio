package cscie97.asn4.housemate.controller;

import cscie97.asn4.housemate.model.HouseMateModel;
import cscie97.asn4.knowledge.engine.Triple;
import java.util.Set;
/**
 * Created by n0305853 on 10/23/18.
 */
public class OvenRules implements Rule{

    public String name = "Oven Rule";
    public String getName(){
        return name;
    }

    /**
     * Runs this rules functions, phrases of the change it is given then acts out it's logic on the model. Oven related rules.
     * @param change
     * @param model
     */
    @Override
    public boolean execute(String change, HouseMateModel model) {

        // Time to cook, spin off a thread timer that comes back
        if(change.toLowerCase().contains("timer")){

            String[] input = change.split(":");
            //System.out.println("DEBUG TIMER!");
            // If oven is on, turn it off
            Set results = model.valueGraph.executeQuery(input[2], "status", "on");
            Triple trip = null;
            for(Object entry: results) {
                trip = (Triple) entry;
            }

            if(trip != null){
                model.removeValue(input[2], "power", "on");
                model.removeValue(input[2], "timer", "alert");
                model.setValue(input[2], "status", "off");
                model.setValue(input[2], "temperature", "0");
            }

            //send ava command
            //--stub
            return true;
        }

        return false;
    }
}
