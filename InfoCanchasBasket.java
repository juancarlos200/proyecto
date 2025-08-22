

public class InfoCanchasBasket {
    private int numeroCancha;
    private int capacidadMax;

    public InfoCanchasBasket(int numeroCancha, int capacidadMax) {
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
        return "Basket #" + numeroCancha + " (Capacidad: " + capacidadMax + ")";
    }
}
