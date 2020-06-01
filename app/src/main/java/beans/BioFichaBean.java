package beans;

import java.util.Date;
import java.util.List;

public class BioFichaBean {

    private String ID;
    private String ID_SEDE;
    private String ID_TIPO_DOCUMENTO;
    private String NOM_DOCUMENTO;
    private String NUM_DOCUMENTO;
    private String COD_PAIS;
    private String NOMBRES;
    private String APELLIDO_PATERNO;
    private String APELLIDO_MATERNO;
    private String FECHA_NACIMIENTO;
    private String GENERO;
    private String ESTATURA;
    private String PESO;
    private String IMC;
    private String GRADO_CELSIUS;
    private String MENSAJE_ESTADO;
    private double LATITUD;
    private double LONGITUD;
    private String OTRO_SINTOMA;
    private String FEC_CREACION;
    private String FEC_ACTUALIZACION;
    private String FEC_ELIMINACION;

    private List<EnfermedadBean> lstEnfermedad;
    private List<SintomaBean> lstSintoma;

    public BioFichaBean() {
    }

    public List<EnfermedadBean> getLstEnfermedad() {
        return lstEnfermedad;
    }

    public void setLstEnfermedad(List<EnfermedadBean> lstEnfermedad) {
        this.lstEnfermedad = lstEnfermedad;
    }

    public String getNOM_DOCUMENTO() {
        return NOM_DOCUMENTO;
    }

    public void setNOM_DOCUMENTO(String NOM_DOCUMENTO) {
        this.NOM_DOCUMENTO = NOM_DOCUMENTO;
    }

    public String getCOD_PAIS() {
        return COD_PAIS;
    }

    public void setCOD_PAIS(String COD_PAIS) {
        this.COD_PAIS = COD_PAIS;
    }

    public List<SintomaBean> getLstSintoma() {
        return lstSintoma;
    }

    public void setLstSintoma(List<SintomaBean> lstSintoma) {
        this.lstSintoma = lstSintoma;
    }

    public String getESTATURA() {
        return ESTATURA;
    }

    public void setESTATURA(String ESTATURA) {
        this.ESTATURA = ESTATURA;
    }

    public String getPESO() {
        return PESO;
    }

    public void setPESO(String PESO) {
        this.PESO = PESO;
    }

    public String getIMC() {
        return IMC;
    }

    public void setIMC(String IMC) {
        this.IMC = IMC;
    }

    public String getGRADO_CELSIUS() {
        return GRADO_CELSIUS;
    }

    public void setGRADO_CELSIUS(String GRADO_CELSIUS) {
        this.GRADO_CELSIUS = GRADO_CELSIUS;
    }

    public String getMENSAJE_ESTADO() {
        return MENSAJE_ESTADO;
    }

    public void setMENSAJE_ESTADO(String MENSAJE_ESTADO) {
        this.MENSAJE_ESTADO = MENSAJE_ESTADO;
    }

    public String getOTRO_SINTOMA() {
        return OTRO_SINTOMA;
    }

    public void setOTRO_SINTOMA(String OTRO_SINTOMA) {
        this.OTRO_SINTOMA = OTRO_SINTOMA;
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

    public String getNOMBRES() {
        return NOMBRES;
    }

    public void setNOMBRES(String NOMBRES) {
        this.NOMBRES = NOMBRES;
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

    public String getFECHA_NACIMIENTO() {
        return FECHA_NACIMIENTO;
    }

    public void setFECHA_NACIMIENTO(String FECHA_NACIMIENTO) {
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
    }

    public String getGENERO() {
        return GENERO;
    }

    public void setGENERO(String GENERO) {
        this.GENERO = GENERO;
    }
    public String getID_SEDE() {
        return ID_SEDE;
    }

    public void setID_SEDE(String ID_SEDE) {
        this.ID_SEDE = ID_SEDE;
    }

    public double getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(double LATITUD) {
        this.LATITUD = LATITUD;
    }

    public double getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(double LONGITUD) {
        this.LONGITUD = LONGITUD;
    }
}
