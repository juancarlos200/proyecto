/**
 * Autor: JuanK Espinal
 * Programa: Gestión de Reservas Deportivas
 * Clase: Main
 * Este ptrograma se encarga de gestionar la organizacion de canchas de futbol, tenis y basket
 * Fecha de creación: 21/08/2025
 * Última modificación: 21/08/2025
 * 
 * 
 * 
 * 
 * 
 * 
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends JFrame {
    // --- Estado del sistema ---
    private final HistorialReserva historial = new HistorialReserva();

    // Disponibilidad de canchas (etiquetas visibles en UI)
    private final java.util.List<String> canchasFut = new ArrayList<>();
    private final java.util.List<String> canchasTenis = new ArrayList<>();
    private final java.util.List<String> canchasBasket = new ArrayList<>();

    // Capacidad (número -> capacidad)
    private final java.util.Map<String, Integer> capacidadPorCancha = new HashMap<>();

    // --- UI ---
    private final DefaultTableModel modeloHistorial =
            new DefaultTableModel(new String[]{"Fecha","Responsable","Evento","Tipo","Inicio","Duración","Cancha"}, 0);

    public Main() {
        // Inicialización de canchas de ejemplo (tres por deporte)
        inicializarCanchas();

        setTitle("Sistema de Reservas Deportivas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra superior con acciones
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        JButton btnReservar = new JButton("Reservar");
        JButton btnModificar = new JButton("Modificar Reserva");
        JButton btnCancelar = new JButton("Cancelar Reserva");
        JButton btnVerCanchas = new JButton("Ver Canchas");
        JButton btnEstadisticas = new JButton("Estadísticas");
        toolbar.add(btnReservar);
        toolbar.add(btnModificar);
        toolbar.add(btnCancelar);
        toolbar.add(btnVerCanchas);
        toolbar.add(btnEstadisticas);
        add(toolbar, BorderLayout.NORTH);

        // Tabla de historial
        JTable tabla = new JTable(modeloHistorial);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos
        btnReservar.addActionListener(e -> hacerReserva());
        btnModificar.addActionListener(e -> modificarReserva(tabla));
        btnCancelar.addActionListener(e -> cancelarReserva(tabla));
        btnVerCanchas.addActionListener(e -> verCanchas());
        btnEstadisticas.addActionListener(e -> verEstadisticas());
    }

    /** Inicializa canchas y capacidades. */
    private void inicializarCanchas() {
        // Fútbol: 3 canchas
        for (int i=1; i<=3; i++) {
            InfoCanchasFut fut = new InfoCanchasFut(i, 22);
            String etiqueta = "Fútbol #" + fut.getNumeroCancha();
            canchasFut.add(etiqueta);
            capacidadPorCancha.put(etiqueta, fut.getCapacidadMaxima());
        }

        // Tenis: 3 canchas
        for (int i=1; i<=3; i++) {
            InfoCanchasTenis ten = new InfoCanchasTenis(i, 4);
            String etiqueta = "Tenis #" + ten.getNumeroCancha();
            canchasTenis.add(etiqueta);
            capacidadPorCancha.put(etiqueta, ten.getCapacidadMaxima());
        }

        // Basket: 3 canchas
        for (int i=1; i<=3; i++) {
            InfoCanchasBasket bas = new InfoCanchasBasket(i, 10);
            String etiqueta = "Basket #" + bas.getNumeroCancha();
            canchasBasket.add(etiqueta);
            capacidadPorCancha.put(etiqueta, bas.getCapacidadMaxima());
        }
    }

    /** Muestra un diálogo para crear nueva reserva. */
    private void hacerReserva() {
        String[] deportes = new String[]{"Fútbol","Tenis","Basket"};
        JComboBox<String> comboDep = new JComboBox<>(deportes);
        JComboBox<String> comboCancha = new JComboBox<>();
        actualizarOpcionesCancha(comboDep, comboCancha);

        JTextField txtResponsable = new JTextField();
        JTextField txtEvento = new JTextField();
        JTextField txtTipo = new JTextField();
        JSpinner spHora = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        JSpinner spDur = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));

        comboDep.addActionListener(e -> actualizarOpcionesCancha(comboDep, comboCancha));

        JPanel panel = new JPanel(new GridLayout(0,2,6,6));
        panel.add(new JLabel("Deporte:")); panel.add(comboDep);
        panel.add(new JLabel("Cancha:")); panel.add(comboCancha);
        panel.add(new JLabel("Responsable:")); panel.add(txtResponsable);
        panel.add(new JLabel("Nombre del evento:")); panel.add(txtEvento);
        panel.add(new JLabel("Tipo de evento:")); panel.add(txtTipo);
        panel.add(new JLabel("Hora inicio (24h):")); panel.add(spHora);
        panel.add(new JLabel("Duración (h):")); panel.add(spDur);

        int opt = JOptionPane.showConfirmDialog(this, panel, "Nueva reserva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION) {
            if (comboCancha.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay canchas disponibles para el deporte seleccionado.");
                return;
            }
            try {
                String cancha = (String) comboCancha.getSelectedItem();
                InfoReserva r = new InfoReserva(
                        txtResponsable.getText().trim(),
                        txtEvento.getText().trim(),
                        txtTipo.getText().trim(),
                        (Integer) spHora.getValue(),
                        (Integer) spDur.getValue(),
                        cancha,
                        LocalDate.now()
                );
                historial.agregarReserva(r);
                // La cancha reservada se retira de disponibilidad
                removerCanchaDeDisponibles(cancha);
                agregarFilaHistorial(r);

                // Reglas + depósito
                String reglas = "- Presentarse 15 minutos antes.\n"
                        + "- Uso obligatorio de indumentaria deportiva.\n"
                        + "- Mantener la limpieza y cuidado de la cancha.\n"
                        + "- Respetar el tiempo asignado.\n"
                        + "- Cualquier daño será cargado al responsable.";
                JOptionPane.showMessageDialog(this,
                        "Reserva realizada con éxito.\n\nREGLAS:\n" + reglas +
                        "\n\nDepósito: Dirigirse a caja administrativa con el código del evento para completar el pago.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Verifique los campos.");
            }
        }
    }

    /** Modifica una reserva seleccionada: puede reasignar cancha y/o cambiar horario. */
    private void modificarReserva(JTable tabla) {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva del historial.");
            return;
        }
        // Construir objeto desde la fila seleccionada
        String fecha = (String) modeloHistorial.getValueAt(row, 0);
        String responsable = (String) modeloHistorial.getValueAt(row, 1);
        String evento = (String) modeloHistorial.getValueAt(row, 2);
        String tipo = (String) modeloHistorial.getValueAt(row, 3);
        int inicio = Integer.parseInt(modeloHistorial.getValueAt(row, 4).toString());
        int dur = Integer.parseInt(modeloHistorial.getValueAt(row, 5).toString());
        String canchaActual = (String) modeloHistorial.getValueAt(row, 6);

        InfoReserva target = historial.getReservas().stream()
                .filter(r -> r.getFechaReserva().toString().equals(fecha)
                        && r.getResponsable().equals(responsable)
                        && r.getNombreEvento().equals(evento)
                        && r.getCancha().equals(canchaActual))
                .findFirst().orElse(null);

        if (target == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la reserva en el historial.");
            return;
        }

        // Preparar UI de modificación
        String deporte = canchaActual.split(" ")[0]; // "Fútbol"/"Tenis"/"Basket"
        JComboBox<String> comboCancha = new JComboBox<>();
        // Opción de mantener la misma cancha (reinsertarla temporalmente si estaba no disponible)
        // Primero, añadimos la cancha actual como opción
        comboCancha.addItem(canchaActual);
        // Luego añadimos el resto de disponibles del mismo deporte
        for (String c : canchasPorDeporte(deporte)) {
            if (!c.equals(canchaActual)) comboCancha.addItem(c);
        }

        JSpinner spHora = new JSpinner(new SpinnerNumberModel(inicio, 0, 23, 1));
        JSpinner spDur = new JSpinner(new SpinnerNumberModel(dur, 1, 8, 1));

        JPanel panel = new JPanel(new GridLayout(0,2,6,6));
        panel.add(new JLabel("Cancha:")); panel.add(comboCancha);
        panel.add(new JLabel("Hora inicio (24h):")); panel.add(spHora);
        panel.add(new JLabel("Duración (h):")); panel.add(spDur);

        int opt = JOptionPane.showConfirmDialog(this, panel, "Modificar reserva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION) {
            String nuevaCancha = (String) comboCancha.getSelectedItem();
            int nuevaHora = (Integer) spHora.getValue();
            int nuevaDur = (Integer) spDur.getValue();

            // Si cambia de cancha, liberar la anterior y ocupar la nueva
            if (!nuevaCancha.equals(canchaActual)) {
                agregarCanchaADisponibles(canchaActual);
                removerCanchaDeDisponibles(nuevaCancha);
                target.setCancha(nuevaCancha);
            }
            target.setHoraInicioEvento(nuevaHora);
            target.setDuracionEvento(nuevaDur);

            // Actualizar tabla
            modeloHistorial.setValueAt(nuevaHora, row, 4);
            modeloHistorial.setValueAt(nuevaDur, row, 5);
            modeloHistorial.setValueAt(target.getCancha(), row, 6);

            JOptionPane.showMessageDialog(this, "Reserva modificada correctamente.");
        }
    }

    /** Cancela la reserva seleccionada y libera la cancha. */
    private void cancelarReserva(JTable tabla) {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva del historial.");
            return;
        }
        String fecha = (String) modeloHistorial.getValueAt(row, 0);
        String responsable = (String) modeloHistorial.getValueAt(row, 1);
        String evento = (String) modeloHistorial.getValueAt(row, 2);
        String cancha = (String) modeloHistorial.getValueAt(row, 6);

        InfoReserva target = historial.getReservas().stream()
                .filter(r -> r.getFechaReserva().toString().equals(fecha)
                        && r.getResponsable().equals(responsable)
                        && r.getNombreEvento().equals(evento)
                        && r.getCancha().equals(cancha))
                .findFirst().orElse(null);

        if (target != null) {
            historial.cancelarReserva(target);
            modeloHistorial.removeRow(row);
            agregarCanchaADisponibles(cancha);
            JOptionPane.showMessageDialog(this, "Reserva cancelada y cancha liberada.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva.");
        }
    }

    /** Muestra número y capacidad de cada cancha disponible actualmente. */
    private void verCanchas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Canchas disponibles y capacidad máxima:\n\n");
        appendGrupo(sb, "Fútbol", canchasFut);
        appendGrupo(sb, "Tenis", canchasTenis);
        appendGrupo(sb, "Basket", canchasBasket);
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    /** Muestra estadística mensual y la cancha con mayor demanda. */
    private void verEstadisticas() {
        Map<YearMonth, Long> mensual = historial.estadisticaMensual();
        String mas = historial.canchaMasDemandada();

        // Ordenar por fecha (ascendente)
        java.util.List<YearMonth> orden = new ArrayList<>(mensual.keySet());
        orden.sort(Comparator.naturalOrder());

        StringBuilder sb = new StringBuilder();
        sb.append("Estadística mensual de encuentros (reservas por mes):\n\n");
        if (orden.isEmpty()) {
            sb.append("Sin datos.\n");
        } else {
            for (YearMonth ym : orden) {
                sb.append(ym).append(": ").append(mensual.get(ym)).append("\n");
            }
        }
        sb.append("\nCancha con mayor demanda: ").append(mas);
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // --- Utilidades ---
    private void actualizarOpcionesCancha(JComboBox<String> comboDep, JComboBox<String> comboCancha) {
        comboCancha.removeAllItems();
        String dep = (String) comboDep.getSelectedItem();
        for (String c : canchasPorDeporte(dep)) {
            comboCancha.addItem(c);
        }
    }

    private java.util.List<String> canchasPorDeporte(String deporte) {
        switch (deporte) {
            case "Fútbol": return canchasFut;
            case "Tenis":  return canchasTenis;
            case "Basket": return canchasBasket;
            default: return java.util.Collections.emptyList();
        }
    }

    private void removerCanchaDeDisponibles(String cancha) {
        if (cancha.startsWith("Fútbol")) canchasFut.remove(cancha);
        else if (cancha.startsWith("Tenis")) canchasTenis.remove(cancha);
        else if (cancha.startsWith("Basket")) canchasBasket.remove(cancha);
    }

    private void agregarCanchaADisponibles(String cancha) {
        if (cancha.startsWith("Fútbol")) { if (!canchasFut.contains(cancha)) canchasFut.add(cancha); }
        else if (cancha.startsWith("Tenis")) { if (!canchasTenis.contains(cancha)) canchasTenis.add(cancha); }
        else if (cancha.startsWith("Basket")) { if (!canchasBasket.contains(cancha)) canchasBasket.add(cancha); }
    }

    private void agregarFilaHistorial(InfoReserva r) {
        modeloHistorial.addRow(new Object[]{ 
            r.getFechaReserva().toString(),
            r.getResponsable(),
            r.getNombreEvento(),
            r.getTipoEvento(),
            r.getHoraInicioEvento(),
            r.getDuracionEvento(),
            r.getCancha()
        });
    }

    private void appendGrupo(StringBuilder sb, String titulo, java.util.List<String> canchas) {
        sb.append(titulo).append(":\n");
        if (canchas.isEmpty()) {
            sb.append("  (No hay disponibles)\n");
        } else {
            for (String c: canchas.stream().sorted().collect(Collectors.toList())) {
                int cap = capacidadPorCancha.getOrDefault(c, 0);
                sb.append("  - ").append(c).append(" | Capacidad: ").append(cap).append("\n");
            }
        }
        sb.append("\n");
    }

    public static void main(String[] args) {
        // Se permiten System.out.println aquí si se desea depurar en consola,
        // cumpliendo con la restricción de no usarlo fuera de la clase Principal.
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
