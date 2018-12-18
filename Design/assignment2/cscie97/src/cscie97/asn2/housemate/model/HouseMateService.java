package cscie97.asn2.housemate.model;
import java.util.Arrays;
import java.util.Set;
/**
 * <h1>HouseMate Model Service</h1>
 * Created by: Brandon T. Wood
 * Date: Sept, 23rd 2018
 */
public class HouseMateService {
    // Global Variables
    private KnowledgeGraph entityGraph = new KnowledgeGraph();
    private KnowledgeGraph associationGraph = new KnowledgeGraph();
    private KnowledgeGraph valueGraph = new KnowledgeGraph();
    private int UUIDCount = 0;


    /**
     * Generates a unique user identification for the entire service instance.
     * @return
     */
    public String generateUUID(){
        UUIDCount++;
        return String.valueOf(UUIDCount);
    }


    /**
     * Finds the triple related to the name in the entityGraph and returns it. Depreciated for all by houses.
     * @param entity
     * @return
     */
    public Triple getEntity(String entity){
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
     * Takes a string array of owners and it's subentities, returns the UUID of the last entity, the most sub entity.
     * @param entities
     * @return
     */
    public String getEntity(String[] entities){
        String UUID = "";
        // If it's a house or an occupant there is only one value, just return the UUID as it's always true.
        if(entities.length == 1){
            //System.out.println("HOUSE or ocupant.");
            Triple trip = getEntity(entities[0]);
            String[] UUIDArray = trip.getIdentifier().split(" ");
            return UUIDArray[0];
        // If it's a room, we need to call getRoomUUID,
        }else if(entities.length == 2){
            //System.out.println("This is a room");
            return getRoomUUID(entities[0], entities[1]);
        // If it's three long it's an appliance.
        }else if(entities.length == 3){
            //System.out.println("An appliance or sensor!");
            return getApplianceUUID(entities[0], entities[1], entities[2]);
        }/*else{
            System.out.println("Not sure what this is, throw an exception!");
        }*/
        // if made it here, entity does not exist, string is malformed.
        return UUID;
    }


    /**
     * Takes a owner house and a sub entity room, returns the unique user id of that room if it exists, otherwise throws exception.
     * @param house
     * @param room
     * @return
     */
    public String getRoomUUID(String house, String room){
        String UUID = "";
        try {
            Triple trip = getEntity(house);
            String[] UUIDArray = trip.getIdentifier().split(" ");
            String houseUUID = UUIDArray[0];

            Set associationResults = associationGraph.executeQuery("?", "?", houseUUID);
            for (Object entry : associationResults) {
                Triple entryTrip = (Triple) entry;
                String[] subUUID = entryTrip.getIdentifier().split(" ");
                if(getName(subUUID[0]).equals(room)){
                    UUID = subUUID[0];
                }
            }
            if (UUID.equals("")) {
                throw new objectNotFoundException();
            }
        } catch (objectNotFoundException ex){
            System.out.print(ex + " Room: " + room + " not found." );
            ex.printStackTrace();
        }
        return UUID;
    }


    /**
     * Takes in a owner house and room, and appliance string, returns the string of the unique user id for the appliance.
     * @param house
     * @param room
     * @param appliance
     * @return
     */
    public String getApplianceUUID(String house, String room, String appliance){
        String roomUUID = getRoomUUID(house, room);
        String UUID = "";
        try {
            Triple trip = getEntity(house);
            String[] UUIDArray = trip.getIdentifier().split(" ");
            String houseUUID = UUIDArray[0];

            Set associationResults = associationGraph.executeQuery("?", "?", roomUUID);
            for (Object entry : associationResults) {
                Triple entryTrip = (Triple) entry;
                String[] subUUID = entryTrip.getIdentifier().split(" ");
                if(getName(subUUID[0]).equals(appliance)){
                    UUID = subUUID[0];
                }
            }
            if (UUID.equals("")) {
                throw new objectNotFoundException();
            }
        } catch (objectNotFoundException ex){
            System.out.print(ex + " Appliance: " + appliance + " not found." );
            ex.printStackTrace();
        }
        return UUID;
    }


    /**
     * Returns the name of the eneity for the UUID provided.
     * @param UUID
     * @return
     */
    public String getName(String UUID){
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
     * Adds new home data to entity and value graphs.
     * @param name
     * @param address
     */
    public void addHouse(String name, String address){
        String UUID = generateUUID();
        //Add house entity
        entityGraph.getTriple(UUID, "house", name);
        //Add values
        valueGraph.getTriple(UUID, "address", address);
    }


    /**
     * Adds a new room to entity graph, it's associated house association and extra values to the value graph.
     * @param name
     * @param floor
     * @param house
     * @param winCount
     */
    public void addRoom(String name, String floor, String type, String house, String winCount){
        String UUID = generateUUID();
        //Add house entity
        entityGraph.getTriple(UUID, "room", name);
        //Add values
        valueGraph.getTriple(UUID, "floor", floor);
        valueGraph.getTriple(UUID, "winCount", winCount);
        valueGraph.getTriple(UUID, "type", type);
        //Add association
        //This trip and parse to array is a pain.. should be refactored out later..
        Triple trip = getEntity(house);
        String[] tripArray = trip.getIdentifier().split(" ");
        associationGraph.getTriple(UUID, "inside", tripArray[0]);
    }


    /**
     * Adds an entry to the entityGraph for a new person dataset, and a value of what type. adult, elder, children, etc to the valueGraph.
     * @param name
     * @param type
     */
    public void addOccupant(String name, String type){
        // Needs restraint checking for various types.
        String UUID = generateUUID();
        //Add house entity
        entityGraph.getTriple(UUID, "occupant", name);
        //Add values
        valueGraph.getTriple(UUID, "type", type);
    }


    /**
     * Adds appliances to entityGraph, association to room to association graph and extra values to the valueGraph.
     * @param name
     * @param type
     * @param house
     * @param room
     * @param energyUse
     */
    public void addAppliance(String name, String type, String house, String room, String energyUse){
        String UUID = generateUUID();
        //Add appliance entity
        entityGraph.getTriple(UUID, "appliance", name);

        //Add values
        valueGraph.getTriple(UUID, "type", type);
        valueGraph.getTriple(UUID, "energyUse", energyUse);

        String roomUUID = getRoomUUID(house, room);
        String[] test = {house, room, name};

        associationGraph.getTriple(UUID, "inside", roomUUID);
    }


    /**
     * Returns the values and associations of an entity and all it's sub entities as a String.
     * @param //entity
     * @return
    */
    private String showConfiguration(String entity){
        // First we need to iterate over entity graph to find UUID
        Triple entityTrip = getEntity(entity);
        String[] UUID = entityTrip.getIdentifier().split(" ");

        // Now that we have the root we can iterate and add to the accumulator string.
        String accum = "";

        //Now we need to print out all relating values.
        Set valueResults = valueGraph.executeQuery(UUID[0], "?", "?");
        for(Object entry: valueResults) {
            Triple resultTrip = (Triple) entry;
            accum = accum + "\n > " + resultTrip.getIdentifier();
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
                accum = accum + "\n===" + nam[2] + "===" + showConfiguration(nam[2]);
            }
        }
        return accum;
    }


    /**
     * New show configuration that uses helper methods to find and return all varaibles of a house level unique entity.
     * @param entities
     * @return
     */
    public String showConfiguration(String[] entities){
        String UUID = getEntity(entities);
        // Now that we have the root we can iterate and add to the accumulator string.
        String accum = "";

        //Now we need to print out all relating values.
        Set valueResults = valueGraph.executeQuery(UUID, "?", "?");
        for(Object entry: valueResults) {
            Triple resultTrip = (Triple) entry;
            accum = accum + "\n > " + resultTrip.getIdentifier();
        }

        //Now we need to print out all sub entities and their values
        Set associationResults = associationGraph.executeQuery("?", "?", UUID);
        for(Object entry: associationResults) {
            Triple entryTrip = (Triple) entry;
            String[] subUUID = entryTrip.getIdentifier().split(" ");
            Set nameSet = entityGraph.executeQuery(subUUID[0], "?", "?");
            for(Object name: nameSet) {
                Triple nameTrip = (Triple) name;
                String [] nam = nameTrip.getIdentifier().split(" ");
                //System.out.println("Debug showconfig variable >>>" + Arrays.toString(nam));
                accum = accum + "\n===" + nam[2] + "===" + showConfiguration(nam[2]);
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
        Set houseResults = entityGraph.executeQuery("?", "house", "?");

        // Iterate over the list of all houses and accum their showConfigs.
        for(Object entry: houseResults) {
            Triple entryTrip = (Triple) entry;
            String[] subUUID = entryTrip.getIdentifier().split(" ");
            Set nameSet = entityGraph.executeQuery(subUUID[0], "?", "?");
            for(Object name: nameSet) {
                Triple nameTrip = (Triple) name;
                String [] nam = nameTrip.getIdentifier().split(" ");
                accum = accum + "\n=====" + nam[2] + "=====" + showConfiguration(nam[2]);
            }
        }
        return accum;
    }


    /**
     * Parrent function, takes an String array of owner and sub entities and if it is a house or house calls methods that summates it's values and subvalues, then returns. Appliances are handled by getValue.
     * @param entities
     * @return
     */
    public String showEnergyUse(String[] entities){
        String UUID = getEntity(entities);

        // Got the UUID, but need it's complete triple.
        Set results = entityGraph.executeQuery(UUID, "?", "?");
        Triple trip = null;
        for(Object entry: results) {
            trip = (Triple) entry;
        }

        // Now parse into an array
        String[] UUIDArray = trip.getIdentifier().split(" ");
        if(UUIDArray[1].equals("house")) {
            // If it's a house, then we need to find all the rooms, and then all the appliances and total them up.
            return totalHouseEnergy(UUIDArray);
        }else if(UUIDArray[1].equals("room")){
            //If it's a room then we need to sum up all the sensors in that room.
            return totalRoomEnergy(UUIDArray);
        }else{
            // It's an appliance or sensor.
            return getValue(entities, "energyUse");
        }
    }


    /**
     * Overriden method for above, that if no string is passed will return root value and return it.
     * @return
     */
    public String showEnergyUse(){
        return totalEnergy();
    }


    /**
     * Looks at all energyUse values, summates and returns them.
     * @return
     */
    private String totalEnergy(){
        int accum = 0;
        Set results = valueGraph.executeQuery("?", "energyUse", "?");
        for(Object entry: results) {
            Triple resultTrip = (Triple) entry;
            String[] tripArray = resultTrip.getIdentifier().split(" ");
            //System.out.println(tripArray[2]);
            accum += Integer.parseInt(tripArray[2]);
        }
        return String.valueOf(accum);
    }


    /**
     * Iterates over a house and it's subentites, adding up and returning their final energy use.
     * @param house
     * @return
     */
    private String totalHouseEnergy(String[] house){
        Set results = associationGraph.executeQuery("?", "inside", house[0]);
        int accum = 0;

        // Iterate over a list of rooms found and summate their powers
        for(Object entry: results) {
            Triple resultTrip = (Triple) entry;
            String[] resultArray = resultTrip.getIdentifier().split(" ");
            accum += Integer.parseInt(totalRoomEnergy(resultArray));
        }
        return String.valueOf(accum);
    }

    /**
     * Summates the values of the room's sub entities.
     * @param room
     * @return
     */
    private String totalRoomEnergy(String[] room){
        Set results = associationGraph.executeQuery("?", "inside", room[0]);
        int accum = 0;

        // Iterate over a list of appliances found
        for(Object entry: results) {
            Triple resultTrip = (Triple) entry;
            String[] resultArray = resultTrip.getIdentifier().split(" ");

            // Find the energy use of the appliance.
            Set resultAppliances = valueGraph.executeQuery(resultArray[0], "energyUse", "?");
            for(Object appliance: resultAppliances) {
                Triple applianceTrip = (Triple) appliance;
                String[] applianceArray = applianceTrip.getIdentifier().split(" ");
                //System.out.println(applianceTrip.getIdentifier());
                accum += Integer.parseInt(applianceArray[2]);
            }
        }
        return String.valueOf(accum);
    }


    /**
     * Takes a string name of an entity, attempts to remove it, it's values, associations and sub entites.
     * @param entities
     */
    public void purgeEntity(String[] entities){
        //System.out.println(Arrays.toString((entities)));
        // First get the unigue user ID from the entities
        String UUIDString = getEntity(entities);
        Set results = entityGraph.executeQuery(UUIDString, "?", "?");
        Triple trip = null;
        for(Object entry: results) {
            trip = (Triple) entry;
        }
        String[] UUID = trip.getIdentifier().split(" ");
        Set ownerEntities = associationGraph.executeQuery(UUID[0], "?", "?");
        Set subEntities = associationGraph.executeQuery( "?", "?", UUID[0]);

        // What about his kids? If he has a sub-entity we append it and rerun.
        for(Object entry: subEntities) {
            Triple resultTrip = (Triple) entry;
            String result = resultTrip.getIdentifier();
            String[] array = result.split(" ");

            String name = getName(array[0]);
            String accum = "";
            for(String element : entities){
                accum += element + " ";
            }
            accum += name;
            //System.out.println("DEBUG Accum Entities>> " + accum);
            purgeEntity(accum.split(" "));
        }

        // Iterate over all owner entities and prune yourself from being inside them.
        for(Object entry: ownerEntities) {
            Triple resultTrip = (Triple) entry;
            //System.out.println("DEBUG Owner Entities>> " + resultTrip.getIdentifier());
            associationGraph.removeTriple(resultTrip);
        }

        // After this we need to remove his values though
        results = valueGraph.executeQuery(UUID[0], "?", "?");
        for(Object entry: results) {
            Triple resultTrip = (Triple) entry;
            valueGraph.removeTriple(resultTrip);
            //System.out.println("DEBUG removed vars>> " + resultTrip.getIdentifier());
        }
        //Last thing we do is remove his triple.
        entityGraph.removeTriple(trip);
    }


    /**
     * Takes two names and if they do not already exist it associates them. Concluding that the first name is IN the second name.
     * @param name1
     * @param name2
     */
    public void setAssociation(String name1, String name2){
        Triple entityTrip1 = getEntity(name1);
        String[] UUID1 = entityTrip1.getIdentifier().split(" ");
        Triple entityTrip2 = getEntity(name2);
        String[] UUID2 = entityTrip2.getIdentifier().split(" ");
        //associationGraph.getTriple(UUID1[0], "inside", UUID2[0]);
    }


    /**
     * Takes three values and if they do not exist in the value graph it adds them as an owner/valueType/value triple. Where owner is an array of entities and their sub entities.
     * @param owner
     * @param valueType
     * @param value
     */
    public void setValue(String[] owner, String valueType, String value){
        String UUID = getEntity(owner);
        // If value already has a setting we should remove it.
        Set results = valueGraph.executeQuery(UUID, valueType, "?");
        for(Object entry: results) {
            valueGraph.removeTriple((Triple) entry);
        }
        //System.out.println("Debug value being saved>>> " + value);
        valueGraph.getTriple(UUID, valueType, value);
    }


    /**
     * Takes a entity of owner and subentities and a value type, finds the value associated. Otherwise returns emtpy string.
     * @param entities
     * @param valueType
     * @return
     */
    public String getValue(String[] entities, String valueType){
        try {
            String UUID = getEntity(entities);
            Set nameSet = valueGraph.executeQuery(UUID, valueType, "?");
            for (Object name : nameSet) {
                Triple nameTrip = (Triple) name;
                // BUG HERE, splits by spaces, wrecks everything. HAH
                String[] nam = nameTrip.getIdentifier().split(" ");
                System.out.println(Arrays.toString(nam));
                if (nam.length == 3) {
                    return nam[2];
                }else{
                    throw new objectNotFoundException();
                }
            }
        } catch (objectNotFoundException ex) {
            System.out.print(ex + " value: " + valueType + " not found for object: " + Arrays.toString(entities) + "." );
            ex.printStackTrace();
        }
        return "";

    }


    /**
     * Inner Exception Class for when entities that don't exist are queried.
     */
    private static class objectNotFoundException extends Throwable {}

}