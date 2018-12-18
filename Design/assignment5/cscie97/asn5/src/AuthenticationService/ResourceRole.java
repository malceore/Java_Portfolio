package AuthenticationService;

import AuthenticationService.Role;
import AuthenticationService.SecurityEntityComposite;

/**
 * Created by n0305853 on 11/16/18.
 */
public class ResourceRole implements SecurityEntityComposite {

    private Role role;
    public String name;
    public ResourceRole(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public boolean checkPermission(String desc) {
        return role.checkPermission(desc);
    }
}
