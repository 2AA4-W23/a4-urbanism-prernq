import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Node;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Edge;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Graph;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.pathway.GraphShortestPath;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.pathway.Pathfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Override;

public class UnitTests {
    Graph graph = new Graph();

    @Before
    //creates a graph to test on
    public void newGraph() {
        this.graph = new Graph();
        this.graph.addNode(1);
        this.graph.addNode(2);
        this.graph.addNode(3);
        this.graph.addNode(4);
        this.graph.addNode(5);
        this.graph.addNode(6);

        this.graph.addEdge(this.graph.getNode(1), this.graph.getNode(2));
        this.graph.addEdge(this.graph.getNode(2), this.graph.getNode(3));
        this.graph.addEdge(this.graph.getNode(3), this.graph.getNode(4));
        this.graph.addEdge(this.graph.getNode(4), this.graph.getNode(5));
        this.graph.addEdge(this.graph.getNode(5), this.graph.getNode(6));

    }

    
    //List<Node> nodepairs = new ArrayList<>();
    //nodepairs.add((this.graph.getNode(1), this.graph.getNode(4)));
    @Test
    public void findPath1(){
        this.graph.addEdge(this.graph.getNode(4), this.graph.getNode(6));
        this.graph.getEdge(4).setWeight(4);
        PathFinder find = new ShortestPath(this.graph);
        List<Node> expectedPath = Arrays.toList(this.graph.getNode(1), this.graph.getNode(2), this.graph.getNode(3), this.graph.getNode(4), this.graph.getNode(5), this.graph.getNode(6));
        assertEquals(expectedPath, path.path_finder(this.graph.getNode(4), this.graph.getNode(6)));
    }

    @Test
    public void findPath2(){
        this.graph.addEdge(this.graph.getNode(3), this.graph.getNode(1));
        this.graph.getEdge(3).setWeight(1);
        PathFinder find = new ShortestPath(this.graph);
        List<Node> expectedPath = Arrays.toList(this.graph.getNode(3), this.graph.getNode(2), this.graph.getNode(1));
        assertEquals(expectedPath, path.path_finder(this.graph.getNode(3), this.graph.getNode(1)));
    }

    @Test
    public void newEdge(){
        int edges = this.graph.getEdgesList().size();
        this.graph.addEdge(this.graph.getNode(1), this.graph.getNode(2));
        assertEquals((this.graph.getEdgeList().size()), edges + 1);
    }

    @Test
    public void newNode(){
        int nodes = this.graph.getNodesList().size();
        this.graph.addNode(nodes+1);
        assertEquals(this.graph.getNodesList().size(), nodes+1); 
    }
}
