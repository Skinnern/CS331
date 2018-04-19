import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;

/**
 * Created by Samuel on 4/17/2018.
 *
 * Example usage in main:
 *
 *  astar search = new astar();
    int testState[] = {0, 0, 0, 3, 3, 1};
    search.solve(testState);
 */
public class astar {

    public static void solve(int[] startState) {
        int expansions = 0;
        stateCompare comparator = new stateCompare();
        PriorityQueue prio = new PriorityQueue(10, comparator);
        Hashtable<String, state> table = new Hashtable<>();
        ArrayList<String> endPath = new ArrayList<>();

        //Setup initial state
        if(startState.length != 6) {
            System.out.println("Invalid start state array for A* solver!");
            return;
        }

        state initial = new state(null, startState[0], startState[1], startState[2], startState[3], startState[4], startState[5],"initial");
        System.out.println("A* solver starting from state: " + initial);

        //put initial state in priority queue
        prio.add(initial);

        //vars for while loop
        state current, child;

        while( !prio.isEmpty() ) {
            //pop next state to check
            current = (state)prio.poll();

            //check for finish
            if( current.checkDone() ) {
                //get endpath
                endPath = current.getPath();
                break;
            }

            //take actions, if new state is valid, add to priority queue based on heuristic
            //take 1 chicken
            child = current.cross("1c",1,0);
            if(checkState(child, table))
                prio.add(child);

            //2 chicken
            child = current.cross("2c",2,0);
            if(checkState(child, table))
                prio.add(child);

            //1 wolf
            child = current.cross("1w",0,1);
            if(checkState(child, table))
                prio.add(child);

            //1 chicken 1 wolf
            child = current.cross("1c1w",1,1);
            if(checkState(child, table))
                prio.add(child);

            //2 wolf
            child = current.cross("2w",0,2);
            if(checkState(child, table))
                prio.add(child);

            //add to closed states
            table.put(current.toString(), current);
            //System.out.println("-------");

            expansions++;
        }

        System.out.println("---- A* results ----");
        System.out.println("Expansions: " + expansions);
        System.out.println("Final Path: ");
        //System.out.println(endPath);
        endPath.forEach(System.out::println); //this might not work on flip servers
    }

    //Determine if state should be added to priority queue
    private static boolean checkState(state child, Hashtable hashTable) {
        state repeat;

        if (child == null)
            return false;

        if( hashTable.containsKey(child.toString())) {
            //Check if this state already exists in table
            repeat = (state)hashTable.get(child.toString());
            //if shallower, we will add it to queue
            if( child.g < repeat.g )
                return true;
            else
                return false;
        }

        return true;
    }

    //private class for state object
    private static class state {
        private int lChick, rChick, lWolf, rWolf, lBoat, rBoat;
        private String action;
        private state parent;
        private int h, g, maxChick, maxWolf;

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

            //Determine h and g
            if(parent != null) {
                h = prev.h + 1; //cost so far
            }
            else {
                h = 0;
            }

            //Heuristic used is number of animals remaining on right side
            g = (rChick + rWolf - 1);
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

    public static class stateCompare implements Comparator<state> {
        public int compare(state first, state second) {
            int priority1 = first.g + first.h;
            int priority2 = second.g + second.h;

            if(priority1 > priority2)
                return 1;
            else if(priority1 < priority2)
                return -1;
            else
                return 0;
        }
    }
}
