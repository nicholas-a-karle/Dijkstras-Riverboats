## Analysis
    This program uses Dijkstra's Algorithm, representing the posted costs of riverboats as a graph
    Dijkstra's algorithm works as such

    0. Initialize Graph g<Nodes, Edges>
    1. Initialize all nodes with a flag UNVISITED (false)
    2. Initialize all nodes except [0] with tag (NONE (-1), INFINITY (representative value))
        a. A tag (A, B) on Node C means that the least-cost path to C ends with edge A->C and has a cost of B
    3. Initialize Node 0 with the tag (NONE, 0)
    4. Initialize the current node as Node 0
    5. Until all nodes are flagged VISITED (true) execute the following
        a. If Current is flagged UNVISITED, flag as VISITED
        b. Check all neighboring nodes of the current node
            i. If replacing the cost of the current path to the neighbor with the path ending with the edge current->neighbor reduces the cost of the path to the neighbor, do it
            aka: If Neighbor.leastPathCost > Current.leastPathCost + EdgeCost(Current -> Neighbor): Replace Neighbor.leastCostPathPredecessor with Current
        c. From all nodes, find the one with the minimum value of leastPathCost, replace current with this node
        d. Go back to 5.a
    6. Return list containing tags with Least Cost and Predecessor

    This algorithm will visit all N nodes, and check all N nodes for each node for both least cost and minimum least cost
    This means the algorithm runs in O(V^2) where V is the number of nodes