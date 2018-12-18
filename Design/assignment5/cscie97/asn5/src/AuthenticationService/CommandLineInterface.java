package AuthenticationService;

import AuthenticationService.HouseMateEntitler;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by n0305853 on 11/10/18.
 */
public class CommandLineInterface {
    private static HouseMateEntitler service = new HouseMateEntitler();
    private static AccessToken token = null;


    /**
     * Main arg just loops continually, checks what type of cli call is made, parses and calls their methods.
     * @param args
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean cont = true;

        while (cont == true) {
            // Need to check and modify based on existence of quotes.

            // Adding big statement for logged in.
            if (token == null) {
                System.out.print("Username:Password please: ");
                String input = scan.nextLine();
                input = input;
                String[] inputSplit = input.split(":");
                token = service.login(inputSplit[0], inputSplit[1]);
            } else {
                // Once logged in can start messing with things.
                System.out.print('>');
                String input = scan.nextLine();
                input = input.toLowerCase();

                String[] inputSplit = input.split(" ");
                if (input.contains("\"")) {
                    // using this to track quotes.
                    boolean flip = false;
                    String accum = "";
                    int index = 0;
                    // find the first occurrence of quote in array.
                    for (int i = 0; i < inputSplit.length; i++) {
                        if (inputSplit[i].contains("\"")) {
                            if (flip) {
                                accum += " " + inputSplit[i];
                                flip = false;
                                inputSplit[index] = accum;
                                accum="";
                                index++;
                            } else {
                                flip = true;
                                if(index == 0) {
                                    index = i;
                                }
                            }
                        }
                        if (flip) {
                            accum += " " + inputSplit[i];
                        }
                    }
                    inputSplit = Arrays.copyOfRange(inputSplit, 0, index);
                    //System.out.println(Arrays.toString(inputSplit));
                }

                switch (inputSplit[0]) {
                    case "exit":
                        cont = false;
                        break;
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
        }
    }


    /**
     * Parses input and acts upon it, adds a resource role to the user.
     * @param args
     */
    public static void addResourceRoleToUser(String[] args){
        try {
            if (args.length == 3) {
                //System.out.println("DEBUG :: " + args[1] + args[2]);
                service.addResourceRole(args[1], args[2]);
                System.out.println("Created " + Arrays.toString(args));
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addResourceRoleToUser " + args.toString());
        }
    }


    /**
     * Parses input and acts upon it,  adds a new resource role.
     * @param args
     */
    public static void addResourceRole(String[] args){
        try {
            if (args.length == 4) {
                //System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.createResourceRole(args[1], args[2], args[3]);
                System.out.println("Created " + Arrays.toString(args));
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addResourceRole " + args.toString());
        }
    }


    /**
     * Parses input and acts upon it,  adds a role to the user
     * @param args
     */
    public static void addRoleToUser(String[] args){
        try {
            if (args.length == 3) {
                //System.out.println("DEBUG :: " + args[1] + args[2]);
                service.addRole(args[1], args[2]);
                System.out.println("Created " + Arrays.toString(args));
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addRoleToUser " + args.toString());
        }
    }


    /**
     * Parses input and acts upon it, adds a new credential
     * @param args
     */
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


    /**
     * Parses input and acts upon it, adds a new user.
     * @param args
     */
    public static void addUser(String[] args){
        try {
            if (args.length == 3) {
                //System.out.println("DEBUG :: " + args[1] + args[2]);
                service.createUser(args[1], args[2]);
                System.out.println("Created " + Arrays.toString(args));
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addUser " + args.toString());
        }
    }


    /**
     * Parses input and acts upon it, adds a new entitlement
     * @param args
     */
    public static void addEntitlement(String[] args){
        try {
            if (args.length == 3) {
                //System.out.println("DEBUG :: " + args[1] + args[2]);
                service.addEntitlement(args[1], args[2]);
                System.out.println("Created " + Arrays.toString(args));
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed addEntitlement " + args.toString());
        }
    }


    /**
     * Parses input and acts upon it,  adds a new role
     * @param args
     */
    public static void defineRole(String[] args){
        try {
            if (args.length == 4) {
                //System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.createRole(args[1], args[2], args[3]);
                System.out.println("Created " + Arrays.toString(args));
            } else {
                throw new malformedSyntaxException();
            }
        }catch(malformedSyntaxException ex){
            System.out.println(ex + " Malformed defineRole " + args.toString());
        }
    }


    /**
     * Parses input and acts upon it, adds a new permission
     * @param args
     */
    public static void definePermission(String[] args){
        try {
            if (args.length == 4) {
                //System.out.println("DEBUG :: " + args[1] + args[2] + args[3]);
                service.createPermission(args[1], args[2], args[3]);
                System.out.println("Created " + Arrays.toString(args));
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
