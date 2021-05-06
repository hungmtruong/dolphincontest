package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a2.MyGame;
public class MoveLeftAction extends AbstractInputAction
{
	private Node avN;
	public MoveLeftAction(Node n)
	{ 
		avN = n;
	}
	public void performAction(float time, Event e)
	{ 
		avN.moveRight(0.01f);
	}
}