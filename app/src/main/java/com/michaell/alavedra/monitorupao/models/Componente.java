package com.michaell.alavedra.monitorupao.models;

public class Componente {

    private String promocional;
    private String codigo;
    private String descripcion;
    private String peso;
    private String nota;
    private boolean esComponente;

    public Componente(String promocional) {
        this.promocional = promocional;
    }

    public Componente(String codigo, String descripcion, String peso, boolean esComponente) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.peso = peso;
        this.esComponente = esComponente;
    }

    public String getPromocional() {
        return promocional;
    }

    public void setPromocional(String promocional) {
        this.promocional = promocional;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPeso() {
        return peso;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public boolean esComponente() {
        return esComponente;
    }

    @Override
    public String toString() {
        return "Componente{" +
                "promocional='" + promocional + '\'' +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", peso='" + peso + '\'' +
                ", nota='" + nota + '\'' +
                ", esComponente=" + esComponente +
                '}';
    }
}
