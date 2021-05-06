package a2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.rage.util.BufferUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.shader.*;
import java.util.ArrayList;
import net.java.games.input.Controller;
import ray.rage.asset.material.Material;
import java.util.Random;
import myGameEngine.*;

public class MyGame extends VariableFrameRateGame {

	// to minimize variable allocation in update()
	GL4RenderSystem rs;
	private float elapsTime1, elapsTime2;
	private String elapsTimeStr1, visitedStr1, dispStr1,
		   elapsTimeStr2, visitedStr2, dispStr2;
	private int elapsTimeSec1, elapsTimeSec2, counter1, 
				counter2, foodPickedUp1, foodPickedUp2;
	private SceneNode nodeArr[], foodArr[];
	private boolean visitedArr1[],visitedArr2[], pickedUpArr[],
			gotFood1, gotFood2, bool;
	private Random random;
	private InputManager im;
	private SceneNode dolphinN,foodN1, foodN2, foodN3, foodN4,
			foodN5, foodN6 , dolphinChild, earthNG, finishLineN;
	
	private Action moveForwardAction1, moveForwardAction2, 
			moveBackwardAction1,moveBackwardAction2,
			moveLeftAction1, moveLeftAction2,moveRightAction1,moveRightAction2,
			quitGameAction,rideAction,rotateLeftAction,rotateRightAction,
			rotateUpAction, rotateDownAction, orbitAAction1, orbitRightAction,
			orbitLeftAction,orbitElevationAction, zoomInAction1,zoomOutAction1,
			zoomInAction2,zoomOutAction2,lookUpAction, lookDownAction,
			xStickAction, yStickAction , locationAction;
			
	private Camera camera;
	private Camera3Pcontroller orbitController1, orbitController2;
	private BounceController bc ;
	private OrbitController oc1, oc2;
	private RotationController rc,rcLine;
	private StretchController sc;
	
	
	
    public MyGame() {
        super();
		nodeArr = new SceneNode[3];
		foodArr = new SceneNode[6];
		counter1 = 0;
		counter2 = 0;
		//visitedArr1 = new boolean[3];
		//visitedArr2 = new boolean[3];
		visitedArr1 = new boolean[]{false,false,false};
		visitedArr2 = new boolean[]{false,false,false};
		//pickedUpArr = new boolean[6];
		pickedUpArr = new boolean[]{false,false,false,false,false,false};
		
		
		gotFood1 = false;
		gotFood2 = false;
		bool = false;
		random = new Random();
		
		bc = new BounceController();
		sc = new StretchController();
		
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
    public static void main(String[] args) {
        Game game = new MyGame();
		System.out.println("Welcome to Food Delivery game.");
		System.out.println("Player 1, black dolphin, will being using the gamepad.");
		System.out.println("Player 2, white dolphin, will being using the keyboard.");
		System.out.println("Gamepad buttons guide:");
		System.out.println("	Y Stick: move forward/backward");
		System.out.println("	X Stick: steer left/right");
		System.out.println("	RY Stick: orbit up/down");
		System.out.println("	RX Stick: orbit left/right");
		System.out.println("	Button 3/Y: zoom in");
		System.out.println("	Button 0/A: zoom out");
		System.out.println();
		System.out.println("Keyboard guide:");
		System.out.println("	W/S: move forward/backward");
		System.out.println("	A/D: steer left/right");
		System.out.println("	Arrows: orbit around dolphin");
		System.out.println("	Key 2: zoom in");
		System.out.println("	Key X: zoom out");
		System.out.println("ESC: Quit game");
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }
	
	/*------------------------------------------------------------------------------------------------------------------------*/
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
	protected void setupWindowViewports(RenderWindow rw)
	{ 
		rw.addKeyListener(this);
		Viewport topViewport = rw.getViewport(0);
		topViewport.setDimensions(.51f, .01f, .99f, .49f); // B,L,W,H
		topViewport.setClearColor(new Color(1.0f, 0.764f, 0.0f));
		Viewport botViewport = rw.createViewport(.01f, .01f, .99f, .49f);
		botViewport.setClearColor(new Color(1.0f, 0.764f, 0.0f));
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
	
    @Override
	protected void setupCameras(SceneManager sm, RenderWindow rw)
	{ 
		SceneNode rootNode = sm.getRootSceneNode();
		Camera camera = sm.createCamera("MainCamera",
		Projection.PERSPECTIVE);
		rw.getViewport(0).setCamera(camera);
		SceneNode cameraN =
		rootNode.createChildSceneNode("MainCameraNode");
		cameraN.attachObject(camera);
		camera.setMode('n');
		camera.getFrustum().setFarClipDistance(1000.0f);
		
		Camera camera2 = sm.createCamera("MainCamera2",
		Projection.PERSPECTIVE);
		rw.getViewport(1).setCamera(camera2);
		SceneNode cameraN2 =
		rootNode.createChildSceneNode("MainCamera2Node");
		cameraN2.attachObject(camera2);
		camera2.setMode('n');
		camera2.getFrustum().setFarClipDistance(1000.0f);
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		
		//setup dolphin1
        Entity dolphinE1 = sm.createEntity("dolphin1", "dolphinHighPoly.obj");
        dolphinE1.setPrimitive(Primitive.TRIANGLES);
        SceneNode dolphinN1 = sm.getRootSceneNode().
			createChildSceneNode("dolphin1Node");
        dolphinN1.attachObject(dolphinE1);
		dolphinN1.setLocalPosition(-1.0f, 0.4f, -29.0f);
		
		//setup dolphin2
        Entity dolphinE2 = sm.createEntity("dolphin2", "dolphinLowPoly.obj");
        dolphinE2.setPrimitive(Primitive.TRIANGLES);
        SceneNode dolphinN2 = sm.getRootSceneNode().
			createChildSceneNode("dolphin2Node");
        dolphinN2.attachObject(dolphinE2);
		dolphinN2.setLocalPosition(1.0f, 0.4f, -30.0f);
		
		
		//Set up group node for planets
		earthNG = sm.getRootSceneNode().
			createChildSceneNode("myEarthNodeG");
		
		 // set up earth
        Entity earthE = sm.createEntity("myEarth", "earth.obj");
        earthE.setPrimitive(Primitive.TRIANGLES);
        SceneNode earthN = earthNG.
			createChildSceneNode(earthE.getName() + "Node");
        earthN.attachObject(earthE);
       // earthN.setLocalPosition(-2.0f, 2.0f, -5.0f);
        earthN.setLocalScale(0.4f, 0.4f, 0.4f);
		nodeArr[0] = earthN;
       
        SceneNode planet1NG = earthNG.
			createChildSceneNode("myPlanet1NodeG");
		
		// set up planet1
		Entity planetE = sm.createEntity("myPlanet", "planet1.obj");
        planetE.setPrimitive(Primitive.TRIANGLES);
        SceneNode planetN = planet1NG.
			createChildSceneNode(planetE.getName() + "Node");
        planetN.attachObject(planetE);
       // planetN.setLocalPosition(2.0f, 1.0f, -3.0f);
        planetN.setLocalScale(0.4f, 0.4f, 0.4f);
		nodeArr[1] = planetN;
		
		SceneNode planet2NG = earthNG.
			createChildSceneNode("myPlanet2NodeG");
		
		// set up planet
		Entity planetE2 = sm.createEntity("myPlanet2", "planet2.obj");
        planetE.setPrimitive(Primitive.TRIANGLES);
        SceneNode planetN2 = planet2NG.
			createChildSceneNode(planetE2.getName() + "Node");
        planetN2.attachObject(planetE2);
       // planetN2.setLocalPosition(4.0f, 1.0f, -1.0f);
        planetN2.setLocalScale(0.4f, 0.4f, 0.4f);
		nodeArr[2] = planetN2; 
		
		
		//set up food 1 of 6 
		Entity foodE1 = sm.createEntity("myFood", "food.obj");
        foodE1.setPrimitive(Primitive.TRIANGLES);
        foodN1 = sm.getRootSceneNode().
			createChildSceneNode("myFoodNode");
        foodN1.attachObject(foodE1); 
		foodN1.setLocalScale(0.4f, 0.4f, 0.4f);
		foodArr[0] = foodN1;
		
		
		//set up food 2 of 6
		Entity foodE2 = sm.createEntity("myFood2", "food.obj");
		foodE2.setPrimitive(Primitive.TRIANGLES);
		foodN2 = sm.getRootSceneNode().
			createChildSceneNode("myFoodNode2");
		foodN2.attachObject(foodE2);
		foodN2.setLocalScale(0.4f, 0.4f, 0.4f);
		foodArr[1] = foodN2;
		
		//set up food 3 of 6
		Entity foodE3 = sm.createEntity("myFood3", "food.obj");
		foodE3.setPrimitive(Primitive.TRIANGLES);
		foodN3 = sm.getRootSceneNode().
			createChildSceneNode("myFoodNode3");
		foodN3.attachObject(foodE3);
		foodN3.setLocalScale(0.4f, 0.4f, 0.4f);
		foodArr[2] = foodN3;
		
		//set up food 4 of 6
		Entity foodE4 = sm.createEntity("myFood4", "food.obj");
		foodE4.setPrimitive(Primitive.TRIANGLES);
		foodN4 = sm.getRootSceneNode().
			createChildSceneNode("myFoodNode4");
		foodN4.attachObject(foodE4);
		foodN4.setLocalScale(0.4f, 0.4f, 0.4f);
		foodArr[3] = foodN4;
		
		//set up food 5 of 6
		Entity foodE5 = sm.createEntity("myFood5", "food.obj");
		foodE5.setPrimitive(Primitive.TRIANGLES);
		foodN5 = sm.getRootSceneNode().
			createChildSceneNode("myFoodNode5");
		foodN5.attachObject(foodE5);
		foodN5.setLocalScale(0.4f, 0.4f, 0.4f);
		foodArr[4] = foodN5;
		
		//set up food 6 of 6
		Entity foodE6 = sm.createEntity("myFood6", "food.obj");
		foodE6.setPrimitive(Primitive.TRIANGLES);
		foodN6 = sm.getRootSceneNode().
			createChildSceneNode("myFoodNode6");
		foodN6.attachObject(foodE6);
		foodN6.setLocalScale(0.4f, 0.4f, 0.4f);
		foodArr[5] = foodN6;
		
		//randomize locations for the food bins
		for(int i = 0; i < 6; i++){
			foodArr[i].setLocalPosition(((float)random.nextInt(25)+20),
										(0.5f),
										((float)random.nextInt(50)-10));
		}
		
		
		//set up ground
		ManualObject ground = makeGround(eng, sm);
		SceneNode groundN = sm.getRootSceneNode().
			createChildSceneNode("GroundNode");
		groundN.attachObject(ground);
		ground.setPrimitive(Primitive.TRIANGLES); 
		
		//set up finish line
		ManualObject finishLine = makeFinishLine(eng,sm);
		finishLineN = sm.getRootSceneNode().
			createChildSceneNode("FinishLineNode");
		finishLineN.attachObject(finishLine);
		finishLine.setPrimitive(Primitive.TRIANGLES); 
		
		//make finish line flag 1 of 2
		Entity flagE1 = sm.createEntity("myFlag1", "flags.obj");
        flagE1.setPrimitive(Primitive.TRIANGLES);
        SceneNode flagN1 = sm.getRootSceneNode().
			createChildSceneNode("flag1Node");
        flagN1.attachObject(flagE1);
		flagN1.setLocalPosition(-20.0f, 0.5f, -25.2f);
		
		//make finish line flag 1 of 2
		Entity flagE2 = sm.createEntity("myFlag2", "flags.obj");
        flagE1.setPrimitive(Primitive.TRIANGLES);
        SceneNode flagN2 = sm.getRootSceneNode().
			createChildSceneNode("flag2Node");
        flagN2.attachObject(flagE2);
		flagN2.setLocalPosition(20.0f, 0.5f, -25.2f);
		
		
		//set up light
        sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
		Light plight = sm.createLight("testLamp1", Light.Type.POINT);
		plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(5f);
		
		SceneNode plightNode = sm.getRootSceneNode()
			.createChildSceneNode("plightNode");
        plightNode.attachObject(plight);

        
		
		
		//randomize the location of the planets
		for(int i=0; i<3; i++) {
			nodeArr[i].setLocalScale(0.8f,0.8f,0.8f);
			nodeArr[i].setLocalPosition(((float)random.nextInt(40)-20),
										(1.0f),
										((float)random.nextInt(40)-20));
		}
		
		
		
		setupOrbitCameras(eng,sm);
		setupInputs(sm);
		dolphinN1.yaw(Degreef.createFrom(45.f));
		dolphinN2.yaw(Degreef.createFrom(45.f));
		
		//initialize some orbit controllers and scenemanager
		oc1 = new OrbitController(dolphinN1,1.0f, 0.3f, 1.0f);
		oc2 = new OrbitController(dolphinN2,1.0f, 0.3f, 1.0f);
		rc = new RotationController(Vector3f.createUnitVectorY(), .04f);
        
		//add all the node controllers to scenemanager
		sm.addController(oc1);
		sm.addController(oc2);
		sm.addController(sc);
		sm.addController(rc);
		sm.addController(bc);
		
    }
	/*------------------------------------------------------------------------------------------------------------------------*/
	protected void setupOrbitCameras(Engine eng, SceneManager sm)
	{
		im = new GenericInputManager();
		SceneNode dolphinN1 = sm.getSceneNode("dolphin1Node");
		SceneNode cameraN1 = sm.getSceneNode("MainCameraNode");
		Camera camera1 = sm.getCamera("MainCamera");
		String gpName = im.getFirstGamepadName();
		orbitController1 = new Camera3Pcontroller
			(camera1, cameraN1, dolphinN1, gpName, im);
		
		SceneNode dolphinN2 = sm.getSceneNode("dolphin2Node");
		SceneNode cameraN2 = sm.getSceneNode("MainCamera2Node");
		Camera camera2 = sm.getCamera("MainCamera2");
		String kbName = im.getKeyboardName();
		orbitController2 = new Camera3Pcontroller
			(camera2, cameraN2, dolphinN2, kbName, im);
		
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------------*/
	protected void setupInputs(SceneManager sm)
	{ 
		im = new GenericInputManager();
		SceneNode dolphinN1 = getEngine().
			getSceneManager().getSceneNode("dolphin1Node");
		SceneNode dolphinN2 = getEngine().
			getSceneManager().getSceneNode("dolphin2Node");
		
		
		ArrayList<Controller> controllers = im.getControllers();
		
		
		moveForwardAction2 = new MoveForwardAction(dolphinN2);
		
		
		moveBackwardAction2 = new MoveBackwardAction(dolphinN2);
		
		moveLeftAction1 = new MoveLeftAction(dolphinN1);
		moveLeftAction2 = new MoveLeftAction(dolphinN2);
		
		moveRightAction1 = new MoveRightAction(dolphinN1);
		moveRightAction2 = new MoveRightAction(dolphinN2);
		
		quitGameAction = new QuitGameAction(this);
		
		rotateLeftAction = new RotateLeftAction(dolphinN2, orbitController2 );
		rotateRightAction = new RotateRightAction(dolphinN2, orbitController2);
		
		orbitElevationAction = new OrbitElevationAction(orbitController1);
		lookUpAction = new LookUpAction(orbitController2);
		lookDownAction = new LookDownAction(orbitController2);
		
		
		zoomInAction1 = new ZoomInAction(orbitController1);
		zoomOutAction1 = new ZoomOutAction(orbitController1);
		
		zoomInAction2 = new ZoomInAction(orbitController2);
		zoomOutAction2 = new ZoomOutAction(orbitController2);
		
		
		rotateUpAction = new RotateUpAction(this,camera);
		rotateDownAction = new RotateDownAction(this,camera);
		
		
		orbitAAction1 = new OrbitAroundAction(orbitController1);
		orbitRightAction = new OrbitRightAction(orbitController2);
		orbitLeftAction = new OrbitLeftAction(orbitController2);
		
		xStickAction = new XStickAction(dolphinN1,orbitController1);
		yStickAction = new YStickAction(dolphinN1);
		
		
		for (Controller c : controllers)
		{ 
			if (c.getType() == Controller.Type.KEYBOARD )
			{ 
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.ESCAPE
				,quitGameAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.W,
				moveForwardAction2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.S,
				moveBackwardAction2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.A,
				rotateLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.D,
				rotateRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.LEFT,
				orbitLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.RIGHT,
				orbitRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				
				im.associateAction( c, net.java.games.input.Component.Identifier.Key._2,
				zoomInAction2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction( c, net.java.games.input.Component.Identifier.Key.X,
				zoomOutAction2,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.UP,
				lookUpAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.DOWN,
				lookDownAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				
				
      
			}
			else if ((c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK)){ 
				im.associateAction( c, net.java.games.input.Component.Identifier.Button._3,
				zoomInAction1,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction( c, net.java.games.input.Component.Identifier.Button._0,
				zoomOutAction1,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN );
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.RX,
				orbitAAction1, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.RY,
				orbitElevationAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.X,
				xStickAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.Y,
				yStickAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
			}   
		}
		
	
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
	public ManualObject makeGround(Engine eng, SceneManager sm) throws IOException{
		ManualObject ground = sm.createManualObject("Ground");
		ManualObjectSection groundSec = ground.
			createManualSection("GroundSection");
		ground.setGpuShaderProgram(sm.getRenderSystem().
		getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		float[] vertices = new float[]
		{
			-50.0f, 0.0f, 50.0f,   50.0f, 0.0f, 50.0f,   -50.0f, 0.0f, -50.0f,
			50.0f, 0.0f,-50.0f,   -50.0f, 0.0f, -50.0f,   50.0f, 0.0f, 50.0f
		};
		float[] texcoords = new float[] 
		{
			0.0f, 0.0f,   1.0f, 0.0f,   0.0f, 1.0f,
			1.0f, 1.0f,	  0.0f, 0.0f,   1.0f, 0.0f
		};
		float[] normals = new float[]
		{
			0.0f, -1.0f, 0.0f,     0.0f, -1.0f, 0.0f,       0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f, 	   0.0f, -1.0f, 0.0f, 		0.0f,-1.0f, 0.0f
		};
		int[] indices = new int[] { 0,1,2,3,4,5};
		FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
		FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
		FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
		IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
		groundSec.setVertexBuffer(vertBuf);
		groundSec.setTextureCoordsBuffer(texBuf);
		groundSec.setNormalsBuffer(normBuf);
		groundSec.setIndexBuffer(indexBuf);
		Texture tex =
		eng.getTextureManager().getAssetByPath("blue.jpeg");
		TextureState texState = (TextureState)sm.getRenderSystem().
		createRenderState(RenderState.Type.TEXTURE);
		texState.setTexture(tex);
		FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
		createRenderState(RenderState.Type.FRONT_FACE);
		ground.setDataSource(DataSource.INDEX_BUFFER);
		ground.setRenderState(texState);
		ground.setRenderState(faceState);
		return ground;
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
	public ManualObject makeFinishLine(Engine eng, SceneManager sm) throws IOException{
		ManualObject finishLine = sm.createManualObject("FinishLine");
		ManualObjectSection finishLineSec = finishLine.
			createManualSection("FinishLineSection");
		finishLine.setGpuShaderProgram(sm.getRenderSystem().
			getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		float[] vertices = new float[]
		{
			-50.0f, 0.02f, -28.0f,   50.0f, 0.02f, -28.0f,   -50.0f, 0.02f, -29.0f,
			50.0f, 0.02f,-29.0f,   -50.0f, 0.02f, -29.0f,		50.0f, 0.02f, -28.0f   
		};
		float[] texcoords = new float[] 
		{
			0.0f, 0.0f,   1.0f, 0.0f,   0.0f, 1.0f,
			1.0f, 1.0f,	  0.0f, 0.0f,   1.0f, 0.0f
		};
		float[] normals = new float[]
		{
			0.0f, -1.0f, 0.0f,     0.0f, -1.0f, 0.0f,       0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f, 	   0.0f, -1.0f, 0.0f, 		0.0f,-1.0f, 0.0f
		};
		int[] indices = new int[] { 0,1,2,3,4,5};
		FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
		FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
		FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
		IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
		finishLineSec.setVertexBuffer(vertBuf);
		finishLineSec.setTextureCoordsBuffer(texBuf);
		finishLineSec.setNormalsBuffer(normBuf);
		finishLineSec.setIndexBuffer(indexBuf);
		Texture tex =
		eng.getTextureManager().getAssetByPath("chain-fence.jpeg");
		TextureState texState = (TextureState)sm.getRenderSystem().
		createRenderState(RenderState.Type.TEXTURE);
		texState.setTexture(tex);
		FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
		createRenderState(RenderState.Type.FRONT_FACE);
		finishLine.setDataSource(DataSource.INDEX_BUFFER);
		finishLine.setRenderState(texState);
		finishLine.setRenderState(faceState);
		return finishLine; 
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void update(Engine engine) {
		SceneNode dolphinN1 = getEngine().
		getSceneManager().getSceneNode("dolphin1Node");
		SceneNode dolphinN2 = getEngine().
		getSceneManager().getSceneNode("dolphin2Node");
		
		// build and set HUD
		rs = (GL4RenderSystem) engine.getRenderSystem();
		elapsTime1 += (engine.getElapsedTimeMillis());
		elapsTime2 += (engine.getElapsedTimeMillis());
		
		
		foodDetection();
		
		
		//elapsTimeSec1 = Math.round(elapsTime1/1000.0f);
		//elapsTimeStr1 = Integer.toString(elapsTimeSec1);
		visitedStr1 = Integer.toString(counter1);
		
		visitedStr2 = Integer.toString(counter2);
		
		collisionDetection();
		
		if(pickedUpArr[foodPickedUp1] == true && gotFood1 == true) 
			dispStr1 = "Deliver Food Package        Amount Delivered = " + visitedStr1;
		else 
			dispStr1 = "Pick Up Food Package		   Amount Delivered = " + visitedStr1;

		if(pickedUpArr[foodPickedUp2] == true && gotFood2 == true)
			dispStr2 = "Deliver Food Package        Amount Delivered = " + visitedStr2;	
		else 
			dispStr2 = "Pick Up Food Package		   Amount Delivered = " + visitedStr2;
		
		if((counter1 == 3) && (counter2 == 3) && bool == false) {
			oc1.removeAllNodes();
			oc2.removeAllNodes();
			sc.removeAllNodes();
			bc.addNode(earthNG);
			bool = true;
		}
		if(counter1 == 3)
				dispStr1 = "All packages delivered. Race back to the finish line!";
		if(counter2 == 3)
				dispStr2 = "All packages delivered. Race back to the finish line!";
		if((counter1 == 3) || (counter2 == 3)){
			whoWin();
		}
		
		rs.setHUD(dispStr2, 15, 15);
		rs.setHUD2(dispStr1, 15, rs.getCanvas().getHeight()/2 + 15);
		im.update(elapsTime1);
		im.update(elapsTime2);
		orbitController1.updateCameraPosition();
		orbitController2.updateCameraPosition();
	}
	
	/*------------------------------------------------------------------------------------------------------------------------*/
	public SceneNode getDolphin(){
		return dolphinN;
	}
	
	/*------------------------------------------------------------------------------------------------------------------------*/
	public SceneNode getDolphinChild() {
		return dolphinChild;
	}
	
	/*------------------------------------------------------------------------------------------------------------------------*/
	
	public void collisionDetection(){
		SceneNode dolphinN1 = getEngine().
		getSceneManager().getSceneNode("dolphin1Node");
		SceneNode dolphinN2 = getEngine().
		getSceneManager().getSceneNode("dolphin2Node");
		
		for(int i = 0; i < 3; i++) {
			Vector3 pPos1 = nodeArr[i].getLocalPosition();
			Vector3 dPos1 = (Vector3)dolphinN1.getLocalPosition();
			double dist =  (Math.sqrt((dPos1.x() - pPos1.x())*
				(dPos1.y() - pPos1.y())*(dPos1.z() - pPos1.z())));
			if((visitedArr1[i] == false) && (dist <= 0.08f)
									 && (gotFood1 == true)) {
			   visitedArr1[i] = true;
			   oc1.removeNode(foodArr[foodPickedUp1]);
			   gotFood1 = false;
			   counter1++;
			   sc.addNode(nodeArr[i]);
			   
			}
		}
		/*------------------------------------------------------------------------------------------------------------------------*/	
		
		for(int i = 0; i < 3; i++) {
			Vector3 pPos2 = nodeArr[i].getLocalPosition();
			Vector3 dPos2 = (Vector3)dolphinN2.getLocalPosition();
			double dist =  (Math.sqrt((dPos2.x() - pPos2.x())*(
				(dPos2.y() + 0.2f) - pPos2.y())*(dPos2.z() - pPos2.z())));
			if((visitedArr2[i] == false) && (dist <= 0.08f)
									 && (gotFood2 == true)) {
			   visitedArr2[i] = true;
			   oc2.removeNode(foodArr[foodPickedUp2]);
			   gotFood2 = false;
			   counter2++;
			   rc.addNode(nodeArr[i]);
			    
			}
		}
		
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
	public void foodDetection () {
		SceneNode dolphinN1 = getEngine().
			getSceneManager().getSceneNode("dolphin1Node");
		SceneNode dolphinN2 = getEngine().
			getSceneManager().getSceneNode("dolphin2Node");
		for(int i = 0; i < 6; i++) {
			Vector3 fPos = foodArr[i].getLocalPosition();
			Vector3 dPos = dolphinN1.getLocalPosition();
			
			double dist =  (Math.sqrt((dPos.x() - fPos.x())*
				(dPos.y() - fPos.y())*(dPos.z() - fPos.z())));
			if((dist < 0.05f) && (pickedUpArr[i] == false) 
									&& (gotFood1 == false)) {
				oc1.addNode(foodArr[i]);
				pickedUpArr[i] = true;
				foodPickedUp1 = i;
				gotFood1 = true;
			}
		}
		for(int i = 0; i < 6; i++) {
			Vector3 fPos = foodArr[i].getLocalPosition();
			Vector3 dPos = dolphinN2.getLocalPosition();
			
			double dist =  (Math.sqrt((dPos.x() - fPos.x())*
				(dPos.y() - fPos.y())*(dPos.z() - fPos.z())));
			if((dist < 0.05f) && (pickedUpArr[i] == false) 
								    && (gotFood2 == false)) {
				oc2.addNode(foodArr[i]);
				pickedUpArr[i] = true;
				foodPickedUp2 = i;
				gotFood2 = true;
			}
		}
	}
	/*------------------------------------------------------------------------------------------------------------------------*/
	public void whoWin(){
		SceneNode dolphinN1 = getEngine().
			getSceneManager().getSceneNode("dolphin1Node");
		SceneNode dolphinN2 = getEngine().
			getSceneManager().getSceneNode("dolphin2Node");
		Vector3 d1Pos = dolphinN1.getLocalPosition();
		Vector3 d2Pos = dolphinN2.getLocalPosition();
		if(d1Pos.z() < -29.0f && counter1 == 3) {
			this.setState(Game.State.STOPPING);
			System.out.println("Black Dolphin won!");
		}else if(d2Pos.z() < -29.0f && counter2==3){
			this.setState(Game.State.STOPPING);
			System.out.println("White Dolphin won!");
		}
	}
		
	/*------------------------------------------------------------------------------------------------------------------------*/
	
	
}






