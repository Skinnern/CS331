import java.util.Vector;
import java.util.LinkedList;



public class depthFirst {

    public static int goalChickenLeft;
    public static int goalWolfLeft;

    public static void depthSolution(int[] Initial, int[] Goal) {
        //declare initial state
        State initial_state = new State(Initial[3],Initial[4],'L',Initial[0],Initial[1]);

        //bad practice, but declaring global goals of numbers of animals on left shore
        goalChickenLeft=Goal[3];
        goalWolfLeft=Goal[4];
        Node node = solve( initial_state );
        System.out.println();

        //node is null if we have no solution
        if (node == null)
            System.out.println("No solution exists.");
        else {
            System.out.println("The solution is:\n");
            node.RecurBacktracePrint();
        }
    }

    //Static class state
    static class State {
        //chickens and wolves variables for left
        int ChickenLeft, WolfLeft;
        //chickens and wolves variables for right
        int ChickenRight, WolfRight;
        //boat position, 'L' or 'R'
        char boat;

        //states list

        //WolfLeft = Initial Wolves
        //boat = boat state, l or r
        //mr = goal chickens
        //WolfRight = goal wolves
        public State(int ChickenLeft, int WolfLeft, char boat, int ChickenRight, int WolfRight) {
            //ChickenLeft = initial chickens
            this.ChickenLeft = ChickenLeft;
            this.WolfLeft = WolfLeft;
            this.boat = boat;
            this.ChickenRight = ChickenRight;
            this.WolfRight = WolfRight;
        }

        //our end goal for number of animals on left side
        public boolean goal_test() {
            return ChickenLeft == goalChickenLeft && WolfLeft == goalWolfLeft;
        }

        //make a string to print out showing the path
        public String toString() {
            return "(" + ChickenLeft + " " + WolfLeft + " " + boat + " " + ChickenRight + " " + WolfRight + ")";
        }

        //compare our instances of state
        public boolean equals(Object obj) {
            if ( ! (obj instanceof State) )
                return false;
            State s = (State)obj;
            return (s.ChickenLeft == ChickenLeft && s.WolfLeft == WolfLeft && s.boat == boat
                    && s.WolfRight == WolfRight && s.ChickenRight == ChickenRight);
        }



        public Vector successor_function() {
            Vector v = new Vector();
            //check boat position (left or right)
            if (boat == 'L') {
                //System.out.println("Left Shore");
                //actions boat can take if it is on left position
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft-2,WolfLeft,'R',ChickenRight+2,WolfRight),
                        new Action("Two chickens cross from the left to the right.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft,WolfLeft-2,'R',ChickenRight,WolfRight+2),
                        new Action("Two wolves cross from the left to the right.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft-1,WolfLeft-1,'R',ChickenRight+1,WolfRight+1),
                        new Action("One chicken and one wolf cross from the left to the right.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft-1,WolfLeft,'R',ChickenRight+1,WolfRight),
                        new Action("One chicken crosses from the left to the right.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft,WolfLeft-1,'R',ChickenRight,WolfRight+1),
                        new Action("One wolf crosses from the left to the right.")));
            }
            else {
                //actions boat can take if it is on right position
                //System.out.println("Right Shore");
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft+2,WolfLeft,'L',ChickenRight-2,WolfRight),
                        new Action("Two chickens cross from the right to the left.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft,WolfLeft+2,'L',ChickenRight,WolfRight-2),
                        new Action("Two wolves cross from the right to the left.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft+1,WolfLeft+1,'L',ChickenRight-1,WolfRight-1),
                        new Action("One chicken and one wolf cross from the right to the left.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft+1,WolfLeft,'L',ChickenRight-1,WolfRight),
                        new Action("One chicken crosses from the right to the left.")));
                testAndAdd(v,new StateActionPair(
                        new State(ChickenLeft,WolfLeft+1,'L',ChickenRight,WolfRight-1),
                        new Action("One wolf crosses right to left.")));
            }
            return v;
        }


        private void testAndAdd(Vector v, StateActionPair pair) {
            State state = pair.state;
            //System.out.println("Node" + v);
            //System.out.println("pair" + pair);
            if (state.ChickenLeft >= 0 && state.ChickenRight >= 0 && state.WolfLeft >= 0 && state.WolfRight >= 0 && (state.ChickenLeft == 0 || state.ChickenLeft >= state.WolfLeft) && (state.ChickenRight == 0 || state.ChickenRight >= state.WolfRight))
                v.addElement(pair);
        }
    }
    //end class State

    //
    static class Action {
        String text;
        public Action(String text) {
            this.text = text;
        }
        public String toString() {
            return text;
        }
        public double cost() {
            return 1;
        }
    }

    //
    static class StateActionPair {
        public State state;
        public Action action;
        public StateActionPair(State state, Action action) {
            this.state = state;
            this.action = action;
        }
    }

    //base node
    static class Node {
        public State state;
        public Node parent_node;
        public Action action;
        public double path_cost;
        public int depth;
        public Node(State state) {
            this.state = state;
            parent_node = null;
            action = new Action("Initial state");
            path_cost = 0;
            depth = 0;
        }

        //node feature construction
        public Node(State state, Node parent, Action action) {
            this.state = state;
            this.parent_node = parent;
            this.action = action;
            this.path_cost = action.cost() + parent.path_cost;
            this.depth = 1 + parent.depth;
        }

        //print our backtrace, use recursive calls to get to deepest state
        public void RecurBacktracePrint() {
            if (parent_node != null)
                parent_node.RecurBacktracePrint();
            System.out.println("	" + depth + ") " + action + " " + state);
        }
    }

    //trail down nodes, if we have not reached our goal state, then add a successor state.
    public static Node solve(State initial_state) {
        LinkedList solver = new LinkedList();
        Vector visited = new Vector();
        solver.add( new Node(initial_state) );
        while ( true ) {
            if (solver.isEmpty()){
                return null;
            }
            Node node = (Node)solver.removeFirst();
            Vector successors = node.state.successor_function();
            for (int i = 0; i < successors.size(); i++) {
                StateActionPair successor = (StateActionPair)successors.elementAt(i);

                //check vector if we've used state before, if not, add it as a new node
                if ( ! containsState(visited,successor.state) ) {
                    Node newNode = new Node(successor.state,node,successor.action);
                    if (successor.state.goal_test()) {
                        return newNode;
                    }
                    solver.add(newNode);
                    visited.add(successor.state);
                }
            }
        }
    }

    //check our vector if we've used this state before
    public static boolean containsState(Vector visitedStates, State state) {
        for (int i = 0; i < visitedStates.size(); i++) {
            if (visitedStates.elementAt(i).equals(state))
                return true;
        }
        return false;
    }
}