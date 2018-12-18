package cscie97.asn4.housemate.test;
import cscie97.asn4.housemate.entitlement.*;
import cscie97.asn4.housemate.model.HouseMateModel;
import cscie97.asn4.housemate.model.SecuredHouseMateModel;
import cscie97.asn4.knowledge.engine.Triple;

import java.util.Arrays;
import java.util.Set;


/**
 * Created by n0305853 on 10/15/18.
 */
public class FunctionalTestDriver {
    public static void main(String [] args) {
        //System.out.println("TEST DRIVER!");
        /*
        User jeb = new User("001", "Jeb");
        Account acc = new Account("002", "Jeb", "jebiscool");
        AccessToken token = new AccessToken("003");
        jeb.addPassword(acc);
        jeb.addToken(token);
        System.out.println(jeb.toString());
        */

        //HouseMateEntitler titler = new HouseMateEntitler();
        SecuredHouseMateModel model = new SecuredHouseMateModel();
        HouseMateEntitler titler = HouseMateEntitler.getInstance();
        titler.createUser("001", "Alex");
        titler.createRole("002", "GuestRole", "Anyone who shows up invited.");
        titler.createResourceRole("DefaultHouseRole", "002", "mansion");
        titler.createPermission("003", "BedPermission", "Allows you to get on the bed without attack drones flying out.");
        titler.addEntitlement("002", "003");
        titler.addRole("001", "002");
        titler.createResourceRole("004", "ChildGuest", "Default rules for children guests.", "house1");
        titler.addCredential("001", "voiceprint", "--alex--");
        titler.addCredential("001", "password", "supersecretpassword");
        System.out.println("Validation Creds : " + titler.checkCredentials("001", "supersecretpassword"));
        System.out.println("Validation Perms : " + titler.checkPermissions("001", "Allows you to get on the bed without attack drones flying out."));

        System.out.println("Validation Exceptions : " + titler.checkCredentials("001", "superwoke"));
        System.out.println("Validation Perms : " + titler.checkPermissions("001", "Allows you to get on the bed without attack drones flying out"));

        // LOGIN SHOULD SUCCEED HERE
        AccessToken token = titler.login("Admin", "trustNo1");
        System.out.println("Testing access token :: Returns should be true " + titler.checkToken(token));
        model.addOccupant("dio", "Adult", token);
        model.addHouse("mansion", "51 Cornwall", token);
        Triple firstTrip = model.getEntity("Dio", token);
        String [] first = firstTrip.getIdentifier().split(" ");
        Triple secondTrip = model.getEntity("Mansion", token);
        String [] second = secondTrip.getIdentifier().split(" ");
        //System.out.println("debug here :: " + first[1] + second[1]);
        model.setAssociation(first[0], second[0], token);

        //titler.addResourceRole("Admin", "*");
        //Token strts working again.
        //token = titler.login("Admin", "trustNo1");
        // testing perms, mary has full permissions on oven because she just set them and she is admin.
        //titler.createPermission("009", "Full Oven Controls", "Full control of the oven.");
        //HouseMateModel service = HouseMateModel.getInstance();
        //HouseMateController observer = new HouseMateController(service);

        System.out.println("-------housemate stuff starts---------");

        model.addHouse("house1", "1 Story Street Cambridge, MA", token);
        model.addHouse("house2", "2 Story Street Cambridge, MA", token);
        model.addHouse("house3", "3 Story Street Cambridge, MA", token);

        model.addRoom("kitchen1", "1", "kitchen", "house1", "2", token);
        model.addRoom("kitchen1", "1", "kitchen", "house2", "400", token);
        model.addRoom("kitchen1", "1", "kitchen", "house3", "2", token);
        model.addRoom("kitchen2", "2", "kitchen", "house1", "2", token);
        model.addRoom("kitchen2", "2", "kitchen", "house2", "4", token);
        model.addAppliance("oven1", "oven", "house1", "kitchen1", "500", token);

        model.addOccupant("martin", "adult", token);
        //service.setAssociation("martin", "house1");
        model.addOccupant("mary", "adult", token);
        //service.setAssociation("martin", "house2");
        model.addAppliance("smoke_detector", "smoke_detector", "house1","kitchen1", "0", token);
        model.addAppliance("oven1", "oven", "house1", "kitchen1", "500", token);
        model.addAppliance("fridge1", "fridge", "house1", "kitchen1", "250", token);
        model.addAppliance("camera1", "camera", "house1","kitchen1", "12", token);
        model.addAppliance("tv1", "tv", "house1","kitchen1", "120", token);

        String[] homeRoom0 = {"house3"};
        //System.out.println(service.showConfiguration());
        String[] homeRoom = {"house3", "kitchen1"};
        //System.out.println(service.showConfiguration(homeRoom));
        String[] homeRoom2 = {"house1", "kitchen1", "oven1"};
        String[] fridge = {"house1", "kitchen1", "fridge1"};
        String[] camera = {"house1", "kitchen1", "camera1"};
        //  String[] fridge = {"house1", "kitchen1", "fridge1"};
        model.setValue(camera, "occupant_entered", "mary", token);
        model.setValue(homeRoom2, "temperature", "500", token);
        model.setValue(homeRoom2, "TimeToCook", "1", token);



        // test child perms
        System.out.println("\nTest showing resourceRoles and Perms in action. Jotaro should be able to close door with no exception, but an exception is thrown when he tries to turn on the oven.");
        model.addOccupant("jotaro", "Child", token);
        titler.addCredential("jotaro", "password", "joestar");
        AccessToken joToken = titler.login("jotaro", "joestar");

        // Forgot to associate him with house to get his role, wasted hours.
        Triple trip = model.getEntity("jotaro", token);
        String[] joUUId = trip.getIdentifier().split(" ");
        trip = model.getEntity("house1", token);
        String[] houseUUId = trip.getIdentifier().split(" ");

        //System.out.println(Arrays.toString(joUUId) + Arrays.toString(houseUUId));
        model.setAssociation(joUUId[0], houseUUId[0], token);

        String[] kitchen_door = {"house1", "kitchen1", "door1"};
        String[] kitchen_oven = {"house1", "kitchen1", "oven1"};
        model.addAppliance("door1", "door", "house1","kitchen1", "5", token);
        model.addAppliance("oven1", "oven", "house1", "kitchen1", "50", token);

        model.setValue(kitchen_oven, "status", "on", joToken);
        model.setValue(kitchen_door, "status", "closed", joToken);


        // LOGOUT SHOULD FAIL FORM HERE
        titler.logout(token);
        System.out.println("Testing logout :: Returns should be false now" + titler.checkToken(token));
        model.addOccupant("Jojo", "Adult", token);

    }
}
