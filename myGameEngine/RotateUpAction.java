package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a2.MyGame;
public class RotateUpAction extends AbstractInputAction
{
	private Camera camera;
	private MyGame myGame;
	public RotateUpAction(MyGame g, Camera c)
	{ 
		camera = c;
		myGame = g;
	}
	public void performAction(float time, Event e)
	{ 
		Angle rotAmt = Degreef.createFrom(-1.3f);
		if(camera.getMode() != 'c'){
			myGame.getDolphin().pitch(rotAmt);
			
		}else {
			Vector3 fn = (camera.getFd().rotate(rotAmt, camera.getRt())).normalize();
			camera.setFd((Vector3f)fn);
			Vector3 fu = (camera.getUp().rotate(rotAmt, camera.getRt())).normalize();
			camera.setUp((Vector3f)fu);
			
		}
	}
}  