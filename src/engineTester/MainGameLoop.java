package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;



import particles.ParticleMaster;
import particles.ParticleSystem;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("pine", loader),new ModelTexture(loader.loadTexture("pine")));
		
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),new ModelTexture(loader.loadTexture("lampTexture")));
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("grassTexture")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		// Add lamp glow
		lamp.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		Random random = new Random();
		//Generate terrain
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")),"heightMap");
		Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")),"heightMap");
		terrains.add(terrain);
		terrains.add(terrain2);
		
		// Adding objects to scene

		
		for(int i=0;i<500;i++){
			float x = random.nextFloat()*800 - 400;
			float z = random.nextFloat() * -600;
			float y;
			if(x< 0){
				y = terrain.getHeightOfTerrain(Math.abs(x), Math.abs(z),1);
			}
			else{
				y = terrain2.getHeightOfTerrain(x, Math.abs(z),-1);	
			}
			entities.add(new Entity(tree, new Vector3f(x,y,z),0,random.nextFloat()*360,0,1));
			x = random.nextFloat()*800 - 400;
			z = random.nextFloat() * -600;
			if(x< 0){
				y= terrain.getHeightOfTerrain(Math.abs(x), Math.abs(z),1);
			}
			else{
				y = terrain2.getHeightOfTerrain(x, Math.abs(z),-1);		
			}
			entities.add(new Entity(grass, new Vector3f(x,y,z),0,random.nextFloat()*360,0,1));
			x = random.nextFloat()*800 - 400;
			z = random.nextFloat() * -600;
			if(x< 0){
				y= terrain.getHeightOfTerrain(Math.abs(x), Math.abs(z),1);
			}
			else{
				y = terrain2.getHeightOfTerrain(x, Math.abs(z),-1);		
			}
			entities.add(new Entity(fern, new Vector3f(x,y,z),0,random.nextFloat()*360,0,0.6f));
		}
		
		List<Light> lights = new ArrayList<Light>();
		//Sun
		Light sun = new Light(new Vector3f(100,4000,-7000),new Vector3f(0.1f,0.1f,0.1f)); 
		lights.add(sun);
		//Point Lights
		float x = -60;
		float z = -30;
		float y;
		if(x< 0){
			y = terrain.getHeightOfTerrain(Math.abs(x), Math.abs(z),1);
		}
		else{
			y = terrain2.getHeightOfTerrain(x, Math.abs(z),-1);	
		}
		lights.add( new Light(new Vector3f(x, y+14, z), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		entities.add(new Entity(lamp, new Vector3f(x,y,z), 0, 0, 0, 1.1f));
		x = -90;
		z = -170;
		if(x< 0){
			y = terrain.getHeightOfTerrain(Math.abs(x), Math.abs(z),1);
		}
		else{
			y = terrain2.getHeightOfTerrain(x, Math.abs(z),-1);	
		}
		lights.add( new Light(new Vector3f(x, y+14, z), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1.1f));
		x = -70;
		z = -100;
		if(x< 0){
			y = terrain.getHeightOfTerrain(Math.abs(x), Math.abs(z),1);
		}
		else{
			y = terrain2.getHeightOfTerrain(x, Math.abs(z),-1);	
		}
		lights.add( new Light(new Vector3f(x, y+14, z), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f)));
		entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0,1.1f));
			
		
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		ParticleSystem system = new ParticleSystem(50, 25, 0.3f, 4, 1);
		system.randomizeRotation();
		system.setDirection(new Vector3f(0,1,0), 0.1f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.4f);
		system.setScaleError(0.8f);
		
		float sunBrightness = 1.5f;
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		//Water
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader,waterShader, renderer.getProjectionMatrix(),fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		//close body of water.
		int WaterHeight = -5;
		waters.add(new WaterTile(-99,-70,WaterHeight));// x; -90 -150   -90
		waters.add(new WaterTile(-140,-180,WaterHeight));
		//far left body of water
		waters.add(new WaterTile(-320,-120,WaterHeight));
		waters.add(new WaterTile(-390,-120,WaterHeight));
		

		while(!Display.isCloseRequested()){
			camera.move();
			system.generateParticles(camera.getPosition());
			ParticleMaster.update();
			renderer.renderShadowMap(entities, sun);
			sunBrightness = renderer.getSunBrightness();
			lights.get(0).setColour(new Vector3f(sunBrightness,sunBrightness,sunBrightness));
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//reflection buffer
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y-WaterHeight);
			camera.getPosition().y -=distance;
			camera.invertPitch();
			renderer.renderScene(entities, terrains, lights, camera,new Vector4f(0,1,0,-WaterHeight+1f));
			camera.getPosition().y +=distance;
			camera.invertPitch();
			//Retraction buffer
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera,new Vector4f(0,-1,0,-WaterHeight+1f));		
			//screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera,new Vector4f(0,-1,0,100000));
			waterRenderer.render(waters, camera,lights.get(0));
			ParticleMaster.renderParticles(camera);
			DisplayManager.updateDisplay();
			
		}
		
		ParticleMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		waterShader.cleanUp();
		fbos.cleanUp();
		DisplayManager.closeDisplay();

	}
	

}
