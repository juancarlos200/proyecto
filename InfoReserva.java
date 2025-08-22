

import java.time.LocalDate;

public class InfoReserva {
    private String responsable;
    private String nombreEvento;
    private String tipoEvento;
    private int horaInicioEvento; // 0-23 (formato 24h)
    private int duracionEvento;   // en horas
    private String cancha;        // Etiqueta de la cancha (ej. "Fútbol #1")
    private LocalDate fechaReserva; // Fecha en que se realiza la reserva

    public InfoReserva(String responsable, String nombreEvento, String tipoEvento,
                       int horaInicioEvento, int duracionEvento, String cancha, LocalDate fechaReserva) {
        this.responsable = responsable;
        this.nombreEvento = nombreEvento;
        this.tipoEvento = tipoEvento;
        this.horaInicioEvento = horaInicioEvento;
        this.duracionEvento = duracionEvento;
        this.cancha = cancha;
        this.fechaReserva = fechaReserva;
    }

    public String getResponsable() { return responsable; }
    public String getNombreEvento() { return nombreEvento; }
    public String getTipoEvento() { return tipoEvento; }
    public int getHoraInicioEvento() { return horaInicioEvento; }
    public int getDuracionEvento() { return duracionEvento; }
    public String getCancha() { return cancha; }
    public LocalDate getFechaReserva() { return fechaReserva; }

    public void setHoraInicioEvento(int horaInicioEvento) { this.horaInicioEvento = horaInicioEvento; }
    public void setDuracionEvento(int duracionEvento) { this.duracionEvento = duracionEvento; }
    public void setCancha(String cancha) { this.cancha = cancha; }

    @Override
    public String toString() {
        return "[" + fechaReserva + "] Evento: " + nombreEvento + " (" + tipoEvento + ") - Responsable: " + responsable +
               ", Inicio: " + horaInicioEvento + ":00, Duración: " + duracionEvento + "h, Cancha: " + cancha;
    }
}
