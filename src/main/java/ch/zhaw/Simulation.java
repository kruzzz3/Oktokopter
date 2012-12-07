package ch.zhaw;
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

import ch.zhaw.oktokopter.IOktokopter;
import ch.zhaw.oktokopter.Oktokopter;
import ch.zhaw.oktokopter.UserData;


public class Simulation extends TestbedTest {
	
	private int anzOkto = 13;
	
	private Body m_bodies[] = new Body[anzOkto];
	private IOktokopter oktokopters[] = new Oktokopter[anzOkto];
	private Joint m_joints[] = new Joint[anzOkto];

	private Body cube;
	
	@Override
	public String getTestName() {
		return "OctoTest";
	}

	@Override
	public void initTest(boolean deserialized) {
	    setTitle("Octocopter-Prototyp v0.1");
	    getWorld().setGravity(new Vec2(0, -50));
	    createGround();
	    createCube();
	    createOktokopter();
	    createJoint();
	}
	
	private void createGround() {
		Body ground = getWorld().createBody(new BodyDef());
		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-60.0f, 0.0f), new Vec2(60.0f, 0.0f));
		ground.createFixture(shape, 0);
	}
	
	private void createOktokopter() {
		float cubeX = cube.getPosition().x;
		float distance = (anzOkto*5)/anzOkto;
		if (anzOkto % 2 == 1) {
			for (int i = 0; i < anzOkto; i++) {
	        	oktokopters[i] = new Oktokopter("Okto "+i, (int)Math.round(Math.random()*100),cubeX-(distance*((anzOkto/2)-i)),20);
		        m_bodies[i] = getWorld().createBody(oktokopters[i].getBodyDef());
		        m_bodies[i].setUserData(new UserData());
		        m_bodies[i].createFixture(oktokopters[i].getFixtureDef());
		        oktokopters[i].setBody(m_bodies[i]);
	        }
		}
		else {
			for (int i = 0; i < anzOkto; i++) {
	        	oktokopters[i] = new Oktokopter("Okto "+i, (int)Math.round(Math.random()*100),cubeX-(distance*((anzOkto/2)-i))+(distance/2),20);
		        m_bodies[i] = getWorld().createBody(oktokopters[i].getBodyDef());
		        m_bodies[i].setUserData(new UserData());
		        m_bodies[i].createFixture(oktokopters[i].getFixtureDef());
		        oktokopters[i].setBody(m_bodies[i]);
	        }
		}
		
		for (int i = 0; i < anzOkto; i++) {
			if (i == 0) { // FIRST
				oktokopters[i].setNext(oktokopters[i+1]);
				oktokopters[i].setPrev(oktokopters[anzOkto-1]);
			}
			else if (i == anzOkto-1) { // LAST
				oktokopters[i].setNext(oktokopters[0]);
				oktokopters[i].setPrev(oktokopters[i-1]);
			}
			else {
				oktokopters[i].setNext(oktokopters[i+1]);
				oktokopters[i].setPrev(oktokopters[i-1]);
			}
		}
		oktokopters[0].start();
		
	}
	
	private void createCube() {
		PolygonShape shape = new PolygonShape();
	    shape.setAsBox(6.0f, 4.0f);

	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = shape;
	    fixtureDef.density = 0.2f;
	    fixtureDef.friction = 0.99f;
	    fixtureDef.restitution = 0.1f;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0.0f, 4.0f);
		bodyDef.angularDamping = 10;
        cube = getWorld().createBody(bodyDef);
        cube.createFixture(fixtureDef);
	}
	
	private void createJoint() {
		DistanceJointDef jd = new DistanceJointDef();
		Vec2 p1 = new Vec2();
		Vec2 p2 = new Vec2();
		Vec2 d = new Vec2();

		jd.frequencyHz = 0f;
		jd.dampingRatio = 0f;
		for (int i = 0; i < anzOkto; ++i) {
			jd.bodyA = cube;
			jd.bodyB = m_bodies[i];
			jd.localAnchorA.set(0, 0.5f);
			jd.localAnchorB.set(0, -0.2f);
			p1 = jd.bodyA.getWorldPoint(jd.localAnchorA);
			p2 = jd.bodyB.getWorldPoint(jd.localAnchorB);
			d = p2.sub(p1);
			jd.length = d.length();
			m_joints[i] = getWorld().createJoint(jd);
		}
	}
	
	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		for (int i=0; i<anzOkto; ++i) {
			if (m_bodies[i].getUserData() instanceof UserData) { 
				if (oktokopters[i].isRunning()) {
					m_bodies[i].applyForce(new Vec2(0, oktokopters[i].getForceUp()), new Vec2(m_bodies[i].getPosition().x,m_bodies[i].getPosition().y));
					m_bodies[i].setLinearVelocity(new Vec2(oktokopters[i].getForceLeftRight(), 0));
					m_bodies[i].setAngularVelocity(oktokopters[i].getForceAngle());
				}
				else {
					getWorld().destroyJoint(m_joints[i]);
				}
			}
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
