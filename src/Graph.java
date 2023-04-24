
public class Graph {

    double[][] matrix;
    int num_nodes;

    public Graph(double[][] matrix) {
        
        num_nodes = matrix.length;
        for (int i = 0; i < matrix.length; ++i) {
            if (num_nodes < matrix[i].length) num_nodes = matrix[i].length;
        }

        this.matrix = new double[num_nodes][num_nodes];

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    /**
     * Return the shortest path between a and b
     * Using Dijkstra's Algorithm
     * Works only for positive weights and non-edges (weight[i][j] >= 0 for all i and j)
     * @param a
     * @return
     */
    public Pair<int[], double[]> pathfind(int a) {
        int num_visited = 1; // already visited first
        Pair<int[], double[]> paths = new Pair<int[], double[]>(new int[num_nodes], new double[num_nodes]);
        for (int i = 0; i < num_nodes; ++i) {
            paths.key[i] = -1; //indicate lack of path to
            paths.val[i] = -1; //indicate infinity
        }
        paths.val[0] = 0; //indicate start

        int cur = 0;
        int nxt = 0;

        //if all visited, cut
        //if no more pahts, cut
        while (num_visited < num_nodes && cur >= 0) {
            //check each edge of cur
            //compare paths.val[i] to paths.val[cur] + matrix[cur][i]
            //if second is lesser, replace paths.val[i] with it and paths.key[i] with cur
            for (int i = 0; i < num_nodes; ++i) {
                if (matrix[cur][i] != 0) {
                    //indicates an edge of cur
                    //if no path found yet, create
                    if (paths.val[i] == -1 || (paths.val[i] > paths.val[cur] + matrix[cur][i])) {
                        paths.key[i] = cur;
                        paths.val[i] = paths.val[cur] + matrix[cur][i];
                    }
                }
            }
            nxt = -1;

            //check all extant paths for minimum, that is the true minimum of that node
            for (int i = 0; i < num_nodes; ++i) {
                if (paths.val[i] >= 0 && i != cur) {
                    //a path has been discovered and its not the current node
                    if (nxt == -1 || paths.val[nxt] > paths.val[i]) {
                        nxt = i;
                    }
                }
            }

            cur = nxt;
            ++num_visited;
        }        
        return paths;
    }

    public void pathfindBF() {
        boolean[] arr = new boolean[num_nodes];
        boolean[] tmp;
        for (int i = 0; i < arr.length; ++i) arr[i] = true;

        for (int i = 0; i < arr.length; ++i) {
            for (int j = 1; j < arr.length - 1; ++j) {
                tmp = arr;
                tmp[i] = !tmp[i];
                if (compPFBF(arr, tmp, i)) arr = tmp;
            }
        }
    }

    //return true if next is better
    private boolean compPFBF(boolean[] old, boolean[] nxt, int i) {
        //cases 
        /*
        nxt[i] = true, old[i] = false

        j     i     k    j     i     k
        1..0..1..0..1 vs 1..0..0..0..1

        C[j][i] + C[i][k] vs. C[j][k]
        
        nxt[i] = false, old[i] = true
        1...0...1 vs 1...1...1
        reverse of above, can compute and flip as such: return (nxt[i]) ? bool : !bool;
        */

        //find j and k
        int j = i - 1;
        int k = i + 1;

        while (j > 1 && !old[j]) --j;
        while (k < old.length - 1 && !old[k]) ++k;

        if (nxt[i]) {
            return matrix[j][i] + matrix[i][k] < matrix[j][k];
        } else {
            return matrix[j][i] + matrix[i][k] > matrix[j][k];
        }
    }
}
