package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import controlador.logica_ventana;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;

// ======================= NUEVOS IMPORTS =======================
// Componentes agregados para cumplir con los nuevos requisitos
import javax.swing.JTable; // Permite mostrar datos en forma de tabla
import javax.swing.table.DefaultTableModel; // Modelo de la tabla
import javax.swing.table.TableRowSorter; // Permite ordenar y filtrar
import javax.swing.RowFilter; // Permite aplicar filtros de búsqueda
import javax.swing.JProgressBar; // Barra visual de progreso
import javax.swing.JTabbedPane; // Permite dividir la interfaz en pestañas
// =============================================================

public class ventana extends JFrame {

	// ======================= COMPONENTES PRINCIPALES =======================
	public JPanel contentPane; // Panel principal que contiene toda la interfaz
	public JTextField txt_nombres; // Campo para nombres
	public JTextField txt_telefono; // Campo para teléfono
	public JTextField txt_email; // Campo para email
	public JTextField txt_buscar; // Campo para buscar contactos
	public JCheckBox chb_favorito; // Checkbox para marcar favorito
	public JComboBox cmb_categoria; // ComboBox de categorías
	public JButton btn_add; // Botón agregar
	public JButton btn_modificar; // Botón modificar
	public JButton btn_eliminar; // Botón eliminar
	public JList lst_contactos; // Lista de contactos
	public JScrollPane scrLista; // Scroll de la lista

	// ======================= NUEVAS VARIABLES =======================
	// Componentes nuevos solicitados en la actividad
	public JTable tabla; // Tabla para mostrar contactos
	public DefaultTableModel modeloTabla; // Modelo de la tabla
	public JProgressBar barra; // Barra de progreso
	public JButton btn_exportar; // Botón exportar CSV
	public JTabbedPane pestañas; // Contenedor de pestañas
	public JPanel panel_contactos; // Panel principal
	public JPanel panel_estadisticas; // Panel de estadísticas

	// Permite ordenar y filtrar la tabla
	public TableRowSorter<DefaultTableModel> sorter;
	// =============================================================

	/**
	 * Método principal que ejecuta la aplicación
	 */
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                // Se crea la ventana principal
	                ventana frame = new ventana();
	                frame.setVisible(true); // Se muestra la ventana
	            } catch (Exception e) {
	                e.printStackTrace(); // Manejo de errores
	            }
	        }
	    });
	}

	/**
	 * Constructor de la ventana
	 */
	public ventana() {
		setTitle("GESTION DE CONTACTOS"); // Título de la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierre de la app
		setResizable(false); // Evita redimensionar
		setBounds(100, 100, 1026, 748); // Tamaño de la ventana
		setLocationRelativeTo(null); // Centra la ventana en pantalla

		// ======================= PANEL PRINCIPAL =======================
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // Posicionamiento manual
		// =============================================================

		// ======================= PESTAÑAS =======================
		// Se crea un contenedor de pestañas para organizar la interfaz
		pestañas = new JTabbedPane();
		pestañas.setBounds(0, 0, 1010, 710);
		contentPane.add(pestañas);

		// Pestaña de contactos
		panel_contactos = new JPanel();
		panel_contactos.setLayout(null);
		pestañas.addTab("Contactos", panel_contactos);

		// Pestaña de estadísticas
		panel_estadisticas = new JPanel();
		panel_estadisticas.setLayout(null);
		pestañas.addTab("Estadísticas", panel_estadisticas);
		// =======================================================

		// ======================= ETIQUETAS =======================
		JLabel lbl_etiqueta1 = new JLabel("NOMBRES:");
		lbl_etiqueta1.setBounds(25, 41, 89, 13);
		lbl_etiqueta1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel_contactos.add(lbl_etiqueta1);

		JLabel lbl_etiqueta2 = new JLabel("TELEFONO:");
		lbl_etiqueta2.setBounds(25, 80, 89, 13);
		lbl_etiqueta2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel_contactos.add(lbl_etiqueta2);

		JLabel lbl_etiqueta3 = new JLabel("EMAIL:");
		lbl_etiqueta3.setBounds(25, 122, 89, 13);
		lbl_etiqueta3.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel_contactos.add(lbl_etiqueta3);

		JLabel lbl_etiqueta4 = new JLabel("BUSCAR POR NOMBRE:");
		lbl_etiqueta4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbl_etiqueta4.setBounds(25, 661, 192, 13);
		panel_contactos.add(lbl_etiqueta4);
		// =======================================================

		// ======================= CAMPOS =======================
		txt_nombres = new JTextField();
		txt_nombres.setBounds(124, 28, 427, 31);
		panel_contactos.add(txt_nombres);

		txt_telefono = new JTextField();
		txt_telefono.setBounds(124, 69, 427, 31);
		panel_contactos.add(txt_telefono);

		txt_email = new JTextField();
		txt_email.setBounds(124, 110, 427, 31);
		panel_contactos.add(txt_email);

		txt_buscar = new JTextField();
		txt_buscar.setBounds(212, 650, 784, 31);
		panel_contactos.add(txt_buscar);
		// =====================================================

		// ======================= CHECKBOX =======================
		chb_favorito = new JCheckBox("CONTACTO FAVORITO");
		chb_favorito.setBounds(24, 170, 193, 21);
		panel_contactos.add(chb_favorito);

		// ======================= COMBOBOX =======================
		cmb_categoria = new JComboBox();
		cmb_categoria.setBounds(300, 167, 251, 31);
		panel_contactos.add(cmb_categoria);

		String[] categorias = {"Elija una Categoria", "Familia", "Amigos", "Trabajo"};
		for (String categoria : categorias) {
		    cmb_categoria.addItem(categoria);
		}
		// =======================================================

		// ======================= BOTONES =======================
		btn_add = new JButton("AGREGAR");
		btn_add.setBounds(601, 70, 125, 65);
		panel_contactos.add(btn_add);

		btn_modificar = new JButton("MODIFICAR");
		btn_modificar.setBounds(736, 70, 125, 65);
		panel_contactos.add(btn_modificar);

		btn_eliminar = new JButton("ELIMINAR");
		btn_eliminar.setBounds(871, 69, 125, 65);
		panel_contactos.add(btn_eliminar);

		// Botón para exportar datos a CSV
		btn_exportar = new JButton("EXPORTAR CSV");
		btn_exportar.setBounds(601, 150, 200, 40);
		panel_contactos.add(btn_exportar);
		// =======================================================

		// ======================= LISTA =======================
		lst_contactos = new JList();
		lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrLista = new JScrollPane(lst_contactos);
		scrLista.setBounds(25, 242, 971, 150);
		panel_contactos.add(scrLista);
		// =====================================================

		// ======================= TABLA =======================
		modeloTabla = new DefaultTableModel(
		        new String[]{"Nombre","Teléfono","Email","Categoría"},0);

		tabla = new JTable(modeloTabla);

		// Permite ordenar al hacer clic en columnas
		tabla.setAutoCreateRowSorter(true);

		// Se crea el sorter para filtrado
		sorter = new TableRowSorter<>(modeloTabla);
		tabla.setRowSorter(sorter);

		JScrollPane scrollTabla = new JScrollPane(tabla);
		scrollTabla.setBounds(25, 400, 971, 200);
		panel_contactos.add(scrollTabla);
		// =====================================================

		// ======================= FILTRO =======================
		// Permite buscar en tiempo real mientras se escribe
		txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyReleased(java.awt.event.KeyEvent e) {
		        String texto = txt_buscar.getText();

		        if (texto.trim().length() == 0) {
		            sorter.setRowFilter(null);
		        } else {
		            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
		        }
		    }
		});
		// =====================================================

		// ======================= BARRA DE PROGRESO =======================
		barra = new JProgressBar();
		barra.setBounds(25, 610, 971, 25);
		barra.setStringPainted(true);
		panel_contactos.add(barra);
		// ===============================================================

		// ======================= PANEL ESTADÍSTICAS =======================
		JLabel lbl_stats = new JLabel("Aquí se pueden mostrar estadísticas de contactos");
		lbl_stats.setBounds(50, 50, 500, 30);
		panel_estadisticas.add(lbl_stats);
		// ===============================================================

		// ======================= CONTROLADOR =======================
		// Se conecta la vista con la lógica del programa
		logica_ventana lv = new logica_ventana(this);
	}
}