package cscie97.asn3.housemate.controller;
import cscie97.asn3.housemate.model.HouseMateModel;
/**
 * Created by n0305853 on 10/19/18.
 */
public interface Rule {
    public String name = "";
    String getName();
    boolean execute(String change, HouseMateModel model);
}
