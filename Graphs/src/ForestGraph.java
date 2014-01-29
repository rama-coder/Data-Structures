import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created with IntelliJ IDEA.
 * User: RAGA
 * Date: 12/13/13
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ForestGraph {
    public ForestGraph() {
        adjListMap = new ConcurrentHashMap<String, CopyOnWriteArraySet<String>>();
    }

    public void addEdge(String c1, String c2)
    {
        System.out.println("Adding edge between " + c1 + " and " + c2);

        if (adjListMap.containsKey(c1))
        {
            if (adjListMap.get(c1).contains(c2))
                throw new IllegalArgumentException("Given edge between "+ c1 + " and " + c2 + " is a duplicate edge.");

            adjListMap.get(c1).add(c2);
        }
        else
        {
            CopyOnWriteArraySet<String> vertexList1 = new CopyOnWriteArraySet<String>();
            vertexList1.add(c2);
            adjListMap.put(c1, vertexList1);
        }

        if (adjListMap.containsKey(c2))
        {
            if (adjListMap.get(c2).contains(c1))
                throw new IllegalArgumentException("Given edge between "+ c2 + " and " + c1 + " is a duplicate edge.");

            adjListMap.get(c2).add(c1);
        }
        else
        {
            CopyOnWriteArraySet<String> vertexList2 = new CopyOnWriteArraySet<String>();
            vertexList2.add(c1);
            adjListMap.put(c2, vertexList2);
        }
    }

    public void print()
    {
        System.out.println("Printing the graph ....");

        for (Map.Entry<String, CopyOnWriteArraySet<String>> entry : adjListMap.entrySet())
        {
            String key = entry.getKey();
            CopyOnWriteArraySet<String> list = entry.getValue();

            System.out.println("Printing the adjacent list of node " + key);
            System.out.println("Size of the list is " + list.size());

            for (String adjVertex : list)
            {
                System.out.println(adjVertex);
            }
        }

        System.out.println("Graph has been printed.");
    }

    public static void main(String[] args)
    {
        ForestGraph graph = new ForestGraph();

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("B", "E");
        graph.addEdge("B", "F");

//        graph.addEdge("C", "F");
//        graph.addEdge("D", "G");
//        graph.addEdge("D", "H");
//        graph.addEdge("E", "I");
//        graph.addEdge("E", "J");
//
//        graph.addEdge("I", "J");
//        graph.addEdge("K", "L");

        graph.print();
    }

    ConcurrentHashMap<String, CopyOnWriteArraySet<String>> adjListMap;
}

