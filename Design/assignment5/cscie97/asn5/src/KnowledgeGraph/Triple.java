package KnowledgeGraph;

/**
 * Created by Brandon T. Wood on 9/7/18.
 */
public class Triple extends Node{
    private Node sub;
    private Predicate pre;
    private Node obj;

    public Triple(Node sub, Predicate pre, Node obj) {
        super(sub.getIdentifier()+" "+pre.getIdentifier()+" "+obj.getIdentifier());
        this.sub = sub;
        this.pre = pre;
        this.obj = obj;
    }
}
