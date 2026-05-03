package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.Locale;

import vista.ventana;
import modelo.*;

public class logica_ventana implements ActionListener,
ListSelectionListener,
ItemListener {

	// ======================= VARIABLES =======================
	private ventana delegado;

	private String nombres;
	private String email;
	private String telefono;
	private String categoria = "";

	private persona persona;

	private List<persona> contactos;

	private boolean favorito = false;

	private TableRowSorter<DefaultTableModel> sorter;
	// ========================================================

	// ======================= CONSTRUCTOR =======================
	public logica_ventana(ventana delegado) {

		this.delegado = delegado;

		// Cargar contactos
		cargarContactosRegistrados();

		// Eventos botones
		this.delegado.btn_add.addActionListener(this);
		this.delegado.btn_eliminar.addActionListener(this);
		this.delegado.btn_modificar.addActionListener(this);
		this.delegado.btn_exportar.addActionListener(this);

		// Eventos lista
		this.delegado.lst_contactos.addListSelectionListener(this);

		// Eventos selección
		this.delegado.cmb_categoria.addItemListener(this);
		this.delegado.chb_favorito.addItemListener(this);

		// ======================= ENTER PARA AGREGAR =======================
		this.delegado.txt_nombres.addKeyListener(
				new KeyAdapter() {

			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_ENTER) {

					delegado.btn_add.doClick();
				}
			}
		});

		// ======================= FILTRO TABLA =======================
		this.delegado.txt_buscar.addKeyListener(
				new KeyAdapter() {

			public void keyReleased(KeyEvent e) {

				if(sorter != null) {

					sorter.setRowFilter(
							RowFilter.regexFilter(
									"(?i)" +
									delegado.txt_buscar.getText()));
				}
			}
		});

		// ======================= MENÚ CLICK DERECHO =======================
		JPopupMenu menu = new JPopupMenu();

		JMenuItem eliminar =
				new JMenuItem("Eliminar");

		menu.add(eliminar);

		delegado.tabla.setComponentPopupMenu(menu);

		// Evento eliminar
		eliminar.addActionListener(
				e -> eliminarDesdeTabla());
	}
	// ===============================================================

	// ======================= INICIALIZAR CAMPOS =======================
	private void inicializacionCampos() {

		nombres =
				delegado.txt_nombres.getText();

		email =
				delegado.txt_email.getText();

		telefono =
				delegado.txt_telefono.getText();
	}
	// ================================================================

	// ======================= CARGAR CONTACTOS =======================
	private void cargarContactosRegistrados() {

		try {

			contactos =
					new personaDAO(
							new persona()).leerArchivo();

			// ======================= LISTA =======================
			DefaultListModel modelo =
					new DefaultListModel();

			for(persona contacto : contactos) {

				modelo.addElement(
						contacto.formatoLista());
			}

			delegado.lst_contactos.setModel(modelo);

			// ======================= IDIOMA ACTUAL =======================
			Locale locale;

			String idioma =
					delegado.cmb_idioma
					.getSelectedItem()
					.toString();

			if(idioma.equals("English")) {

				locale = new Locale("en");

			}
			else if(idioma.equals("Français")) {

				locale = new Locale("fr");

			}
			else {

				locale = new Locale("es");
			}

			ResourceBundle mensajes =
					ResourceBundle.getBundle(
							"idioma.mensajes",
							locale);

			// ======================= TABLA =======================
			DefaultTableModel modeloTabla =
					new DefaultTableModel(

					new String[] {

							mensajes.getString("col_nombre"),
							mensajes.getString("col_telefono"),
							mensajes.getString("col_email"),
							mensajes.getString("col_categoria")
					},

					0
			);

			// ======================= AGREGAR FILAS =======================
			for(persona contacto : contactos) {

				String categoriaTraducida =
						contacto.getCategoria();

				// ESPAÑOL
				if(locale.getLanguage().equals("es")) {

					if(categoriaTraducida.equals("Family") ||
					   categoriaTraducida.equals("Famille")) {

						categoriaTraducida = "Familia";
					}

					if(categoriaTraducida.equals("Friends") ||
					   categoriaTraducida.equals("Amis")) {

						categoriaTraducida = "Amigos";
					}

					if(categoriaTraducida.equals("Work") ||
					   categoriaTraducida.equals("Travail")) {

						categoriaTraducida = "Trabajo";
					}
				}

				// INGLÉS
				else if(locale.getLanguage().equals("en")) {

					if(categoriaTraducida.equals("Familia") ||
					   categoriaTraducida.equals("Famille")) {

						categoriaTraducida = "Family";
					}

					if(categoriaTraducida.equals("Amigos") ||
					   categoriaTraducida.equals("Amis")) {

						categoriaTraducida = "Friends";
					}

					if(categoriaTraducida.equals("Trabajo") ||
					   categoriaTraducida.equals("Travail")) {

						categoriaTraducida = "Work";
					}
				}

				// FRANCÉS
				else {

					if(categoriaTraducida.equals("Familia") ||
					   categoriaTraducida.equals("Family")) {

						categoriaTraducida = "Famille";
					}

					if(categoriaTraducida.equals("Amigos") ||
					   categoriaTraducida.equals("Friends")) {

						categoriaTraducida = "Amis";
					}

					if(categoriaTraducida.equals("Trabajo") ||
					   categoriaTraducida.equals("Work")) {

						categoriaTraducida = "Travail";
					}
				}

				modeloTabla.addRow(
						new Object[] {

								contacto.getNombre(),
								contacto.getTelefono(),
								contacto.getEmail(),
								categoriaTraducida
						});
			}

			// ======================= ACTUALIZAR TABLA =======================
			delegado.tabla.setModel(modeloTabla);

			// Ordenamiento
			sorter =
					new TableRowSorter<>(modeloTabla);

			delegado.tabla.setRowSorter(sorter);

			// ======================= ESTILO TABLA =======================
			delegado.tabla.setRowHeight(26);

			delegado.tabla.setFont(
					new java.awt.Font(
							"Times New Roman",
							java.awt.Font.PLAIN,
							18));

			delegado.tabla.getTableHeader().setFont(
					new java.awt.Font(
							"Times New Roman",
							java.awt.Font.BOLD,
							16));

			delegado.tabla.setBackground(
					new java.awt.Color(255,240,245));

			delegado.tabla.setForeground(
					new java.awt.Color(90,40,90));

			delegado.tabla.getTableHeader().setBackground(
					new java.awt.Color(255,182,193));

			delegado.tabla.getTableHeader().setForeground(
					java.awt.Color.WHITE);

		}
		catch(IOException e) {

			JOptionPane.showMessageDialog(
					delegado,
					"Error al cargar contactos");
		}
	}
	// ================================================================

	// ======================= LIMPIAR CAMPOS =======================
	private void limpiarCampos() {

		delegado.txt_nombres.setText("");

		delegado.txt_telefono.setText("");

		delegado.txt_email.setText("");

		categoria = "";

		favorito = false;

		delegado.chb_favorito.setSelected(false);

		delegado.cmb_categoria.setSelectedIndex(0);

		cargarContactosRegistrados();
	}
	// ===============================================================

	// ======================= MODIFICAR CONTACTO =======================
	private void modificarContacto() {

		int fila =
				delegado.tabla.getSelectedRow();

		if(fila == -1) {

			JOptionPane.showMessageDialog(
					delegado,
					"Seleccione un contacto");

			return;
		}

		int filaReal =
				delegado.tabla
				.convertRowIndexToModel(fila);

		try {

			persona personaModificada =
					new persona(

							delegado.txt_nombres.getText(),
							delegado.txt_telefono.getText(),
							delegado.txt_email.getText(),
							categoria,
							delegado.chb_favorito.isSelected()
					);

			contactos.set(
					filaReal,
					personaModificada);

			new personaDAO(
					new persona())
			.actualizarContactos(contactos);

			cargarContactosRegistrados();

			JOptionPane.showMessageDialog(
					delegado,
					"Contacto modificado");

		}
		catch(IOException e) {

			e.printStackTrace();
		}
	}
	// =================================================================

	// ======================= ELIMINAR =======================
	private void eliminarDesdeTabla() {

		int fila =
				delegado.tabla.getSelectedRow();

		if(fila == -1) {

			JOptionPane.showMessageDialog(
					delegado,
					"Seleccione un contacto");

			return;
		}

		int opcion =
				JOptionPane.showConfirmDialog(

						delegado,
						"¿Desea eliminar este contacto?",
						"Confirmar",
						JOptionPane.YES_NO_OPTION
				);

		if(opcion == JOptionPane.YES_OPTION) {

			try {

				int filaReal =
						delegado.tabla
						.convertRowIndexToModel(fila);

				contactos.remove(filaReal);

				new personaDAO(
						new persona())
				.actualizarContactos(contactos);

				cargarContactosRegistrados();

				JOptionPane.showMessageDialog(
						delegado,
						"Contacto eliminado");

			}
			catch(IOException ex) {

				ex.printStackTrace();
			}
		}
	}
	// ===============================================================

	// ======================= EVENTOS BOTONES =======================
	@Override
	public void actionPerformed(ActionEvent e) {

		inicializacionCampos();

		// ======================= AGREGAR =======================
		if(e.getSource() == delegado.btn_add) {

			if((!nombres.equals("")) &&
			   (!telefono.equals("")) &&
			   (!email.equals(""))) {

				if((!categoria.equals("Elija una Categoria")) &&
				   (!categoria.equals(""))) {

					persona =
							new persona(

									nombres,
									telefono,
									email,
									categoria,
									favorito
							);

					new personaDAO(persona)
					.escribirArchivo();

					limpiarCampos();

					JOptionPane.showMessageDialog(
							delegado,
							"Contacto registrado");

					// ======================= BARRA ROSADA =======================
					new Thread(() -> {

						for(int i = 0; i <= 100; i++) {

							delegado.barra.setValue(i);

							delegado.barra.setForeground(
									new java.awt.Color(
											255,
											105,
											180));

							try {

								Thread.sleep(10);

							}
							catch(Exception ex) {

							}
						}
					}).start();

				}
				else {

					JOptionPane.showMessageDialog(
							delegado,
							"Elija una categoría");
				}
			}
			else {

				JOptionPane.showMessageDialog(
						delegado,
						"Complete todos los campos");
			}
		}

		// ======================= ELIMINAR =======================
		else if(e.getSource() == delegado.btn_eliminar) {

			eliminarDesdeTabla();
		}
		

		// ======================= MODIFICAR =======================
		else if(e.getSource() == delegado.btn_modificar) {

			modificarContacto();
		}

		// ======================= EXPORTAR =======================
		else if(e.getSource() == delegado.btn_exportar) {

			JOptionPane.showMessageDialog(
					delegado,
					"Los contactos se guardan en:\nC:/gestionContactos/datosContactos.csv");
		}
	}
	// ===============================================================

	// ======================= EVENTO LISTA =======================
	@Override
	public void valueChanged(ListSelectionEvent e) {

		int index =
				delegado.lst_contactos
				.getSelectedIndex();

		if(index != -1 && index >= 0) {

			cargarContacto(index);
		}
	}
	// ===============================================================

	// ======================= CARGAR CONTACTO =======================
	private void cargarContacto(int index) {

		delegado.txt_nombres.setText(
				contactos.get(index).getNombre());

		delegado.txt_telefono.setText(
				contactos.get(index).getTelefono());

		delegado.txt_email.setText(
				contactos.get(index).getEmail());

		delegado.chb_favorito.setSelected(
				contactos.get(index).isFavorito());

		delegado.cmb_categoria.setSelectedItem(
				contactos.get(index).getCategoria());
	}
	// ===============================================================

	// ======================= EVENTO ITEM =======================
	@Override
	public void itemStateChanged(ItemEvent e) {

		// ======================= COMBOBOX =======================
		if(e.getSource() == delegado.cmb_categoria) {

			// Validar que no sea null
			if(delegado.cmb_categoria.getSelectedItem() != null) {

				categoria =
						delegado.cmb_categoria
						.getSelectedItem()
						.toString();
			}
		}

		// ======================= CHECKBOX =======================
		else if(e.getSource() == delegado.chb_favorito) {

			favorito =
					delegado.chb_favorito
					.isSelected();
		}
	}
}