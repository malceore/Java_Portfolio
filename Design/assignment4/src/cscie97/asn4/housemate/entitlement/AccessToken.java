package cscie97.asn4.housemate.entitlement;

import java.util.Date;

/**
 * Created by n0305853 on 11/11/18.
 */
public class AccessToken implements CredentialEntityComposite {

    public String UUID;
    public String state;
    public String lastUsed;
    public AccessToken(String UUID){
        this.UUID = UUID;
        this.state = "active";
        Date date = new Date();
        this.lastUsed = date.toString();
    }

    /**
     * Returns the type of this credential entity, token.
     * @return
     */
    public String getType(){
        return "token";
    }

    @Override
    public boolean checkCredentials(String creds){
        return false;
    }
}
