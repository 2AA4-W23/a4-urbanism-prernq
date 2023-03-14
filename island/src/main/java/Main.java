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



    public static void main(String[] args) throws IOException {
        MeshFactory factory = new MeshFactory();

        for (int i = 0; i < args.length; i++){
            String flag;
            String arg;

            if (args[i].startsWith("-")){
                flag = args[i];
                System.out.println("Flag:"+flag);

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
                    default:
                        System.out.println("just passing through");

                }

            }


        }

        Structs.Mesh aMesh = factory.read(input);
        islandGen generator = new islandGen();
        Structs.Mesh myMesh = generator.generate(aMesh);
        factory.write(myMesh, output);
    }
}
