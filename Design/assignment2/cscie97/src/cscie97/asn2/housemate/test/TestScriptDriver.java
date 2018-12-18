package cscie97.asn2.housemate.test;
import cscie97.asn2.housemate.model.HouseMateService;

/**
 * Created by n0305853 on 9/26/18.
 */
public class TestScriptDriver {
    public static void main(String[] args) {
        HouseMateService service = new HouseMateService();
        service.addHouse("house1", "1 Story Street Cambridge, MA");
        service.addRoom("kitchen1", "1", "kitchen", "house1", "2");
        service.addOccupant("martin_fowler", "adult");
        service.setAssociation("martin_fowler", "house1");
        service.addAppliance("smoke_detector", "smoke_detector", "house1","kitchen1", "0");
        service.addAppliance("oven1", "oven", "house1", "kitchen1", "500");
        String[] oven1 = {"house1", "kitchen1", "oven1"};
        service.setValue(oven1, "power", "on");
        service.setValue(oven1, "temperature", "350");
        System.out.println(service.getValue(oven1, "temperature"));
        System.out.println(service.showConfiguration(oven1));

        oven1[2]="smoke_detector";
        System.out.println(service.showConfiguration(oven1));
        System.out.println(service.showConfiguration());

        String[] oven2 = {"house1"};
        String[] oven3 = {"house1", "kitchen1"};
        System.out.println(service.showConfiguration(oven2));
        System.out.println(service.showConfiguration(oven3));

        System.out.println(service.showEnergyUse());
        System.out.println(service.showEnergyUse(oven2));
        System.out.println(service.showEnergyUse(oven3));

        System.out.println(service.getValue(oven1, "energyUse"));
        // Also names must be unique, which works here, but does not fit requirements.
        // Also have to fix values so they delete old one when new one is set.
    }
}
