package test;
import AuthenticationService.AccessToken;
import AuthenticationService.HouseMateEntitler;
import InventorySystem.InventoryModel;


/**
 * Created by Brandon Wood on 11/28/18.
 */
public class FunctionalTestDriver {
    public static void main(String [] args) {
        //System.out.println("Running functional test driver program...");
        InventoryModel service = InventoryModel.getInstance();
        HouseMateEntitler entitler = HouseMateEntitler.getInstance();
        //HouseMateController observer = new HouseMateController(service);

        System.out.println("Requesting auth token...");
        Object token = entitler.login("Admin", "trustNo1");

        System.out.println();
        System.out.println("\nCreating and then printing out example asteroids...");
        service.addAsteroid("1_Ceres", "G_TYPE", "1000", "1000", "1000", "9.43(Some_science_numbers)", "0.028", "2.9858", "2.5468", token);
        service.addNote("1_Ceres", "11-27-2013", "Nigel", "potential_source_of_water_for_rocket_fuel", token);
        service.addMineralDiscovery("1_Ceres", "titanium", "100000000", "surface", token);
        service.addWaterDiscovery("1_Ceres", "true", "1000000000", "ice", token);
        service.addLifeDiscovery("1_Ceres", "multi_cell", "true", "true", token);
        service.showConfiguration();

        System.out.println();
        System.out.println("\nCreating and then printing spaceship and mission examples...");
        service.createMission("Ceres_Explorer_Mission", "Ceres_Explorer_Mission", "Explore_ceres_and_locate_the_best_location_to_retrieve_water", "Ceres_Explorer_Spacecraft", "12/1/2013", "12/1/2015", "1_Ceres", "waiting_for_launch", token);
        service.addSpaceShip("Ceres_Explorer_Spacecraft", "12/1/2015", "Ceres_Explorer_Mission", "explorer", "100", "OK", "OK", "waiting_for_launch", "1", "1_Ceres", token);
        service.showConfiguration();

        System.out.println();
        System.out.println("\nTesting mission API settings...");
        service.addMissionControl("Asteroid_Control","100000", "10000", "10000", "OK", "OK", token);
        service.showConfiguration();

        System.out.println();
        System.out.println("Values after changes");
        service.incrementFuel("Asteroid_Control", "-10000", token);
        service.incrementResource("Asteroid_Control", "-1", token);
        service.incrementBudget("Asteroid_Control", "-100000", token);
        service.setCommunicationLinkStatus("Asteroid_Control", "OK", token);
        service.setControlSystemStatus("Asteroid_Control", "NOT_OK", token);
        service.showConfiguration();

        System.out.println();
        System.out.println("Testing search capabilities for asteroids and spaceships, adding more dummy values..");

        service.addAsteroid("4_Vesta", "G_TYPE", "1000", "1000", "1000", "9.43(Some_science_numbers)", "0.028", "2.9858", "2.5468", token);
        service.addAsteroid("1_Xerces", "A_TYPE", "1000", "1000", "1000", "9.43(Some_science_numbers)", "0.028", "2.9858", "2.5468", token);
        service.addAsteroid("2_Ceres", "F_TYPE", "1000", "1000", "1000", "9.43(Some_science_numbers)", "0.028", "2.9858", "2.5468", token);
        service.list("asteroid", token);
        service.list("spaceship", token);

        service.addNote("2_Ceres", "11-27-2028", "Thorner", "just_saying_hi", token);
        service.query("2_Ceres", token);
        service.query("Ceres_Explorer_Spacecraft", token);
        service.query("saying", token);
        service.query("SOS", token);

        /*
        System.out.println();
        System.out.println("Testing exceptions with invalided token, should throw exception..");
        entitler.logout((AccessToken) token);
        service.incrementResource("Asteroid_Control", "-1", token);
        */

    }
}
