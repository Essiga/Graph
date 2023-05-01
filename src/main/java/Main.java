import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Graph graph = Graph.create(new File("src/matrix.txt"));

        Vertex v = new Vertex("C");

        List<Vertex> neighbors= graph.getNeighbors(v);

        for (Vertex neighbor: neighbors) {
            System.out.println(neighbor.getName());
        }


    }
}
