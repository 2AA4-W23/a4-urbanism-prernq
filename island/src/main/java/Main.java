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
    public static String input;
    public static String output;
    public static String mode;
    public static String shape;
    public static String altitude;
    public static String lakes;
    public static String rivers;
    public static String aquifers;
    public static String soil;
    public static String biomes;
    public static String seed;

    public static void init(){
        input = null;
        output = null;
        mode = null;
        shape = null;
        altitude = null;
        lakes = null;
        rivers = null;
        aquifers = null;
        soil = null;
        biomes = null;
        seed = null;

    }

    public static void main(String[] args) throws IOException {
        Main.init();
        MeshFactory factory = new MeshFactory();

        for (int i = 0; i < args.length; i++){
            String flag;
            String arg;

            if (args[i].startsWith("-")){
                flag = args[i];
                //System.out.println("Flag:"+flag);

                arg = args[i+1];
                System.out.println(arg);

                switch (flag){
                    case "-i":
                        input = arg;
                        break;
                    case "-o":
                        output = arg;
                        break;
                    case "--mode":
                        mode = arg;
                        break;
                    case "--shape":
                        shape = arg;
                    case "--altitude":
                        altitude = arg;
                    case "--lakes":
                        lakes = arg;
                    case "--rivers":
                        rivers = arg;
                    case "--aquifers":
                        aquifers = arg;
                    case "--soil":
                        soil = arg;
                    case "--biomes":
                        biomes = arg;
                    case "--seed":
                        seed = arg;
                    default:
                        System.out.println("just passing through");
                }
            }
        }

        Structs.Mesh aMesh = factory.read(input);
        islandGen generator = new islandGen();
        Structs.Mesh myMesh = generator.generate(aMesh, mode, shape, altitude, lakes, rivers, aquifers, soil, biomes, seed);
        factory.write(myMesh, output);
    }
}
