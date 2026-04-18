package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter; // NUEVO: para evento teclado
import java.awt.event.KeyEvent;   // NUEVO: para detectar ENTER
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;   // NUEVO: menú clic derecho
import javax.swing.JMenuItem;    // NUEVO: opción eliminar
import javax.swing.table.DefaultTableModel; // NUEVO: JTable
import javax.swing.RowFilter; // NUEVO: filtro tabla
import javax.swing.table.TableRowSorter; // NUEVO: ordenar tabla

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vista.ventana;
import modelo.*;

//Definición de la clase logica_ventana que implementa eventos
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

	private ventana delegado; // Referencia a la ventana principal
	private String nombres, email, telefono, categoria=""; 
	private persona persona; 
	private List<persona> contactos; 
	private boolean favorito = false; 

	// NUEVO: para ordenar y filtrar la tabla
	private TableRowSorter<DefaultTableModel> sorter;

	// Constructor
	public logica_ventana(ventana delegado) {
	    this.delegado = delegado;

	    // Cargar contactos al iniciar
	    cargarContactosRegistrados(); 

	    // Eventos de botones
	    this.delegado.btn_add.addActionListener(this);
	    this.delegado.btn_eliminar.addActionListener(this);
	    this.delegado.btn_modificar.addActionListener(this);
	    this.delegado.btn_exportar.addActionListener(this);

	    // Evento lista
	    this.delegado.lst_contactos.addListSelectionListener(this);

	    // Eventos de selección
	    this.delegado.cmb_categoria.addItemListener(this);
	    this.delegado.chb_favorito.addItemListener(this);

	    // ======================= EVENTO TECLADO =======================
	    // Permite agregar contacto presionando ENTER
	    this.delegado.txt_nombres.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	                delegado.btn_add.doClick();
	            }
	        }
	    });

	    // ======================= FILTRO EN TIEMPO REAL =======================
	    this.delegado.txt_buscar.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            if(sorter != null){
	                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + delegado.txt_buscar.getText()));
	            }
	        }
	    });

	    // ======================= MENÚ CLICK DERECHO =======================
	    JPopupMenu menu = new JPopupMenu();
	    JMenuItem eliminar = new JMenuItem("Eliminar");
	    menu.add(eliminar);

	    delegado.tabla.setComponentPopupMenu(menu);

	    // ======================= ELIMINAR DESDE TABLA =======================
	    eliminar.addActionListener(e -> eliminarDesdeTabla());
	}
	// ======================= MODIFICAR CONTACTO =======================
	private void modificarContacto() {

	    int fila = delegado.tabla.getSelectedRow();

	    // Validar selección
	    if (fila == -1) {
	        JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para modificar");
	        return;
	    }

	    // Validar campos llenos
	    if (delegado.txt_nombres.getText().isEmpty() ||
	        delegado.txt_telefono.getText().isEmpty() ||
	        delegado.txt_email.getText().isEmpty() ||
	        categoria.equals("Elija una Categoria") || categoria.equals("")) {

	        JOptionPane.showMessageDialog(delegado, "Complete todos los campos correctamente");
	        return;
	    }

	    // Convertir índice visual a real (IMPORTANTE con filtro/orden)
	    int filaReal = delegado.tabla.convertRowIndexToModel(fila);

	    try {
	        // Crear nuevo objeto con datos actualizados
	        persona personaModificada = new persona(
	                delegado.txt_nombres.getText(),
	                delegado.txt_telefono.getText(),
	                delegado.txt_email.getText(),
	                categoria,
	                delegado.chb_favorito.isSelected()
	        );

	        // Reemplazar en la lista
	        contactos.set(filaReal, personaModificada);

	        // Guardar cambios en CSV
	        new personaDAO(new persona()).actualizarContactos(contactos);

	        // Recargar datos
	        cargarContactosRegistrados();

	        JOptionPane.showMessageDialog(delegado, "Contacto modificado correctamente");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// ======================= MÉTODO CENTRAL DE ELIMINACIÓN =======================
	private void eliminarDesdeTabla() {

	    int fila = delegado.tabla.getSelectedRow();

	    // Validación
	    if(fila == -1){
	        JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para eliminar");
	        return;
	    }

	    int opcion = JOptionPane.showConfirmDialog(delegado,
	            "¿Desea eliminar este contacto?",
	            "Confirmar eliminación",
	            JOptionPane.YES_NO_OPTION);

	    if(opcion == JOptionPane.YES_OPTION){

	        try {
	            // Convertir índice visual a real (IMPORTANTE con filtros)
	            int filaReal = delegado.tabla.convertRowIndexToModel(fila);

	            // Eliminar de la lista
	            contactos.remove(filaReal);

	            // Guardar cambios en archivo
	            new personaDAO(new persona()).actualizarContactos(contactos);

	            // Recargar interfaz
	            cargarContactosRegistrados();

	            JOptionPane.showMessageDialog(delegado, "Contacto eliminado correctamente");

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	// Inicializa campos
	private void incializacionCampos() {
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	// ======================= CARGAR CONTACTOS =======================
	private void cargarContactosRegistrados() {
		 try {
		        contactos = new personaDAO(new persona()).leerArchivo();
		        DefaultListModel modelo = new DefaultListModel();
		        for (persona contacto : contactos) {
		            modelo.addElement(contacto.formatoLista());
		        }

		        delegado.lst_contactos.setModel(modelo);
		        // ======================= TABLA =======================
		        DefaultTableModel modeloTabla = new DefaultTableModel(
		                new String[]{"Nombre","Teléfono","Email","Categoría"},0);

		        for (persona contacto : contactos) {
		            modeloTabla.addRow(new Object[]{
		                    contacto.getNombre(),
		                    contacto.getTelefono(),
		                    contacto.getEmail(),
		                    contacto.getCategoria()
		            });
		        }

		        delegado.tabla.setModel(modeloTabla);

		        // Ordenamiento y filtro
		        sorter = new TableRowSorter<>(modeloTabla);
		        delegado.tabla.setRowSorter(sorter);

		    } catch (IOException e) {
		        JOptionPane.showMessageDialog(delegado, "Error al cargar contactos");
		    }
	}

	// Limpiar campos
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

	// ======================= EVENTOS BOTONES =======================
	@Override
	public void actionPerformed(ActionEvent e) {
		incializacionCampos();

	    if (e.getSource() == delegado.btn_add) {

	        if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {

	            if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {

	                persona = new persona(nombres, telefono, email, categoria, favorito);
	                new personaDAO(persona).escribirArchivo();

	                limpiarCampos();

	                JOptionPane.showMessageDialog(delegado, "Contacto Registrado");

	                // Barra de progreso
	                new Thread(() -> {
	                    for(int i=0;i<=100;i++){
	                        delegado.barra.setValue(i);
	                        try { Thread.sleep(10); } catch(Exception ex){}
	                    }
	                }).start();

	            } else {
	                JOptionPane.showMessageDialog(delegado, "Elija una categoría");
	            }

	        } else {
	            JOptionPane.showMessageDialog(delegado, "Complete todos los campos");
	        }

	    } 
	    else if (e.getSource() == delegado.btn_eliminar) {
	        // ======================= ELIMINAR DESDE BOTÓN =======================
	        eliminarDesdeTabla();
	    } 
	    else if (e.getSource() == delegado.btn_modificar) {
	        modificarContacto();
	    } 
	    else if (e.getSource() == delegado.btn_exportar) {
	        JOptionPane.showMessageDialog(delegado, 
	            "Los contactos se guardan en: C:/gestionContactos/datosContactos.csv");
	    }
	}

	// Evento lista
	@Override
	public void valueChanged(ListSelectionEvent e) {
	    int index = delegado.lst_contactos.getSelectedIndex();
	    if (index != -1 && index > 0) {
	        cargarContacto(index);
	    }
	}

	// Cargar contacto en campos
	private void cargarContacto(int index) {
	    delegado.txt_nombres.setText(contactos.get(index).getNombre());
	    delegado.txt_telefono.setText(contactos.get(index).getTelefono());
	    delegado.txt_email.setText(contactos.get(index).getEmail());
	    delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
	    delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	// Evento cambios
	@Override
	public void itemStateChanged(ItemEvent e) {
	    if (e.getSource() == delegado.cmb_categoria) {
	        categoria = delegado.cmb_categoria.getSelectedItem().toString();
	    } else if (e.getSource() == delegado.chb_favorito) {
	        favorito = delegado.chb_favorito.isSelected();
	    }
	}
}