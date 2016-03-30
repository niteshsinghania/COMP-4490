package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
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
		Random random = new Random();
		//Generate terrain
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")),"heightMap");
		Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")),"heightMap");
		
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
			System.out.println(y);
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
		lights.add(new Light(new Vector3f(100,4000,-7000),new Vector3f(0.1f,0.1f,0.1f)));
		//Point Lights
		lights.add( new Light(new Vector3f(40, 14, -100), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add( new Light(new Vector3f(0, 14, -100), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add( new Light(new Vector3f(-40, 14, -100), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f)));

		entities.add(new Entity(lamp, new Vector3f(40, 0f, -100), 0, 0, 0, 1.1f));
		entities.add(new Entity(lamp, new Vector3f(0, 0f, -100), 0, 0, 0, 1.1f));
		entities.add(new Entity(lamp, new Vector3f(-40, 0f, -100), 0, 0, 0,1.1f));
		

		
		
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer(loader);
		float sunBrightness = 1.5f;
		while(!Display.isCloseRequested()){
			camera.move();
			sunBrightness = renderer.getSunBrightness();
			lights.get(0).setColour(new Vector3f(sunBrightness,sunBrightness,sunBrightness));
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			DisplayManager.updateDisplay();
			
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	

}
