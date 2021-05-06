package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a2.*;

public class ZoomOutAction extends AbstractInputAction
{ 
	Camera3Pcontroller c3Pc;
	float radias;
	
	public ZoomOutAction(Camera3Pcontroller c) {
		c3Pc = c;
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		radias = c3Pc.getCameraRadias();
		if(radias <= 10) {
			radias += 0.05f;
		}else {
			radias = 10;
		}
	
		radias = radias % 360;
		c3Pc.setCameraRadias(radias);
		c3Pc.updateCameraPosition();
	} 
}