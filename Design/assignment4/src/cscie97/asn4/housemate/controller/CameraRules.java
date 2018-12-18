package cscie97.asn4.housemate.controller;

import cscie97.asn4.housemate.model.HouseMateModel;
import cscie97.asn4.knowledge.engine.Triple;

import java.util.Set;

/**
 * Created by n0305853 on 10/20/18.
 */
public class CameraRules implements Rule{

    public String name = "Camera Rule";
    public String getName(){
        return name;
    }

    /**
     * checks if rules applies to change, if so acts upon it.
     * @param change
     * @param model
     * @return
     */
    @Override
    public boolean execute(String change, HouseMateModel model) {
        // Is this a camera based change?
        try {
            if (change.contains("camera")) {
                String[] input = change.split(":");

                // Find user UUID
                Triple trip = model.getEntity(input[4]);
                String[] subject = trip.getIdentifier().split(" ");

                // Find camera's room UUID
                String[] room = null;
                Set results = model.associationGraph.executeQuery(input[2], "?", "?");
                for(Object entry: results) {
                    Triple trip2 = (Triple) entry;
                    room = trip2.getIdentifier().split(" ");
                }
                if(room == null){
                    throw new cameraChangeMalformed();
                }

                // Now what to do with the above values.
                if (change.contains("occupant_entered")) {
                    //System.out.println("DEBUG:: Subject detected");
                    model.setAssociation(subject[0], room[2]);
                    //System.out.println(subject[2] + room[2]);

                } else if (change.contains("occupant_exited")) {
                    //System.out.println("DEBUG:: Subject leaving");
                    model.removeAssociation(subject[0], room[2]);

                } else if (change.contains("occupant_active")) {
                    //System.out.println("DEBUG:: Subject active");
                    model.removeValue(subject[0], "status", "inactive.");
                    model.setValue(subject[0], "status", "active");

                } else if (change.contains("occupant_inactive")) {
                    //System.out.println("DEBUG:: Subject inactive");
                    model.removeValue(subject[0], "status", "active.");
                    model.setValue(subject[0], "status", "inactive");

                }else{
                    throw new cameraChangeMalformed();
                }
                return true;
            }
        } catch (cameraChangeMalformed ex){
            System.out.println("Camera related change was malformed or not understood : " + ex);
            return false;
        }
        return false;
    }
}


