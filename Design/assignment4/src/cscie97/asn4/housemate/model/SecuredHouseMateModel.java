package cscie97.asn4.housemate.model;

import cscie97.asn4.housemate.entitlement.AccessToken;
import cscie97.asn4.housemate.entitlement.HouseMateEntitler;
import cscie97.asn4.housemate.entitlement.User;
import cscie97.asn4.knowledge.engine.Triple;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by n0305853 on 11/16/18.
 * Meant to be a decorator around each function of the house model to add the security token without touching the internal interface.
 */
public class SecuredHouseMateModel extends HouseMateModel {

    HouseMateEntitler titler = HouseMateEntitler.getInstance();

    public String getValue(String[] entities, String valueType, AccessToken token){
        if(titler.checkToken(token)) {
            return getValue(entities, valueType);
        }
        return "";
    }

    public void removeValue(String UUID, String valueType, String value, AccessToken token){
        if(titler.checkToken(token)) {
            removeValue(UUID, valueType, value);
        }
    }

    public void setValue(String UUID, String valueType, String value, AccessToken token){
        if(titler.checkToken(token)) {
            setValue(UUID, valueType, value);
        }
    }

    public void setValue(String[] owner, String valueType, String value, AccessToken token){
        String userId = titler.getUserId(token);
        String tmp = owner[owner.length-1];
        String target = tmp.substring(0,tmp.length()-1);
        //System.out.println("DEBUG setVALUE :: " + target);
        if(titler.checkToken(token) && titler.checkPermissions(userId, target) ) {
            setValue(owner, valueType, value);

        }
    }

    public void removeAssociation(String UUID1, String UUID2, AccessToken token){
        if(titler.checkToken(token)) {
            removeAssociation(UUID1, UUID2);
        }
    }

    /**
     * If this is the occupant entering the house then we need to add any related resource roles.
     * @param UUID1
     * @param UUID2
     * @param token
     */
    public void setAssociation(String UUID1, String UUID2, AccessToken token) {
        if (titler.checkToken(token)) {
            setAssociation(UUID1, UUID2);

            // If UUID is a house and UUID1 is an occupant. Update resource role
            String name1 = getName(UUID1, token);
            String name2 = getName(UUID2, token);
            Triple firstTrip = getEntity(name1, token);
            String [] first = firstTrip.getIdentifier().split(" ");
            Triple secondTrip = getEntity(name2, token);
            String [] second = secondTrip.getIdentifier().split(" ");
            /*if(first[1].equals("occupant") && second[1].equals("house")){
                titler.addResourceRole(first[2], second[2]);
            }*/

            String[] type = null;
            Set nameSet = valueGraph.executeQuery(UUID1, "type", "?");
            for(Object name: nameSet) {
                Triple nameTrip = (Triple) name;
                type = nameTrip.getIdentifier().split(" ");
            }
            //System.out.println("added user debug" + Arrays.toString(type));
            // If they are child or adult add their resourceroles.
            if (type != null && type[2].toLowerCase().equals("adult") ){
                titler.addRole(first[2],"adult_resident");
            } else if(type != null && type[2].toLowerCase().equals("child") ){
                titler.addRole(first[2],"child_resident");
            }
        }
    }

    public void purgeEntity(String[] entities, AccessToken token){
        if(titler.checkToken(token)) {
            purgeEntity(entities);
        }
    }

    public String showEnergyUse(AccessToken token){
        if(titler.checkToken(token)) {
            return showEnergyUse();
        }
        return "";
    }

    public String showEnergyUse(String[] entities, AccessToken token){
        if(titler.checkToken(token)) {
            return showEnergyUse(entities);
        }
        return "";
    }

    public String showConfiguration(AccessToken token){
        if(titler.checkToken(token)) {
            return showConfiguration();
        }
        return "";
    }

    public String showConfiguration(String[] entities, AccessToken token){
        if(titler.checkToken(token)) {
            return showConfiguration(entities);
        }
        return "";
    }

    public void addAppliance(String name, String type, String house, String room, String energyUse, AccessToken token){
        if(titler.checkToken(token)) {
            addAppliance(name, type, house, room, energyUse);
        }
    }

    /**
     * Overrides, creates a new user in the entitler to tie to the occupant.
     * @param name
     * @param type
     * @param token
     */
    public void addOccupant(String name, String type, AccessToken token){
        if(titler.checkToken(token)){
            addOccupant(name, type);
            //System.out.println("debug occupant added");
            titler.createUser(name, name);
        }
    }

    public void addRoom(String name, String floor, String type, String house, String winCount, AccessToken token){
        if(titler.checkToken(token)) {
            addRoom(name, floor, type, house, winCount);
        }
    }

    public void addHouse(String name, String address, AccessToken token){
        if(titler.checkToken(token)) {
            addHouse(name, address);
        }
    }

    public String getName(String UUID, AccessToken token){
        if(titler.checkToken(token)) {
            return getName(UUID);
        }
        return "";
    }

     public String getApplianceUUID(String house, String room, String appliance, AccessToken token){
         if(titler.checkToken(token)) {
             return getApplianceUUID(house, room, appliance);
         }
         return "";
     }

     public String getRoomUUID(String house, String room, AccessToken token){
         if(titler.checkToken(token)) {
             return getRoomUUID(house, room);
         }
         return "";
     }

     public String getEntity(String[] entities, AccessToken token){
         if(titler.checkToken(token)) {
             return getEntity(entities, token);
         }
         return "";
     }

     public Triple getEntity(String entity, AccessToken token){
         if(titler.checkToken(token)) {
             return getEntity(entity);
         }
         return null;
     }
}



