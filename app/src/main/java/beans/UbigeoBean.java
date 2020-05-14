package beans;

public class UbigeoBean {

    public class DepartamentoBean {

        private String iddepartamento;
        private String nombre_departamento;

        public DepartamentoBean() {
        }

        public String getIddepartamento() {
            return iddepartamento;
        }

        public void setIddepartamento(String iddepartamento) {
            this.iddepartamento = iddepartamento;
        }

        public String getNombre_departamento() {
            return nombre_departamento;
        }

        public void setNombre_departamento(String nombre_departamento) {
            this.nombre_departamento = nombre_departamento;
        }
    }
    public class ProvinciaBean {

        private String idprovincia;
        private String iddepartamento;
        private String nombre_provincia;

        public ProvinciaBean() {
        }

        public String getIdprovincia() {
            return idprovincia;
        }

        public void setIdprovincia(String idprovincia) {
            this.idprovincia = idprovincia;
        }

        public String getIddepartamento() {
            return iddepartamento;
        }

        public void setIddepartamento(String iddepartamento) {
            this.iddepartamento = iddepartamento;
        }

        public String getNombre_provincia() {
            return nombre_provincia;
        }

        public void setNombre_provincia(String nombre_provincia) {
            this.nombre_provincia = nombre_provincia;
        }
    }
    public class DistritoBean {

        private String iddistrito;
        private String idprovincia;
        private String nombre_distrito;

        public DistritoBean() {
        }

        public String getIddistrito() {
            return iddistrito;
        }

        public void setIddistrito(String iddistrito) {
            this.iddistrito = iddistrito;
        }

        public String getIdprovincia() {
            return idprovincia;
        }

        public void setIdprovincia(String idprovincia) {
            this.idprovincia = idprovincia;
        }

        public String getNombre_distrito() {
            return nombre_distrito;
        }

        public void setNombre_distrito(String nombre_distrito) {
            this.nombre_distrito = nombre_distrito;
        }
    }
}
