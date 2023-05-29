import java.util.ArrayList;
import java.util.Queue;

public class Task extends Node {
    private ArrayList<String> instructions;

    public ArrayList<Resource> getRequested() {
        return requested;
    }

    private ArrayList<Resource> requested = new ArrayList<>();

    public Task(String name, ArrayList<String> list) {
        super(name);
        instructions = list;
    }

    public void removeRequest(Resource resource) {
        requested.remove(resource);
    }

    public void addRequest(Resource resource) {
        requested.add(resource);
    }

    /**
     * visszaadja a kovetkezo instrukciojat a tasknak
     * @return a kovi instrukcio
     */
    public String executeNext() {
        if (instructions.isEmpty()) return null;
        String instruction = instructions.get(0);
        instructions.remove(0);

        return instruction;
    }

    @Override
    public boolean isOccupied() {
        return true;
    }

    @Override
    public void removeUser() {}
}
