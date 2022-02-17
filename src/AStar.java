import javafx.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    /**
     * Find the path between the start and finish locations on the given board using the given heuristic
     * @param start The location of the start position
     * @param finish The location of the goal position
     * @param b The board being used
     * @param heuristic The heuristic value being used
     */
    public void astar(Coordinate start, Coordinate finish, Board b, int heuristic) {
        // Create a priority queue for the frontier and add the starting location to it
        PriorityQueue<Pair<Coordinate, Integer>> frontier = new PriorityQueue<>((x, y) -> x.getValue() - y.getValue());
        frontier.add(new Pair(start, 0));

        // Create a hash map to keep track of which moves led to which moves and put the
        // starting location in
        HashMap<Coordinate, Coordinate> cameFrom = new HashMap<>();
        cameFrom.put(start, null);

        // Create a hash map to keep track of the cost so far from the starting location
        // for each coordinate
        HashMap<Coordinate, Integer> costSoFar = new HashMap<>();
        costSoFar.put(start, 0);

        int nodesExpanded = 0;

        while(!frontier.isEmpty()) {
            // Remove a coordinate to expand
            Pair<Coordinate, Integer> currLocationCost = frontier.poll();
            nodesExpanded++;
            Coordinate currLocation = currLocationCost.getKey();

            // Check if the current coordinate is the goal position
            if(currLocation.equals(finish)) {
                // Print the score of the path found, the number of actions required to reach the
                // goal, the number of nodes expanded, and the series of actions
                System.out.print("Score of the path found: ");
                System.out.println(100 - costSoFar.get(currLocation));
                String[] movesMade = currLocation.path.split("\n");
                int movesTaken = movesMade.length - 1;
                System.out.println("Number of Actions: " + movesTaken);
                System.out.println("Nodes Expanded: " + nodesExpanded);
                System.out.println("Series of Actions:");
                System.out.println(currLocation.path.trim());
                break;
            }

            // Get the possible next moves for the current coordinate
            List<Coordinate> moves = b.possibleMoves(currLocation);

            // Loop through the possible next moves
            for (int i = 0; i < moves.size(); i++) {
                Coordinate next = moves.get(i);
                next.setPath(currLocation.path + "\n" + next.move.toString());
                int newCost = costSoFar.get(currLocation) + b.getCost(currLocation, next);
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    int priority = newCost + b.calculateHeuristic(heuristic, finish, next);
                    frontier.add(new Pair(next, priority));
                    cameFrom.put(next, currLocation);
                }
            }
        }
    }
}
