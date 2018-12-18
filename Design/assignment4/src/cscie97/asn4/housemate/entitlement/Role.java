package cscie97.asn4.housemate.entitlement;

import java.util.ArrayList;

/**
 * Created by n0305853 on 11/11/18.
 */
public class Role implements SecurityEntityComposite {

    public String UUID;
    public String name;
    public String desc;
    public ArrayList permissions;
    public Role(String UUID, String name, String desc){
        this.UUID = UUID;
        this.name = name;
        this.desc = desc;
        permissions = new ArrayList<Permission>();
    }


    /**
     * Given a perm object, adds it to the permissions list of this object.
     * @param perm
     */
    public void addPermission(Permission perm){
        permissions.add(perm);
    }


    @Override
    public boolean checkPermission(String desc) {
        //System.out.println(permissions.size());
        for(Object obj : permissions){//S
           Permission perm = (Permission) obj;
           boolean check = perm.checkPermission(desc);
           if(check){
               return check;
           }
        }
        return false;
    }
}
