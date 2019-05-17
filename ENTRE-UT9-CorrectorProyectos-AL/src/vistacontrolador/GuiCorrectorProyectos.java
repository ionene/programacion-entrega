
package vistacontrolador;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.AlumnoNoExistenteExcepcion;
import modelo.CorrectorProyectos;
import modelo.Proyecto;

/** 
 * @author Ione Ibañez
 */

public class GuiCorrectorProyectos extends Application
{

	private MenuItem itemLeer;
	private MenuItem itemGuardar;
	private MenuItem itemSalir;

	private TextField txtAlumno;
	private Button btnVerProyecto;

	private RadioButton rbtAprobados;
	private RadioButton rbtOrdenados;
	private Button btnMostrar;

	private TextArea areaTexto;

	private Button btnClear;
	private Button btnSalir;

	private CorrectorProyectos corrector; // el modelo

	@Override
	public void start(Stage stage) {

		corrector = new CorrectorProyectos();

		BorderPane root = crearGui();

		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.setTitle("- Corrector de proyectos -");
		scene.getStylesheets().add(getClass()
		                .getResource("/css/application.css").toExternalForm());
		stage.show();
	}

	private BorderPane crearGui() {

		BorderPane panel = new BorderPane();
		MenuBar barraMenu = crearBarraMenu();
		panel.setTop(barraMenu);

		VBox panelPrincipal = crearPanelPrincipal();
		panel.setCenter(panelPrincipal);

		HBox panelBotones = crearPanelBotones();
		panel.setBottom(panelBotones);

		return panel;
	}

	private MenuBar crearBarraMenu() {

		MenuBar barraMenu = new MenuBar();

		Menu menu = new Menu("Archivo");

		itemLeer = new MenuItem("_Leer de fichero");
		itemLeer.setAccelerator(KeyCombination.keyCombination("CTRL+L"));
		itemLeer.setOnAction(event->leerDeFichero());
		
		itemGuardar = new MenuItem("_Guardar en fichero");
		itemGuardar.setAccelerator(KeyCombination.keyCombination("CTRL+G"));
		itemGuardar.setDisable(true);
		itemGuardar.setOnAction(event->salvarEnFichero());
		
		itemSalir = new MenuItem("_Salir");
		itemSalir.setAccelerator(KeyCombination.keyCombination("CTRL+S"));
		itemSalir.setOnAction(event->salir());

		menu.getItems().addAll(itemLeer, itemGuardar, new SeparatorMenuItem(), itemSalir);
		barraMenu.getMenus().add(menu);
		
		return barraMenu;
	}

	private VBox crearPanelPrincipal() {

		VBox panel = new VBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);

		Label lblEntrada = new Label("Panel de entrada");
		lblEntrada.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		lblEntrada.getStyleClass().add("titulo-panel");
		
		Label lblOpciones = new Label("Panel de opciones");
		lblOpciones.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		lblOpciones.getStyleClass().add("titulo-panel");
	
		areaTexto = new TextArea();
		areaTexto.setMaxHeight(Integer.MAX_VALUE);
		VBox.setVgrow(areaTexto, Priority.ALWAYS);
		
		HBox entrada = crearPanelEntrada();
		HBox opciones = crearPanelOpciones();

		panel.getChildren().addAll(lblEntrada, entrada, lblOpciones, opciones, areaTexto);
		return panel;
	}

	private HBox crearPanelEntrada() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);

		Label lblAlumno = new Label("Alumno");
		
		txtAlumno = new TextField();
		txtAlumno.setPrefColumnCount(30);
		txtAlumno.setAlignment(Pos.CENTER_LEFT);
		txtAlumno.setOnAction(event->verProyecto());
		
		btnVerProyecto = new Button("Ver proyecto");
		btnVerProyecto.setPrefWidth(120);
		btnVerProyecto.setOnAction(event->verProyecto());

		panel.getChildren().addAll(lblAlumno, txtAlumno, btnVerProyecto);
		return panel;
	}

	private HBox crearPanelOpciones() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(50);
		panel.setAlignment(Pos.CENTER);

		rbtAprobados = new RadioButton("Mostrar aprobados");
		rbtAprobados.setSelected(true);

		
		rbtOrdenados = new RadioButton("Mostrar ordenados");
		
		ToggleGroup grupo = new ToggleGroup();
		rbtAprobados.setToggleGroup(grupo);
		rbtOrdenados.setToggleGroup(grupo);
		
		btnMostrar = new Button("Mostrar");
		btnMostrar.setPrefWidth(120);
		btnMostrar.setOnAction(event->mostrar());
		
		panel.getChildren().addAll(rbtAprobados, rbtOrdenados, btnMostrar);
		return panel;
	}

	private HBox crearPanelBotones() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);
		panel.setAlignment(Pos.CENTER_RIGHT);	
		
		btnClear = new Button("Clear");
		btnClear.setPrefWidth(90);
		btnClear.setOnAction(event->clear());
		
		btnSalir = new Button("Salir");
		btnSalir.setPrefWidth(90);
		btnSalir.setOnAction(event->salir());

		panel.getChildren().addAll(btnClear, btnSalir);
		return panel;
	}

	private void salvarEnFichero() {

		try {
			corrector.guardarOrdenadosPorNota();
			areaTexto.setText("Guardados en fichero de texto los proyectos ordenados");
		} catch (IOException e) {
			areaTexto.setText("No se ha podido guardar");
		}

		itemLeer.setDisable(false);
		itemGuardar.setDisable(true);

	}

	private void leerDeFichero() {
		
		corrector.leerDatosProyectos();
		areaTexto.setText("Leído fichero de texto\n" + "\n" + corrector.getErrores().toString() + "\n" + "\nYa están guardados en memoria los datos de los proyectos");
		
		itemLeer.setDisable(true);
		itemGuardar.setDisable(false);
	}

	private void verProyecto() {

		try {
			if(txtAlumno.getText().isEmpty())
			{
				areaTexto.setText("Inserta un nombre de alumno");
				cogerFoco();
			} else if(itemGuardar.isDisable()) {
				areaTexto.setText("Todavía no se ha leído ningún archivo");
				cogerFoco();
			}else {
			Proyecto pro = corrector.proyectoDe(String.valueOf(txtAlumno.getText()));
			areaTexto.setText(pro.toString());
			cogerFoco();
			}
		}
		catch(AlumnoNoExistenteExcepcion e) {
			areaTexto.setText(e.getMessage());
			cogerFoco();
		}
		
	}

	private void mostrar() {

		clear();
		if(itemGuardar.isDisable())
		{
			areaTexto.setText("Todavía no se ha leído ningún archivo");
			cogerFoco();
		}else if(rbtAprobados.isSelected()) {
			areaTexto.setText("Nº de proyectos aprobados: " + corrector.aprobados());
		}else {
			List<Entry<String, Proyecto>> lista  = corrector.ordenadosPorNota();
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Proyecto> entrada : lista) {
				sb.append(String.format("%20s: %s\n", "Alumno/a",
				                entrada.getKey()));
				sb.append(entrada.getValue());
				sb.append("-------------------------------------------\n");
			}

 
			areaTexto.setText(sb.toString());
		}
	}

	private void cogerFoco() {

		txtAlumno.requestFocus();
		txtAlumno.selectAll();

	}

	private void salir() {

		System.exit(0);
	}

	private void clear() {

		areaTexto.clear();
		cogerFoco();
	}

	public static void main(String[] args) {

		launch(args);
	}
}
