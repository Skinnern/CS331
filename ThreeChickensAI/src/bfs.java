import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by Samuel on 4/19/2018.
 */
public class bfs {

    public static void solve(int[] startState) {
        ArrayList<String> endPath = new ArrayList<>();
        LinkedList<state> queue = new LinkedList<>();
        Hashtable<String, state> table = new Hashtable<>();
        int expansions = 0;

        //Setup initial state
        if(startState.length != 6) {
            System.out.println("Invalid start state array for BFS solver!");
            return;
        }

        state initial = new state(null, startState[0], startState[1], startState[2], startState[3], startState[4], startState[5],"initial");
        System.out.println("BFS solver starting from state: " + initial);

        //Place initial state in queue
        queue.add(initial);

        //while loop vars
        state current, child;

        while(!queue.isEmpty()) {
            //pop next state
            current = queue.poll();
            System.out.println("Examining: " + current);

            //check if goal state
            if( current.checkDone() ) {
                //get endpath
                endPath = current.getPath();
                break;
            }

            //create children from actions, then place on queue
            //take 1 chicken
            child = current.cross("1c",1,0);
            if(checkState(child, table)) {
                queue.add(child);
                table.put(child.toString(), child);
            }

            //2 chicken
            child = current.cross("2c",2,0);
            if(checkState(child, table)) {
                queue.add(child);
                table.put(child.toString(), child);
            }

            //1 wolf
            child = current.cross("1w",0,1);
            if(checkState(child, table)) {
                queue.add(child);
                table.put(child.toString(), child);
            }

            //1 chicken 1 wolf
            child = current.cross("1c1w",1,1);
            if(checkState(child, table)) {
                queue.add(child);
                table.put(child.toString(), child);
            }

            //2 wolf
            child = current.cross("2w",0,2);
            if(checkState(child, table)) {
                queue.add(child);
                table.put(child.toString(), child);
            }

            //add to table
            table.put(current.toString(), current);

            //increment expansions
            expansions++;
        }

        //Print results
        System.out.println("---- BFS results ----");
        System.out.println("Final Path: ");
        endPath.forEach(System.out::println); //this might not work on flip servers
        System.out.println("Expansions: " + expansions);
        System.out.println("Path length: " + (endPath.size()-1));
    }

    //Determine if state should be added to queue
    private static boolean checkState(state child, Hashtable hashTable) {

        if (child == null)
            return false;

        if( hashTable.containsKey(child.toString()) ) {
                return false;
        }

        return true;
    }

    //private class for state object
    private static class state {
        private int lChick, rChick, lWolf, rWolf, lBoat, rBoat;
        private String action;
        private state parent;
        private int maxChick, maxWolf;
        private int g;

        public state (state prev, int leftChickens, int leftWolves, int leftBoat,
                      int rightChickens, int rightWolves, int rightBoat, String newAction) {
            parent = prev;
            lChick = leftChickens;
            lWolf = leftWolves;
            lBoat = leftBoat;
            rChick = rightChickens;
            rWolf = rightWolves;
            rBoat = rightBoat;
            action = newAction;

            //Setup g (or depth)
            if(parent != null)
                g = parent.g + 1;
            else
                g = 0;

            //Get maximums
            if(parent == null) {
                //if initial node, calculate
                maxChick = (leftChickens + rightChickens);
                maxWolf = (leftWolves + rightWolves);
            }
            else {
                //otherwise get from parent
                maxChick = parent.maxChick;
                maxWolf = parent.maxWolf;
            }
        }

        //Check if failure or invalid state
        public boolean isValid() {
            //check invalid states
            if( this.lChick > maxChick || this.lChick < 0  ||
                    this.lWolf > maxWolf || this.lWolf < 0 ||
                    this.rChick > maxChick || this.rChick < 0 ||
                    this.rWolf > maxWolf || this.rWolf < 0 ||
                    this.lBoat > 1 || this.lBoat < 0 ||
                    this.rBoat > 1 || this.rBoat < 0 ||
                    ((this.lWolf > this.lChick) && lChick != 0) ||
                    ((this.rWolf > this.rChick) && rChick != 0)) {
                return false;
            }
            //check for repeat action (parent opposite)
            if( this.action.equals(this.parent.action)) {
                return false;
            }
            return true;
        }

        //Take an action
        public state cross(String newAction, int chicken, int wolf) {
            state child;

            //Determine direction and create new state
            if(this.lBoat == 0) {
                //moving right to left
                child = new state(this, this.lChick + chicken, this.lWolf + wolf, 1,
                        this.rChick - chicken, this.rWolf - wolf, 0, newAction);
            }
            else {
                //moving left to right
                child = new state(this, this.lChick - chicken, this.lWolf - wolf, 0,
                        this.rChick + chicken, this.rWolf + wolf, 1, newAction);
            }

            //if invalid, return null
            if( !child.isValid() )
                return null;

            return child;
        }

        public boolean checkDone() {
            //check victory state
            if( this.lChick == maxChick && this.lWolf == maxWolf) {
                return true;
            }
            return false;
        }

        public ArrayList<String> getPath() {
            state node = this;
            ArrayList<String> result = new ArrayList<>();
            while(node.parent != null) {
                result.add(0, node.toString());
                node = node.parent;
            }

            //add final
            if(node != null)
                result.add(0, node.toString());

            return result;
        }

        public String toString() {
            String result;
            result = (
                    String.valueOf(lChick) + "," +
                            String.valueOf(lWolf) + "," +
                            String.valueOf(lBoat) + "," +
                            String.valueOf(rChick) + "," +
                            String.valueOf(rWolf) + "," +
                            String.valueOf(rBoat)
            );

            return result;
        }
    }
}
