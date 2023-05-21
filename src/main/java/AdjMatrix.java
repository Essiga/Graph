import org.javatuples.Triplet;

import java.util.*;

public class AdjMatrix extends AdjStruct {
    private List<Vertex> vertices;
    private List<List<Edge>> edges;
    private List<Vertex> closeList;
    private int edgeCount = 0;

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

            this.edges.get(rowIndex).set(colIndex, new Edge(Float.parseFloat(edge.getValue2()), new Vertex(row), new Vertex(col)));
            edgeCount++;
        }
    }


    public LinkedList<Edge> getAllEdges(){
        LinkedList<Edge> results = new LinkedList<>();

        for (List<Edge> edgeList:edges) {
            for (Edge edge:edgeList) {
                if(edge != null){
                    results.add(edge);
                }
            }
        }

        return results;
    }

    public void kruskal(){
        LinkedList<Edge> edges = getAllEdges();
        edges.sort(Comparator.comparing(Edge::getWeight));
        List<Edge> minimalEdges = new LinkedList<>();
       // Map<Vertex, Set<Vertex>> vertexSets = new HashMap<>();
        List<Set<Vertex>> vertexList = new LinkedList<>();
        for (Vertex vertex:vertices) {
            Set<Vertex> set = new HashSet<>();
            set.add(vertex);
            vertexList.add(set);
        }
        while (!edges.isEmpty()) {
            Edge edge = edges.poll();
            if (!minimalEdges.contains(edge)){
                boolean alreadyPresent = false;
                for (Set<Vertex> vertexSet:vertexList) {
                    if(vertexSet.contains(edge.getTo()) && vertexSet.contains(edge.getFrom())){
                       alreadyPresent = true;
                    }
                }

                if(!alreadyPresent){
                    minimalEdges.add(edge);
                    Set<Vertex> fromSet = new HashSet<>();
                    Set<Vertex> toSet = new HashSet<>();
                    for (Set<Vertex> vertexSet:vertexList) {
                        if(vertexSet.contains(edge.getFrom())){
                            fromSet = vertexSet;
                        }
                        if(vertexSet.contains(edge.getTo())){
                            toSet = vertexSet;
                        }
                    }
                    vertexList.remove(toSet);
                    vertexList.remove(fromSet);
                    fromSet.addAll(toSet);
                    vertexList.add(fromSet);
                }
            }
        }
        System.out.println(minimalEdges);
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

    public List<Edge> getEdges(Vertex vertex){
        return edges.get(vertices.indexOf(vertex));
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

    public void dijkstra2(Vertex currentVertex){
        HashMap<Vertex, Float> vertexValues = new HashMap<>();
        //Map Vertex Vorgängerknoten beim eintragen vorgängerknoten miteintragen
        HashMap<Vertex, Vertex> parentVertex = new HashMap<>();
        List<Vertex> markedVertices = new LinkedList<>();
        for (Vertex v: vertices) {
            vertexValues.put(v, Float.MAX_VALUE);
        }
        vertexValues.put(currentVertex, 0f);

        while(markedVertices.size() < vertices.size()){

            Map.Entry<Vertex, Float> minimalEntry = null;
            for (Map.Entry<Vertex, Float> entry:vertexValues.entrySet()) {
                if(!markedVertices.contains(entry.getKey())){
                    if(minimalEntry == null){
                        minimalEntry = entry;
                    }
                    if(minimalEntry.getValue() > entry.getValue()){
                        minimalEntry = entry;
                    }
                }
            }
            markedVertices.add(minimalEntry.getKey());
            currentVertex = minimalEntry.getKey();

            List<Edge> edges = getEdges(currentVertex);
            for (Edge edge:edges) {
                if(edge != null && !markedVertices.contains(edge.getTo())){
                    if(vertexValues.get(currentVertex) + edge.getWeight() < vertexValues.get(edge.getTo())){
                        vertexValues.put(edge.getTo(), vertexValues.get(currentVertex) + edge.getWeight());
                        parentVertex.put(edge.getTo(), currentVertex);
                    }
                }
            }
        }
        System.out.println(parentVertex);
    }

    public void dijkstra(Vertex startVertex){
        HashMap<Vertex, Float> vertexValues = new HashMap<>();
        HashMap<Vertex, Vertex> parentChild = new HashMap<>();

        for(Vertex v : vertices){
            vertexValues.put(v, Float.MAX_VALUE);
        }
        vertexValues.put(startVertex, 0f);
        List<Vertex> unvisitedVertices = new LinkedList<>(vertices);
        while(unvisitedVertices.size() > 0){
            Vertex minimalVertex = getMinimalVertex(vertexValues, unvisitedVertices);
            unvisitedVertices.remove(minimalVertex);
            Vertex currentVertex = minimalVertex;

            List<Edge> edges = getEdges(currentVertex);

            for (Edge edge:edges){
                if(edge != null && unvisitedVertices.contains(edge.getTo())){
                    if(vertexValues.get(currentVertex) + edge.getWeight() < vertexValues.get(edge.getTo())){
                        vertexValues.put(edge.getTo(), vertexValues.get(currentVertex) + edge.getWeight());
                        parentChild.put(edge.getTo(), currentVertex);
                    }
                }
            }

        }
        System.out.println(parentChild);
    }

    public Vertex getMinimalVertex(HashMap<Vertex, Float> vertexValues, List<Vertex> unvisitedVertices){
        Float minValue = Float.MAX_VALUE;
        Vertex result = null;
        for(Map.Entry<Vertex, Float> e : vertexValues.entrySet()){
            if(unvisitedVertices.contains(e.getKey()) && e.getValue() <= minValue){
                minValue = e.getValue();
                result = e.getKey();
            }
        }
        return result;
    }

//    //niedrigste kante finden und danach einen der knoten auswählen und dijkstra machen
//    public void prim(Vertex currentVertex){
//        HashMap<Edge, Float> edgeValues = new HashMap<>();
//        HashMap<Vertex, Vertex> parentVertex = new HashMap<>();
//        List<Vertex> markedVertices = new LinkedList<>();
//        for (Edge e: edges) {
//            edgeValues.put(e, Float.MAX_VALUE);
//        }
//        edgeValues.put(currentVertex, 0f);
//
//        while(markedVertices.size() < vertices.size()){
//
//            Map.Entry<Vertex, Float> minimalEntry = null;
//            for (Map.Entry<Vertex, Float> entry:edgeValues.entrySet()) {
//                if(!markedVertices.contains(entry.getKey())){
//                    if(minimalEntry == null){
//                        minimalEntry = entry;
//                    }
//                    if(minimalEntry.getValue() > entry.getValue()){
//                        minimalEntry = entry;
//                    }
//                }
//            }
//            markedVertices.add(minimalEntry.getKey());
//
//            List<Edge> edges = getEdges(currentVertex);
//            for (Edge edge:edges) {
//                if(edge != null && !markedVertices.contains(edge.getTo())){
//                    if(edge.getWeight() < edgeValues.get(edge.getTo())){
//                        parentVertex.put(currentVertex, edge.getTo());
//                    }
//                }
//            }
//        }
//        System.out.println(parentVertex);
//    }


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

    public boolean hasEulerPath(){
        int oddCount = 0;
        for (Vertex v: vertices) {
            List<Vertex> neighbors = getNeighbors(v);
            if(neighbors.size() % 2 != 0){
                oddCount++;
            }
        }
        if (oddCount > 2){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void findEulerPath(Vertex v){
        List<Edge> visitedEdges = new LinkedList<>();

        findEulerPathRecursive(v, visitedEdges);

    }

    private int getEdgeCount(){
        return edgeCount/2;
    }

    private void findEulerPathRecursive(Vertex currentVertex, List<Edge> visitedEdges){
        if(visitedEdges.size() == (getEdgeCount())){
            System.out.println(visitedEdges);
            System.out.println("end");
            return;
        }

        for (Edge edge: getEdges(currentVertex)) {
            if (!visitedEdges.contains(edge) && edge != null) {
                visitedEdges.add(edge);
                //currentVertex = edge.getFrom().equals(currentVertex) ? edge.getTo() : edge.getFrom();
                findEulerPathRecursive(edge.getTo(), visitedEdges);
                //currentVertex = edge.getFrom().equals(currentVertex) ? edge.getTo() : edge.getFrom();
                visitedEdges.remove(edge);
            }
        }
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
