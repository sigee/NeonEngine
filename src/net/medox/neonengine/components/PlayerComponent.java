package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.Collider;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.Camera;

public class PlayerComponent extends EntityComponent{
	private final InputKey forwardKey;
	private final InputKey backKey;
	private final InputKey leftKey;
	private final InputKey rightKey;
	private final InputKey sprintKey;
	private final InputKey jumpKey;
	
	private final CharacterController controller;
	private final Camera camera;
	
	private float speed;
	private float sprintSpeed;
	
	public PlayerComponent(Collider collider, Camera camera, float speed, float sprintSpeed){
		this(collider, camera, speed, sprintSpeed, new InputKey(Input.KEYBOARD, Input.KEY_W), new InputKey(Input.KEYBOARD, Input.KEY_S), new InputKey(Input.KEYBOARD, Input.KEY_A), new InputKey(Input.KEYBOARD, Input.KEY_D), new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT), new InputKey(Input.KEYBOARD, Input.KEY_SPACE));
	}
	
	public PlayerComponent(Collider collider, Camera camera, float speed, float sprintSpeed, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey, InputKey sprintKey, InputKey jumpKey){
		controller = new CharacterController(collider, 0.3f);
		
		controller.setMaxJumpHeight(4);
//		controller.setJumpSpeed(4);
//		controller.setJumpSpeed(100);
//		controller.setMaxJumpHeight(4f);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
		this.camera = camera;
		this.speed = speed;
		this.sprintSpeed = sprintSpeed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.sprintKey = sprintKey;
		this.jumpKey = jumpKey;
	}
	
	@Override
	public void input(float delta){
		float moveSpeed = speed;
		
		if(Input.getInputKey(sprintKey)){
			moveSpeed = sprintSpeed;
		}
		
		Vector3f dir = new Vector3f(0, 0, 0);
		boolean changed = false;
		
		if(Input.getInputKey(forwardKey) && !Input.getInputKey(backKey)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.getInputKey(leftKey) && !Input.getInputKey(rightKey)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.getInputKey(backKey) && !Input.getInputKey(forwardKey)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.getInputKey(rightKey) && !Input.getInputKey(leftKey)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		
		if(Input.getInputKeyDown(jumpKey)){
			controller.jump();
		}
		
		if(changed){
			move(dir.normalized().mul(moveSpeed));
		}else{
			move(dir);
		}
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(controller.getPos());
	}
	
	public void move(Vector3f vel){
		controller.setWalkDirection(vel.mul(0.015f));
	}
	
	public CharacterController getController(){
		return controller;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addController(controller);
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeController(controller);
	}
}
