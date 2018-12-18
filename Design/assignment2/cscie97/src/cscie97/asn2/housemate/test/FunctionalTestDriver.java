package cscie97.asn2.housemate.test;
import cscie97.asn2.housemate.model.HouseMateService;

/**
 * Created by n0305853 on 9/26/18.
 */
public class FunctionalTestDriver {
    public static void main(String[] args) {
        // Test initiation or service and adding of entities.
        HouseMateService service = new HouseMateService();

        service.addHouse("r_house", "34_Middle_Street");
        service.addRoom("r_kitchen", "1", "kitchen","r_house", "2");
        service.addAppliance("Sherman_Oven", "oven", "r_house", "r_kitchen", "150");
        service.addOccupant("John_Joestar", "Adult");

        service.addRoom("r_living_room", "1", "living_room", "r_house", "2");
        service.addAppliance("Samsung", "TV", "r_house", "r_living_room", "70");
        service.addAppliance("Nest", "Sensor", "r_house", "r_living_room", "0");

        service.addHouse("t_house", "35_Middle_Street");
        service.addRoom("t_kitchen", "1", "kitchen","t_house", "1");
        service.addAppliance("Sherman_Oven", "oven", "t_house", "t_kitchen", "100");

        // Test shows, including unique names per house level.
        String[] houseRoomAppliance = {"r_house", "r_kitchen", "sherman_oven"};
        String[] houseRoom = {"r_house", "r_kitchen"};
        String[] house = {"r_house"};

        System.out.println("\n\nShowConfiguration r_house: " + service.showConfiguration(house));
        System.out.println("ShowEnergyUse r_house: " + service.showEnergyUse(house));
        System.out.println("ShowEnergyUse r_kitchen: " + service.showEnergyUse(houseRoom));
        System.out.println("ShowEnergyUse r_kitchen: " + service.showEnergyUse(houseRoomAppliance));
        System.out.println("Total Energy Usage: " + service.showEnergyUse());

        // Test Delete
        String[] house2 = {"t_house"};
        System.out.println("\n\nTotal EnergyUse before purge t_house: " + service.showEnergyUse());

        service.purgeEntity(house2);
        String[] house3 = {"t_house", "t_kitchen"};
        System.out.println("Total EnergyUse after purge t_house: " + service.showEnergyUse());

        service.addOccupant("Rupert_Goldberg", "Elderly");
        // Test setValue\association each takes three strings
        service.setAssociation("Rupert_Goldberg", "r_house");
        houseRoomAppliance[1] = "r_living_room";
        houseRoomAppliance[2] = "nest";
        houseRoom[1] = "r_living_room";
        service.setValue(houseRoomAppliance, "temperature", "73");
        System.out.println("\n\nShowConfiguation r_living_room after setting Nest Thermostat: " + service.showConfiguration(houseRoom));

        System.out.println("\n\nShow exception checking bad occupant add and bad room add to non existent houses.");
        // Test exception handling for creating rooms for non existent houses
        service.addRoom("t_lobby", "3", "lobby","x_house", "700");

        // Exception for adding user to house that doesn't exist.
        service.addOccupant("Dio_Brando", "Vampire");
        service.setAssociation("Dio_Brandon", "x_house");
        houseRoomAppliance[2] = "VCR";
        service.setValue(houseRoomAppliance, "temperature", "73");
    }
}
