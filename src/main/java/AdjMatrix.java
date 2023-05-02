import org.javatuples.Triplet;

import java.util.*;

public class AdjMatrix extends AdjStruct {
    private List<Vertex> vertices;
    private List<List<Edge>> edges;
    private List<Vertex> closeList;

    public AdjMatrix(List<Vertex> vertices, List<Triplet<String, String, String>> edges){
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            this.edges.add(i, new ArrayList<>(Collections.nCopies(vertices.size(), null)));
        }

        for (Triplet<String, String, String> edge: edges) {
            String row = edge.getValue0();
            String col = edge.getValue1();
            int rowIndex = vertices.indexOf(new Vertex(row));
            int colIndex = vertices.indexOf(new Vertex(col));

            this.edges.get(rowIndex).set(colIndex, new Edge(Float.parseFloat(edge.getValue2())));
        }
    }


    @Override
    public List<Vertex> getNeighbors(Vertex vertex){
        List<Vertex> neighbors = new LinkedList<Vertex>();
        int vertexIndex = vertices.indexOf(vertex);

        for(int i = 0; i < edges.size(); i++){
            if(edges.get(vertexIndex).get(i) != null){
                neighbors.add(vertices.get(i));
            }
        }

        return neighbors;
    }

    @Override
    public void print() {
        System.out.println("**+");
        System.out.print(" * |");
        vertices.forEach(vertex -> System.out.print("  " + vertex.getName() + "  |"));
        System.out.println();
        vertices.forEach(vertex -> {
            int vertexIndex = vertices.indexOf(vertex);

            System.out.print("  " + vertex.getName() + "  |");
            edges.get(vertexIndex).forEach(edge -> {
                if (edge == null) {
                    System.out.print(" -x- |");
                } else if (edge.getWeight() < 10) {
                    System.out.print("  " + edge.getWeight() + "|");
                } else {
                    System.out.print(" " + edge.getWeight() + "|");
                }
            });
            System.out.println();
        });
        System.out.println("**+");
    }

    @Override
    public void traverse(TraversalType traversalType) {
        closeList = new LinkedList<>();
        switch (traversalType) {
            case RECURSIVE:
                traverseRecursive(vertices.get(0));
                break;
            case DEPTH_FIRST:
                traverseDepth(vertices.get(0));
                break;
            case BREADTH_FIRST:
                traverseBreadth(vertices.get(0));
                break;

        }
        System.out.println(closeList.toString());
    }

    private void traverseBreadth(Vertex v){
         List<Vertex> openList = new ArrayList<>();
         openList.add(v);
         while(!openList.isEmpty()) {
             Vertex currentVertex = openList.remove(0);
             if (!closeList.contains(currentVertex)) {
                 closeList.add(currentVertex);
                 List<Vertex> neighbors = getNeighbors(currentVertex);
                 openList.addAll(neighbors);
             }
         }
    }

    private void traverseDepth(Vertex v){
        Stack<Vertex> openList = new Stack<>();
        openList.add(v);
        while(!openList.isEmpty()){
            Vertex currentVertex = openList.pop();
            if(!closeList.contains(currentVertex)){
                closeList.add(currentVertex);
                List<Vertex> neighbors = getNeighbors(currentVertex);
                for (Vertex neighbor:neighbors) {
                    openList.push(neighbor);
                }
            }
        }
    }

    private void traverseRecursive(Vertex v){
        if(closeList.contains(v)){
            return;
        }
        closeList.add(v);
        for (int i = 0; i < getNeighbors(v).size(); i++){
            traverseRecursive(getNeighbors(v).get(i));
        }
    }
}
