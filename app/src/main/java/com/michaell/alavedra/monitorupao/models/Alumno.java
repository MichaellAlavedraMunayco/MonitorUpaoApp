package com.michaell.alavedra.monitorupao.models;

public class Alumno {
    private String id_alumno;

    public Alumno() {
    }

    public Alumno(int creA, int curAT, String egre, double PPA, double PPS, String USA, String apellidos_nombres, String id_alumno, String id_carrera, String password) {
        int creA1 = creA;
        int curAT1 = curAT;
        String egre1 = egre;
        double PPA1 = PPA;
        double PPS1 = PPS;
        String USA1 = USA;
        String apellidos_nombres1 = apellidos_nombres;
        this.id_alumno = id_alumno;
        String id_carrera1 = id_carrera;
        String password1 = password;
    }

    public String getId_alumno() {
        return id_alumno;
    }

}
