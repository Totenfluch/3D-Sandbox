package me.christian.pack;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Sandbox extends Application {

	final Group root = new Group();
	final Group axisGroup = new Group();
	final Xform world = new Xform();
	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();
	final double cameraDistance = 600;
	final Xform moleculeGroup = new Xform();
	boolean timelinePlaying = false;
	double ONE_FRAME = 1.0/24.0;
	double DELTA_MULTIPLIER = 200.0;
	double CONTROL_MULTIPLIER = 0.1;
	double SHIFT_MULTIPLIER = 0.1;
	double ALT_MULTIPLIER = 0.5;

	private Shape3D prevObject;
	private Xform prevXform;
	private int objType = 0;
	
	private double pivotX=0;
	private double pivotY=0;
	private double pivotZ=0;

	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;

	private void buildScene() {
		System.out.println("buildScene");
		root.getChildren().add(world);
	}

	private void buildCamera() {
		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXform.ry.setAngle(320.0);
		cameraXform.rx.setAngle(40);
	}

	@Override
	public void start(Stage primaryStage) {
		System.out.println("start");
		buildScene();
		buildCamera();
		buildAxes();
		buildMolecule();


		Stage ToolStage = new Stage();
		VBox ToolBox = new VBox();
		ToolBox.setPrefSize(200, 768);

		Slider size = new Slider(1, 50, 1);
		size.setShowTickLabels(true);
		size.setShowTickMarks(true);
		size.setMajorTickUnit(0.5);
		size.valueProperty().addListener((a, b,c)->{
			{
				prevObject.setScaleX(c.doubleValue());
				prevObject.setScaleY(c.doubleValue());
				prevObject.setScaleZ(c.doubleValue());
			}
		});

		Button Sphere = new Button("Sphere");
		Sphere.setOnAction(ae ->{
			objType = 1;
			Material matBack = prevObject.getMaterial();
			prevXform.getChildren().clear();
			prevObject = new Sphere(10);
			prevObject.setScaleX(size.getValue());
			prevObject.setScaleY(size.getValue());
			prevObject.setScaleZ(size.getValue());
			prevObject.setMaterial(matBack);
			prevXform.getChildren().add(prevObject);
		});
		Button Box = new Button("Box");
		Box.setOnAction(ae ->{
			objType = 0;
			Material matBack = prevObject.getMaterial();
			prevXform.getChildren().clear();
			prevObject = new Box(10, 10, 10);
			prevObject.setScaleX(size.getValue());
			prevObject.setScaleY(size.getValue());
			prevObject.setScaleZ(size.getValue());
			prevObject.setMaterial(matBack);
			prevXform.getChildren().add(prevObject);
		});
		Button Cylinder = new Button("Cylinder");
		Cylinder.setOnAction(ae ->{
			objType = 2;
			Material matBack = prevObject.getMaterial();
			prevXform.getChildren().clear();
			prevObject = new Cylinder(10, 10);
			prevObject.setScaleX(size.getValue());
			prevObject.setScaleY(size.getValue());
			prevObject.setScaleZ(size.getValue());
			prevObject.setMaterial(matBack);
			prevXform.getChildren().add(prevObject);
		});
		
		
		HBox colors = new HBox();
		Button blue = new Button("     ");
		blue.setStyle("-fx-background-color: blue");
		blue.setOnAction(ae ->{
			final PhongMaterial mat = new PhongMaterial();
			mat.setDiffuseColor(Color.DARKBLUE);
			mat.setSpecularColor(Color.BLUE);
			prevObject.setMaterial(mat);
		});
		Button red = new Button("     ");
		red.setStyle("-fx-background-color: red");
		red.setOnAction(ae ->{
			final PhongMaterial mat = new PhongMaterial();
			mat.setDiffuseColor(Color.RED);
			mat.setSpecularColor(Color.DARKRED);
			prevObject.setMaterial(mat);
		});
		Button orange = new Button("     ");
		orange.setStyle("-fx-background-color: orange");
		orange.setOnAction(ae ->{
			final PhongMaterial mat = new PhongMaterial();
			mat.setDiffuseColor(Color.ORANGE);
			mat.setSpecularColor(Color.ORANGE);
			prevObject.setMaterial(mat);
		});
		Button indigo = new Button("     ");
		indigo.setStyle("-fx-background-color: indigo");
		indigo.setOnAction(ae ->{
			final PhongMaterial mat = new PhongMaterial();
			mat.setDiffuseColor(Color.INDIGO);
			mat.setSpecularColor(Color.INDIGO);
			prevObject.setMaterial(mat);
		});
		Button lime = new Button("     ");
		lime.setStyle("-fx-background-color: lime");
		lime.setOnAction(ae ->{
			final PhongMaterial mat = new PhongMaterial();
			mat.setDiffuseColor(Color.LIME);
			mat.setSpecularColor(Color.GREEN);
			prevObject.setMaterial(mat);
		});
		colors.getChildren().addAll(blue, red, orange, indigo, lime);
		colors.setSpacing(2);
		colors.setAlignment(Pos.CENTER);
		
		ToolBox.setStyle("-fx-background-color: black");
		ToolBox.getChildren().add(colors);



		HBox shapes = new HBox();
		shapes.setSpacing(2);
		shapes.setAlignment(Pos.CENTER);
		shapes.getChildren().addAll(Sphere, Box, Cylinder);
		ToolBox.getChildren().addAll(shapes, size);
		
		Button Place = new Button("Place Object");
		Place.setOnAction(ae ->{
			placeElement();
		});
		ToolBox.getChildren().add(Place);
		
		ToolBox.setPadding(new Insets(5));
		ToolBox.setSpacing(5);
		ToolBox.setAlignment(Pos.CENTER);

		ToolStage.setAlwaysOnTop(true);
		ToolStage.setX(150);
		Scene ToolScene = new Scene(ToolBox);
		ToolStage.setScene(ToolScene);
		ToolStage.show();

		primaryStage.setOnCloseRequest(ae ->{
			System.exit(0);
		});
		ToolStage.setOnCloseRequest(ae ->{
			System.exit(0);
		});


		Scene scene = new Scene(root, 1024, 768, true);
		scene.setFill(Color.GREY);
		handleKeyboard(ToolScene, world);
		handleMouse(ToolScene, world);
		handleKeyboard(scene, world);
		handleMouse(scene, world);

		primaryStage.setTitle("Minecraft für Arme");
		primaryStage.setScene(scene);
		primaryStage.show();
		scene.setCamera(camera);

	}
	
	private void placeElement(){
		Xform tempXform = new Xform();
		Shape3D myObj = null;
		if(objType == 0){
			myObj = new Box(10, 10, 10);
		}else if(objType == 1){
			myObj = new Sphere(10);
		}else if(objType == 2){
			myObj = new Cylinder(10, 10);
		}
		
		tempXform.setTranslateX(prevXform.getTranslateX());
		tempXform.setTranslateY(prevXform.getTranslateY());
		tempXform.setTranslateZ(prevXform.getTranslateZ());
		tempXform.setRotate(prevXform.getRotate());
		
		myObj.setMaterial(prevObject.getMaterial());
		myObj.setScaleX(prevObject.getScaleX());
		myObj.setScaleY(prevObject.getScaleY());
		myObj.setScaleZ(prevObject.getScaleZ());

		tempXform.getChildren().add(myObj);
		tempXform.getTransforms().clear();
		tempXform.getTransforms().add(new Rotate(prevXform.getRotate(), pivotX, pivotY, pivotZ));
		moleculeGroup.getChildren().add(tempXform);
	}

	private void buildAxes() {
		System.out.println("buildAxes()");
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);

		final Box xAxis = new Box(240.0, 1, 1);
		final Box yAxis = new Box(1, 240.0, 1);
		final Box zAxis = new Box(1, 1, 240.0);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		world.getChildren().addAll(axisGroup);
	}

	private void buildMolecule() {

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.BLUE);
		blueMaterial.setSpecularColor(Color.PURPLE);

		prevXform = new Xform();

		prevObject = new Box(10, 10, 10);
		prevObject.setMaterial(blueMaterial);
		prevObject.setOpacity(0.4);
		prevXform.getChildren().add(prevObject);
		prevXform.setTranslate(0, 0, 0);
		prevXform.setOpacity(0.4);
		moleculeGroup.getChildren().add(prevXform);

		world.getChildren().addAll(moleculeGroup);
	}

	private void handleMouse(Scene scene, final Node root) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent me) {
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent me) {
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = (mousePosX - mouseOldX); 
				mouseDeltaY = (mousePosY - mouseOldY); 

				double modifier = 1.0;
				double modifierFactor = 0.1;

				if (me.isControlDown()) {
					modifier = 0.1;
				} 
				if (me.isShiftDown()) {
					modifier = 10.0;
				}     
				if (me.isPrimaryButtonDown()) {
					cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*modifierFactor*modifier*2.0);  // +
					cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*modifierFactor*modifier*2.0);  // -
				}
				else if (me.isSecondaryButtonDown()) {
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX*modifierFactor*modifier;
					camera.setTranslateZ(newZ);
				}
				else if (me.isMiddleButtonDown()) {
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*modifierFactor*modifier*0.3);  // -
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*modifierFactor*modifier*0.3);  // -
				}
			}
		});
		scene.setOnScroll(ae ->{
			double z = camera.getTranslateZ();
			double newZ = z + ae.getDeltaY();
			camera.setTranslateZ(newZ);
		});
	}

	private void handleKeyboard(Scene scene, final Node root) {
		final boolean moveCamera = true;
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Duration currentTime;
				switch (event.getCode()) {
				case Z:
					if (event.isShiftDown()) {
						cameraXform.ry.setAngle(0.0);
						cameraXform.rx.setAngle(0.0);
						camera.setTranslateZ(-300.0);
					}   
					cameraXform2.t.setX(0.0);
					cameraXform2.t.setY(0.0);
					break;
				case X:
					if (event.isControlDown()) {
						if (axisGroup.isVisible()) {
							System.out.println("setVisible(false)");
							axisGroup.setVisible(false);
						}
						else {
							System.out.println("setVisible(true)");
							axisGroup.setVisible(true);
						}
					}   
					break;
				case S:
					if (event.isControlDown()) {
						if (moleculeGroup.isVisible()) {
							moleculeGroup.setVisible(false);
						}
						else {
							moleculeGroup.setVisible(true);
						}
					}
					prevXform.setTranslateX(prevXform.getTranslateX()-5);
					break;
				case SPACE:
					placeElement();
					break;
				case UP:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() - 10.0*CONTROL_MULTIPLIER);  
					}  
					else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 10.0*ALT_MULTIPLIER);  
					}
					else if (event.isControlDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() - 1.0*CONTROL_MULTIPLIER);  
					}
					else if (event.isAltDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 2.0*ALT_MULTIPLIER);  
					}
					else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z + 5.0*SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					break;
				case DOWN:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() + 10.0*CONTROL_MULTIPLIER);  
					}  
					else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 10.0*ALT_MULTIPLIER);  
					}
					else if (event.isControlDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() + 1.0*CONTROL_MULTIPLIER);  
					}
					else if (event.isAltDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0*ALT_MULTIPLIER);  
					}
					else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z - 5.0*SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					break;
				case RIGHT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() + 10.0*CONTROL_MULTIPLIER);  
					}  
					else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 10.0*ALT_MULTIPLIER);  
					}
					else if (event.isControlDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() + 1.0*CONTROL_MULTIPLIER);  
					}
					else if (event.isAltDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 2.0*ALT_MULTIPLIER);  
					}
					break;
				case LEFT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() - 10.0*CONTROL_MULTIPLIER);  
					}  
					else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 10.0*ALT_MULTIPLIER);  // -
					}
					else if (event.isControlDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() - 1.0*CONTROL_MULTIPLIER);  
					}
					else if (event.isAltDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0*ALT_MULTIPLIER);  // -
					}
					break;
				case  W:
					prevXform.setTranslateX(prevXform.getTranslateX()+5);
					break;
				case A:
					prevXform.setTranslateY(prevXform.getTranslateY()+5);
					break;
				case D:
					prevXform.setTranslateY(prevXform.getTranslateY()-5);
					break;
				case Q:
					prevXform.setTranslateZ(prevXform.getTranslateZ()+5);
					break;
				case E:
					prevXform.setTranslateZ(prevXform.getTranslateZ()-5);
					break;
				case R:
					prevXform.setRotate(prevXform.getRotate()+2);
					break;
				case F:
					pivotX+=2;
					prevXform.getTransforms().clear();
					prevXform.getTransforms().add(new Rotate(prevXform.getRotate(), pivotX, pivotY, pivotZ));
					break;
				case G:
					pivotY+=2;
					prevXform.getTransforms().clear();
					prevXform.getTransforms().add(new Rotate(prevXform.getRotate(), pivotX, pivotY, pivotZ));
					break;
				case H:
					pivotZ+=2;
					prevXform.getTransforms().clear();
					prevXform.getTransforms().add(new Rotate(prevXform.getRotate(), pivotX, pivotY, pivotZ));
					break;
				case J:
					prevXform.getTransforms().clear();
					prevXform.getTransforms().add(new Rotate(prevXform.getRotate()+2, pivotX, pivotY, pivotZ));
					break;
				}
			}
		});
	}

	public static void main(String[] args) {
		System.setProperty("prism.dirtyopts", "false");
		launch(args);
	}
}