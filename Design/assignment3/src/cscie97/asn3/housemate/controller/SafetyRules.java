package cscie97.asn3.housemate.controller;

import cscie97.asn3.housemate.model.HouseMateModel;
import cscie97.asn3.knowledge.engine.Triple;

import java.util.Set;

/**
 * Created by n0305853 on 10/20/18.
 */
public class SafetyRules implements Rule{

    public String name = "Safety Rules";
    public String getName(){
        return name;
    }

    @Override
    public boolean execute(String change, HouseMateModel model) {
        // If change would require safety rules to be triggered

        if(change.contains("fire") && change.contains("smoke")){
            //  smoke_detector2_1:appliance:31:status:fire
            String[] input = change.split(":");
            System.out.println("Calling 911 and directing to scene of fire.");
            // Find the room of the smoke alarm
            String[] room = null;
            Set roomSet = model.associationGraph.executeQuery(input[2], "?", "?");
            for (Object entry : roomSet) {
                Triple nameTrip = (Triple) entry;
                room = nameTrip.getIdentifier().split(" ");
                //System.out.println("DEBUG room of detector:"  +room[2]);
            }

            // Find the house of the room.
            String[] house = null;
            roomSet = model.associationGraph.executeQuery(room[2], "?", "?");
            for (Object entry : roomSet) {
                Triple nameTrip = (Triple) entry;
                house = nameTrip.getIdentifier().split(" ");
                //System.out.println("DEBUG house of detector:"  + house[2]);
            }

            // Turn on all house lights.
            turnOnHouseLights(house[2], model);

            // Iterate over all rooms in house, output warning about fire.
            ///String[] house = null;
            roomSet = model.associationGraph.executeQuery("?", "?", house[2]);
            for (Object entry : roomSet) {
                Triple nameTrip = (Triple) entry;
                String[] rooms = nameTrip.getIdentifier().split(" ");
                //System.out.println("DEBUG room:"  + model.getName(rooms[0]));
                outputAvaWarnings(rooms[0], model, "Fire detected, please vacate the premises!");
            }

            // if it has first floor windows, please jump out of them. So does it
            Set floorSet = model.valueGraph.executeQuery("?", "floor", "1");
            for (Object entry : floorSet) {
                Triple floorTrip = (Triple) entry;
                String[] floor = floorTrip.getIdentifier().split(" ");
                //System.out.println("Debug this room is on the second floor " + model.getName(floor[0]) + " " + floor[2]);
                outputAvaWarnings(floor[0], model, "Please evacuate out windows!");
            }

            return true;
        }
        return false;
    }

    /**
     * Given a model and a uuid of a house, will turn on all the lights in said house.
     * @param UUID
     * @param model
     */
    public void turnOnHouseLights(String UUID, HouseMateModel model){
        Set roomSet = model.associationGraph.executeQuery("?", "?", UUID);
        for (Object entry : roomSet) {
            Triple nameTrip = (Triple) entry;
            String[] rooms = nameTrip.getIdentifier().split(" ");
            //System.out.println("DEBUG room:"  + model.getName(rooms[0]));

            // For each room we check for lights in it.
            Set applianceSet = model.associationGraph.executeQuery("?", "?", rooms[0]);
            for (Object applianceEntry : applianceSet) {
                Triple applianceTrip = (Triple) applianceEntry;
                String[] appliance = applianceTrip.getIdentifier().split(" ");
                //System.out.println("DEBUG appliance:"  + model.getName(appliance[0]));

                if(model.getName(appliance[0]).contains("light")){
                    model.setValue(appliance[0], "status", "on");
                    //System.out.println("DEBUG turning on light: " + appliance[0]);
                }
            }
        }
    }

    /**
     * Give the uuid of a room, will broadcast a warning string to all Ava devices in that room. Model used to make calls against.
     * @param UUID
     * @param model
     * @param warning
     */
    public void outputAvaWarnings(String UUID, HouseMateModel model, String warning){
        Set roomSet = model.associationGraph.executeQuery("?", "?", UUID);
        for (Object entry : roomSet) {
            Triple applianceTrip = (Triple) entry;
            String[] appliance = applianceTrip.getIdentifier().split(" ");
            //System.out.println("DEBUG appliance:"  + appliance[0]);
            if(model.getName(appliance[0]).contains("ava")){
                model.setValue(appliance[0], "voice_command", "warning:"+warning);
                //System.out.println("DEBUG turning on light: " + appliance[0]);
            }
        }
    }

}
