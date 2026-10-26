package co.edu.unbosque.sigevet.modelo;


public class Auxiliar extends Empleado {
    private String area;
    private String turno;

    public Auxiliar() {
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String getDescripcion() {
        return "Auxiliar de " + area + ". Turno: " + turno;
    }
}
