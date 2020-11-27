package com.daute.proyectofinaldaute.ui.notifications;

public class DtoProductos {
    private int idProd;
    private String nombreProd;
    private String descripcion;
    private double stock;
    private double precio;
    private String unidadMedida;
    private int estadoProducto;
    private int categoria;
    private String fecha;

    public DtoProductos() {
    }

    public DtoProductos(int idProd, String nombreProd, String descripcion, double stock, double precio, String unidadMedida, int estadoProducto, int categoria, String fecha) {
        this.idProd = idProd;
        this.nombreProd = nombreProd;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.unidadMedida = unidadMedida;
        this.estadoProducto = estadoProducto;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public int getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(int estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
