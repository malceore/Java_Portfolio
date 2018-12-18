package cscie97.asn4.housemate.entitlement;

/**
 * Created by n0305853 on 11/11/18.
 */
public class VoicePrint implements CredentialEntityComposite {

    public String UUID;
    public String voiceSig;
    public VoicePrint(String UUID, String voiceSig){
        this.UUID = UUID;
        this.voiceSig = voiceSig;
    }

    /**
     * Returns the type of this credentialentity, voiceprint.
     * @return
     */
    public String getType(){
        return "voiceprint";
    }

    @Override
    public boolean checkCredentials(String creds){
        return false;
    }
}
