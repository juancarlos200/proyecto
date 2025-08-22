
public class InfoCanchasTenis {
    private int numeroCancha;
    private int capacidadMax;

    public InfoCanchasTenis(int numeroCancha, int capacidadMax) {
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
        return "Tenis #" + numeroCancha + " (Capacidad: " + capacidadMax + ")";
    }
}
