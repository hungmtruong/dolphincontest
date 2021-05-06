package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a2.MyGame;
public class YStickAction extends AbstractInputAction
{
	private Node avN;
	public YStickAction(Node n)
	{ 
		avN = n;
	}
	public void performAction(float time, Event evt)
	{ 
		
		
		if (evt.getValue() < -0.2) {
			avN.moveForward(0.03f);
		}
		else
		{ 
			if (evt.getValue() > 0.2)
				avN.moveBackward(0.03f);
			else{} 
		}
	}
}