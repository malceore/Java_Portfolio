package AuthenticationService;

import AuthenticationService.SecurityEntityComposite;

import java.util.ArrayList;

/**
 * Created by n0305853 on 11/11/18.
 */
public class User implements SecurityEntityComposite {

    public String UUID = null;
    public String name = null;
    public ArrayList roles = null;
    public AccessToken token = null;
    //public CredentialEntityComposite creds;
    public ArrayList credList;
    public User(String UUID, String name){
        this.UUID = UUID;
        this.name = name;
        roles = new ArrayList<SecurityEntityComposite>();
        credList = new ArrayList<CredentialEntityComposite>();
        //default voice role
        VoicePrint print = new VoicePrint("Default", "--default--");
        credList.add(print);
    }


    /**
     * Adds the voice print to the creds list.
     * @param print
     */
    public void addVoicePrint(CredentialEntityComposite print){
        credList.add((VoicePrint) print);
    }


    /**
     * Adds the given password(account cred) to the creds list
     * @param passwd
     */
    public void addPassword(CredentialEntityComposite passwd){
        //this.creds = (Account) passwd;
        credList.add((Account) passwd);
    }


    /**
     * Adds the given token object to the creds list, also sets it as a variable.
     * @param token
     */
    public void addToken(CredentialEntityComposite token){
        //this.token = (AccessToken) token;
        credList.add((AccessToken) token);
        this.token = (AccessToken) token;
    }


    /**
     * Adds the given role to the list of roles.
     * @param role
     */
    public void addRole(SecurityEntityComposite role){
        this.roles.add(role);
    }


    /**
     * Given a desc, will check it's role and their permissions for a match, return true if it does.
     * @param desc
     * @return
     */
    public boolean checkPermission(String desc) {
        //System.out.println(roles.size());
            for (Object obj : roles) {
                Role role = (Role) obj;
                //System.out.println(role.permissions.size());
                boolean check = role.checkPermission(desc);
                if (check) {
                    return check;
                }
            }
        return false;
    }


    /**
     * Given a string credential(password) will check against it's list of creds and return true if it matches any.
     * @param cred
     * @return
     */
    public boolean checkCredentials(String cred){
        for(Object obj : credList) {
            CredentialEntityComposite secEnt = (CredentialEntityComposite) obj;
            if (secEnt.getType().equals("account")) {
                Account acc = (Account) secEnt;
                if (acc.password.equals(cred)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Can stringify the users for easy debugging.
     * @return
     */
    public String toString(){
        return "Name:" + name + " UUID:" + UUID + " Roles:" + roles.size() + " token:" + token + " creds:" + credList;
    }
}
