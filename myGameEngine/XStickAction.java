package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a2.*;

public class XStickAction extends AbstractInputAction
{ 
	Camera3Pcontroller c3Pc;
	float cameraAzimuth;
	Node avN;
	
	public XStickAction(Node n, Camera3Pcontroller c) {
		c3Pc = c;
		avN = n;
		
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		cameraAzimuth = c3Pc.getCameraAzimuth();
		
		
		if (evt.getValue() < -0.2) {
			Angle rotAmt = Degreef.createFrom(1.3f);
			avN.yaw(rotAmt);
			cameraAzimuth += 1.3f;
		}
		else
		{ 
			if (evt.getValue() > 0.2){
				Angle rotAmt = Degreef.createFrom(-1.3f);
				avN.yaw(rotAmt);
				cameraAzimuth -= 1.3f;
			}
			else{}
		}
		
		cameraAzimuth = cameraAzimuth % 360;
		c3Pc.setCameraAzimuth(cameraAzimuth);
		c3Pc.updateCameraPosition();
	} 
}
