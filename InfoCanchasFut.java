

public class InfoCanchasFut {
    private int numeroCancha;
    private int capacidadMax;

    public InfoCanchasFut(int numeroCancha, int capacidadMax) {
        this.numeroCancha = numeroCancha;
        this.capacidadMax = capacidadMax;
    }

    public int getNumeroCancha() {
        return numeroCancha;
    }

    public int getCapacidadMaxima() {
        return capacidadMax;
    }

    @Override
    public String toString() {
        return "Fútbol #" + numeroCancha + " (Capacidad: " + capacidadMax + ")";
    }
}
