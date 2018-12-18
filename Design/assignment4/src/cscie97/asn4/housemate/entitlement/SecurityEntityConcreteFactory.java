package cscie97.asn4.housemate.entitlement;

/**
 * Created by n0305853 on 11/15/18.
 */
public class SecurityEntityConcreteFactory implements SecurityEntityAbstractFactory{

    private int nextUUID;
    public SecurityEntityConcreteFactory(){
        this.nextUUID = 0; // Can replace with a random seed, but would need a way to check it.
    }


    /**
     * Generates and returns the next UUID in the list.
     * @return
     */
    public String getNextUUID(){
        nextUUID++;
        return Integer.toString(this.nextUUID);
    }


    /**
     * Generates and returns a new user with given params. Generting a unique id for the user.
     * @param name
     * @return
     */
    public User createUser(String name){
        return createUser(getNextUUID(), name);
    }


    /**
     * Generates and returns a new user with given params. using the unique id given
     * @param UUID
     * @param name
     * @return
     */
    public User createUser(String UUID, String name){
        return new User(UUID, name);
    }


    /**
     * Generates and returns a new permission based off given params with generated UUID.
     * @param name
     * @param desc
     * @return
     */
    public Permission createPermission(String name, String desc){
        return createPermission(getNextUUID(), name, desc);
    }


    /**
     * Generates and retrns a new permission based off given params, uses given UUID.
     * @param UUID
     * @param name
     * @param desc
     * @return
     */
    public Permission createPermission(String UUID, String name, String desc){
        return new Permission(UUID, name, desc);
    }


    /**
     * Generates and returns a new role based on given params, generated UUID.
     * @param name
     * @param desc
     * @return
     */
    public Role createRole(String name, String desc){
        return createRole(getNextUUID(), name, desc);
    }


    /**
     * Generates and returns a new role based on given params, uses given uuid.
     * @param UUID
     * @param name
     * @param desc
     * @return
     */
    public Role createRole(String UUID, String name, String desc){
        return new Role(UUID, name, desc);
    }


    /**
     * Generates and returns a new resource role with e given UUID and existing role object.
     * @param UUID
     * @param role
     * @return
     */
    public ResourceRole createResourceRole(String UUID, Role role){
        return new ResourceRole(UUID, role);
    }
}
