package cscie97.asn4.housemate.entitlement;

import java.util.ArrayList;

/**
 * Created by n0305853 on 11/11/18.
 */
public class Permission implements SecurityEntityComposite {

    public String UUID;
    public String name;
    public String desc;
    public Permission(String UUID, String name, String desc){
        this.UUID = UUID;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public boolean checkPermission(String desc) {
        //System.out.println(desc + " should contain " + this.desc);
        if(this.desc.toLowerCase().contains(desc) || this.desc.contains("*")){
            return true;
        }
        return false;
    }
}
