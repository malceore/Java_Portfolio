package cscie97.asn4.housemate.entitlement;

import java.util.HashMap;

/**
 * <h1>HouseMate Model Entitler</h1>
 * Created by: Brandon T. Wood
 * Date: Nov 11th, 2018
 */
public class HouseMateEntitler {
    private User AdminUser;
    private ResourceRole defaultResourceRole;
    private SecurityEntityConcreteFactory factory;
    private static HouseMateEntitler instance = new HouseMateEntitler();
    // Where the String is the UUID
    private HashMap<String, SecurityEntityComposite> SecurityEntities;
    // Where String is the house name.
    private HashMap<String, SecurityEntityComposite> ResourceRoles;
    private HashMap<AccessToken, String> AccessTokens;
    private HashMap<VoicePrint, User> VoicePrints;


    public HouseMateEntitler() {
        // Setup lists and factory for building Security Entities.
        factory = new SecurityEntityConcreteFactory();
        SecurityEntities = new HashMap<String, SecurityEntityComposite>();
        ResourceRoles = new HashMap<String, SecurityEntityComposite>();
        AccessTokens = new HashMap<AccessToken, String>();

        // Setup the default admin account.
        User AdminUser = new User("Admin", "Admin");
        CredentialEntityComposite creds = new Account("Admin", "Admin", "trustNo1");
        AdminUser.addPassword(creds);
        SecurityEntities.put(AdminUser.UUID, AdminUser);

        // Setup default permissions
        createPermission("adminPerm", "adminPerm", "Full control of *");
        createPermission("controlOven", "controlOven", "Full control of Oven");
        createPermission("controlThermostat", "controlThermostat", "Full control of Thermostat");
        createPermission("controlDoor", "controlDoor", "Full control of Door");
        createPermission("controlWindow", "controlWindow", "Full control of Window");

        //add defaults to admin and resource roles.
        createRole("admin_role", "admin_role", "give full privilege");
        addEntitlement("admin_role", "adminPerm");
        addRole("Admin", "admin_role");

        // Setup default resource roles.
        createRole("adult_resident", "adult_resident", "Has all permissions of resident");
        createRole("child_resident", "child_resident", "Has all permissions of resident");

        addEntitlement("adult_resident", "controlOven");
        addEntitlement("adult_resident", "controlThermostat");
        addEntitlement("adult_resident", "controlDoor");
        addEntitlement("adult_resident", "controlWindow");
        addEntitlement("child_resident", "controlDoor");
        addEntitlement("child_resident", "controlWindow");

    }


    /**
     * Returns an instance of the entitler, implementing a singleton.
     * @return
     */
    public static HouseMateEntitler getInstance() {
        return instance;
    }


    /**
     * Taking a String that uniquely identifies the user and a name, creates a new user tracked by the entitler and stores it in SecurityEntites map.
     * @param UUID
     * @param name
     */
    public void createUser(String UUID, String name) {
        User user = factory.createUser(UUID, name);
        SecurityEntities.put(user.UUID, user);
    }


    /**
     * Given a Uniquely identifying string, a name and a description creates a new permission and adds it to the securityEntites map.
     * @param UUID
     * @param name
     * @param desc
     */
    public void createPermission(String UUID, String name, String desc) {
        Permission perm = factory.createPermission(UUID, name, desc);
        SecurityEntities.put(perm.UUID, perm);
    }

    /**
     * Given a unique identifying string, a name and a description, creates a role tracked by the entitler and stores it in SecurityEntites map
     * @param UUID
     * @param name
     * @param desc
     */
    public void createRole(String UUID, String name, String desc) {
        Role role = factory.createRole(UUID, name, desc);
        SecurityEntities.put(role.UUID, role);
    }


    /**
     * Given a unique name, an existing roleID and a resource(house), creates a new resource role and tracks it in the SecurityEntites map.
     * @param name
     * @param roleID
     * @param resource
     */
    public void createResourceRole(String name, String roleID, String resource) {
        SecurityEntityComposite secRole = SecurityEntities.get(name);
        Role role = (Role) secRole;
        ResourceRole resourceRole = factory.createResourceRole(name, role);
        ResourceRoles.put(resource, resourceRole);
    }


    /**
     * Given a unique identifying string, a name, a description and a resource(house) creates a new role and associated resource role. Then they are tracked in the securityentites map.
     * @param UUID
     * @param name
     * @param desc
     * @param resource
     */
    public void createResourceRole(String UUID, String name, String desc, String resource) {
        Role role = factory.createRole(UUID, name, desc);
        SecurityEntities.put(role.UUID, role);
        ResourceRoles.put(resource, role);
    }


    /**
     * Given an existing roleID and existing entitlementID, will add the entitlement to the role.
     * @param roleID
     * @param entitlementID
     */
    public void addEntitlement(String roleID, String entitlementID) {
        SecurityEntityComposite secRole = SecurityEntities.get(roleID);
        SecurityEntityComposite secPerm = SecurityEntities.get(entitlementID);
        Role role = (Role) secRole;
        Permission perm = (Permission) secPerm;
        role.addPermission(perm);
    }


    /**
     * Give a userId and a resource(houseID) it will add the associated house resource role to the user.
     * @param userID
     * @param houseID
     */
    public void addResourceRole(String userID, String houseID) {
        SecurityEntityComposite secRole = ResourceRoles.get(houseID);
        if (secRole != null) {
            SecurityEntityComposite secUser = SecurityEntities.get(userID);
            ResourceRole role = (ResourceRole) secRole;
            User user = (User) secUser;
            user.addRole(role);
        }
    }


    /**
     * Give an existing userID and roleID will add the role to the user.
     * @param userID
     * @param roleID
     */
    public void addRole(String userID, String roleID) {
        //System.out.println(userID + roleID + "debug");
        SecurityEntityComposite secRole = SecurityEntities.get(roleID);
        SecurityEntityComposite secUser = SecurityEntities.get(userID);
        Role role = (Role) secRole;
        User user = (User) secUser;
        user.addRole(role);
    }


    /**
     * Given an existing userID, a type of credential and a value will create that value and add it to the user's credentials.
     * @param userID
     * @param type
     * @param value
     */
    public void addCredential(String userID, String type, String value) {
        try {
            SecurityEntityComposite secUser = SecurityEntities.get(userID);
            User user = (User) secUser;

            // If it's a voice print use add voice print,
            if (type.equals("voiceprint")) {
                CredentialEntityComposite creds = new VoicePrint("000", value);
                user.addVoicePrint(creds);

                //  if it's a password add password
            } else if (type.equals("password")) {
                CredentialEntityComposite creds = new Account("000", userID, value);
                user.addPassword(creds);

                //  if it's a token add the token.
            } else if (type.equals("token")) {
                CredentialEntityComposite creds = new AccessToken("000");
                user.addToken(creds);
                //AccessTokens.put((AccessToken) creds, user.UUID);
            } else {
                throw new CredentialEntityDoesNotExistException();
            }
        } catch (CredentialEntityDoesNotExistException ex) {
            System.out.println("Credential type does not exist! ::" + ex);
        }
    }


    /**
     * Given an existing userID and the description of a permission will check if that user has that permission. returning true if it does
     * @param userID
     * @param desc
     * @return
     */
    public boolean checkPermissions(String userID, String desc) {
        try {
            SecurityEntityComposite secUser = SecurityEntities.get(userID);
            User user = (User) secUser;
            //System.out.println("made it: " + user.name);
            if (user != null) {
                boolean check = user.checkPermission(desc);
                //System.out.println("jrfieiuh " + check);
                if (!check) {
                    throw new AccessDeniedException();
                }
            }
            return true;
        } catch (AccessDeniedException ex) {
            System.out.println("Access denied, user " + userID + " cannot perform this task " + desc + " :: " + ex);
        }
        return false;
    }


    /**
     * Given a token, returns the related userID.
     * @param token
     * @return
     */
    public String getUserId(AccessToken token){
        return AccessTokens.get(token);
    }


    /**
     * Given an existing userID and a secure password string, will check if it matches any of the users credentials. returning true if it does
     * @param userID
     * @param password
     * @return
     */
    public boolean checkCredentials(String userID, String password) {
        try {
            SecurityEntityComposite secUser = SecurityEntities.get(userID);
            User user = (User) secUser;
            boolean check = user.checkCredentials(password);
            if (!check) {
                throw new InvalidCredentialsException();
            }
            return true;
        } catch (InvalidCredentialsException ex) {
            System.out.println("Invalid credentials for " + userID + " :: " + ex);
        }

        return false;
    }


    /**
     * Given a token, will check the list of active AccessTokens and return true if it exists.
     * @param token
     * @return
     */
    public boolean checkToken(AccessToken token) {
        //System.out.println("DEBUG SHOW ME ACCES TOKENS size:" + AccessTokens.size() + "   ");
        try {
            if (AccessTokens.containsKey(token)) {
                return true;
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (InvalidCredentialsException ex) {
            System.out.println("Presented token is invalid or inactive :: " + token.UUID + " :: " + ex);
        }
        return false;
    }


    /**
     * Given a voiceprint will check if that print exists in the active voiceprints map. Returns an access token if approved.
     * @param print
     * @return
     */
    public AccessToken voicePrintLookUp(VoicePrint print){
        try {
            User user = VoicePrints.get(print);
            if (user == null) {
                throw new AccessDeniedException();
            }
            return user.token;
        } catch (AccessDeniedException ex) {
            System.out.println("Access denied, user " + ex);
        }
        return null;
    }

    /**
     * Given a username(userID) for an existing user and a password string, will check if that is the password matches the users. If so will return an active token to use for authentication actions.
     * @param userID
     * @param password
     * @return
     */
    public AccessToken login(String userID, String password) {
        try {
            SecurityEntityComposite secUser = SecurityEntities.get(userID);
            if (secUser == null) {
                throw new InvalidCredentialsException();
            }
            User user = (User) secUser;
            boolean check = user.checkCredentials(password);
            if (!check) {
                throw new InvalidCredentialsException();
            }
            addCredential(userID, "token", "000");
            //System.out.println(userID);
            AccessTokens.put(user.token, userID);
            return user.token;

        } catch (InvalidCredentialsException ex) {
            System.out.println("Invalid credentials for " + userID + " :: " + ex);
        }
        return null;
    }


    /**
     * Invalidates the given access token so it can no longer be used.
     * @param token
     */
    public void logout(AccessToken token) {
        token.state = "inactive";
        AccessTokens.remove(token);
    }
}
