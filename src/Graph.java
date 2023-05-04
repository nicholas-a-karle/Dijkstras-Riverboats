
public class Graph {

    double[][] matrix;
    int numNodes;

    public Graph(double[][] matrix) {
        
        numNodes = matrix.length;
        for (int i = 0; i < matrix.length; ++i) {
            if (numNodes < matrix[i].length) numNodes = matrix[i].length;
        }

        this.matrix = new double[numNodes][numNodes];

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    /*
    // getters
    public int getNumNodes() { return numNodes; }
    public double[][] getMatrix() { return matrix; }
    public double getEdge(int strt, int end) { return matrix[strt][end]; }
    public double[] getEdgeArray(int strt) { return matrix[strt]; }
    public double[] getEdgeList(int strt) {
        int len = 0;
        for (int i = 0; i < numNodes; ++i) {
            if (matrix[strt][i] > 0) ++len;
        }

        double[] arr = new double[len];
        len = 0;
        for (int i = 0; i < numNodes; ++i) {
            if (matrix[strt][i] > 0) {
                arr[len] = matrix[strt][i];
                ++len;
            }
        }
        
        return arr;
    }

    // setters
    public void setMatrix(double[][] matrix) {
        numNodes = matrix.length;
        for (int i = 0; i < matrix.length; ++i) {
            if (numNodes < matrix[i].length) numNodes = matrix[i].length;
        }

        this.matrix = new double[numNodes][numNodes];

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }
    public double setEdge(int strt, int end, double edge) {
        double tmp = matrix[strt][end];
        this.matrix[strt][end] = edge;
        return tmp;
    }
    public double popEdge(int strt, int end) {
        double tmp = matrix[strt][end];
        this.matrix[strt][end] = 0;
        return tmp;
    }
    */
}
