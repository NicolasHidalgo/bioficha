package beans;

import java.util.Date;

public class UsuarioBean {
    private String ID;
    private String ID_TIPO_DOCUMENTO;
    private String NUM_DOCUMENTO;
    private String NACIONALIDAD;
    private String NOMBRES;
    private String APELLIDO_PATERNO;
    private String APELLIDO_MATERNO;
    private String ID_EMPRESA;
    private String GENERO;
    private String CORREO;
    private String FECHA_NACIMIENTO;
    private String NOMBRES_CONTACTO;
    private String DIRECCION_CONTACTO;
    private String TELEFONO_CONTACTO;
    private String CORREO_CONTACTO;
    private String USUARIO;
    private String CONTRASENA;
    private String ID_ROL;
    private String FEC_CREACION;
    private String FEC_ACTUALIZACION;
    private String FEC_ELIMINACION;

    public UsuarioBean() {
    }

    public String getGENERO() {
        return GENERO;
    }

    public void setGENERO(String GENERO) {
        this.GENERO = GENERO;
    }

    public String getID_TIPO_DOCUMENTO() {
        return ID_TIPO_DOCUMENTO;
    }

    public void setID_TIPO_DOCUMENTO(String ID_TIPO_DOCUMENTO) {
        this.ID_TIPO_DOCUMENTO = ID_TIPO_DOCUMENTO;
    }

    public String getNUM_DOCUMENTO() {
        return NUM_DOCUMENTO;
    }

    public void setNUM_DOCUMENTO(String NUM_DOCUMENTO) {
        this.NUM_DOCUMENTO = NUM_DOCUMENTO;
    }

    public String getNACIONALIDAD() {
        return NACIONALIDAD;
    }

    public void setNACIONALIDAD(String NACIONALIDAD) {
        this.NACIONALIDAD = NACIONALIDAD;
    }

    public String getFECHA_NACIMIENTO() {
        return FECHA_NACIMIENTO;
    }

    public void setFECHA_NACIMIENTO(String FECHA_NACIMIENTO) {
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
    }

    public String getNOMBRES_CONTACTO() {
        return NOMBRES_CONTACTO;
    }

    public void setNOMBRES_CONTACTO(String NOMBRES_CONTACTO) {
        this.NOMBRES_CONTACTO = NOMBRES_CONTACTO;
    }

    public String getDIRECCION_CONTACTO() {
        return DIRECCION_CONTACTO;
    }

    public void setDIRECCION_CONTACTO(String DIRECCION_CONTACTO) {
        this.DIRECCION_CONTACTO = DIRECCION_CONTACTO;
    }

    public String getTELEFONO_CONTACTO() {
        return TELEFONO_CONTACTO;
    }

    public void setTELEFONO_CONTACTO(String TELEFONO_CONTACTO) {
        this.TELEFONO_CONTACTO = TELEFONO_CONTACTO;
    }

    public String getCORREO_CONTACTO() {
        return CORREO_CONTACTO;
    }

    public void setCORREO_CONTACTO(String CORREO_CONTACTO) {
        this.CORREO_CONTACTO = CORREO_CONTACTO;
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

    public String getAPELLIDO_PATERNO() {
        return APELLIDO_PATERNO;
    }

    public void setAPELLIDO_PATERNO(String APELLIDO_PATERNO) {
        this.APELLIDO_PATERNO = APELLIDO_PATERNO;
    }

    public String getAPELLIDO_MATERNO() {
        return APELLIDO_MATERNO;
    }

    public void setAPELLIDO_MATERNO(String APELLIDO_MATERNO) {
        this.APELLIDO_MATERNO = APELLIDO_MATERNO;
    }

    public void setFEC_ACTUALIZACION(String FEC_ACTUALIZACION) {
        this.FEC_ACTUALIZACION = FEC_ACTUALIZACION;
    }
    public String getFEC_ACTUALIZACION() {
        return FEC_ACTUALIZACION;
    }
}
