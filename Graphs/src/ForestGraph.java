import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

// Implements a graph using adjacency lists
// Uses map of string and skip list set to maintain mapping from vertex label to sorted list of adjacent vertices
// Uses map of string and vertex object to maintain mapping from vertex label to vertex properties

// 1. Implements dfs and explore
// 2. Determines connected component for undirected graphs
// 3. Determines pre and post order visits for each vertex
// 4. Checks if the given vertex is a tree edge / forward edge or back edge or cross edge

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

        startVertex.setPreVisit(visit++);
//        System.out.println("Reached node " + startVertexLabel + " with pre visit " + startVertex.getPreVisit());

        startVertex.setVisited(true);
        startVertex.setConnectCompNumber(ccNum);
        ConcurrentSkipListSet<String> adjVertexList = adjListMap.get(startVertexLabel);

        if (adjVertexList != null)
        {
            for(String adjVertexLabel:adjVertexList)
            {
                Vertex adjVertex = vertexMap.get(adjVertexLabel);

                if (! adjVertex.getVisited())
                {
                    explore(adjVertexLabel);
                }
            }
        }

        startVertex.setPostVisit(visit++);
//        System.out.println("Exited node " + startVertexLabel + " with post visit " + startVertex.getPostVisit());
    }

    public void dfs() throws Exception
    {
        System.out.println("DFS traversing ...");

        for(Map.Entry<String, Vertex> entry:vertexMap.entrySet())
        {
            entry.getValue().setVisited(false);
        }

        for(Map.Entry<String, Vertex> entry:vertexMap.entrySet())
        {
            Vertex v = entry.getValue();

            if ( ! v.getVisited())
            {
                System.out.println("Calling explore from dfs on " + v.getLabel());

                v.setConnectCompNumber(ccNum++);
                explore(v.getLabel());
            }
        }

        for(Map.Entry<String, Vertex> entry:vertexMap.entrySet())
        {
            String key = entry.getKey();
            Vertex vertex = entry.getValue();

            System.out.println("(Pre-Visit,Post-Visit) of " + key + " => (" + vertex.getPreVisit() + "," + vertex.getPostVisit() + ")");
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

    public String getEdgeType(String startVertex, String endVertex)
    {
        Vertex v1 = vertexMap.get(startVertex);
        Vertex v2 = vertexMap.get(endVertex);

        if (v1 == null || v2 == null)
        {
            System.out.println("*******  ERROR => One or both of the vertex not found  *********");
            return "Invalid Edge";
        }

        int v1Pre = v1.getPreVisit();
        int v1Post = v1.getPostVisit();
        int v2Pre = v2.getPreVisit();
        int v2Post = v2.getPostVisit();

        if ( v1Pre < v2Pre && v2Post < v1Post )
            return "Tree Edge";

        if (v2Pre < v1Pre && v1Post < v2Post )
            return "Back Edge";

        return "Cross Edge";

    }

    public static void main(String[] args) throws Exception
    {
        // If digraph, pass true
        ForestGraph graph = new ForestGraph(false);

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

        Scanner scan = new Scanner(System.in);

        while (true)
        {
            System.out.println("*** Finding edge type ***");
            System.out.println("Enter first vertex label (enter quit to exit)");
            String s1 = scan.nextLine();

            if (s1 == null || s1.length()==0 || s1.equalsIgnoreCase("quit"))
                break;

            System.out.println("Enter second vertex label (enter quit to exit)");
            String s2 = scan.nextLine();

            if (s2 == null || s2.length()==0 || s2.equalsIgnoreCase("quit"))
                break;

            System.out.println("*** Edge type of " + s1 + " => " + s2 + " = " + graph.getEdgeType(s1, s2));
        }

    }

    ConcurrentHashMap<String, ConcurrentSkipListSet<String>> adjListMap;
    ConcurrentHashMap<String, Vertex> vertexMap;
    boolean directed;
    int ccNum, visit;

    private static final int TREE_EDGE = 1;
    private static final int BACK_EDGE = 2;
    private static final int CROSS_EDGE = 3;
}

class Vertex
{
    String label;
    boolean isVisited;
    int connectCompNumber;
    int preVisit;
    int postVisit;

    int getPostVisit() {
        return postVisit;
    }

    void setPostVisit(int postVisit) {
        this.postVisit = postVisit;
    }

    int getPreVisit() {
        return preVisit;
    }

    void setPreVisit(int preVisit) {
        this.preVisit = preVisit;
    }

    int getConnectCompNumber() {
        return connectCompNumber;
    }

    void setConnectCompNumber(int connectCompNumber) {
        this.connectCompNumber = connectCompNumber;
    }

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
