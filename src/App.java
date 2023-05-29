import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    RAG graph = new RAG();

    public void readInstructions() {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> inputLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equalsIgnoreCase(""))
                break;
            inputLines.add(line);
        }

        scanner.close();

        // bemenet felbontása instrukciok sorozataira (soronkent)
        ArrayList<ArrayList<String>> inputLinesParsed = new ArrayList<>();
        for (String str : inputLines) {
            str = str.trim();
            ArrayList<String> lineSplit = new ArrayList<>(Arrays.asList(str.split(",")));
            inputLinesParsed.add(lineSplit);
        }

        graph.addTasks(inputLinesParsed);

        graph.execute();
    }




}
