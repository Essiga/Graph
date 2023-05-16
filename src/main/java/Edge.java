import java.util.Objects;

public class Edge {
    private final float weight;
    private final Vertex from;
    private final Vertex to;

    public Edge(float weight, Vertex from, Vertex to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }

    public float getWeight() {
        return weight;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Float.compare(edge.weight, weight) == 0 && ((Objects.equals(from, edge.from) && Objects.equals(to, edge.to)) || (Objects.equals(to, edge.from) && Objects.equals(from, edge.to)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, from, to);
    }

    @Override
    public String toString() {
        return from +
                "->" + to;
    }
}
