import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;


class LoginFrame extends JFrame implements ActionListener {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciar;
    private JLabel lblMensaje;
    private JLabel lblImagen; 
    private ImageIcon originalIcon;

    public LoginFrame() {
        super("Iniciar Sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel con imagen y texto a la izquierda ---
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(new Color(60, 160, 230));
        panelIzquierdo.setLayout(new BorderLayout());

        // Placeholder para imagen
        lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        // Aquí se carga la imagen
        originalIcon = new ImageIcon("src\\Image\\2250401.png");
        lblImagen.setIcon(originalIcon);
        panelIzquierdo.add(lblImagen, BorderLayout.CENTER);

        // Texto descriptivo (sin cambios)
        JPanel panelTexto = new JPanel();
        panelTexto.setOpaque(false);
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel("Proyecto Pilas y Colas");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(10));
        JLabel lblSub = new JLabel("Gestión de Paquetería");
        lblSub.setForeground(Color.WHITE);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTexto.add(lblSub);
        panelIzquierdo.add(panelTexto, BorderLayout.SOUTH);

        // --- Panel de login a la derecha --- (sin cambios)
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblLogin = new JLabel("Inicia sesión");
        lblLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelDerecho.add(lblLogin, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 1, 0, 10));
        form.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        form.add(txtUsuario);
        form.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        form.add(txtContrasena);
        panelDerecho.add(form, BorderLayout.CENTER);

        btnIniciar = new JButton("ACCEDER");
        btnIniciar.setBackground(new Color(60, 160, 230));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.addActionListener(this);
        panelDerecho.add(btnIniciar, BorderLayout.SOUTH);

      

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        panelDerecho.add(lblMensaje, BorderLayout.NORTH);

        // Combinar panels en SplitPane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        split.setDividerLocation(300);
        split.setEnabled(false);
        add(split, BorderLayout.CENTER);

        // Agregar listener para redimensionar la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                escalarImagen();
            }
        });
    }

    private void escalarImagen() {
        if (originalIcon != null) {
            int w = lblImagen.getWidth();
            int h = lblImagen.getHeight();
            if (w > 0 && h > 0) {
                Image img = originalIcon.getImage();
                Image scaledImg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(scaledImg));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());
        if ("admin".equals(usuario) && "1234".equals(pass)) {
            new GestionPaquetesGUI().setVisible(true);
            dispose();
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}