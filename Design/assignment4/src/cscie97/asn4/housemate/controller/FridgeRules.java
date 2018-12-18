package cscie97.asn4.housemate.controller;

import cscie97.asn4.housemate.model.HouseMateModel;

/**
 * Created by n0305853 on 10/19/18.
 */
public class FridgeRules implements Rule {

    public String name = "Fridge Rule";
    public String getName(){
        return name;
    }

    @Override
    public boolean execute(String change, HouseMateModel model){
        // If change involves a fridge type appliance.
        //System.out.println("Debug :: FridgeRules executing " + change);
        // Is this fridge related?
        //if (change.contains("fridge")){
            //System.out.println("Yup it's a fridge problem!");
            // Now to parse change..
            String[] input = change.split(":");
            if(input[3].contains("beer")){
                return beerAlert(input, model);
            }
        //}
        return false;
    }


    /**
     * Takes in the input of a command and a model, checks to see if your beer count is getting low and prompts you to orders more!
     * @param input
     * @param model
     */
    public boolean beerAlert(String[] input, HouseMateModel model){
        if(Integer.parseInt(input[4]) < 4){
            System.out.println("Sending email alert to ask if user wants more beer! >> You have mail!");
            return true;
        }
        return false;
    }
}
