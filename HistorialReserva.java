

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class HistorialReserva {
    private final List<InfoReserva> reservas = new ArrayList<>();

    public void agregarReserva(InfoReserva reserva) {
        reservas.add(reserva);
    }

    public void cancelarReserva(InfoReserva reserva) {
        reservas.remove(reserva);
    }

    public List<InfoReserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }

    /** Retorna un mapa YearMonth -> cantidad de reservas en ese mes. */
    public Map<YearMonth, Long> estadisticaMensual() {
        return reservas.stream().collect(Collectors.groupingBy(
            r -> YearMonth.from(r.getFechaReserva()), Collectors.counting()
        ));
    }

    /** Retorna la etiqueta de la cancha con mayor demanda (más reservas acumuladas). */
    public String canchaMasDemandada() {
        if (reservas.isEmpty()) return "No hay reservas";
        Map<String, Long> conteo = reservas.stream().collect(Collectors.groupingBy(
            InfoReserva::getCancha, Collectors.counting()
        ));
        return conteo.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    /** Obtiene una lista de reservas filtradas por etiqueta de cancha. */
    public List<InfoReserva> reservasPorCancha(String cancha) {
        return reservas.stream().filter(r -> r.getCancha().equals(cancha)).collect(Collectors.toList());
    }
}
