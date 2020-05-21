package beans;

import java.util.Date;

public class SedeBean {

    private String ID;
    private String NOMBRE_SEDE;
    private String ID_EMPRESA;
    private String DIRECCION;
    private String ID_DISTRITO;
    private String LATITUD;
    private String LONGITUD;
    private String FEC_CREACION;
    private String FEC_ACTUALIZACION;
    private String FEC_ELIMINACION;

    public SedeBean() {
    }

    public String getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(String LATITUD) {
        this.LATITUD = LATITUD;
    }

    public String getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(String LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getFEC_CREACION() {
        return FEC_CREACION;
    }

    public void setFEC_CREACION(String FEC_CREACION) {
        this.FEC_CREACION = FEC_CREACION;
    }

    public String getFEC_ACTUALIZACION() {
        return FEC_ACTUALIZACION;
    }

    public void setFEC_ACTUALIZACION(String FEC_ACTUALIZCION) {
        this.FEC_ACTUALIZACION = FEC_ACTUALIZCION;
    }

    public String getFEC_ELIMINACION() {
        return FEC_ELIMINACION;
    }

    public void setFEC_ELIMINACION(String FEC_ELIMINACION) {
        this.FEC_ELIMINACION = FEC_ELIMINACION;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNOMBRE_SEDE() {
        return NOMBRE_SEDE;
    }

    public void setNOMBRE_SEDE(String NOMBRE_SEDE) {
        this.NOMBRE_SEDE = NOMBRE_SEDE;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getID_EMPRESA() {
        return ID_EMPRESA;
    }

    public void setID_EMPRESA(String ID_EMPRESA) {
        this.ID_EMPRESA = ID_EMPRESA;
    }

    public String getID_DISTRITO() {
        return ID_DISTRITO;
    }

    public void setID_DISTRITO(String ID_DISTRITO) {
        this.ID_DISTRITO = ID_DISTRITO;
    }
}
