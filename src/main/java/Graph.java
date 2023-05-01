import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Graph {
//TODO: Für grafische darstellung nur liste und matrix ausgeben fürs erste (später eventuell library für bäume und graphen)
    private AdjStruct adjStruct;

    public void create(File file) {
        Scanner myReader = null;
        try {
            myReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Vertex<String>> vertices = new LinkedList<>();
        List<Triplet<String, String, String>> triplets = new LinkedList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (data.charAt(0) == 'V') {
                data = data.substring(3, data.length() - 1);
                String[] vertexStrings = data.split(",");
                for (String vertexString : vertexStrings) {
                    vertices.add(new Vertex<String>(vertexString));
                }
            } else if (data.charAt(0) == 'E') {
                data = data.substring(3, data.length() - 1);
                String[] edgeStrings = data.split(";");
                for (String edgeString : edgeStrings) {
                    //remove "()"
                    edgeString = edgeString.substring(0, edgeString.length() - 1);
                    String[] values = edgeString.split(",");
                    triplets.add(new Triplet<String, String, String>(values[0], values[1], values[2]));
                }


            }
        }
    }
}


