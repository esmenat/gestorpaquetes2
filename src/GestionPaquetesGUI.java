import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import com.itextpdf.text.Font;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GestionPaquetesGUI extends JFrame implements ActionListener {

    private JTextField txtIdPaquete;
    private JCheckBox chkEsPremium;
    private JButton btnAgregar;
    private JButton btnReporte;
    private JButton btnEntregar;
    private JButton btnActualizar;
    private JButton btnVerPremiumExpand;
    private JButton btnVerNormalExpand;
    private JButton btnVerPilaExpand;
    private JButton btnVerLotes;
    private JTextArea txtAreaPremium;
    private JTextArea txtAreaNormal;
    private JTextArea txtAreaHistorial;
    private JLabel lblStatus;
    private JTextField txtNombre;
    private JComboBox<String> comboCiudad;

    private SistemaGestionPaquetes sistema;
    private static final int CAPACIDAD_BODEGA = 5;
    private int contadorLlegadas = 1;
    private final Color COLOR_PRIMARIO = new Color(60, 160, 230);
    private final Color COLOR_TEXTO = new Color(0, 0, 139); // Azul oscuro

    public GestionPaquetesGUI() {
        super("Gestión de Paquetes");
        sistema = new SistemaGestionPaquetes(CAPACIDAD_BODEGA);
        initComponents();
        actualizarPantallas();
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650); // Aumentar un poco la altura para el nuevo panel superior
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // --- Panel Superior: Nombre y Ciudad ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSuperior.setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(COLOR_TEXTO);
        panelSuperior.add(lblNombre);
        txtNombre = new JTextField(15);
        panelSuperior.add(txtNombre);

        JLabel lblCiudad = new JLabel("Ciudad:");
        lblCiudad.setForeground(COLOR_TEXTO);
        panelSuperior.add(lblCiudad);
        comboCiudad = new JComboBox<>(new String[] { "Ibarra", "Otavalo", "Atuntaqui" });
        panelSuperior.add(comboCiudad);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central: Controles y Listas ---
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        // Panel de Controles (arriba del centro)
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControles.setBackground(Color.WHITE);
        panelControles.add(new JLabel("ID Paquete:", SwingConstants.LEFT)).setForeground(COLOR_TEXTO);
        txtIdPaquete = new JTextField(10);
        panelControles.add(txtIdPaquete);
        chkEsPremium = new JCheckBox("Premium");
        chkEsPremium.setForeground(COLOR_TEXTO);
        chkEsPremium.setBackground(Color.WHITE);
        panelControles.add(chkEsPremium);
        btnAgregar = new JButton("Agregar");
        estilizarBoton(btnAgregar);
        btnAgregar.addActionListener(this);
        panelControles.add(btnAgregar);
        btnEntregar = new JButton("Despachar");
        estilizarBoton(btnEntregar);
        btnEntregar.addActionListener(this);
        panelControles.add(btnEntregar);
        btnActualizar = new JButton("Actualizar Tipo");
        estilizarBoton(btnActualizar);
        btnActualizar.addActionListener(this);
        panelControles.add(btnActualizar);
        panelCentral.add(panelControles, BorderLayout.NORTH);

        btnReporte = new JButton("Reporte");
        btnReporte.setBackground(new Color(60, 160, 230));
        btnReporte.setForeground(Color.WHITE);
        btnReporte.setFocusPainted(false);
        btnReporte.addActionListener(this);
        panelControles.add(btnReporte, BorderLayout.EAST);

        // Panel de Listas (centro del centro)
        JPanel panelListas = new JPanel(new GridLayout(1, 3, 10, 10));
        panelListas.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Premium
        JPanel pPrem = new JPanel(new BorderLayout());
        pPrem.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO),
                "Cola Premium (FIFO)", 0, 0, null, COLOR_TEXTO));
        txtAreaPremium = new JTextArea();
        txtAreaPremium.setEditable(false);
        pPrem.add(new JScrollPane(txtAreaPremium), BorderLayout.CENTER);
        btnVerPremiumExpand = new JButton("Expandir");
        estilizarBoton(btnVerPremiumExpand);
        btnVerPremiumExpand.addActionListener(_ -> expandir("Premium", txtAreaPremium.getText()));
        pPrem.add(btnVerPremiumExpand, BorderLayout.SOUTH);
        panelListas.add(pPrem);

        // Normal
        JPanel pNorm = new JPanel(new BorderLayout());
        pNorm.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO),
                "Cola Normal (FIFO)", 0, 0, null, COLOR_TEXTO));
        txtAreaNormal = new JTextArea();
        txtAreaNormal.setEditable(false);
        pNorm.add(new JScrollPane(txtAreaNormal), BorderLayout.CENTER);
        btnVerNormalExpand = new JButton("Expandir");
        estilizarBoton(btnVerNormalExpand);
        btnVerNormalExpand.addActionListener(_ -> expandir("Normal", txtAreaNormal.getText()));
        pNorm.add(btnVerNormalExpand, BorderLayout.SOUTH);
        panelListas.add(pNorm);

        // Historial pila
        JPanel pHist = new JPanel(new BorderLayout());
        pHist.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO),
                "Historial (Pila LIFO)", 0, 0, null, COLOR_TEXTO));
        txtAreaHistorial = new JTextArea();
        txtAreaHistorial.setEditable(false);
        pHist.add(new JScrollPane(txtAreaHistorial), BorderLayout.CENTER);
        btnVerPilaExpand = new JButton("Expandir");
        estilizarBoton(btnVerPilaExpand);
        btnVerPilaExpand.addActionListener(_ -> expandir("Historial", txtAreaHistorial.getText()));
        pHist.add(btnVerPilaExpand, BorderLayout.SOUTH);
        panelListas.add(pHist);

        panelCentral.add(panelListas, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // --- Panel Sur: Estado y Ver Lotes ---
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(Color.WHITE);
        lblStatus = new JLabel("Listo.", SwingConstants.CENTER);
        lblStatus.setForeground(COLOR_TEXTO);
        panelSur.add(lblStatus, BorderLayout.CENTER);
        btnVerLotes = new JButton("Ver Lotes");
        estilizarBoton(btnVerLotes);
        btnVerLotes.addActionListener(_ -> mostrarLotes());
        panelSur.add(btnVerLotes, BorderLayout.SOUTH);
        add(panelSur, BorderLayout.SOUTH);
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar)
            agregarPaquete();
        else if (e.getSource() == btnEntregar)
            despacharPaquete();
        else if (e.getSource() == btnActualizar)
            actualizarTipo();
        else if (e.getSource() == btnReporte)
            generarReportePDF();

    }

    private void agregarPaquete() {
        String nombre = txtNombre.getText().trim();
        String ciudad = (String) comboCiudad.getSelectedItem();
        boolean premium = chkEsPremium.isSelected();

        if (nombre.isEmpty() || ciudad == null) {
            mostrarError("Nombre y ciudad no pueden estar vacíos.");
            return;
        }

        try {
            Paquete p = new Paquete(nombre, ciudad, contadorLlegadas, premium);
            sistema.registrarPaquete(p);
            lblStatus.setText("Paquete agregado: " + p.getId());
            contadorLlegadas++;
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }

        limpiarEntrada();
        actualizarPantallas();
    }

    private void despacharPaquete() {
        try {
            Paquete p = sistema.despacharSiguiente();
            lblStatus.setText("Despachado: " + p);
        } catch (NoSuchElementException ex) {
            lblStatus.setText("No hay paquetes para despachar.");
        }
        actualizarPantallas();
    }

    private void actualizarTipo() {
        String id = txtIdPaquete.getText().trim();
        boolean premium = chkEsPremium.isSelected();
        boolean ok = sistema.actualizarTipo(id, premium);
        if (ok)
            lblStatus.setText("Tipo actualizado: " + id);
        else
            mostrarError("Paquete no encontrado");
        limpiarEntrada();
        actualizarPantallas();
    }

    private void actualizarPantallas() {
        txtAreaPremium.setText(formatPaquetes(sistema.getColaPremium()));
        txtAreaNormal.setText(formatPaquetes(sistema.getColaRegular()));
        txtAreaHistorial.setText(formatPaquetes(sistema.getHistoricoPila()));
    }

    private String formatPaquetes(Paquete[] arr) {
        if (arr.length == 0)
            return "<Vacía>";
        StringBuilder sb = new StringBuilder();
        for (Paquete p : arr)
            sb.append(p).append("\n");
        return sb.toString();
    }

    private void expandir(String titulo, String texto) {
        JFrame f = new JFrame(titulo);
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        f.add(new JScrollPane(area));
        f.setSize(300, 400);
        f.setLocationRelativeTo(this);
        f.setVisible(true);
    }

    private void mostrarLotes() {
        Paquete[] bodega = sistema.getBodega(); // Asumiendo que este método retorna el arreglo actual
        StringBuilder sb = new StringBuilder();
        sb.append("Lote único\n");
        for (Paquete p : bodega) {
            if (p != null)
                sb.append(p).append("\n");
        }
        expandir("Lote", sb.toString());
    }

    private void limpiarEntrada() {
        txtNombre.setText("");
        comboCiudad.setSelectedIndex(0);
        txtIdPaquete.setText("");
        chkEsPremium.setSelected(false);
    }

    private void mostrarError(String msg) {
        lblStatus.setText("Error: " + msg);
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void generarReportePDF() {
        Document document = new Document();
        String nombreArchivo = "reporte_paquetes.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();

            // Definición de fuentes
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font fontContenido = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Título principal
            Paragraph titulo = new Paragraph("REPORTE DE PAQUETES\n\n", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Tabla Premium
            Paragraph subtitulo1 = new Paragraph("Cola Premium (FIFO):\n\n", fontSubtitulo);
            document.add(subtitulo1);
            document.add(crearTablaDesdeTexto(txtAreaPremium.getText(), fontContenido));

            // Tabla Normal
            Paragraph subtitulo2 = new Paragraph("Cola Normal (FIFO):\n\n", fontSubtitulo);
            document.add(subtitulo2);
            document.add(crearTablaDesdeTexto(txtAreaNormal.getText(), fontContenido));

            // Tabla Historial
            Paragraph subtitulo3 = new Paragraph("Historial (Pila LIFO):\n\n", fontSubtitulo);
            document.add(subtitulo3);
            document.add(crearTablaDesdeTexto(txtAreaHistorial.getText(), fontContenido));

            // Total de llegadas
            Paragraph total = new Paragraph("\nTotal de llegadas: " + (contadorLlegadas - 1), fontContenido);
            document.add(total);

            lblStatus.setText("Reporte PDF generado con éxito.");
            JOptionPane.showMessageDialog(this, "Reporte generado: " + nombreArchivo, "PDF Generado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | IOException ex) {
            mostrarError("Error al generar el PDF: " + ex.getMessage());
        } finally {
            document.close();
        }

        // Abrir automáticamente el PDF generado
        try {
            File archivoPDF = new File(nombreArchivo);
            if (archivoPDF.exists()) {
                Desktop.getDesktop().open(archivoPDF);
            }
        } catch (IOException e) {
            mostrarError("No se pudo abrir el PDF automáticamente: " + e.getMessage());
        }
    }

    private PdfPTable crearTablaDesdeTexto(String texto, Font fontContenido) {
        PdfPTable tabla = new PdfPTable(1); // Una sola columna por simplicidad
        tabla.setWidthPercentage(100);

        String[] lineas = texto.split("\n");
        if (lineas.length == 0 || (lineas.length == 1 && lineas[0].trim().isEmpty())) {
            PdfPCell celda = new PdfPCell(new Phrase("Vacía", fontContenido));
            tabla.addCell(celda);
            return tabla;
        }

        for (String linea : lineas) {
            PdfPCell celda = new PdfPCell(new Phrase(linea, fontContenido));
            tabla.addCell(celda);
        }

        return tabla;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionPaquetesGUI().setVisible(true));
    }
}