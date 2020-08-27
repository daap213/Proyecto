package com.example.proyecto.entidades;
import java.io.Serializable;
public class Usuario implements Serializable {

    private Integer id;
    private String nombre;
    private String correo;
    private String ubicacion;
    private String telefono;
    private String contraseña;
    private String genero;

    public Usuario() {
    }

    public Usuario(Integer id, String nombre, String correo, String ubicacion, String telefono, String contraseña, String genero) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña= contraseña;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.genero= genero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}