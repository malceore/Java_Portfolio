package cscie97.asn3.housemate.controller;
import cscie97.asn3.housemate.model.HouseMateModel;
import cscie97.asn3.knowledge.engine.Triple;

import java.util.Set;

/**
 * Created by n0305853 on 10/20/18.
 */
public class AvaRules implements Rule {

    public String name = "Ava Rule";
    public String getName(){
        return name;
    }


    /**
     * Runs this rules functions, prases of the change it is given then acts out it's logic on the model. Either a command or question.
     * @param change
     * @param model
     */
    @Override
    public boolean execute(String change, HouseMateModel model) {
        //[set, sensor, house1:kitchen:ava1_1, status, voice_command, value, “martin_fowler:where_is_grandmother”_]
        // Need to clean out ugly syntax.
        change = change.replaceAll("”", "");
        change = change.replaceAll("“", "");
        //change = change.replaceAll("?", "");
        String[] input = change.split(":");

        // Check if this is an ava command.
        if (change.contains("voice_command")) {
            // ava2_2:appliance:60:voice_command:“dim_lights”
            // kitchen_ava:appliance:17:voice_command:jane:where_is_astro
            String[] command = input[4].split(" ");
            //System.out.println("they are good kids");

            //Where
            if(change.contains("where_is")){
                String[] person = input[5].split("_");
                //System.out.println(person[2]);
                String answer = where(person[2], model);
                String[] reply = answer.split(":");
                speakerOutput(person[2] + " is in " + model.getName(reply[1]) + " of " + model.getName(reply[0]));
                return true;
            }else if(change.contains("door")){
                // Find user making call and open the first door to that room.
                String answer = where(input[4], model);
                String[] answerSplit = answer.split(":");
                //System.out.println("Door debug " + answer);
                if (change.contains("close")){
                    roomDoorSetState(answerSplit[1], model, "closed");
                }else{
                    roomDoorSetState(answerSplit[1], model, "opened");
                }
                return true;
            } else if(change.contains("light")){
                // Find user making call and open the first door to that room.
                //System.out.println("light debug ");
                String answer = where(input[4], model);
                String[] answerSplit = answer.split(":");
                if (change.contains("off")) {
                    roomLightSetState(answerSplit[1], model, "off");
                }else if(change.contains("dim")){
                    roomLightSetState(answerSplit[1], model, "dimmed");
                }else{
                    roomLightSetState(answerSplit[1], model, "on");
                }
                return true;
            } else if(change.contains("tv")){
                // Find user making call and open the first door to that room.
                //System.out.println("light debug ");
                String answer = where(input[4], model);
                String[] answerSplit = answer.split(":");
                if (change.contains("off")) {
                    roomTVSetState(answerSplit[1], model, "off");
                }else if(change.contains("channel")) {
                    //System.out.println(input[5]);
                    String[] channel = input[5].split("_");
                    roomTVSetChannel(answerSplit[1], model,  channel[2]);
                }else if(change.contains("volume")) {
                    //System.out.println(input[5]);
                    String[] volume = input[5].split("_");
                    roomTVSetVolume(answerSplit[1], model, volume[2]);
                }else{
                    roomTVSetState(answerSplit[1], model, "on");
                }
                return true;
            } else if(change.contains("warning")){
                //String[] output = input[4].split(":");
                //System.out.println("debug" + input[5]);
                speakerOutput(input[5]);
            }
        }
        return false;
    }


    /**
     * Takes a UUID of a room, a model and a state(open or closed) sets the state of those tv of the room to given channel.
     * @param UUID
     * @param model
     * @param state
     */
    public void roomTVSetChannel(String UUID, HouseMateModel model, String state){
        // Find that door, setValue of it's state.
        Set applianceResults = model.associationGraph.executeQuery("?", "inside", UUID);
        for (Object entry : applianceResults) {
            Triple entryTrip = (Triple) entry;
            String[] association = entryTrip.getIdentifier().split(" ");
            String name = model.getName(association[0]);

            if(name.contains("tv")){
                //System.out.println("debug Modifying a door we found!");
                model.setValue(association[0], "channel", state);
            }
        }
    }


    /**
     * Takes a UUID of a room, a model and a state(open or closed) sets the state of those tv of the room to given volume.
     * @param UUID
     * @param model
     * @param state
     */
    public void roomTVSetVolume(String UUID, HouseMateModel model, String state){

        // Find that door, setValue of it's state.
        Set applianceResults = model.associationGraph.executeQuery("?", "inside", UUID);
        for (Object entry : applianceResults) {
            Triple entryTrip = (Triple) entry;
            String[] association = entryTrip.getIdentifier().split(" ");
            String name = model.getName(association[0]);

            if(name.contains("tv")){
                //System.out.println("debug Modifying a door we found!");
                model.setValue(association[0], "volume", state);
            }
        }
    }


    /**
     * Takes a UUID of a room, a model and a state(open or closed) sets the state of those doors of the room to given state.
     * @param UUID
     * @param model
     * @param state
     */
    public void roomTVSetState(String UUID, HouseMateModel model, String state){

        // Find that door, setValue of it's state.
        Set applianceResults = model.associationGraph.executeQuery("?", "inside", UUID);
        for (Object entry : applianceResults) {
            Triple entryTrip = (Triple) entry;
            String[] association = entryTrip.getIdentifier().split(" ");
            String name = model.getName(association[0]);

            if(name.contains("tv")){
                //System.out.println("debug Modifying a door we found!");
                model.setValue(association[0], "status", state);
            }
        }
    }


    /**
     * Takes a UUID of a room, a model and a state(open or closed) sets the state of those doors of the room to given state.
     * @param UUID
     * @param model
     * @param state
     */
    public void roomDoorSetState(String UUID, HouseMateModel model, String state){

        // Find that door, setValue of it's state.
        Set applianceResults = model.associationGraph.executeQuery("?", "inside", UUID);
        for (Object entry : applianceResults) {
            Triple entryTrip = (Triple) entry;
            String[] association = entryTrip.getIdentifier().split(" ");
            String name = model.getName(association[0]);

            if(name.contains("door")){
                //System.out.println("debug Modifying a door we found!");
                model.setValue(association[0], "status", state);
            }
        }
    }


    /**
     * Takes a UUID of a room, a model and a state(on or off) sets the state of those lights of the room to given state.
     * @param UUID
     * @param model
     * @param state
     */
    public void roomLightSetState(String UUID, HouseMateModel model, String state){
        // Find that light, setValue of it's state.
        Set applianceResults = model.associationGraph.executeQuery("?", "inside", UUID);
        for (Object entry : applianceResults) {
            Triple entryTrip = (Triple) entry;
            String[] association = entryTrip.getIdentifier().split(" ");
            String name = model.getName(association[0]);
            if(name.contains("lights")){
                //System.out.println("debug Modifying a light we found!");
                model.setValue(association[0], "status", state);
            }
        }
    }


    /**
     * Takes a string and prints it out as a stub for audio out.
     * @param output
     */
    public void speakerOutput(String output){
        System.out.println("AUDIO OUTPUT >> " + output);
    }


    /**
     * Given an name of an occupant and a model, returns the where question targets house:room:user in UUID.
     * @param name
     * @param model
     */
    //public String where(String[] input, HouseMateModel model){
    public String where(String name, HouseMateModel model){
        try {
            //if(name.length() <= 0){
                //System.out.println("debug find: " + name);
                Triple UUIDTrip = model.getEntity(name);
                String[] UUID = UUIDTrip.getIdentifier().split(" ");
                Set associationResults = model.associationGraph.executeQuery(UUID[0], "inside", "?");
                for (Object entry : associationResults) {
                    Triple entryTrip = (Triple) entry;
                    String[] room = entryTrip.getIdentifier().split(" ");
                    //System.out.println("Debug Found Them in " +  model.getName(room[2]) + " !");
                    Set roomResults = model.associationGraph.executeQuery(room[2], "inside", "?");
                    for (Object entryHouse : roomResults) {
                        Triple entryHouseTrip = (Triple) entryHouse;
                        String[] house = entryHouseTrip.getIdentifier().split(" ");
                        //System.out.println("Debug Found Them in " +  model.getName(house[2]) + " !");
                        return house[2] + ":" + room[2] + ":" + UUID[0];
                    }
                }

            //} else {
            //if(false){
                throw new invalidAvaCommand();
            //}

        } catch (invalidAvaCommand ex){
            System.out.println(ex + "Ava command where could not find " + name + " or is invalid");
        }

        return "";
    }
}
