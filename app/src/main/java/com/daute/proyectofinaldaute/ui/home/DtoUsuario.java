package com.daute.proyectofinaldaute.ui.home;

public class DtoUsuario {
        private int idUsuario;
        private String nombreUsuario;
        private String apellidoUsuario;
        private String correo;
        private String usuario;
        private String clave;
        private int tipo;
        private int estado;
        private String pregunta;
        private String respuesta;

        public DtoUsuario() {
        }

        public DtoUsuario(int idUsuario, String nombreUsuario, String apellidoUsuario, String correo, String usuario, String clave, int tipo, int estado, String pregunta, String respuesta) {
            this.idUsuario = idUsuario;
            this.nombreUsuario = nombreUsuario;
            this.apellidoUsuario = apellidoUsuario;
            this.correo = correo;
            this.usuario = usuario;
            this.clave = clave;
            this.tipo = tipo;
            this.estado = estado;
            this.pregunta = pregunta;
            this.respuesta = respuesta;
        }

}
