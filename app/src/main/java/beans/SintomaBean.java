package beans;

import java.util.Date;

public class SintomaBean {

    private String ID;
    private String DESCRIPCION;
    private String FEC_CREACION;
    private String FEC_ACTUALIZCION;
    private String FEC_ELIMINACION;

    public SintomaBean() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public String getFEC_CREACION() {
        return FEC_CREACION;
    }

    public void setFEC_CREACION(String FEC_CREACION) {
        this.FEC_CREACION = FEC_CREACION;
    }

    public String getFEC_ACTUALIZCION() {
        return FEC_ACTUALIZCION;
    }

    public void setFEC_ACTUALIZCION(String FEC_ACTUALIZCION) {
        this.FEC_ACTUALIZCION = FEC_ACTUALIZCION;
    }

    public String getFEC_ELIMINACION() {
        return FEC_ELIMINACION;
    }

    public void setFEC_ELIMINACION(String FEC_ELIMINACION) {
        this.FEC_ELIMINACION = FEC_ELIMINACION;
    }
}
