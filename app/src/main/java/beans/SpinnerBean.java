package beans;

public class SpinnerBean {

    private int ID;
    private String VALUE;

    public SpinnerBean(int ID, String VALUE) {
        this.ID = ID;
        this.VALUE = VALUE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVALUE() {
        return VALUE;
    }

    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }
}
