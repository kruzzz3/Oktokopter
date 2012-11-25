package ch.zhaw;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import ch.zhaw.oktokopter.Oktokopter;


public class Simulation extends TestbedTest {
	private Body m_bodies[] = new Body[9];
	private Oktokopter oktokopters[] = new Oktokopter[9];
	private Joint m_joints[] = new Joint[9];

	@Override
	public String getTestName() {
		return "OctoTest";
	}

	@Override
	public void initTest(boolean deserialized) {
	    setTitle("Octocopter-Prototyp v0.1");
	    
	    createGround();
	     

	    // Copters
        for (int i = 0; i < 9; ++i) {
        	Oktokopter oktokopter = new Oktokopter("Okto "+i, (int)Math.round(Math.random()*100),-20+(i*5),20);
        	oktokopters[i] = oktokopter;
	        m_bodies[i] = getWorld().createBody(oktokopter.getBD());
	        m_bodies[i].createFixture(oktokopter.getFD());
        }
        //oktokopters[0].start();
        
        PolygonShape shape = new PolygonShape();
	    shape.setAsBox(2.0f, 2.0f);

	    FixtureDef fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 1.0f;
	    fd.friction = 0.9f;
	    fd.restitution = 0.9f;
        
        // Cube
    	BodyDef bd2 = new BodyDef();
    	bd2.type = BodyType.DYNAMIC;
        bd2.position.set(0.0f, 10.0f);
        Body cube = getWorld().createBody(bd2);
        cube.createFixture(fd);
        
		DistanceJointDef jd = new DistanceJointDef();
		Vec2 p1 = new Vec2();
		Vec2 p2 = new Vec2();
		Vec2 d = new Vec2();
		
		//jd.frequencyHz = 0.1f;
		//jd.dampingRatio = 1.1f;
		jd.frequencyHz = 0.5f;
		jd.dampingRatio = 0.8f;
		
		for (int i = 0; i < 9; ++i) {
			jd.bodyA = cube;
			jd.bodyB = m_bodies[i];
			//jd.localAnchorA.set(0, 0.5f);
			//jd.localAnchorB.set(0, -0.5f);
			jd.localAnchorA.set(0, 0);
			jd.localAnchorB.set(0, 0);
			p1 = jd.bodyA.getWorldPoint(jd.localAnchorA);
			p2 = jd.bodyB.getWorldPoint(jd.localAnchorB);
			d = p2.sub(p1);
			jd.length = d.length();
			m_joints[i] = getWorld().createJoint(jd);
		}

	}
	
	private void createGround() {
		Body ground = getWorld().createBody(new BodyDef());
		
		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
		
		ground.createFixture(shape, 0);
	}
	
	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		addTextLine("Text text");
		for (int i=0; i<9; ++i) {
			m_bodies[i].applyForce(new Vec2(oktokopters[i].getForceLeftRight(), oktokopters[i].getAuftrieb()), new Vec2(0,0));
		}
	}
	
	@Override
	public void keyPressed(char argKeyChar, int argKeyCode) {
		switch (argKeyChar) {
			case ',':
			break;
		}
	}
}
