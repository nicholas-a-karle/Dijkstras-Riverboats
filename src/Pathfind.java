import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JTextArea;

public class Pathfind {

    /**
     * Return the shortest path between a and b
     * Using Dijkstra's Algorithm
     * Works only for positive weights and non-edges (weight[i][j] >= 0 for all i and j)
     * @param a
     * @return
     */
    public static Pair<int[], double[]> dijkstras(Graph g) {
        int numVisited = 1; // already visited first
        Pair<int[], double[]> paths = new Pair<int[], double[]>(new int[g.numNodes], new double[g.numNodes]);
        for (int i = 0; i < g.numNodes; ++i) {
            paths.key[i] = -1; //indicate lack of path to
            paths.val[i] = Double.MAX_VALUE; //indicate infinity
        }
        paths.val[0] = 0; //indicate start

        int cur = 0;
        int nxt = 0;

        //if all visited, cut
        //if no more pahts, cut
        while (numVisited < g.numNodes && cur >= 0) {
            //check each edge of cur
            //compare paths.val[i] to paths.val[cur] + g.matrix[cur][i]
            //if second is lesser, replace paths.val[i] with it and paths.key[i] with cur
            for (int i = 0; i < g.numNodes; ++i) {
                if (g.matrix[cur][i] != 0) {
                    //indicates an edge of cur
                    if ((paths.val[i] > paths.val[cur] + g.matrix[cur][i])) {
                        paths.key[i] = cur;
                        paths.val[i] = paths.val[cur] + g.matrix[cur][i];
                    }
                }
            }
            nxt = -1;

            //check all extant paths for minimum, that is the true minimum of that node
            for (int i = 0; i < g.numNodes; ++i) {
                if (paths.val[i] >= 0 && i != cur) {
                    //a path has been discovered and its not the current node
                    if (nxt == -1 || paths.val[nxt] > paths.val[i]) {
                        nxt = i;
                    }
                }
            }
 
            cur = nxt;
            ++numVisited;
        }        
        return paths;
    }

    public static void output(Pair<int[], double[]> paths, PrintStream ps, JTextArea a, File f) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(f));

        String tmp = "";
        tmp += " |\tNode \t|\tPred. \t|\tDist.\t|\n";
        for (int i = 0; i < paths.key.length; ++i) {
            if (paths.key[i] == -1) {
                tmp += " |\t" + i + " \t|\tNone \t|\t" + paths.val[i] + " \t|\n";
            } else {
                tmp += " |\t" + i + " \t|\t" + paths.key[i] + " \t|\t" + paths.val[i] + " \t|\n";
            }
        }

        ps.print(tmp);
        w.write(tmp);
        a.setText(tmp);

        w.close();
    }   
}