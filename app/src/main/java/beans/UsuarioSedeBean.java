package beans;

public class UsuarioSedeBean {

    private String ID_USUARIO;
    private String ID_SEDE;
    private String ID_ROL;
    private String NOM_SEDE;

    public UsuarioSedeBean() {
    }

    public String getNOM_SEDE() {
        return NOM_SEDE;
    }

    public void setNOM_SEDE(String NOM_SEDE) {
        this.NOM_SEDE = NOM_SEDE;
    }

    public String getID_USUARIO() {
        return ID_USUARIO;
    }

    public void setID_USUARIO(String ID_USUARIO) {
        this.ID_USUARIO = ID_USUARIO;
    }

    public String getID_SEDE() {
        return ID_SEDE;
    }

    public void setID_SEDE(String ID_SEDE) {
        this.ID_SEDE = ID_SEDE;
    }

    public String getID_ROL() {
        return ID_ROL;
    }

    public void setID_ROL(String ID_ROL) {
        this.ID_ROL = ID_ROL;
    }
}
