package a2;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

public class BounceController extends AbstractController
{
	private float scaleRate = .04f;
	private float cycleTime = 1000.0f;	// default cycle time
	private float totalTime = 0.0f;
	private float direction = 1.0f;
	
	@Override
	protected void updateImpl(float elapsedTimeMillis)
	{   totalTime += elapsedTimeMillis;
		if (totalTime > cycleTime)
		{	direction = -direction;
			totalTime = 0.0f;
		}
	
		for (Node n : super.controlledNodesList)
		{	Vector3 curScale = n.getLocalPosition();
			curScale = Vector3f.createFrom(curScale.x(), curScale.y()+.08f*direction, curScale.z());
			n.setLocalPosition(curScale);
		}
	}
}
