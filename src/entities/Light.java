package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuaiton = new Vector3f(1,0,0);
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}

	public Light(Vector3f position, Vector3f colour, Vector3f attenuaiton) {
		this.position = position;
		this.colour = colour;
		this.attenuaiton = attenuaiton;
	}
	
	public Vector3f getAttenuaiton() {
		return attenuaiton;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	

}
