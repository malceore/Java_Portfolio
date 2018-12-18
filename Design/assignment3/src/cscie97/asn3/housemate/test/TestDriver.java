
package cscie97.asn3.housemate.test;
import cscie97.asn3.housemate.controller.HouseMateController;
import cscie97.asn3.housemate.model.HouseMateModel;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

/**
 * Created by n0305853 on 9/26/18.
 */
public class TestDriver {
    private static HouseMateModel model = HouseMateModel.getInstance();  //new HouseMateModel();
    //private HouseMateController service = new HouseMateController(model);

    /**
     * Main arg just loops continually, checks what type of cli call is made, parses and calls their methods.
     * @param args
     */
    public static void main(String[] args) {
        HouseMateController service = new HouseMateController(model);
        // Take in file, loop over it's lines and process.
        try{
            File file = new File(args[0]);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String input = scan.nextLine();
                //cast to lower case for formality.
                input = input.toLowerCase();

                // Need to check and modify based on existence of quotes.
                // Got split from this article: https://stackoverflow.com/questions/13081527/splitting-string-on-multiple-spaces-in-java
                String[] inputSplit = input.split("\\s+");
                if (input.contains("\"")|| input.contains("“") || input.contains("”")) {
                    // find the first occurrence of quote in array.
                    for(int i=0; i < inputSplit.length-1; i++){
                        if(inputSplit[i].contains("\"") || inputSplit[i].contains("“") || inputSplit[i].contains("”")){

                            // We need to convert the quote into one string and store it back in the array, then trim.
                            String[] holder = Arrays.copyOfRange(inputSplit, i, inputSplit.length);
                            String accum = "";
                            for (String entry: holder){
                                accum += entry + "_";
                            }

                            //inputSplit[i] = Arrays.toString(holder);
                            inputSplit[i] = accum;

                            //Trim
                            inputSplit = Arrays.copyOfRange(inputSplit, 0, i+1);
                            //System.out.println("DEBUG quotes>>> " + Arrays.toString(inputSplit));
                            break;
                        }
                    }
                }

                // Have to break for comments
                if (input.contains("#") || input.contains("*") || input.contains("--") || inputSplit.length <= 1) {
                    //System.out.println("comment!");
                    continue;
                }

                System.out.println("\n>> " + Arrays.toString(inputSplit));

                switch (inputSplit[0]) {
                    case "define":
                        define(inputSplit);
                        break;
                    case "set":
                        set(inputSplit);
                        break;
                    case "add":
                        add(inputSplit);
                        break;
                    case "show":
                        show(inputSplit);
                        break;
                }
            }
            scan.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex + " File " + args[0] + " not found!");
        }
    }

    /**
     * Adds an occupant to a house.
     * @param args
     */
    public static void add(String[] args){
        try {
            if (args.length == 5) {
                model.setAssociation(args[2], args[4]);
            } else {
                throw new malformedSyntax();
            }
        }catch(malformedSyntax ex){
            System.out.println(ex + " Malformed add string syntax. Did not understand: " + Arrays.toString(args));
        }
    }


    /**
     * Attempts to define a new entity, house, rooms, appliances, occupants from args passed to it.
     *      Throws malformed exception with wrong arg count.
     * @param args
     */
    private static void define(String[] args){
        System.out.println("DEFINING >>" + Arrays.toString(args));
        try{
            switch(args[1]) {
                case "house":
                    if (args.length == 5) {
                        model.addHouse(args[2], args[4]);
                    }else{
                        throw new malformedSyntax();
                    }
                    break;
                case "room":
                    if (args.length != 11){
                        throw new malformedSyntax();
                    }
                    model.addRoom(args[2], args[4], args[6], args[8], args[10]);
                    break;
                case "sensor":
                case "appliance":
                    String[] houseRoom = args[6].split(":");
                    if (args.length == 7) {
                        // It's a sensors
                        model.addAppliance(args[2], args[4], houseRoom[0], houseRoom[1], "0");
                    }else if(args.length == 9){
                        model.addAppliance(args[2], args[4], houseRoom[0], houseRoom[1], args[8]);
                    }else{
                        throw new malformedSyntax();
                    }
                    break;
                case "occupant":
                    if (args.length != 5){
                        throw new malformedSyntax();
                    }
                    model.addOccupant(args[2], args[4]);
                    break;
                default:
                    throw new malformedSyntax();
            }
        } catch (malformedSyntax ex){
            System.out.println(ex + " Malformed Syntax exception! Did not understand: " + Arrays.toString(args));
        }
    }


    /**
     * Takes in string or args, attempts to form a new value related to a appliance or sensor. If syntax is wrong throws an error.
     * @param args
     */
    private static void set(String[] args){
        //System.out.println("DEBUG Num of values in the set>> " + args.length);
        try{
            if(args.length == 5) {
                String[] houseRoomAppliance = args[2].split(":");
                model.setValue(houseRoomAppliance, args[3], args[args.length - 1]);

            } else if(args.length == 7 || args.length == 6) {
                String[] houseRoomAppliance = args[2].split(":");
                //System.out.println(Arrays.toString(houseRoomAppliance)+ args[4]+ args[args.length-1]);
                model.setValue(houseRoomAppliance, args[4], args[args.length-1]);
            }else{
                throw new malformedSyntax();
            }
        }catch(malformedSyntax ex){
            System.out.println(ex + " Malformed Syntax exception! Did not understand: " + Arrays.toString(args));
        }
    }


    /**
     * Parses out the args string given, attempts to print out the info requested.
     * @param args
     */
    private static void show(String[] args){
        try{
            //show sensor or appliance
            if (args[1].equals("appliance") || args[1].equals("sensor")){

                String[] houseRoomAppliance = args[2].split(":");
                if(args.length == 5){
                    //System.out.println(service.getValue(houseRoomAppliance[houseRoomAppliance.length-1], args[4]));
                    System.out.println(model.getValue(houseRoomAppliance, args[4]));
                }else if(args.length == 3) {
                    System.out.println(model.showConfiguration(houseRoomAppliance));
                    //System.out.println(service.showConfiguration(houseRoomAppliance[houseRoomAppliance.length-1]));
                }else{
                    throw new malformedSyntax();
                }

                //show configurations
            }else if (args[1].equals("configuration")){
                // for all
                if(args.length == 2){
                    System.out.println(model.showConfiguration());
                    // house, room or appliance.
                }else if(args.length == 3) {
                    String[] houseRoomAppliance = args[2].split(":");
                    System.out.println(model.showConfiguration(houseRoomAppliance));
                    //System.out.println(service.showConfiguration(houseRoomAppliance[houseRoomAppliance.length-1]));
                }else{
                    throw new malformedSyntax();
                }

                //show energyUse
            }else if (args[1].equals("energy-use")) {
                //System.out.println("DEBUG EnergyUSE >> " + Arrays.toString(args));
                // For ALL
                if(args.length == 2){
                    System.out.println(model.showEnergyUse());
                }else if(args.length == 3) {
                    String[] houseRoomAppliance = args[2].split(":");
                    System.out.println(model.showEnergyUse(houseRoomAppliance));
                    //System.out.println(service.showEnergyUse(houseRoomAppliance[houseRoomAppliance.length-1]));
                }else{
                    throw new malformedSyntax();
                }
            }else{
                throw new malformedSyntax();
            }
        }catch(malformedSyntax ex){
            System.out.println(ex + " Malformed Syntax exception! Did not understand: " + Arrays.toString(args));
        }
    }


    // Simple throwable exception for generic incorrect syntax.
    private static class malformedSyntax extends Throwable {}
}