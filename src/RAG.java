import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashSet;

public class RAG {
    private ArrayList<Node> nodes = new ArrayList<>();
    private int numberOfTasks = 0;

    /**
     * uj nodeot ad hozza
     * @param node uj node
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * visszaadja a nodeot nev alapjan
     * @param str keresett nev
     * @return adott nevu node
     */
    public Node getNodeByName(String str) {
        for (Node node : nodes) {
            if (Objects.equals(node.getName(), str))
                return node;
        }
        return null;
    }

    private boolean detectDeadlockHelper(Node currentNode, HashSet<Node> visitedNodes) {
        if (currentNode instanceof Resource) {
            Node nextNode = ((Resource) currentNode).getUsedBy();
            if (nextNode != null) {
                if (visitedNodes.contains(nextNode)) {
                    System.out.println(currentNode.getName() + " " + nextNode.getName());
                    return true;  // Cycle detected
                } else {
                    visitedNodes.add(nextNode);
                    return detectDeadlockHelper(nextNode, visitedNodes);
                }
            }
        } else if (currentNode instanceof Task) {
            for (Node requestedNode : ((Task) currentNode).getRequested()) {
                if (!visitedNodes.contains(requestedNode)) {
                    visitedNodes.add(requestedNode);
                    if (detectDeadlockHelper(requestedNode, visitedNodes)) {
                        System.out.println(currentNode.getName() + " " + requestedNode.getName());
                        return true;  // Cycle detected
                    }
                }
                else {
                    System.out.println(currentNode.getName() + " " + requestedNode.getName());
                }
            }
        }

        return false;  // No deadlock detected
    }

    public boolean detectDeadlock(Resource startingNode) {
        HashSet<Node> visitedNodes = new HashSet<>();
        visitedNodes.add(startingNode);

        return detectDeadlockHelper(startingNode, visitedNodes);
        /*
        //TODO korkereso algoritmus
        visitedNodes.add(startingNode);
        Node nextNode = null;
        Node currentNode = startingNode;

        for (int i = 0; i < 2; i++) {
            // ha paros akkor resourceon allunk
            if (i == 0) {
                // ha letezik kovetkezo node
                nextNode = ((Resource)currentNode).getUsedBy();
                if (nextNode != null) {
                    // ha a kovi nodeon mar jartunk => kor van
                    if (visitedNodes.contains(nextNode)) {
                        System.out.println(currentNode.getName() + " " + nextNode.getName());
                        return true;
                    }
                    // ha a kovi nodeon meg nem jartunk
                    else {
                        visitedNodes.add(currentNode);
                        currentNode = nextNode;
                    }
                }
                else return false;
            }
            // ha paratlan akkor task
            else {
                // a task minden requestelt resourcejara lefuttatjuk az eddigieket rekurzivan
                boolean hasCircle = false;
                for (Node node : ((Task) currentNode).getRequested()) {
                    nextNode = node;
                    if (detectDeadlock((Resource) nextNode, visitedNodes)) {
                        System.out.println(currentNode.getName() + " " + nextNode.getName());
                        hasCircle = true;
                    }
                }
                visitedNodes.add(currentNode);
                return hasCircle;
            }
        }
        return false;

         */
    }

    public void execute() {
        boolean finished = false;

        while (!finished) {
            for (int i = 0; i < numberOfTasks; i++) {
                Node node = nodes.get(i);
                String inst = ((Task) node).executeNext();
                if (inst == null) {
                    nodes.remove((Task)node);
                    numberOfTasks--;
                    if (numberOfTasks == 0) {
                        finished = true;
                        break;
                    }
                }
                else {
                    if (inst.contains("+R")) {
                        inst = inst.substring(1);
                        // ha letezik
                        if (resourceExists(inst)) {
                            Resource res = (Resource) getNodeByName(inst);
                            // ha szabad
                            if (res.isOccupied()) {
                                res.addUser((Task) node);
                            }
                            // ha foglalt
                            else {
                                // ha deadlock lenne miatta
                                ((Task) node).addRequest(res);
                                ArrayList<Node> visitedNodes = new ArrayList<>();
                                if (detectDeadlock(res)) {
                                    ((Task) node).removeRequest(res);
                                }
                            }
                        }
                        // ha nem letezik
                        else {
                            addResource(inst);
                            Resource res = (Resource) getNodeByName(inst);
                            res.addUser((Task) node);
                        }
                    } else if (inst.contains("-R")) {
                        inst = inst.substring(1);
                        // ha felszabaditas tortenik
                        Task t = ((Resource) getNodeByName(inst)).getUsedBy();
                        // ha van hasznalo
                        if (t != null) {
                            if (t.getName().equals(node.getName())) {
                                // ha a hasznalo es a felszabadito ugyanaz
                                getNodeByName(inst).removeUser();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * returns true ha letezik a resource
     * @param resourceName letezik e
     * @return bool
     */
    public boolean resourceExists(String resourceName) {
        for (Node node :
                nodes) {
            if (node.getName().equals(resourceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * hozzaadja a taskokat a grafhoz a bemenet alapjan
     * @param inputLinesParsed
     */
    public void addTasks(ArrayList<ArrayList<String>> inputLinesParsed) {
        // felbontott bementbol a graf nodejainak eloallitasa
        for (ArrayList<String> list : inputLinesParsed) {
            // minden sorbol letrehozzuk a taskot
            String taskName = list.get(0);
            list.remove(0);
            nodes.add(new Task(taskName, list));
            numberOfTasks++;
        }
    }

    /**
     * trueval ter vissza ha mar letezik az eroforras
     * @param res uj resource neve
     * @return boolean
     */
    public boolean addResource(String res) {
        for (Node node : nodes) {
            if (Objects.equals(node.getName(), res)) {
                return true;
            }
        }
        nodes.add(new Resource(res));
        return false;
    }
}
