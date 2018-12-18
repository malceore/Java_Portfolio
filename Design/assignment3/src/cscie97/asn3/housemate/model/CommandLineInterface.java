package cscie97.asn3.housemate.model;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Created by n0305853 on 9/26/18.
 */
public class CommandLineInterface {
    private static HouseMateModel service = new HouseMateModel();

    /**
     * Main arg just loops continually, checks what type of cli call is made, parses and calls their methods.
     * @param args
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean cont = true;
        while (cont == true) {
            System.out.print('>');
            String input = scan.nextLine();
            input = input.toLowerCase();

            // Need to check and modify based on existence of quotes.
            String[] inputSplit = input.split(" ");
            if (input.contains("\"")) {
                // find the first occurrence of quote in array.
                for(int i=0; i < inputSplit.length-1; i++){
                    if(inputSplit[i].contains("\"")){
                        String[] holder = Arrays.copyOfRange(inputSplit, i, inputSplit.length);
                        inputSplit = Arrays.copyOfRange(inputSplit, 0, i+1);
                        inputSplit[i] = Arrays.toString(holder);
                        //System.out.println(Arrays.toString(inputSplit));
                        break;
                    }
                }
            }

            switch (inputSplit[0]) {
                case "exit":
                    cont = false;
                    break;
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
    }

    /**
     * Adds an occupant to a house.
     * @param args
     */
    public static void add(String[] args){
        try {
            if (args.length == 5) {
                service.setAssociation(args[2], args[4]);
            } else {
                throw new malformedSyntax();
            }
        }catch(malformedSyntax ex){
            System.out.println(ex + " Malformed add string syntax.");
        }
    }


    /**
     * Attempts to define a new entity, house, rooms, appliances, occupants from args passed to it.
     *      Throws malformed exception with wrong arg count.
     * @param args
     */
    private static void define(String[] args){
        //System.out.println(args.length);
        try{
            switch(args[1]) {
                case "house":
                    if (args.length == 5) {
                        service.addHouse(args[2], args[4]);
                    }else{
                        throw new malformedSyntax();
                    }
                    break;
                case "room":
                    if (args.length != 11){
                        throw new malformedSyntax();
                    }
                    service.addRoom(args[2], args[4], args[6], args[8], args[10]);
                    break;
                case "sensor":
                case "appliance":
                    String[] houseRoom = args[6].split(":");
                    if (args.length == 7) {
                        service.addAppliance(args[2], args[4], houseRoom[0], houseRoom[1], "0");
                    }else if(args.length == 8){
                        service.addAppliance(args[2], args[4], houseRoom[0], houseRoom[1], args[8]);
                    }else{
                        throw new malformedSyntax();
                    }
                    break;
                case "occupant":
                    if (args.length != 5){
                        throw new malformedSyntax();
                    }
                    service.addOccupant(args[2], args[4]);
                    break;
                default:
                    throw new malformedSyntax();
            }
        } catch (malformedSyntax ex){
            System.out.println(ex + " Malformed Syntax exception!");
        }
    }


    /**
     * Takes in string or args, attempts to form a new value related to a appliance or sensor. If syntax is wrong throws an error.
     * @param args
     */
    private static void set(String[] args){
        try{
            if (args.length != 5) {
                throw new malformedSyntax();
            }
            String[] houseRoomAppliance = args[2].split(":");
            service.setValue(houseRoomAppliance, args[4], args[6]);
        }catch(malformedSyntax ex){
            System.out.println(ex + " Malformed Syntax exception!");
        }
    }


    /**
     * Parses out the args string given, attempts to print out the info requested.
     * @param args
     */
    private static void show(String[] args){
        try{
            //show sensor or appliance
            if (args[1].equals("appliance") || args[1].equals("sensors")){
                String[] houseRoomAppliance = args[2].split(":");
                if(args.length == 5){
                    //System.out.println(service.getValue(houseRoomAppliance[houseRoomAppliance.length-1], args[4]));
                    System.out.println(service.getValue(houseRoomAppliance, args[4]));
                }else if(args.length == 3) {
                    System.out.println(service.showConfiguration(houseRoomAppliance));
                    //System.out.println(service.showConfiguration(houseRoomAppliance[houseRoomAppliance.length-1]));
                }else{
                    throw new malformedSyntax();
                }

             //show configurations
            }else if (args[1].equals("configuration")){
                // for all
                if(args.length == 2){
                    System.out.println(service.showConfiguration());
                // house, room or appliance.
                }else if(args.length == 3) {
                    String[] houseRoomAppliance = args[2].split(":");
                    System.out.println(service.showConfiguration(houseRoomAppliance));
                    //System.out.println(service.showConfiguration(houseRoomAppliance[houseRoomAppliance.length-1]));
                }else{
                    throw new malformedSyntax();
                }

            //show energyUse
            }else if (args[1].equals("energyUse")) {
                // For ALL
                if(args.length == 2){
                    System.out.println(service.showEnergyUse());
                }else if(args.length == 3) {
                    String[] houseRoomAppliance = args[2].split(":");
                    System.out.println(service.showEnergyUse(houseRoomAppliance));
                    //System.out.println(service.showEnergyUse(houseRoomAppliance[houseRoomAppliance.length-1]));
                }else{
                    throw new malformedSyntax();
                }
            }else{
                throw new malformedSyntax();
            }
        }catch(malformedSyntax ex){
            System.out.println(ex + " Malformed Syntax exception!");
        }
    }


    // Simple throwable exception for generic incorrect syntax.
    private static class malformedSyntax extends Throwable {}
}
