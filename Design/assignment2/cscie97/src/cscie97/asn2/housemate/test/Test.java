package cscie97.asn2.housemate.test;
import cscie97.asn2.housemate.model.HouseMateService;
//import cscie97.asn2.housemate.model.Triple;

/**
 * Created by n0305853 on 9/26/18.
 */
public class Test {
    public static void main(String[] args) {
        HouseMateService service = new HouseMateService();
        service.addHouse("house1", "1 Story Street Cambridge, MA");
        service.addHouse("house2", "2 Story Street Cambridge, MA");
        service.addHouse("house3", "3 Story Street Cambridge, MA");

        service.addRoom("kitchen1", "1", "kitchen", "house1", "2");
        service.addRoom("kitchen1", "1", "kitchen", "house2", "400");
        service.addRoom("kitchen1", "1", "kitchen", "house3", "2");
        service.addRoom("kitchen2", "1", "kitchen", "house1", "2");
        service.addRoom("kitchen2", "1", "kitchen", "house2", "4");
        service.addAppliance("oven1", "oven", "house1", "kitchen1", "500");

        service.addOccupant("martin_fowler", "adult");
        service.setAssociation("martin_fowler", "house1");
        service.addAppliance("smoke_detector", "smoke_detector", "house1","kitchen1", "0");
        service.addAppliance("oven1", "oven", "house1", "kitchen1", "500");
        /*service.setValue("oven1", "power", "on");
        service.setValue("oven1", "temperature", "350");
        System.out.println(service.getValue("oven1", "temperature"));
        System.out.println(service.showConfiguration("oven1"));
        System.out.println(service.showConfiguration("smoke_detector"));
        System.out.println(service.showConfiguration());
        System.out.println(service.showConfiguration("house1"));
        System.out.println(service.showConfiguration("kitchen1"));
        System.out.println(service.showConfiguration("oven1"));
        System.out.println(service.showEnergyUse());
        System.out.println(service.showEnergyUse("house1"));
        System.out.println(service.showEnergyUse("kitchen1"));
        System.out.println(service.getValue("oven1", "energyUse"));
        // Also names must be unique, which works here, but does not fit requirements.
        // Also have to fix values so they delete old one when new one is set.

        System.out.println(service.getName("1"));
        System.out.println(service.getRoomUUID("house3", "kitchen1"));
        System.out.println(service.getApplianceUUID("house2", "kitchen2", "oven1"));
        */

        String[] homeRoom0 = {"house3"};
        System.out.println(service.showConfiguration());
        String[] homeRoom = {"house3", "kitchen1"};
        //System.out.println(service.showConfiguration(homeRoom));
        String[] homeRoom2 = {"house1", "kitchen1", "oven1"};
        //System.out.println(service.showConfiguration(homeRoom2));
        //String[] homeRoom3 = {"nohouse"};
        //System.out.println(service.showConfiguration(homeRoom3));
    }
}
