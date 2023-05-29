public abstract class Node {
    private String Name;

    public Node(String name) {
        Name = name;
    }

    /**
     * visszaadja a node nevet
     * @return a node neve
     */
    public String getName() {
        return Name;
    }

    //public abstract String executeNext();
    public abstract boolean isOccupied();
    public abstract void removeUser();
}
