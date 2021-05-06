package a2;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.input.*;
import ray.input.action.*;
import net.java.games.input.Controller;
import java.util.ArrayList;

public class Camera3Pcontroller
{ 
	private Camera camera; //the camera being controlled
	private SceneNode cameraN; //the node the camera is attached to
	private SceneNode target; //the target the camera looks at
	private float cameraAzimuth; //rotation of camera around Y axis
	private float cameraElevation; //elevation of camera above target
	private float radias; //distance between camera and target
	private Vector3 targetPos; //target’s position in the world
	private Vector3 worldUpVec;
	public Camera3Pcontroller(Camera cam, SceneNode camN, SceneNode targ,
							  String controllerName, InputManager im)
	{ 
		camera = cam;
		cameraN = camN;
		target = targ;
		cameraAzimuth = 225.0f; // start from BEHIND and ABOVE the target
		cameraElevation = 20.0f; // elevation is in degrees
		radias = 2.0f;
		worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
		//setupInput(im, controllerName);
		updateCameraPosition();
	}
	// Updates camera position: computes azimuth, elevation, and distance
	// relative to the target in spherical coordinates, then converts those
	// to world Cartesian coordinates and setting the camera position
	public void updateCameraPosition()
	{ 
		double theta = Math.toRadians(cameraAzimuth); // rot around target
		double phi = Math.toRadians(cameraElevation); // altitude angle
		double x = radias * Math.cos(phi) * Math.sin(theta);
		double y = radias * Math.sin(phi);
		double z = radias * Math.cos(phi) * Math.cos(theta);
		cameraN.setLocalPosition(Vector3f.createFrom
			((float)x, (float)y, (float)z).add(target.getWorldPosition()));
		cameraN.lookAt(target, worldUpVec);
	}
	public float getCameraAzimuth() {
		return cameraAzimuth;
	}
	public void setCameraAzimuth(float azimuth) {
		cameraAzimuth = azimuth;
	}
	public float getCameraElevation() {
		return cameraElevation;
	}
	public void setCameraElevation(float elevation) {
		cameraElevation = elevation;
	}
	public float getCameraRadias() {
		return radias;
	}
	public void setCameraRadias(float r) {
		radias = r;
	}
	
	
	
		
		
		// similar input set up for OrbitRadiasAction, OrbitElevationAction
	} 
	
	 // similar for OrbitRadiasAction, OrbitElevationAction

// Moves the camera around the target (changes camera azimuth).
	



