package cscie97.asn4.housemate.entitlement;

/**
 * Created by n0305853 on 11/11/18.
 */
public interface CredentialEntityComposite {
    /**
     * Compares a string of creds against the internal value of the EntityComposite, returns true if they match.
     * @param creds
     * @return
     */
    public boolean checkCredentials(String creds);
    public String getType();
}
