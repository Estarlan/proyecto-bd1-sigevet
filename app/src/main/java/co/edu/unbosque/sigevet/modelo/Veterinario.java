package co.edu.unbosque.sigevet.modelo;

import java.util.ArrayList;

public class Veterinario extends Empleado {
    private String tarjetaProfesional;
    private String universidad;
    private ArrayList<String> especialidades = new ArrayList<String>();

    public Veterinario() {
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public ArrayList<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<String> especialidades) {
        this.especialidades = especialidades;
    }

    @Override
    public String getDescripcion() {
        return "Veterinario de " + getSede() + ". Tarjeta: " + tarjetaProfesional + ". Universidad: " + universidad;
    }
}
