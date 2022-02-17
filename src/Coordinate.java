public class Coordinate {
    enum dir {
        N,
        S,
        E,
        W
    }
    enum move {
        LEFT,
        RIGHT,
        FORWARD,
        BASH,
        CURRENT
    }

    int columnloc;
    int rowloc;
    int value;
    dir dir;
    move move;
    String path;
    boolean hasBashed;

    public Coordinate() {
        this.dir = dir.N;
        this.move = move.CURRENT;
        this.hasBashed = false;
        this.path = "";
    }

    public void setColumnloc(int columnloc) {
        this.columnloc = columnloc;
    }

    public void setRowloc(int rowloc) {
        this.rowloc = rowloc;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDir(dir dir) {
        this.dir = dir;
    }

    public void setPath(String string) {
        this.path = string;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        return columnloc == other.columnloc && rowloc == other.rowloc;
    }

    /**
     * Get the current move as a string
     * @return The value of the current move as a string
     */
    public String toString() {
        String move = "";
        switch (this.move) {
            case FORWARD:
                move = "FORWARD";
                break;
            case BASH:
                move = "BASH";
                break;
            case LEFT:
                move = "LEFT";
                break;
            case RIGHT:
                move = "RIGHT";
                break;
        }
        return move;
    }

    /**
     * Change the direction on a left turn
     * @return The new direction
     */
    public Coordinate turnLeft() {
        this.move = move.LEFT;
        switch (this.dir) {
            case N:
                this.dir = dir.W;
                break;
            case S:
                this.dir = dir.E;
                break;
            case E:
                this.dir = dir.N;
                break;
            case W:
                this.dir = dir.S;
                break;
        }
        return this;
    }

    /**
     * Change the direction on a right turn
     * @return The new direction
     */
    public Coordinate turnRight() {
        this.move = move.RIGHT;
        switch (this.dir) {
            case N:
                this.dir = dir.E;
                break;
            case S:
                this.dir = dir.W;
                break;
            case E:
                this.dir = dir.S;
                break;
            case W:
                this.dir = dir.N;
                break;
        }
        return this;
    }

    /**
     * Create a coordinate for a forward move
     * @param b The board
     * @return A new coordinate that has moved forward
     */
    public Coordinate forward(Board b) {
        Coordinate coord = new Coordinate();
        coord.move = move.FORWARD;
        switch (this.dir) {
            case N:
                coord.dir = this.dir;
                coord.value = b.board[this.rowloc - 1][this.columnloc];
                coord.rowloc = this.rowloc - 1;
                coord.columnloc = this.columnloc;
                break;
            case S:
                coord.dir = this.dir;
                coord.value = b.board[this.rowloc + 1][this.columnloc];
                coord.rowloc = this.rowloc + 1;
                coord.columnloc = this.columnloc;
                break;
            case E:
                coord.dir = this.dir;
                coord.value = b.board[this.rowloc][this.columnloc + 1];
                coord.columnloc = this.columnloc + 1;
                coord.rowloc = this.rowloc;
                break;
            case W:
                coord.dir = this.dir;
                coord.value = b.board[this.rowloc][this.columnloc - 1];
                coord.columnloc = this.columnloc - 1;
                coord.rowloc = this.rowloc;
                break;
        }
        return coord;
    }

    /**
     * Create a coordinate for a bash move
     * @param b The board
     * @return A new coordinate that has bashed
     */
    public Coordinate bash(Board b) {
        this.move = move.BASH;
        switch (this.dir) {
            case N:
                this.value = b.board[this.rowloc - 1][this.columnloc];
                this.rowloc--;
                break;
            case S:
                this.value = b.board[this.rowloc + 1][this.columnloc];
                this.rowloc++;
                break;
            case E:
                this.value = b.board[this.rowloc][this.columnloc + 1];
                this.columnloc++;
                break;
            case W:
                this.value = b.board[this.rowloc][this.columnloc - 1];
                this.columnloc--;
                break;
        }
        this.hasBashed = true;
        return this;
    }
}
