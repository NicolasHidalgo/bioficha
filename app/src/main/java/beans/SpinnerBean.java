package beans;

public class SpinnerBean {

    private int ID;
    private String COD;
    private String VALUE;

    public SpinnerBean(){

    }

    public SpinnerBean(int ID, String VALUE) {
        this.ID = ID;
        this.VALUE = VALUE;
    }

    public String getCOD() {
        return COD;
    }

    public void setCOD(String COD) {
        this.COD = COD;
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

    public String toString() {
        return getVALUE();
    }
}
