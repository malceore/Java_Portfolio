package cscie97.asn4.housemate.test;

import cscie97.asn4.housemate.entitlement.HouseMateEntitler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by n0305853 on 11/10/18.
 */
public class EntitlerTestDriver {
    private static HouseMateEntitler service = new HouseMateEntitler();

    /**
     * Main arg just loops continually, checks what type of cli call is made, parses and calls their methods.
     * @param args
     */
    public static void main(String[] args) {
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
                    case "define_permission":
                        definePermission(inputSplit);
                        //System.out.println("define perm");
                        break;
                    case "define_role":
                        defineRole(inputSplit);
                        //System.out.println("define_role");
                        break;
                    case "add_entitlement":
                        addEntitlement(inputSplit);
                        //System.out.println("add entitlement");
                        break;
                    case "create_user":
                        addUser(inputSplit);
                        //System.out.println("create user");
                        break;
                    case "add_user_credential":
                        addCredential(inputSplit);
                        //System.out.println("add user creds");
                        break;
                    case "add_role_to_user":
                        addRoleToUser(inputSplit);
                        //System.out.println("add role to user");
                        break;
                    case "create_resource_role":
                        addResourceRole(inputSplit);
                        System.out.println("create resource role");
                        break;
                    case "add_resource_role_to_user":
                        addResourceRoleToUser(inputSplit);
                        //System.out.println("add resource role to user.");
                        break;
                }
            }
            scan.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex + " File " + args[0] + " not found!");
        }
    }

    public static void addResourceRoleToUser(String[] args){
        try {
            if (args.length == 3) {
                System.out.println("DEBUG :: " + args[1] + args[2]);
                service.addResourceRole(args[1], args[2]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addResourceRoleToUser " + args.toString());
        }
    }

    public static void addResourceRole(String[] args){
        try {
            if (args.length == 4) {
                System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.createResourceRole(args[1], args[2], args[3]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addResourceRole " + args.toString());
        }
    }

    public static void addRoleToUser(String[] args){
        try {
            if (args.length == 3) {
                System.out.println("DEBUG :: " + args[1] + args[2]);
                service.addRole(args[1], args[2]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addRoleToUser " + args.toString());
        }
    }

    public static void addCredential(String[] args){
        try {
            if (args.length == 4) {
                System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.addCredential(args[1], args[2], args[3]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addCredential " + args.toString());
        }
    }

    public static void addUser(String[] args){
        try {
            if (args.length == 3) {
                System.out.println("DEBUG :: " + args[1] + args[2]);
                service.createUser(args[1], args[2]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addUser " + args.toString());
        }
    }

    public static void addEntitlement(String[] args){
        try {
            if (args.length == 3) {
                System.out.println("DEBUG :: " + args[1] + args[2]);
                service.addEntitlement(args[1], args[2]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addEntitlement " + args.toString());
        }
    }

    public static void defineRole(String[] args){
        try {
            if (args.length == 4) {
                System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.createRole(args[1], args[2], args[3]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed defineRole " + args.toString());
        }
    }

    public static void definePermission(String[] args){
        try {
            if (args.length == 4) {
                System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.createPermission(args[1], args[2], args[3]);
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex) {
            System.out.println(ex + " Malformed definePermission " + args.toString());
        }
    }

    // Simple throwable exception for generic incorrect syntax.
    private static class malformedSyntaxException extends Throwable {}
}
