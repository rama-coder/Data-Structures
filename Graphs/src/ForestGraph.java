import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ForestGraph {

        public ForestGraph() {
            this(false);
        }

        public ForestGraph(boolean isDirected) {
        adjListMap = new ConcurrentHashMap<String, ConcurrentSkipListSet<String>>();
        vertexMap = new ConcurrentHashMap<String, Vertex>();
        directed = isDirected;
    }

    public void addEdge(String c1, String c2)
    {
        System.out.println("Adding edge between " + c1 + " and " + c2);

        if ( ! vertexMap.containsKey(c1))
        {
               Vertex v1 = new Vertex(c1);
               vertexMap.put(c1, v1);
        }

        if ( ! vertexMap.containsKey(c2))
        {
            Vertex v2 = new Vertex(c2);
            vertexMap.put(c2, v2);
        }

        if (adjListMap.containsKey(c1))
        {
            if (adjListMap.get(c1).contains(c2))
                throw new IllegalArgumentException("Given edge between "+ c1 + " and " + c2 + " is a duplicate edge.");

            adjListMap.get(c1).add(c2);
        }
        else
        {
            ConcurrentSkipListSet<String> vertexList1 = new ConcurrentSkipListSet<String>();
            vertexList1.add(c2);
            adjListMap.put(c1, vertexList1);
        }

        if ( ! directed)
        {
            if (adjListMap.containsKey(c2))
            {
                if (adjListMap.get(c2).contains(c1))
                    throw new IllegalArgumentException("Given edge between "+ c2 + " and " + c1 + " is a duplicate edge.");

                adjListMap.get(c2).add(c1);
            }
            else
            {
                ConcurrentSkipListSet<String> vertexList2 = new ConcurrentSkipListSet<String>();
                vertexList2.add(c1);
                adjListMap.put(c2, vertexList2);
            }
        }
    }

    public void explore(String startVertexLabel) throws Exception
    {
        Vertex  startVertex = vertexMap.get(startVertexLabel);

        if (startVertex == null)
        {
            System.out.println("Given startVertex " + startVertexLabel + " not found");
        }

        System.out.println("Reached node " + startVertexLabel);

        startVertex.setVisited(true);

        ConcurrentSkipListSet<String> adjVertexList = adjListMap.get(startVertexLabel);

        if (adjVertexList == null)
            return;

        for(String adjVertexLabel:adjVertexList)
        {
            Vertex adjVertex = vertexMap.get(adjVertexLabel);

            if (! adjVertex.getVisited())
            {
                  explore(adjVertexLabel);
            }
        }
    }

    public void dfs() throws Exception
    {
        System.out.println("DFS traversing ...");

        for(Map.Entry<String, Vertex> entry:vertexMap.entrySet())
        {
            String key = entry.getKey();
            Vertex v = entry.getValue();

            if ( ! v.getVisited())
                explore(v.getLabel());
        }
    }

    public void print()
    {
        System.out.println("Printing the graph ....");

        for (Map.Entry<String, ConcurrentSkipListSet<String>> entry : adjListMap.entrySet())
        {
            String key = entry.getKey();
            ConcurrentSkipListSet<String> list = entry.getValue();

            System.out.println("Printing the adjacent list of node " + key);
            System.out.println("Size of the list is " + list.size());

            for (String adjVertex : list)
            {
                System.out.println(adjVertex);
            }
        }

        System.out.println("Graph has been printed.");
    }

    public static void main(String[] args) throws Exception
    {
        ForestGraph graph = new ForestGraph(true);

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
        graph.addEdge("I", "J");
//        graph.addEdge("K", "L");

        graph.print();
        graph.dfs();
    }

    ConcurrentHashMap<String, ConcurrentSkipListSet<String>> adjListMap;
    ConcurrentHashMap<String, Vertex> vertexMap;
    boolean directed;
}

class Vertex
{
    String label;
    boolean isVisited;

    public Vertex(String nodeValue)
    {
        label = nodeValue;
    }

    public String getLabel()
    {
        return label;
    }
    public boolean getVisited()
    {
        return isVisited;
    }

    public void setVisited(boolean visited)
    {
        isVisited = visited;
    }
}

