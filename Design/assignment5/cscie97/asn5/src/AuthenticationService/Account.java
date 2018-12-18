package AuthenticationService;

import AuthenticationService.CredentialEntityComposite;

/**
 * Created by n0305853 on 11/11/18.
 */
public class Account implements CredentialEntityComposite {

    public String UUID;
    public String username;
    public String password;

    public Account(String UUID, String username, String password){
        this.UUID = UUID;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the type of this credentialentity, account
     * @return
     */
    public String getType(){
        return "account";
    }

    @Override
    public boolean checkCredentials(String creds){
        return false;
    }
}
