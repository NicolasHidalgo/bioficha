package beans;

public class SedePoligonoBean {
    private double LATITUD;
    private double LONGITUD;
    private String ID_SEDE;

    public SedePoligonoBean() {
    }


    public Double getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(Double LATITUD) {
        this.LATITUD = LATITUD;
    }

    public Double getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(Double LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getID_SEDE() {
        return ID_SEDE;
    }

    public void setID_SEDE(String ID_SEDE) {
        this.ID_SEDE = ID_SEDE;
    }
}
