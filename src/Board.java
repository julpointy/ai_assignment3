import java.util.ArrayList;
import java.util.List;

public class Board {
    int height;
    int width;
    int[][] board;

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        this.board = new int[height][width];
    }

    /**
     * Determine the next possible moves based on the current coordinate
     * @param current The current coordinate
     * @return A list of possible moves
     */
    public List<Coordinate> possibleMoves(Coordinate current) {
        List<Coordinate> options = new ArrayList<>();

        // Create coordinate for left move
        Coordinate newCoordinateLeft = new Coordinate();
        newCoordinateLeft.setValue(current.value);
        newCoordinateLeft.setColumnloc(current.columnloc);
        newCoordinateLeft.setRowloc(current.rowloc);
        newCoordinateLeft.setDir(current.dir);

        // Create coordinate for right move
        Coordinate newCoordinateRight = new Coordinate();
        newCoordinateRight.setValue(current.value);
        newCoordinateRight.setColumnloc(current.columnloc);
        newCoordinateRight.setRowloc(current.rowloc);
        newCoordinateRight.setDir(current.dir);

        // Create coordinate for forward move
        Coordinate newCoordinateForward = new Coordinate();
        newCoordinateForward.setValue(current.value);
        newCoordinateForward.setColumnloc(current.columnloc);
        newCoordinateForward.setRowloc(current.rowloc);
        newCoordinateForward.setDir(current.dir);

        // Create coordinate for bash move
        Coordinate newCoordinateBash = new Coordinate();
        newCoordinateBash.setValue(current.value);
        newCoordinateBash.setColumnloc(current.columnloc);
        newCoordinateBash.setRowloc(current.rowloc);
        newCoordinateBash.setDir(current.dir);

        Coordinate left = newCoordinateLeft.turnLeft();
        Coordinate right = newCoordinateRight.turnRight();

        // Check the bounds of the board for each direction to determine the possible moves
        switch (current.dir) {
            case N:
                // Add forward move if the robot doesn't go out of bounds in N direction
                if (newCoordinateForward.rowloc > 0) {
                    Coordinate coord  = (newCoordinateForward.forward(this));
                    options.add(coord);
                }
                // Add bash move if the robot doesn't go out of bounds in N direction
                // and the previous move is not bash
                if (newCoordinateBash.rowloc > 1 && !current.hasBashed) {
                    options.add(newCoordinateBash.bash(this));
                }
                break;
            case E:
                // Add forward move if the robot doesn't go out of bounds in E direction
                if (newCoordinateForward.columnloc + 1 < width) {
                    options.add(newCoordinateForward.forward(this));
                }
                // Add bash move if the robot doesn't go out of bounds in E direction
                // and the previous move is not bash
                if (newCoordinateBash.columnloc + 2 < width && !current.hasBashed) {
                    options.add(newCoordinateBash.bash(this));
                }
                break;
            case S:
                // Add forward move if the robot doesn't go out of bounds in S direction
                if (newCoordinateForward.rowloc + 1 < height) {
                    options.add(newCoordinateForward.forward(this));
                }
                // Add bash move if the robot doesn't go out of bounds in S direction
                // and the previous move is not bash
                if (newCoordinateBash.rowloc + 2 < height && !current.hasBashed) {
                    options.add(newCoordinateBash.bash(this));
                }
                break;
            case W:
                // Add forward move if the robot doesn't go out of bounds in W direction
                if (newCoordinateForward.columnloc > 0) {
                    options.add(newCoordinateForward.forward(this));
                }
                // Add bash move if the robot doesn't go out of bounds in W direction
                // and the previous move is not bash
                if (newCoordinateBash.columnloc > 1 && !current.hasBashed) {
                    options.add(newCoordinateBash.bash(this));
                }
                break;
        }

        // Eliminate back and forth turns that would bring the robot back to the initial position
        if (!current.hasBashed) {
            if (current.move != left.move) {
                options.add(right);
            }
            if (current.move != right.move) {
                options.add(left);
            }
        }

        return options;
    }

    /**
     * Calculate the cost for each type of move
     * @param current The current coordinate
     * @param next The next coordinate
     * @return The cost for the next move
     */
    public int getCost(Coordinate current, Coordinate next) {
        int cost = 0;
        switch (next.move) {
            case LEFT:
            case RIGHT:
                cost = (int)Math.ceil((double)current.value / 2);
                break;
            case FORWARD:
                cost = next.value;
                break;
            case BASH:
                cost = 3;
                break;
        }
        return cost;
    }

    /**
     * method that calculates the xDistance from the current coordinate to the goal
     * @param finish the coordinate for the goal
     * @param next the current coordinate (place where the robot currently is)
     * @return returns an int which is how far the robot is from the goal
     */
    public int xDistance(Coordinate finish, Coordinate next){
        return Math.abs(finish.columnloc - next.columnloc);
    }

    /**
     * method that calculates the yDistance from the current coordinate to the goal
     * @param finish the coordinate for the goal
     * @param next the current coordinate (place where the robot currently is)
     * @return returns an int which is how far the robot is from the goal
     */
    public int yDistance(Coordinate finish, Coordinate next){
        return Math.abs(finish.rowloc - next.rowloc);
    }

    /**
     * method that returns the cost of the move to enter the goal piece
     * if the robot is approaching from 2 sides (at an angle) it will take the average of the two possible entrances
     * @param next the current coordinate that the robot is in
     * @param finish the goal coordinate
     * @return returns the cost to enter the goal
     */
    public int goalEnterCost(Coordinate next, Coordinate finish){
        int enterCost = 0; // initializes the return variable
        int verticalDifference = finish.rowloc - next.rowloc;  // calculates the vertical distance to the goal
        int horizontalDifference = finish.columnloc - next.columnloc;  //calculates the horizontal distance to the goal

        if(verticalDifference == 0){  //if the robot is appraching from directly above
            if(horizontalDifference > 0){   //check if robot is below goal
                //enter from west
                int finishCol = finish.columnloc;
                int finishRow = finish.rowloc;
                finishCol--;
                enterCost = board[finishRow][finishCol];  // cost is to enter the goal from the south coordinate
            }
            else if(horizontalDifference < 0){ // check if robot is above goal
                //enter from east
                int finishCol = finish.columnloc;
                int finishRow = finish.rowloc;
                finishCol++;
                enterCost = board[finishRow][finishCol];  // cost is to enter the goal from the north coordinate
            }
        }
        if(horizontalDifference == 0){ // check if the robot is approaching from left or right
            if(verticalDifference > 0){  // if left, cost is west coordinate from goal
                //enter from south
                int finishCol = finish.columnloc;
                int finishRow = finish.rowloc;
                finishRow--;
                enterCost = board[finishRow][finishCol];  //left cost
            }
            else if(verticalDifference < 0){ // if right approach
                //enter from north
                int finishCol = finish.columnloc;
                int finishRow = finish.rowloc;
                finishRow++;
                enterCost = board[finishRow][finishCol]; //cost of the east coordinate from goal
            }
        }
        if(verticalDifference < 0 && horizontalDifference > 0){ // check if approaching from 1 of 4 quadrants from the goal
            int finishCol = finish.columnloc;
            int finishRow = finish.rowloc;

            int cost1 = board[finishRow + 1][finishCol];
            int cost2 = board[finishRow][finishCol - 1];
            enterCost = (cost1 + cost2)/2;  //average the two values
        }
        if(verticalDifference > 0 && horizontalDifference > 0){
            int finishCol = finish.columnloc;
            int finishRow = finish.rowloc;

            int cost1 = board[finishRow - 1][finishCol];
            int cost2 = board[finishRow][finishCol - 1];
            enterCost = (cost1 + cost2)/2;
        }
        if(verticalDifference > 0 && horizontalDifference < 0){
            int finishCol = finish.columnloc;
            int finishRow = finish.rowloc;

            int cost1 = board[finishRow - 1][finishCol];
            int cost2 = board[finishRow][finishCol + 1];
            enterCost = (cost1 + cost2)/2;
        }
        if(verticalDifference < 0 && horizontalDifference < 0){
            int finishCol = finish.columnloc;
            int finishRow = finish.rowloc;

            int cost1 = board[finishRow + 1][finishCol];
            int cost2 = board[finishRow][finishCol + 1];
            enterCost = (cost1 + cost2)/2;
        }
        return enterCost;
    }

    /**
     * method to calculate the minimum amount of turns needed to get to the goal
     * @param next current coordinate the robot is at
     * @param finish the goal coordinate
     * @return the number of turns
     */
    public int minTurns(Coordinate next, Coordinate finish){
        int totalCost = 0;
        int newVertical5 = finish.rowloc - next.rowloc;
        int newHorizontal5 = finish.columnloc - next.columnloc;

        if ((newVertical5 == 0) && (newHorizontal5 > 0)) { // checks if the robot is directly left or right to goal with no vertical difference
            switch (next.dir) {
                case N:
                case S:
                    totalCost += 1;  //if facing up, only needs to turn once
                    break;
                case E: // if facing the right way, no turns
                    break;
                case W:  // if facing the opposite way, 2 turns
                    totalCost += 2;
                    break;
            }
        } else if ((newVertical5 == 0) && (newHorizontal5 < 0)) { // same as above, other case however
            switch (next.dir) {
                case N:
                case S:
                    totalCost += 1;
                    break;
                case E:
                    totalCost += 2;
                    break;
                case W:
                    break;
            }
        } else if ((newVertical5 < 0) && (newHorizontal5 == 0)) {
            switch (next.dir) {
                case N:
                    break;
                case S:
                    totalCost += 2;
                    break;
                case E:
                case W:
                    totalCost += 1;
                    break;
            }
        } else if ((newVertical5 > 0) && (newHorizontal5 == 0)) {
            switch (next.dir) {
                case N:
                    totalCost += 2;
                    break;
                case S:
                    break;
                case E:
                case W:
                    totalCost += 1;
                    break;
            }
        } else if (newVertical5 > 0) {
            switch (next.dir) {
                case N:
                    totalCost += 2;
                    break;
                case S:
                    break;
                case E:
                case W:
                    totalCost += 1;
                    break;
            }
        } else if (newVertical5 < 0) {
            switch (next.dir) {
                case N:
                    break;
                case S:
                    totalCost += 2;
                    break;
                case E:
                case W:
                    totalCost += 1;
                    break;
            }
        }
        return totalCost;
    }

    /**
     * Calculate the heuristic value
     * @param heuristic The heuristic
     * @param finish The goal position
     * @param next The next coordinate
     * @return The heuristic value
     */
    public int calculateHeuristic(int heuristic, Coordinate finish, Coordinate next) {
        int totalCost = 0;
        int vertical = Math.abs(finish.rowloc - next.rowloc);
        int horizontal = Math.abs(finish.columnloc - next.columnloc);
        int vertical5 = finish.rowloc - next.rowloc;
        int horizontal5 = finish.columnloc - next.columnloc;

        // Calculate the heuristic based on the given value
        switch (heuristic) {
            case 1:
                break;
            case 2:
                totalCost = Math.min(vertical, horizontal);
                break;
            case 3:
                totalCost = Math.max(vertical, horizontal);
                break;
            case 4:
                totalCost = vertical + horizontal;
                break;
            case 6:
            case 5:
                // Add 1 to the total cost for each turn needed
                if ((vertical5 == 0) && (horizontal5 > 0)) {
                    switch (next.dir) {
                        case N:
                        case S:
                            totalCost += 1;
                            break;
                        case E:
                            break;
                        case W:
                            totalCost += 2;
                            break;
                    }
                } else if ((vertical5 == 0) && (horizontal5 < 0)) {
                    switch (next.dir) {
                        case N:
                        case S:
                            totalCost += 1;
                            break;
                        case E:
                            totalCost += 2;
                            break;
                        case W:
                            break;
                    }
                } else if ((vertical5 < 0) && (horizontal5 == 0)) {
                    switch (next.dir) {
                        case N:
                            break;
                        case S:
                            totalCost += 2;
                            break;
                        case E:
                        case W:
                            totalCost += 1;
                            break;
                    }
                } else if ((vertical5 > 0) && (horizontal5 == 0)) {
                    switch (next.dir) {
                        case N:
                            totalCost += 2;
                            break;
                        case S:
                            break;
                        case E:
                        case W:
                            totalCost += 1;
                            break;
                    }
                } else if (vertical5 > 0) {
                    switch (next.dir) {
                        case N:
                            totalCost += 2;
                            break;
                        case S:
                            break;
                        case E:
                        case W:
                            totalCost += 1;
                            break;
                    }
                } else if (vertical5 < 0) {
                    switch (next.dir) {
                        case N:
                            break;
                        case S:
                            totalCost += 2;
                            break;
                        case E:
                        case W:
                            totalCost += 1;
                            break;
                    }
                }
                totalCost += (vertical + horizontal);
                break;
        }
        if (heuristic == 6) {
            totalCost = (totalCost * 3);
        }
        return totalCost;
    }
}
