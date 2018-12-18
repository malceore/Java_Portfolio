package AuthenticationService;

/**
 * Created by n0305853 on 11/10/18.
 */
public interface SecurityEntityComposite {
    /**
     * Checks if the object or it's children have the permission desc passed to it, returns true if so.
     * @param desc
     * @return
     */
    public boolean checkPermission(String desc);
}
