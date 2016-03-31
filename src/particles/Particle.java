package particles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;


public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private float elapsedTime = 0;

	public Particle(Vector3f postion, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
		this.position = postion;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public Vector3f getPostion() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	protected boolean update(){
		velocity.y += Terrain.GRAVITY * gravityEffect * DisplayManager.getCurrentTime();
		Vector3f change = new Vector3f(velocity);
		change.scale( DisplayManager.getCurrentTime());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
		
	}
	
	
	
 
}
