import java.io.IOException;
//import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import islandGenerator.islandGen;

import java.io.IOException;
import java.util.Objects;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
public class Main {

    public static void main(String[] args) throws IOException {
        MeshFactory factory = new MeshFactory();


        //extract command line arguments
        String input = args[0];
        String output = args[1];


        Structs.Mesh aMesh = factory.read(input);
        islandGen generator = new islandGen();
        Structs.Mesh myMesh = generator.generate(aMesh);
        factory.write(myMesh, output);
    }
}
