package vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import controlador.logica_ventana;

public class ventana extends JFrame {

	public JPanel contentPane;
	public JTextField txt_nombres, txt_telefono, txt_email, txt_buscar;
	public JCheckBox chb_favorito;
	public JComboBox<String> cmb_categoria, cmb_idioma;
	public JButton btn_add, btn_modificar, btn_eliminar, btn_exportar;
	public JLabel lbl_nombre, lbl_telefono, lbl_email, lbl_buscar;
	public JList<String> lst_contactos;
	public JTable tabla;
	public DefaultTableModel modeloTabla;
	public JProgressBar barra;
	public JTabbedPane pestañas;
	public JPanel panel_contactos, panel_estadisticas;

	public ventana() {
		Locale locale = new Locale("es");
		ResourceBundle mensajes = ResourceBundle.getBundle("idioma.mensajes", locale);

		setTitle(mensajes.getString("titulo"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 1026, 748);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(255, 240, 245));
		setContentPane(contentPane);

		pestañas = new JTabbedPane();
		pestañas.setBounds(0, 0, 1010, 710);
		pestañas.setBackground(new Color(255, 192, 203));
		contentPane.add(pestañas);

		panel_contactos = new JPanel();
		panel_contactos.setLayout(null);
		panel_contactos.setBackground(new Color(255, 228, 240));
		pestañas.addTab("Contactos", panel_contactos);

		panel_estadisticas = new JPanel();
		panel_estadisticas.setLayout(null);
		panel_estadisticas.setBackground(new Color(255, 228, 240));
		pestañas.addTab("Estadísticas", panel_estadisticas);

		// 1. INICIALIZAR COMPONENTES 
		inicializarComponentes();
		
		// 2. APLICAR TEXTOS INICIALES
		cambiarTextos(mensajes);

		// 3. EVENTO IDIOMA
		cmb_idioma.addActionListener(e -> {
		    String seleccion = cmb_idioma.getSelectedItem().toString();
		    Locale l = seleccion.equals("English") ? new Locale("en") : 
		               seleccion.equals("Français") ? new Locale("fr") : new Locale("es");
		    
		    ResourceBundle res = ResourceBundle.getBundle("idioma.mensajes", l);
		    
		    // Actualizar etiquetas y botones
		    cambiarTextos(res); 

		    // Actualizar encabezados de la tabla 
		    String[] col = {
		        res.getString("col_nombre"), 
		        res.getString("col_telefono"), 
		        res.getString("col_email"), 
		        res.getString("col_categoria")
		    };
		    
		    modeloTabla.setColumnIdentifiers(col);
		    
		   
		    tabla.getTableHeader().revalidate();
		    tabla.getTableHeader().repaint();
		});

		new logica_ventana(this);
	}

	private void inicializarComponentes() {
		Font fuente = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16);

		// Campos de texto
		txt_nombres = new JTextField(); txt_nombres.setBounds(130, 30, 430, 35); panel_contactos.add(txt_nombres);
		txt_telefono = new JTextField(); txt_telefono.setBounds(130, 80, 430, 35); panel_contactos.add(txt_telefono);
		txt_email = new JTextField(); txt_email.setBounds(130, 130, 430, 35); panel_contactos.add(txt_email);
		txt_buscar = new JTextField(); txt_buscar.setBounds(230, 645, 760, 35); panel_contactos.add(txt_buscar);

		// Labels
		lbl_nombre = new JLabel(); lbl_nombre.setBounds(30, 30, 100, 30); lbl_nombre.setFont(fuente); panel_contactos.add(lbl_nombre);
		lbl_telefono = new JLabel(); lbl_telefono.setBounds(30, 80, 100, 30); lbl_telefono.setFont(fuente); panel_contactos.add(lbl_telefono);
		lbl_email = new JLabel(); lbl_email.setBounds(30, 130, 100, 30); lbl_email.setFont(fuente); panel_contactos.add(lbl_email);
		lbl_buscar = new JLabel(); lbl_buscar.setBounds(30, 645, 200, 30); lbl_buscar.setFont(fuente); panel_contactos.add(lbl_buscar);

		// Botones
		btn_add = crearBoton(580, 50, 130, 80, new Color(255, 105, 180), "/img/add.png");
		btn_modificar = crearBoton(720, 50, 130, 80, new Color(186, 85, 211), "/img/pencil.png");
		btn_eliminar = crearBoton(860, 50, 130, 80, new Color(219, 112, 147), "/img/trash.png");
		btn_exportar = crearBoton(580, 170, 200, 45, new Color(221, 160, 221), "/img/file-export.png");
		panel_contactos.add(btn_add); panel_contactos.add(btn_modificar); panel_contactos.add(btn_eliminar); panel_contactos.add(btn_exportar);

		// Combos
		chb_favorito = new JCheckBox(); chb_favorito.setBounds(30, 190, 220, 30); chb_favorito.setBackground(new Color(255, 228, 240)); panel_contactos.add(chb_favorito);
		cmb_categoria = new JComboBox<>(); cmb_categoria.setBounds(260, 190, 250, 35); cmb_categoria.setBackground(new Color(255, 192, 203)); panel_contactos.add(cmb_categoria);
		cmb_idioma = new JComboBox<>(new String[]{"Español", "English", "Français"});
		cmb_idioma.setBounds(810, 170, 170, 40); cmb_idioma.setBackground(new Color(255, 192, 203)); panel_contactos.add(cmb_idioma);

		// Lista
		lst_contactos = new JList<>();
		JScrollPane scrL = new JScrollPane(lst_contactos); scrL.setBounds(30, 260, 960, 130); panel_contactos.add(scrL);
		
		// TABLA (Arreglado: Solo una inicialización)
		modeloTabla = new DefaultTableModel();
		tabla = new JTable(modeloTabla);
		JScrollPane scrT = new JScrollPane(tabla); 
		scrT.setBounds(30, 410, 960, 180); 
		panel_contactos.add(scrT);

		// BARRA DE PROGRESO
		barra = new JProgressBar();
		barra.setBounds(30, 605, 960, 25);
		barra.setForeground(new Color(186, 85, 211));
		barra.setBackground(Color.WHITE);
		barra.setStringPainted(true);
		panel_contactos.add(barra);
	}

	private void cambiarTextos(ResourceBundle res) {
	
	    this.setTitle(res.getString("titulo")); 

	    // 1. Actualizar Pestañas
	    pestañas.setTitleAt(0, res.getString("contactos"));
	    pestañas.setTitleAt(1, res.getString("estadisticas"));

	    // 2. Actualizar Etiquetas (Labels)
	    lbl_nombre.setText(res.getString("nombre"));
	    lbl_telefono.setText(res.getString("telefono"));
	    lbl_email.setText(res.getString("email"));
	    lbl_buscar.setText(res.getString("buscar"));
	    chb_favorito.setText(res.getString("favorito"));

	    // 3. Actualizar Botones
	    btn_add.setText(res.getString("agregar"));
	    btn_modificar.setText(res.getString("modificar"));
	    btn_eliminar.setText(res.getString("eliminar"));
	    btn_exportar.setText(res.getString("exportar"));
	    
	    // 4. Actualizar ComboBox de Categorías
	    cmb_categoria.removeAllItems();
	    cmb_categoria.addItem(res.getString("categoria_default"));
	    cmb_categoria.addItem(res.getString("familia"));
	    cmb_categoria.addItem(res.getString("amigos"));
	    cmb_categoria.addItem(res.getString("trabajo"));

	    // 5. ACTUALIZAR ENCABEZADOS DE LA TABLA 
	    String[] col = {
	        res.getString("col_nombre"), 
	        res.getString("col_telefono"), 
	        res.getString("col_email"), 
	        res.getString("col_categoria")
	    };
	    
	    modeloTabla.setColumnIdentifiers(col);
	    
	    // Forzado directo para las columnas físicas
	    for (int i = 0; i < col.length; i++) {
	        tabla.getColumnModel().getColumn(i).setHeaderValue(col[i]);
	    }
	    
	    // Refresco visual 
	    if (tabla.getTableHeader() != null) {
	        tabla.getTableHeader().repaint();
	    }
	
	}

	private JButton crearBoton(int x, int y, int w, int h, Color bg, String ruta) {
		JButton btn = new JButton();
		btn.setBounds(x, y, w, h);
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		try {
			Image img = new ImageIcon(getClass().getResource(ruta)).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
			btn.setIcon(new ImageIcon(img));
		} catch(Exception e) {}
		return btn;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try { new ventana().setVisible(true); } catch (Exception e) {}
		});
	}
}