package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a2.*;

public class ZoomInAction extends AbstractInputAction
{ 
	Camera3Pcontroller c3Pc;
	float radias;
	
	public ZoomInAction(Camera3Pcontroller c) {
		c3Pc = c;
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		radias = c3Pc.getCameraRadias();
		if(radias >= 1) {
			radias -= 0.05f;
		}else {
			radias = 1;
		}
		
		radias = radias % 360;
		c3Pc.setCameraRadias(radias);
		c3Pc.updateCameraPosition();
	} 
}