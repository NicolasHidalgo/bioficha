package beans;

public class BioFichaEnfermedadBean {

    private String ID_FICHA;
    private String ID_ENFERMEDAD;
    private String NOM_ENFERMEDAD;

    public BioFichaEnfermedadBean() {
    }

    public String getNOM_ENFERMEDAD() {
        return NOM_ENFERMEDAD;
    }

    public void setNOM_ENFERMEDAD(String NOM_ENFERMEDAD) {
        this.NOM_ENFERMEDAD = NOM_ENFERMEDAD;
    }

    public String getID_FICHA() {
        return ID_FICHA;
    }

    public void setID_FICHA(String ID_FICHA) {
        this.ID_FICHA = ID_FICHA;
    }

    public String getID_ENFERMEDAD() {
        return ID_ENFERMEDAD;
    }

    public void setID_ENFERMEDAD(String ID_ENFERMEDAD) {
        this.ID_ENFERMEDAD = ID_ENFERMEDAD;
    }
}
