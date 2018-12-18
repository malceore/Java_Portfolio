package cscie97.asn2.housemate.model;
import java.util.*;
/**
 * <h1>Knowledge Graph</h1>
 * A shared singleton object acting as a datastore for our triple data, it can be queried and have data inserted.
 * @author  Brandon T. Wood
 */
public class KnowledgeGraph {
    private Map<String, Node> tripleMap = new HashMap<String, Node>();
    private Map<String, Node> predicateMap = new HashMap<String, Node>();
    private Map<String, Node> nodeMap = new HashMap<String, Node>();
    private Map<String, Set> queryMapSet = new HashMap<String, Set>();
    private static KnowledgeGraph instance = null;


    /**
     * Singleton Constructor
     * @return
     */
    public static KnowledgeGraph getInstance() {
        if(instance == null) {
            instance = new KnowledgeGraph();
        }
        return instance;
    }


    /**
     * Method takes in three string params, assembles them each into their respective nodes and adds it to it's internal queryMap.
     * @param s_sub
     * @param s_pre
     * @param s_obj
     */
    public void importTriple(String s_sub, String s_pre, String s_obj){
        Predicate pred = new Predicate(s_pre.toLowerCase());
        Node sub = new Node(s_sub.toLowerCase());
        Node obj = new Node(s_obj.toLowerCase());
        // Setup the triple map.
        Triple trip = new Triple(sub, pred, obj);
        tripleMap.put(trip.getIdentifier(), trip);
        // Setup the predicate map.
        predicateMap.put(pred.getIdentifier(), pred);
        // Setup the node map.
        nodeMap.put(sub.getIdentifier(), sub);
        nodeMap.put(obj.getIdentifier(), obj);
        // Setup querySetMap
        this.queryMapSet.put(trip.getIdentifier(), generateSetMap(trip.getIdentifier()));
    }


    /**
     * Override of importTriple that can iterate over a list of strings instead of just taking a single instance.
     * @param list
     */
    public void importTriple(List<String[]> list){
        for (String[] trips : list){
            this.importTriple(trips[0], trips[1], trips[2]);
        }
    }


    /**
     * Method generates all possible queries that map to the given string and returns them as a String array.
     * @param identifier
     * @return
     */
    public Set generateSetMap(String identifier){
        Set accum = new HashSet();
        // Could iterate but this is simple and easy to understand.
        String[] triple = identifier.split(" ");
        accum.add(identifier);
        accum.add("? " + triple[1] + " " + triple[2]);
        accum.add(triple[0] + " ? " + triple[2]);
        accum.add(triple[0] + " " + triple[1] +" ?");
        accum.add("? ? " + triple[2]);
        accum.add("? " + triple[1] + " ?");
        accum.add(triple[0] + " ? ?");
        accum.add("? ? ?");
        return accum;
    }


    /**
     * Method iterates over the queryMap comparing each string's list of possible iteractions to the given query.
     * Returns a set of resulting matches as Triples.
     * @param sub
     * @param pre
     * @param obj
     * @return
     */
    public Set<Triple> executeQuery(String sub, String pre, String obj){
        Set accum = new HashSet();
        // toLowerCase keeps things case insensitive.
        String query = sub.toLowerCase() + " " + pre.toLowerCase() + " " + obj.toLowerCase();
        // Iterate over thequery map.
        for(Map.Entry<String, Set> pair : queryMapSet.entrySet()){
            // Iterate over the String array associated with the map's key.
            for(Object entry : pair.getValue()){
                String s_entry = entry.toString();
                // If we find a match we append it to the return list and break from the string array loop.
                if(s_entry.equals(query)){
                    String s_hold = pair.getKey();
                    String[] hold = s_hold.split(" ");
                    accum.add(getTriple(hold[0], hold[1], hold[2]));
                    break;
                }
            }
        }
        return accum;
    }


    /**
     * Method fetches the nodes from the node map matching it's string id.
     * @param id
     * @return
     */
    public Node getNode(String id){
        Node n = nodeMap.get(id);
        if(n == null){
            nodeMap.put(id, new Node(id));
            n = nodeMap.get(id);
        }
        return n;
    }


    /**
     * Method fetches the predicate from the predicate map matching the string id.
     * @param id
     * @return
     */
    public Predicate getPredicate(String id){
        Predicate pred = (Predicate) predicateMap.get(id);
        if(pred == null){
            predicateMap.put(id, new Predicate(id));
            pred = (Predicate) predicateMap.get(id);
        }
        return pred;
    }


    /**
     * Method fetches the predicate from the predicate map matching the string id.
     * @param s_sub
     * @param s_pre
     * @param s_obj
     * @return
     */
    public Triple getTriple(String s_sub, String s_pre, String s_obj){
        Triple trip = new Triple(getNode(s_sub), getPredicate(s_pre), getNode(s_obj));
        return getTriple(trip);
    }


    /**
     * Method searches over tripleMap for the triple given, when it finds it it is returned, otherwise it is created and returned.
     * @param trip
     * @return
     */
    public Triple getTriple(Triple trip){
        // Check to see if it exists in knowledge map, if not, add it, then return it.
        if(!tripleMap.containsKey(trip.getIdentifier())){
            String s_trip = trip.getIdentifier();
            String[] hold = s_trip.split(" ");
            importTriple(hold[0], hold[1], hold[2]);
        }
        return trip;
    }

    public void removeTriple(Triple triple){
        queryMapSet.remove(triple.getIdentifier());
    }
}
