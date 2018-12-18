package cscie97.asn3.housemate.test;
import cscie97.asn3.housemate.controller.HouseMateController;
import cscie97.asn3.housemate.model.HouseMateModel;


/**
 * Created by n0305853 on 10/15/18.
 */
public class FunctionalTestDriver {
    public static void main(String [] args) {
        //System.out.println("TEST DRIVER!");
        HouseMateModel service = HouseMateModel.getInstance();
        HouseMateController observer = new HouseMateController(service);

        service.addHouse("house1", "1 Story Street Cambridge, MA");
        service.addHouse("house2", "2 Story Street Cambridge, MA");
        service.addHouse("house3", "3 Story Street Cambridge, MA");

        service.addRoom("kitchen1", "1", "kitchen", "house1", "2");
        service.addRoom("kitchen1", "1", "kitchen", "house2", "400");
        service.addRoom("kitchen1", "1", "kitchen", "house3", "2");
        service.addRoom("kitchen2", "2", "kitchen", "house1", "2");
        service.addRoom("kitchen2", "2", "kitchen", "house2", "4");
        service.addAppliance("oven1", "oven", "house1", "kitchen1", "500");

        service.addOccupant("martin", "adult");
        //service.setAssociation("martin", "house1");
        service.addOccupant("mary", "adult");
        //service.setAssociation("martin", "house2");
        service.addAppliance("smoke_detector", "smoke_detector", "house1","kitchen1", "0");
        service.addAppliance("oven1", "oven", "house1", "kitchen1", "500");
        service.addAppliance("fridge1", "fridge", "house1", "kitchen1", "250");
        service.addAppliance("camera1", "camera", "house1","kitchen1", "12");
        service.addAppliance("tv1", "tv", "house1","kitchen1", "120");

        String[] homeRoom0 = {"house3"};
        //System.out.println(service.showConfiguration());
        String[] homeRoom = {"house3", "kitchen1"};
        //System.out.println(service.showConfiguration(homeRoom));
        String[] homeRoom2 = {"house1", "kitchen1", "oven1"};
        String[] fridge = {"house1", "kitchen1", "fridge1"};
        String[] camera = {"house1", "kitchen1", "camera1"};
        //  String[] fridge = {"house1", "kitchen1", "fridge1"};
        service.setValue(camera, "occupant_entered", "mary");
        service.setValue(homeRoom2, "temperature", "500");
        service.setValue(homeRoom2, "TimeToCook", "1");

        String[] kitchen_door = {"house1", "kitchen1", "kitchen_door"};
        String[] kitchen_ava = {"house1", "kitchen1", "kitchen_ava"};
        service.addAppliance("kitchen_door", "door", "house1","kitchen1", "5");
        service.addAppliance("kitchen_ava", "ava", "house1", "kitchen1", "5");

        service.addAppliance("kitchen_ava", "ava", "house1", "kitchen1", "5");
        // kitchen_ava:appliance:17:voice_command:jane:where_is_astro
        //service.setValue(kitchen_ava, "voice_command", "martin:where_is_mary");
        service.showConfiguration();
        service.setValue(kitchen_ava, "voice_command", "mary:open_door");
        service.setValue(kitchen_ava, "voice_command", "mary:lights_off");
        service.setValue(kitchen_ava, "voice_command", "mary:dim_lights");
        service.setValue(kitchen_ava, "voice_command", "mary:tv_channel_7");
        String[] kitchen_smoke = {"house1", "kitchen1", "smoke_detector"};
        //service.setValue(kitchen_smoke, "status", "fire");
        //service.showConfiguration();
        //service.setValue(fridge, "beerCount", "24");
        // Some party happens and... should set off an alarm.
        //service.setValue(fridge, "beerCount", "3");

        // Prove exceptions
        service.setValue(kitchen_ava, "voice_command", "“martin:where_is_martin”");
        //service.setValue(camera, "occupant_entered", "stewart");

    }
}
