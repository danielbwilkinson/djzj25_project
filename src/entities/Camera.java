package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

import java.lang.Math;

public class Camera {
	
	private Vector3f position = new Vector3f(0,150,0);
	private float pitch = 10;
	private float yaw = 180;
	private float roll;
	
	private final float SPEED = 1.3f;
	private final float CAM_HEIGHT = 1.7f;
	
	public Camera(){}
	
	public void move(Terrain terrain){
		boolean posMoved = false;
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z-=SPEED*Math.cos(Math.toRadians(yaw));
			position.x+=SPEED*Math.sin(Math.toRadians(yaw));
			posMoved = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z+=SPEED*Math.cos(Math.toRadians(yaw));
			position.x-=SPEED*Math.sin(Math.toRadians(yaw));
			posMoved = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.z+=SPEED*Math.sin(Math.toRadians(yaw));
			position.x+=SPEED*Math.cos(Math.toRadians(yaw));
			posMoved = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.z-=SPEED*Math.sin(Math.toRadians(yaw));
			position.x-=SPEED*Math.cos(Math.toRadians(yaw));
			posMoved = true;
		}
		
		float terrainHeight = terrain.getHeightAtPos(Math.round(position.x), Math.round(position.z));
		if (posMoved && (terrainHeight + CAM_HEIGHT >= position.y + 10 || terrainHeight + CAM_HEIGHT <= position.y - 10)){
			//assume flying
			position.y = (float)(position.y - SPEED*Math.sin(Math.toRadians(pitch)));
		}
		else if (posMoved){
			position.y = (terrainHeight + CAM_HEIGHT + position.y)/2;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y+=2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y-=2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			yaw -= 0.5 * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			yaw += 0.5 * SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && pitch >= -90){
			pitch -= 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && pitch <= 90){
			pitch += 0.5f;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	

}
