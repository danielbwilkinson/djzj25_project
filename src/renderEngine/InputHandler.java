package renderEngine;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import toolbox.Maths;


public class InputHandler {
	
	private boolean mouseClickRecorded;
	private Camera camera;
	private Matrix4f projectionMatrix;
	
	public InputHandler(Camera camera, MasterRenderer renderer){
		mouseClickRecorded = false;
		this.camera = camera;
		projectionMatrix = renderer.getProjectionMatrix();
		
	}
	
	public void pollInput(){
		if(Mouse.isButtonDown(0) && !mouseClickRecorded){
			int x = Mouse.getX();
			int y = Mouse.getY();
			System.out.printf("Mouse position: %d %d\n", x, y);
			handleClick(x, y);
			mouseClickRecorded = true;
		} else if (!Mouse.isButtonDown(0) && mouseClickRecorded){
			mouseClickRecorded = false;
		}
	}

	private void handleClick(int x, int y){
		System.out.printf("%d %d", x,y);
		Matrix4f viewTransform = Maths.createViewMatrix(camera);
		
		Vector4f mouseClip = new Vector4f(
				(float)(x*2)/(float)DisplayManager.getWidth() - 1,
				1-(float)(y*2)/(float)DisplayManager.getHeight(),
				0,
				1
			);
		
		
		Matrix4f mouseWorldspaceIntermediate = new Matrix4f();
		System.out.println(projectionMatrix.toString());
		Matrix4f.mul((Matrix4f)viewTransform.invert(), (Matrix4f)projectionMatrix.invert(), mouseWorldspaceIntermediate);
		System.out.println(projectionMatrix.toString());
		Vector4f mouseWorldspace = new Vector4f();
		Matrix4f.transform(mouseWorldspaceIntermediate, mouseClip, mouseWorldspace);
		System.out.println("Mouse pos world");
		System.out.print(mouseWorldspace);
		
		Vector3f mouseWorldspace3d = new Vector3f(mouseWorldspace.x, mouseWorldspace.y, mouseWorldspace.z);
		Vector3f rayOrigin = camera.getPosition();
		Vector3f rayDirection = new Vector3f();
		Vector3f.sub(mouseWorldspace3d, rayOrigin, rayDirection);
		rayDirection = (Vector3f)rayDirection;//.normalise();
		
		System.out.println("Ray direction");
		System.out.println(rayDirection.toString());
		
		
	}
}
