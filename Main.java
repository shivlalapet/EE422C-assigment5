package assignment5;
/*
 * Critter2 - GUI
 * Shiv Lalapet
 * EID: sl39596
 * Hamza Sharif
 * EID: mhs2285
 * Unique: ?????
 * Spring 2018
 */

//imports
import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.util.*;
import java.io.*;
import assignment5.Critter.CritterShape; //from abstract critter class

public class Main extends Application {
	private static String myPackage;
	static {myPackage = Critter.class.getPackage().toString().split(" ")[1];}

	//UI tools
	static GridPane scene = new GridPane();
	static GridPane world = new GridPane();
	static VBox controls = new VBox(10);
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static ComboBox<String> critterList = new ComboBox<String>();
	static List<String> existingCritters = new java.util.ArrayList<>();

	//Screen size
	static double screen_width = screenSize.getWidth();
	static double screen_height = screenSize.getHeight();

	//Shapes
	static Polygon square = new Polygon();
	static Polygon triangle = new Polygon();
	static Polygon diamond = new Polygon();
	static Polygon star = new Polygon();
	static Circle circle = new Circle();

	//Quit
	static VBox quit = new VBox();
	static Button quit_button = new Button("Quit");

	//Make pane
	static GridPane make_gridPane = new GridPane();
	static Button make_button = new Button("Make");
	static Label make_numCritters = new Label("Number of Critters");
	static Label make_multiplier = new Label("Mutiplier");
	static Slider make_numSlider = new Slider(0,100,1);
	static Slider make_multiplierSlider = new Slider(0,10,1);
	static Label make_value = new Label(Integer.toString((int)make_numSlider.getValue()));

	//Step pane
	static GridPane step_gridPane = new GridPane();
	static Button step_button = new Button("Step");
	static Label step_numCritters = new Label("Number of Critters");
	static Label step_multiplier = new Label("Mutiplier");
	static Slider step_numSlider = new Slider(0,100,1);
	static Slider step_multiplierSlider = new Slider(0,10,1);
	static Label step_value = new Label(Integer.toString((int)step_numSlider.getValue()));

	//Animation pane
	static class AnimateTimer extends AnimationTimer{
		private int count = 0;
		AnimateTimer(int count){
			this.count = count;
		}
		@Override
		public void handle(long now) {
			for(int i=0; i < count; i++){
				Critter.worldTimeStep();
			}
			Critter.displayWorld();
		}
	}

	static GridPane animation_gridPane = new GridPane();
	static Button animation_button = new Button("Animate");
	static Label animation_speed = new Label("Animation Speed");
	static Slider animation_slider = new Slider(0,100,1);
	static Label animation_value = new Label(Integer.toString((int)animation_slider.getValue()));
	static boolean animation_boolean = false;
	static AnimateTimer clock = new AnimateTimer((int)animation_slider.getValue());

	//Seed pane
	static GridPane seed_gridPane = new GridPane();
	static Button seed_button = new Button("Set Seed");
	static Label seed_label = new Label("Seed");
	static TextField seed_textField = new TextField();
	/*
	@Override public void replaceText(int start, int end, String text){
		if(text.matches("[0-9]*"))
			super.replaceText(start, end, text);
	}
	@Override public void replaceSelection(String text){
		if(text.matches("[0-9]*"))
			super.replaceSelection(text);
	}
	*/

	//Run stats
	static class Console extends OutputStream{
		private TextArea text;
		public Console(TextArea input){
			this.text = input;
		}
		@Override public void write(int num) throws IOException {
			text.appendText(String.valueOf((char) num));
		}
	}
	static VBox stats_tracker = new VBox();
	static Button stats_button = new Button("Run Stats");
	static TextArea stats_textArea = new TextArea();
	static PrintStream p_stream;
	static boolean combo = false;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Starts the program
	 * @param primary
	 */
	@Override public void start(Stage primary) throws ClassNotFoundException, URISyntaxException {
		primary.setTitle("Critters 2");

		setScene();
		setWorld();
		setShapes();
		setAnimation();
		setStep();
		setMake();
		setQuit();
		//setCombo();
		setStats();
		setUpSeed();
		setHandlers();

		//add pane controls
		controls.getChildren().add(critterList);
		controls.getChildren().add(make_gridPane);
		controls.getChildren().add(step_gridPane);
		controls.getChildren().add(seed_gridPane);
		controls.getChildren().add(stats_tracker);
		controls.getChildren().add(quit);

		//add to scene
		scene.add(world,0,0);
		scene.add(controls,2,0);
		scene.add(animation_gridPane,0,1);

		//set stage
		primary.setScene(new Scene(scene, screenSize.getWidth(), screenSize.getHeight()));
		primary.show();
	}

	/**
	 * Initialize the scene
	 */
	private static void setScene(){
		scene.setHgap(10);
		scene.setPadding(new Insets(10,10,10,10));

		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(75);
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(25);
		c2.setHalignment(HPos.CENTER);
		scene.getColumnConstraints().add(c1);
		scene.getColumnConstraints().add(c2);


		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(85);
		r1.setValignment(VPos.CENTER);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(15);
		r2.setValignment(VPos.TOP);
		scene.getRowConstraints().add(r1);
		scene.getRowConstraints().add(r2);
	}

	/**
	 * Set up the world
	 */
	private static void setWorld(){
		world.setGridLinesVisible(true);

		for(int i=0; i<Params.world_width; i++){
			ColumnConstraints c1 = new ColumnConstraints();
			c1.setPercentWidth(100);
			c1.setHalignment(HPos.LEFT);
			world.getColumnConstraints().add(c1);
		}
		for(int j=0; j<Params.world_height; j++){
			RowConstraints r1 = new RowConstraints();
			r1.setPercentHeight(100);
			r1.setValignment(VPos.TOP);
			world.getRowConstraints().add(r1);
		}
	}

	/**
	 * Set up shapes
	 */
	private static void setShapes(){
		double newSize = 0;
		double f1 = (screen_height / 2) / Params.world_height;
		double f2 = (screen_width * 0.3) / Params.world_width;
		if(f1 < f2)
			newSize = f1;
		else
			newSize = f2;
		circle.setRadius(newSize/2);
		square.getPoints().clear();
		square.getPoints().addAll(0.0,0.0,newSize,0.0,newSize,newSize,0.0,newSize);
		triangle.getPoints().clear();
		triangle.getPoints().addAll(newSize/2,0.0,0.0,newSize,newSize,newSize);
		diamond.getPoints().clear();
		diamond.getPoints().addAll(newSize/2,0.0,newSize,newSize/2,newSize/2,newSize,0.0,newSize/2);
		star.getPoints().clear();
		star.getPoints().addAll(newSize/2,-(newSize/12), newSize/1.6, newSize/4, newSize, newSize/4, newSize/1.33, newSize/2, newSize/1.2, newSize/1.2, newSize/2, newSize/2, newSize/6, newSize/6, newSize/4, newSize/2, 0.0, newSize/4, newSize/2.67, newSize/4);
	}

	/**
	 * Set up the quit button
	 */
	private static void setQuit(){
		quit.setAlignment(Pos.CENTER);
		quit.setPadding(new Insets(20,0,0,0));
		quit.getChildren().add(quit_button);
		quit_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
	}

	/**
	 * Set up the make feature
	 */
	private static void setMake(){
		//set up columns and rows
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(75);
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(25);
		c2.setHalignment(HPos.CENTER);
		make_gridPane.getColumnConstraints().add(c1);
		make_gridPane.getColumnConstraints().add(c2);

		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(50);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(50);
		RowConstraints r3 = new RowConstraints();
		r3.setPercentHeight(50);
		RowConstraints r4 = new RowConstraints();
		r4.setPercentHeight(50);
		make_gridPane.getRowConstraints().add(r1);
		make_gridPane.getRowConstraints().add(r2);
		make_gridPane.getRowConstraints().add(r3);
		make_gridPane.getRowConstraints().add(r4);

		//set up slider
		make_numSlider.setShowTickMarks(true);
		make_numSlider.setShowTickLabels(true);
		make_numSlider.setMajorTickUnit(25);
		make_numSlider.setMinorTickCount(4);
		make_numSlider.setPrefWidth(1000);
		//account for change in slider using handler override

		//set up multiplier
		make_multiplierSlider.setShowTickMarks(true);
		make_multiplierSlider.setShowTickLabels(true);
		make_multiplierSlider.setMajorTickUnit(5);
		make_multiplierSlider.setMinorTickCount(4);
		make_multiplierSlider.setPrefWidth(1000);
		//account for change in slider using handler override

		//add to pane
		make_gridPane.add(make_numCritters,0,0);
		make_gridPane.add(make_numSlider,0,1);
		make_gridPane.add(make_multiplier,0,2);
		make_gridPane.add(make_multiplierSlider,0,3);
		make_gridPane.add(make_value,1,1);
		make_gridPane.add(make_button,1,2);
	}

	/**
	 * Set up the step feature
	 */
	private static void setStep(){
		//set up columns and rows
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(75);
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(25);
		c2.setHalignment(HPos.CENTER);
		step_gridPane.getColumnConstraints().add(c1);
		step_gridPane.getColumnConstraints().add(c2);

		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(50);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(50);
		RowConstraints r3 = new RowConstraints();
		r3.setPercentHeight(50);
		RowConstraints r4 = new RowConstraints();
		r4.setPercentHeight(50);
		step_gridPane.getRowConstraints().add(r1);
		step_gridPane.getRowConstraints().add(r2);
		step_gridPane.getRowConstraints().add(r3);
		step_gridPane.getRowConstraints().add(r4);

		//set up slider
		step_numSlider.setShowTickMarks(true);
		step_numSlider.setShowTickLabels(true);
		step_numSlider.setMajorTickUnit(25);
		step_numSlider.setMinorTickCount(4);
		step_numSlider.setPrefWidth(1000);
		//account for change in slider using handler override

		//set up multiplier
		step_multiplierSlider.setShowTickMarks(true);
		step_multiplierSlider.setShowTickLabels(true);
		step_multiplierSlider.setMajorTickUnit(5);
		step_multiplierSlider.setMinorTickCount(4);
		step_multiplierSlider.setPrefWidth(1000);
		//account for change in slider using handler override

		//add to pane
		step_gridPane.add(step_numCritters,0,0);
		step_gridPane.add(step_numSlider,0,1);
		step_gridPane.add(step_multiplier,0,2);
		step_gridPane.add(step_multiplierSlider,0,3);
		step_gridPane.add(step_value,1,1);
		step_gridPane.add(step_button,1,2);
	}

	/**
	 * Set up the combo feature
	 */
	private static void setCombo(){
		String[] file = (new File("src/assignment5")).list();
		for(int i = 0; i < file.length; i++){
			file[i] = file[i].substring(0,file[i].length()-5);
		}
		for(String name : file){
			Class<?> crit;
			Constructor<?> constructor;
			Object clone = null;
			try{
				crit = Class.forName("assignment5." + name);
			}
			catch (ClassNotFoundException e){continue;}
			try{
				constructor = crit.getConstructor();
				clone = constructor.newInstance();
			}
			catch (Exception e){continue;}
			try{
				Critter newCrit = (Critter)clone;
				newCrit.getEnergy();
				if(Critter.class.isAssignableFrom(crit)){
					existingCritters.add(name);
				}
			}
			catch (Exception e){}
		}
		critterList.getItems().add(String.valueOf(existingCritters));
		world.add(critterList,4,1);
	}

	/**
	 * Set up the animation feature
	 */
	private static void setAnimation(){
		//set up columns and rows
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(60);
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(50);
		c2.setHalignment(HPos.CENTER);
		ColumnConstraints c3 = new ColumnConstraints();
		c3.setPercentWidth(20);
		c3.setHalignment(HPos.CENTER);
		animation_gridPane.getColumnConstraints().add(c1);
		animation_gridPane.getColumnConstraints().add(c2);
		animation_gridPane.getColumnConstraints().add(c3);

		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(50);
		r1.setValignment(VPos.BOTTOM);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(50);
		r2.setValignment(VPos.CENTER);
		animation_gridPane.getRowConstraints().add(r1);
		animation_gridPane.getRowConstraints().add(r2);

		animation_slider.setShowTickMarks(true);
		animation_slider.setShowTickLabels(true);
		animation_slider.setMajorTickUnit(25);
		animation_slider.setPrefWidth(1000);
		//account for change in slider using handler override

		//add to pane
		animation_gridPane.setPadding(new Insets(0,0,10,0));
		animation_gridPane.add(animation_speed,0,0);
		animation_gridPane.add(animation_slider,0,1);
		animation_gridPane.add(animation_value,1,1);
		animation_gridPane.add(animation_button,2,1);
	}

	/**
	 * Set up the seed feature
	 * Disclaimer: already a setSeed() method in Critter.java
	 */
	private static void setUpSeed(){
		//set up columns and rows
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(60);
		c1.setHalignment(HPos.CENTER);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(20);
		c2.setHalignment(HPos.CENTER);
		scene.getColumnConstraints().add(c1);
		scene.getColumnConstraints().add(c2);

		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(50);
		r1.setValignment(VPos.BOTTOM);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(50);
		r2.setValignment(VPos.CENTER);
		scene.getRowConstraints().add(r1);
		scene.getRowConstraints().add(r2);
		seed_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try{
					Critter.setSeed(Long.parseLong(seed_textField.getText()));
				}
				catch (NumberFormatException e){
					System.out.println("Invalid number");
				}
			}
		});

		seed_gridPane.setAlignment(Pos.CENTER);
		seed_gridPane.add(seed_label,0,0);
		seed_gridPane.add(seed_textField,0,1);
		seed_gridPane.add(seed_button,1,1);
	}

	/**
	 * Set up the stats feature
	 */
	private static void setStats(){
		stats_tracker.setAlignment(Pos.CENTER);
		stats_tracker.setPadding(new Insets(20,0,0,0));
		stats_tracker.setSpacing(10);
		stats_tracker.getChildren().add(stats_textArea);
		stats_tracker.getChildren().add(stats_button);
		//runStatsHelper();
	}

	/**
	 * Sets up the handlers for each particular feature above
	 */
	private static void setHandlers(){
		//Step
		step_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int count = (int)(step_numSlider.getValue() * step_multiplierSlider.getValue());
				for(int i=0; i < count; i++){
					Critter.worldTimeStep();
				}
				Critter.displayWorld();
			}
		});


		//Make
		make_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int count = (int)(make_numSlider.getValue() * make_multiplierSlider.getValue());
				for(int i=0; i < count; i++){
					try{
						Critter.makeCritter(critterList.getValue());
					} catch (Exception e){}
				}
				Critter.displayWorld();
			}
		});


		//Combo
		critterList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				combo = true;
			}
		});

		//Animation
		animation_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try{
					clock.count = (int)animation_slider.getValue();
					animation_boolean = !animation_boolean;
					if(animation_boolean){
						animation_button.setText("Stop");
						clock.start();
						disableControls();
					}
					else{
						animation_button.setText("Animate");
						clock.stop();
						disableControls();
					}
				}
				catch (Exception e){}
			}
		});

		//Stats
		stats_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stats_textArea.clear();
				Console comp = new Console(stats_textArea);
				PrintStream p = new PrintStream(comp);
				System.setOut(p);
				System.setErr(p);
				runStatsHelper();
			}
		});
	}

	public static void refreshWindow(){
		//Clear world
		Node root = world.getChildren().get(0);	//secure root node
		world.getChildren().clear();
		world.getChildren().add(0,root);

		String s = "Crittersgetter";
		try{
			List<Critter> temp = Critter.getInstances(s);
			for(Critter c : temp){
				//store current point coordinates
				int x = c.getX();
				int y = c.getY();
				CritterShape shape = c.viewShape();
				//Critter shape is a circle
				if(shape == CritterShape.CIRCLE){
					Circle round = new Circle();
					round.setFill(c.viewFillColor());
					round.setStroke(c.viewOutlineColor());
					round.setRadius(circle.getRadius());
					world.add(round,x,y);
				}
				//else shape is either a square,triangle,diamond,or star
				else{
					Polygon p = new Polygon();
					p.setFill(c.viewFillColor());
					p.setStroke(c.viewOutlineColor());
					if(shape == CritterShape.SQUARE){
						p.getPoints().addAll(square.getPoints());
					}
					else if(shape == CritterShape.TRIANGLE){
						p.getPoints().addAll(triangle.getPoints());
					}
					else if(shape == CritterShape.DIAMOND){
						p.getPoints().addAll(diamond.getPoints());
					}
					else if(shape == CritterShape.STAR){
						p.getPoints().addAll(star.getPoints());
					}
					world.add(p,x,y);
				}
				//Run stats
				stats_textArea.clear();
				Console comp = new Console(stats_textArea);
				PrintStream ps = new PrintStream(comp);
				System.setOut(ps);
				System.setErr(ps);
				runStatsHelper();
			}
		}
		catch (Exception e){}
	}

	/**
	 * RunStats helper method
	 */
	private static void runStatsHelper(){
			try { Class.forName(myPackage + "." + critterList.getValue()).getMethod("runStats", List.class).invoke(critterList.getValue(), Critter.getInstances(critterList.getValue()));
			}
			catch (Exception e){}
	}

	/**
	 * Disables all controls (minus animation button) when an animation is running
	 */
	private static void disableControls(){
		make_numSlider.setDisable(animation_boolean);
		make_multiplierSlider.setDisable(animation_boolean);
		make_button.setDisable(animation_boolean);
		step_numSlider.setDisable(animation_boolean);
		step_multiplierSlider.setDisable(animation_boolean);
		step_button.setDisable(animation_boolean);
		animation_slider.setDisable(animation_boolean);
		stats_button.setDisable(animation_boolean);
		seed_textField.setDisable(animation_boolean);
		seed_button.setDisable(animation_boolean);
		quit_button.setDisable(animation_boolean);
	}
}
