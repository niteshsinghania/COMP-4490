package entities;

import java.awt.AWTException;
import java.awt.Robot;
import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,5,0);
	private float pitch = 10;
	private float yaw = 0;
	private float roll;
	Robot mouseMover;
	Display canvas;
	boolean cameraMouse;
	double xpos, ypos;
	Cursor emptyCursor;
	private boolean night = false;
	
	public Camera(){
		cameraMouse = false;
		try {
			mouseMover = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		int min = org.lwjgl.input.Cursor.getMinCursorSize();
		IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
		
		try {
			emptyCursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void move(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z-=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x-=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y-=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S) && position.z <= 0.0f){
			position.z+=0.2f;
		}
		while (Keyboard.next()) {
			if(Keyboard.getEventKey() == (Keyboard.KEY_ESCAPE)){
				if (!Keyboard.getEventKeyState()) {
					cameraMouse = ! cameraMouse;
				}

			}
			else if(Keyboard.getEventKey() == (Keyboard.KEY_F)){
				if (!Keyboard.getEventKeyState()) {
					night = ! night;
				}
			}
		}
		xpos = Mouse.getX();
		ypos = Mouse.getY();
		if(cameraMouse){
			yaw -= 0.1f*(Display.getWidth()/2-xpos);
			pitch += 0.1f*(Display.getHeight()/2-ypos);
			Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
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
	public boolean getNight() {
		return night;
	}
	

}
