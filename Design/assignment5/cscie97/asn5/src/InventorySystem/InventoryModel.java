package InventorySystem;
import AuthenticationService.AccessToken;
import AuthenticationService.HouseMateEntitler;
import KnowledgeGraph.*;
import java.util.Set;

/**
 * <h1>HouseMate Model Service</h1>
 * Created by: Brandon T. Wood
 * Date: Sept, 23rd 2018
 */
public class InventoryModel {
    private static InventoryModel instance = new InventoryModel();
    private HouseMateEntitler entitler = HouseMateEntitler.getInstance();
    public KnowledgeGraph entityGraph = new KnowledgeGraph();
    public KnowledgeGraph associationGraph = new KnowledgeGraph();
    public KnowledgeGraph valueGraph = new KnowledgeGraph();
    //private ArrayList<HouseMateController> observerList = new ArrayList<HouseMateController>();
    //private ArrayList<Observer> observerList = new ArrayList<Observer>();
    private int UUIDCount = 0;


    /**
     * Singleston reference, returns the instance of the HouseMateModel.
     * @return
     */
    public static InventoryModel getInstance(){
        return instance;
    }


    /**
     * Adds the given observer to the list of observers for tracking and updating.
     * @param observer

    public void addObserver(Observer observer){
        observerList.add(observer);
    }


    /**
     * Updates all observers in the observerList with recent changes.
     *  @param change
     *
    private void updateObservers(String change){
        for (Observer obs : observerList){
            obs.update(change);
        }
    }
    */

    /**
     * Generates a unique user identification for the entire service instance.
     * @return
     */
    private String generateUUID(){
        UUIDCount++;
        return String.valueOf(UUIDCount);
    }


    /**
     * Finds the triple related to the name in the entityGraph and returns it. Depreciated for all by houses.
     * @param entity
     * @return
     */
    private Triple getEntity(String entity){
        try {
            Set entrySet = entityGraph.executeQuery("?", "?", entity);
            Triple entryTrip = null;
            for (Object entry : entrySet) {
                entryTrip = (Triple) entry;
                break;
            }
            if (entryTrip != null) {
                return entryTrip;
            } else {
                throw new objectNotFoundException();
            }
        } catch (objectNotFoundException ex) {
            System.out.print(ex + " entity: " + entity + " not found." );
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Returns the name of the eneity for the UUID provided.
     * @param UUID
     * @return
     */
    private String getName(String UUID){
        String name="";
        try {
            Set nameSet = entityGraph.executeQuery(UUID, "?", "?");
            for (Object nameEntry : nameSet) {
                Triple nameTrip = (Triple) nameEntry;
                String[] nam = nameTrip.getIdentifier().split(" ");
                name = nam[2];
            }
            if(name.equals("")){
                throw new objectNotFoundException();
            }
        }catch (objectNotFoundException ex){
            System.out.print(ex + " UUID: " + UUID + " not found." );
            ex.printStackTrace();
        }
        return name;
    }


    /**
     * Returns the values and associations of an entity and all it's sub entities as a String.
     * @param entity
     * @return
     */
    public String showConfiguration(String entity){
        // First we need to iterate over entity graph to find UUID
        Triple entityTrip = getEntity(entity);
        String[] UUID = entityTrip.getIdentifier().split(" ");

        // Now that we have the root we can iterate and add to the accumulator string.
        String accum = "";

        //Now we need to print out all relating values.
        Set valueResults = valueGraph.executeQuery(UUID[0], "?", "?");
        for(Object entry: valueResults) {
            Triple resultTrip = (Triple) entry;
            accum = accum + "\n  > " + resultTrip.getIdentifier();
        }

        //Now we need to print out all sub enties and their values
        Set associationResults = associationGraph.executeQuery("?", "?", UUID[0]);
        for(Object entry: associationResults) {
            Triple entryTrip = (Triple) entry;
            String[] subUUID = entryTrip.getIdentifier().split(" ");
            Set nameSet = entityGraph.executeQuery(subUUID[0], "?", "?");
            for(Object name: nameSet) {
                Triple nameTrip = (Triple) name;
                String [] nam = nameTrip.getIdentifier().split(" ");
                accum = accum + "\n =" + nam[2] + "=" + showConfiguration(nam[2]);
            }
        }
        return accum;
    }


    /**
     * Override for show configuration that iterates over all houses, calls the original method, summates and returns the String of all configuration.
     * @return
     */
    public String showConfiguration(){
        String accum = "";

        //Kinda hacky but can clean this up another time.
        Set asteroidResults = entityGraph.executeQuery("?", "asteroid", "?");
        Set controlResults = entityGraph.executeQuery("?", "control", "?");
        Set houseResults = entityGraph.executeQuery("?", "spaceship", "?");
        houseResults.addAll(asteroidResults);
        houseResults.addAll(controlResults);

        // Iterate over the list of all houses and accum their showConfigs.
        for(Object entry: houseResults) {
            Triple entryTrip = (Triple) entry;
            String[] subUUID = entryTrip.getIdentifier().split(" ");
            //Set nameSet = entityGraph.executeQuery(subUUID[0], "?", "?");
            System.out.println();
            System.out.print("====" + subUUID[2] + "====");
            System.out.print(showConfiguration(subUUID[2]));
        }
        return accum;
    }


    /**
     * Takes two names and if they do not already exist it associates them. Concluding that the first name is IN the second name.
     * @param UUID1
     * @param UUID2
     */
    private void setAssociation(String UUID1, String UUID2){
        // If object is already associated we need to delete it.
        // If value already has a setting we should remove it.
        Set results = associationGraph.executeQuery(UUID1, "inside", "?");
        for(Object entry: results) {
            associationGraph.removeTriple((Triple) entry);
        }
        //valueGraph.getTriple(UUID, valueType, value);
        associationGraph.getTriple(UUID1, "inside", UUID2);
    }


    /**
     * Removes an association between two entities.
     * @param UUID1
     * @param UUID2
     */
    private void removeAssociation(String UUID1, String UUID2){
        try {
            // If value already has a setting we should remove it.
            Set results = associationGraph.executeQuery(UUID1, "inside", UUID2);
            if(results.size() < 1){
                throw new objectNotFoundException();
            }
            for (Object entry : results) {
                associationGraph.removeTriple((Triple) entry);
            }
        } catch (objectNotFoundException ex){
            System.out.println("Cannot remove non-existing association between: " + UUID1 + " : " + UUID2 + " :" + ex);
        }
    }


    /**
     * another set value that takes a UUID instead of a owner, takes in that UUId a value type and a value, then creates the value in the valueGraph.
     * @param UUID
     * @param valueType
     * @param value
     */
    private void setValue(String UUID, String valueType, String value){
        // If value already has a setting we should remove it.
        Set results = valueGraph.executeQuery(UUID, valueType, "?");
        for(Object entry: results) {
            valueGraph.removeTriple((Triple) entry);
        }
        valueGraph.getTriple(UUID, valueType, value);

        //Get room and House for update
        // The only time an update needs to be called, also covers initial values well enough.
        //System.out.println("debug setValue " + nam[2] + ":" + nam[1] + ":" + UUID + ":" + valueType + ":" + value);
        //updateObservers(nam[2] + ":" + nam[1] + ":" + UUID + ":" + valueType + ":" + value);
    }


    /**
     * Removes a value from the valueGraph, takes all an UUID, value type and value.
     * @param UUID
     * @param valueType
     * @param value
     */
    private void removeValue(String UUID, String valueType, String value){
        try {
            // If value already has a setting we should remove it.
            Set results = valueGraph.executeQuery(UUID, valueType, value);
            if(results.size() < 1){
                throw new objectNotFoundException();
            }
            for (Object entry : results) {
                valueGraph.removeTriple((Triple) entry);
            }
            //System.out.println("Debug value being saved>>> " + value);
        } catch (objectNotFoundException ex){

            System.out.println("Cannot remove non-existing value: " + valueType + " : " + value + " :" + ex);
        }
    }


    /**
     * Takes a entity of owner and subentities and a value type, finds the value associated. Otherwise returns emtpy string.
     * @param entities
     * @param valueType
     * @return
     */
    private String getValue(String entities, String valueType){
        try {
            Triple entityTrip = getEntity(entities);
            String[] UUID = entityTrip.getIdentifier().split(" ");
            Set nameSet = valueGraph.executeQuery(UUID[0], valueType, "?");
            for (Object name : nameSet) {
                Triple nameTrip = (Triple) name;
                // BUG HERE, splits by spaces, wrecks everything. HAH
                String[] nam = nameTrip.getIdentifier().split(" ");
                //System.out.println(Arrays.toString(nam));
                if (nam.length == 3) {
                    return nam[2];
                }else{
                    throw new objectNotFoundException();
                }
            }
        } catch (objectNotFoundException ex) {
            System.out.print(ex + " value: " + valueType + " not found for object: " + entities + "." );
            ex.printStackTrace();
        }
        return "";

    }


    /**
     * Given below variables creates a new asterdoid entity in the enetiy graph and associates its values in the value graph.
     * @param name
     * @param type
     * @param width
     * @param length
     * @param height
     * @param mass
     * @param gravity
     * @param aphelion
     * @param perihelion
     */
    public void addAsteroid(String name, String type, String width, String length, String height, String mass, String gravity, String aphelion, String perihelion, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "asteroid", name);

            //Add values
            valueGraph.getTriple(UUID, "type", type);
            valueGraph.getTriple(UUID, "width", width);
            valueGraph.getTriple(UUID, "length", length);
            valueGraph.getTriple(UUID, "height", height);
            valueGraph.getTriple(UUID, "mass", mass);
            valueGraph.getTriple(UUID, "gravity", gravity);
            valueGraph.getTriple(UUID, "aphelion", aphelion);
            valueGraph.getTriple(UUID, "perihelion", perihelion);

            //System.out.println("Asteroid Created!");
        }
    }


    /**
     * Given below values creates a new note and associates it to it's related asteroid.
     * @param asteroidName
     * @param date
     * @param author
     * @param msg
     */
    public void addNote(String asteroidName, String date, String author, String msg, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "note", "Log_" + date);

            //Add values
            valueGraph.getTriple(UUID, "date", date);
            valueGraph.getTriple(UUID, "author", author);
            valueGraph.getTriple(UUID, "msg", msg);

            // Associate it to it's Asteroid
            Triple entityTrip = getEntity(asteroidName);
            String[] asteroidUUID = entityTrip.getIdentifier().split(" ");
            associationGraph.getTriple(UUID, "note", asteroidUUID[0]);
        }

    }


    /**
     * Given below params will produce a new discovery entity and associate it with it's related asteroid.
     * @param asteroidName
     * @param mineralType
     * @param mass
     * @param depositType
     */
    public void addMineralDiscovery(String asteroidName, String mineralType, String mass, String depositType, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "discovery", "discovered:" + mineralType);

            //Add values
            valueGraph.getTriple(UUID, "mineralType", mineralType);
            valueGraph.getTriple(UUID, "mass", mass);
            valueGraph.getTriple(UUID, "depositType", depositType);

            // Associate it to it's Asteroid
            Triple entityTrip = getEntity(asteroidName);
            String[] asteroidUUID = entityTrip.getIdentifier().split(" ");
            associationGraph.getTriple(UUID, "discovery", asteroidUUID[0]);
        }
    }


    /**
     * Given below params will produce a new discovery entity and associate it with it's related asteroid.
     * @param asteroidName
     * @param found
     * @param amount
     * @param state
     */
    public void addWaterDiscovery(String asteroidName, String found, String amount, String state, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "discovery", "discovered:" + state);

            //Add values
            valueGraph.getTriple(UUID, "found", found);
            valueGraph.getTriple(UUID, "amount", amount);
            valueGraph.getTriple(UUID, "state", state);

            // Associate it to it's Asteroid
            Triple entityTrip = getEntity(asteroidName);
            String[] asteroidUUID = entityTrip.getIdentifier().split(" ");
            associationGraph.getTriple(UUID, "discovery", asteroidUUID[0]);
        }
    }


    /**
     * Given below params will produce a new discovery entity and associate it with it's related asteroid.
     * @param asteroidName
     * @param type
     * @param intelligent
     * @param friendly
     */
    public void addLifeDiscovery(String asteroidName, String type, String intelligent, String friendly, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "discovery", "discovered:life");

            //Add values
            valueGraph.getTriple(UUID, "type", type);
            valueGraph.getTriple(UUID, "intelligent", intelligent);
            valueGraph.getTriple(UUID, "friendly", friendly);

            // Associate it to it's Asteroid
            Triple entityTrip = getEntity(asteroidName);
            String[] asteroidUUID = entityTrip.getIdentifier().split(" ");
            associationGraph.getTriple(UUID, "discovery", asteroidUUID[0]);
        }
    }


    /**
     * Given the below values it generates a new ship and adds it to the entity graph and it's values to the value graph.
     * @param shipName
     * @param launchDate
     * @param mission
     * @param type
     * @param fuel
     * @param guidanceSys
     * @param communicationSys
     * @param state
     * @param location
     * @param target
     */
    public void addSpaceShip(String shipName, String launchDate, String mission, String type, String fuel, String guidanceSys, String communicationSys, String state, String location, String target, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "spaceship", shipName);

            //Add values
            valueGraph.getTriple(UUID, "launchDate", launchDate);
            valueGraph.getTriple(UUID, "mission", mission);
            valueGraph.getTriple(UUID, "type", type);
            valueGraph.getTriple(UUID, "fuel", fuel);
            valueGraph.getTriple(UUID, "guidanceSys", guidanceSys);
            valueGraph.getTriple(UUID, "communicationSys", communicationSys);
            valueGraph.getTriple(UUID, "state", state);
            valueGraph.getTriple(UUID, "location", location);
            valueGraph.getTriple(UUID, "target", target);

            // Associate it to it's Mission
            Triple entityTrip = getEntity(mission);
            String[] missionUUID = entityTrip.getIdentifier().split(" ");
            associationGraph.getTriple(missionUUID[0], "mission", UUID);
        }
    }


    /**
     * Given below paramters will create a new mission entity, add it to the entity graph and also the relating values.
     * @param missionID
     * @param missionName
     * @param desc
     * @param spacecraftName
     * @param launchDate
     * @param etaDate
     * @param target
     * @param state
     */
    public void createMission(String missionID, String missionName, String desc, String spacecraftName, String launchDate, String etaDate, String target, String state, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add asteroid entity
            entityGraph.getTriple(UUID, "mission", missionName);

            //Add values
            valueGraph.getTriple(UUID, "description", desc);
            valueGraph.getTriple(UUID, "spacecraft", spacecraftName);
            valueGraph.getTriple(UUID, "launchDate", launchDate);
            valueGraph.getTriple(UUID, "etaDate", etaDate);
            valueGraph.getTriple(UUID, "state", state);
            valueGraph.getTriple(UUID, "target", target);
        }
    }


    /**
     * Given below params creates a new mission control station, generating an entity graph value and a value graph value set.
     * @param name
     * @param fuel
     * @param resources
     * @param budget
     * @param commLink
     * @param controlSys
     */
    public void addMissionControl(String name, String fuel, String resources, String budget, String commLink, String controlSys, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            String UUID = generateUUID();
            //Add mission control entity
            entityGraph.getTriple(UUID, "control", name);

            //Add values
            valueGraph.getTriple(UUID, "fuel", fuel);
            valueGraph.getTriple(UUID, "resources", resources);
            valueGraph.getTriple(UUID, "budget", budget);
            valueGraph.getTriple(UUID, "commLink", commLink);
            valueGraph.getTriple(UUID, "controlSys", controlSys);
        }
    }


    /**
     * Modifies the fuel value of the control that has teh value name.
     * @param name
     * @param value
     * @param token
     */
    public void incrementFuel(String name, String value, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            // get entity UUID
            Triple entityTrip = getEntity(name);
            String[] UUID = entityTrip.getIdentifier().split(" ");

            // get and set integer value
            int currValue = Integer.parseInt(getValue(name, "fuel"));
            int newValue = Integer.parseInt(value);
            newValue += currValue;
            //System.out.println(":"+newValue);
            setValue(UUID[0], "fuel", "" + newValue);
        }
    }


    /**
     * Modifies the resources value of the control that has teh value name.
     * @param name
     * @param value
     * @param token
     */
    public void incrementResource(String name, String value, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            // get entity UUID
            Triple entityTrip = getEntity(name);
            String[] UUID = entityTrip.getIdentifier().split(" ");

            // get and set integer value
            int currValue = Integer.parseInt(getValue(name, "resources"));
            int newValue = Integer.parseInt(value);
            newValue += currValue;
            //System.out.println(":"+newValue);
            setValue(UUID[0], "resources", "" + newValue);
        }
    }


    /**
     * Modifies the budget value of the control that has the value name.
     * @param name
     * @param value
     * @param token
     */
    public void incrementBudget(String name, String value, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            // get entity UUID
            Triple entityTrip = getEntity(name);
            String[] UUID = entityTrip.getIdentifier().split(" ");

            // get and set integer value
            int currValue = Integer.parseInt(getValue(name, "budget"));
            int newValue = Integer.parseInt(value);
            newValue += currValue;
            //System.out.println(":"+newValue);
            setValue(UUID[0], "budget", "" + newValue);
        }
    }


    /**
     * Modifies the communication status value of the control that has the value name.
     * @param name
     * @param value
     * @param token
     */
    public void setCommunicationLinkStatus(String name, String value, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            // get entity UUID
            Triple entityTrip = getEntity(name);
            String[] UUID = entityTrip.getIdentifier().split(" ");
            // Set vlue
            setValue(UUID[0], "commLink", value);
        }
    }


    /**
     * Modifies the control system status value of the control that has the value name.
     * @param name
     * @param value
     * @param token
     */
    public void setControlSystemStatus(String name, String value, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            // get entity UUID
            Triple entityTrip = getEntity(name);
            String[] UUID = entityTrip.getIdentifier().split(" ");
            // Set vlue
            setValue(UUID[0], "controlSys", value);
        }
    }


    public void list(String type, Object token) {
        if(entitler.checkToken((AccessToken) token)) {
            System.out.println("===Results===");
            // Prints out all entities of type given.
            Set valueResults = entityGraph.executeQuery("?", type, "?");
            for(Object entry: valueResults) {
                Triple resultTrip = (Triple) entry;
                System.out.println("  > " + resultTrip.getIdentifier());
            }
        }
    }


    public void query(String query, Object token) {
        String accum = "";
        // First we check for it as a name in entity and return that.
        Set valueResults = entityGraph.executeQuery("?", "?", query);
        for(Object entry: valueResults) {
            Triple resultTrip = (Triple) entry;
            accum += "\n  > " + resultTrip.getIdentifier();
        }

        // If that doesn't return anything we need to check the notes and return their.
        if(accum.equals("")) {
            valueResults = valueGraph.executeQuery("?", "msg", "?");
            for(Object entry: valueResults) {
                Triple resultTrip = (Triple) entry;
                String result[] = resultTrip.getIdentifier().split(" ");

                // If we found it then we need to grab the related note then the related asteroid.
                if (result[2].contains(query)) {

                    // Another more time to get entity
                    Set associationResults = associationGraph.executeQuery(result[0], "?", "?");
                    for (Object association : associationResults) {
                        Triple associationTrip = (Triple) association;
                        String resultEntity[] = associationTrip.getIdentifier().split(" ");

                        // Final time to get related entity.
                        Set asteroidResults = entityGraph.executeQuery(resultEntity[2], "?", "?");
                        for (Object asteroid : asteroidResults) {
                            Triple asteroidTrip = (Triple) asteroid;
                            accum += "\n  > " + asteroidTrip.getIdentifier();

                            // Will need to consider this I think which is what he is looking for but does not specify.
                            //showConfiguration();
                        }
                        //accum += "\n>" + asteroidTrip.getIdentifier();
                    }
                }
            }
        }
        // Else print zero results.
        if (accum.equals("")){
            accum += "\n  > Zero results found";
        }

        System.out.println("==Results==" + accum);
    }


    /**
     * Inner Exception Class for when entities that don't exist are queried.
     */
    private static class objectNotFoundException extends Throwable {}

}