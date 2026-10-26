package co.edu.unbosque.sigevet.modelo;

import java.util.ArrayList;

public class ResultadoConsulta {
    private ArrayList<String> columnas = new ArrayList<String>();
    private ArrayList<ArrayList<String>> filas = new ArrayList<ArrayList<String>>();

    public ResultadoConsulta() {
    }

    public ArrayList<String> getColumnas() {
        return columnas;
    }

    public void setColumnas(ArrayList<String> columnas) {
        this.columnas = columnas;
    }

    public ArrayList<ArrayList<String>> getFilas() {
        return filas;
    }

    public void setFilas(ArrayList<ArrayList<String>> filas) {
        this.filas = filas;
    }
}
