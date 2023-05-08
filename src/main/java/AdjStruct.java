import java.util.List;

public abstract class AdjStruct {

    public abstract void findEulerPath(Vertex v);
    public abstract void traverse(TraversalType traversalType);
    public abstract void print();
    public abstract List<Vertex> getNeighbors(Vertex vertex);
}
