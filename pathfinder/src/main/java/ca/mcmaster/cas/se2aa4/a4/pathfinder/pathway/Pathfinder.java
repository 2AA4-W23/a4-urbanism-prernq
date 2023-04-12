package ca.mcmaster.cas.se2aa4.a4.pathfinder.pathway;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Node;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.graph.Edge;
import java.util.List;


public interface Pathfinder {
    List <Node> path_finder(Node start, Node end);
}
