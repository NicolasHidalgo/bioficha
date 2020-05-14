package beans;

import java.util.Date;

public class UsuarioBean {
    private String ID;
    private String NOMBRES;
    private String APELLIDOS;
    private String ID_EMPRESA;
    private String CORREO;
    private String USUARIO;
    private String CONTRASENA;
    private String ID_ROL;
    private String FEC_CREACION;
    private String FEC_ACTUALIZCION;
    private String FEC_ELIMINACION;

    public UsuarioBean() {
    }

    public String getID_EMPRESA() {
        return ID_EMPRESA;
    }

    public void setID_EMPRESA(String ID_EMPRESA) {
        this.ID_EMPRESA = ID_EMPRESA;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNOMBRES() {
        return NOMBRES;
    }

    public void setNOMBRES(String NOMBRES) {
        this.NOMBRES = NOMBRES;
    }

    public String getAPELLIDOS() {
        return APELLIDOS;
    }

    public void setAPELLIDOS(String APELLIDOS) {
        this.APELLIDOS = APELLIDOS;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCONTRASENA() {
        return CONTRASENA;
    }

    public void setCONTRASENA(String CONTRASENA) {
        this.CONTRASENA = CONTRASENA;
    }

    public String getID_ROL() {
        return ID_ROL;
    }

    public void setID_ROL(String ID_ROL) {
        this.ID_ROL = ID_ROL;
    }
}
