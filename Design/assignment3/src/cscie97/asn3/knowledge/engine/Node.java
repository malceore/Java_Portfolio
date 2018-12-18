package cscie97.asn3.knowledge.engine;

/**
 * Created by n0305853 on 9/7/18.
 */
public class Node {
    private String identifier;
    private long createDate;

    public Node(String id) {
        this.identifier = id;
        this.createDate = getDate();
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    // Not mentioned in source, but added because seemed to insue that
    private long getDate() {
        return System.currentTimeMillis() / 1000L;
    }
}