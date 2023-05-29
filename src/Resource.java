public class Resource extends Node {
    private Task usedBy = null;

    public Resource(String name) {
        super(name);
    }

    /**
     * visszaadja az eroforrast hasznalo taskot
     * @return
     */
    public Task getUsedBy() {
        return usedBy;
    }

    /**
     * felszabaditja az eroforrast
     */
    @Override
    public void removeUser() {
        usedBy = null;
    }


    /**
     * lefoglalja az eroforrast, ha az szabad
     * @param task foglalo task
     * @return boolean, sikerult-e
     */
    public boolean addUser(Task task) {
        if (usedBy == null) {
            usedBy = task;
            return true;
        }
        return false;
        // TODO mi tortenik ellenkezo esetben
    }



    @Override
    public boolean isOccupied() {
        return usedBy == null;
    }
}
