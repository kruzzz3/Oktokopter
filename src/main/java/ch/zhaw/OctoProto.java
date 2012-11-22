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


public class OctoProto extends TestbedTest
{
	Body m_bodies[] = new Body[5];
	Joint m_joints[] = new Joint[8];

	@Override
	public String getTestName()
	{
		// TODO Auto-generated method stub
		return "OctoTest";
	}

	@Override
	public void initTest(boolean deserialized)
	{
	    setTitle("Octocopter-Prototyp v0.1");
	    
		Body ground = null;
		{
			BodyDef bd = new BodyDef();
			ground = getWorld().createBody(bd);
			
			PolygonShape shape = new PolygonShape();
			shape.setAsEdge(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
			ground.createFixture(shape, 0.0f);
		}	    
	      
	    PolygonShape shape = new PolygonShape();
	    shape.setAsBox(1.0f, 1.0f);

	    FixtureDef fd = new FixtureDef();
	    fd.shape = shape;
	    fd.density = 1.0f;
	    fd.friction = 0.9f;
	    fd.restitution = 0.9f;

	    // Copters
        for (int i = 0; i < 5; ++i) {
        	BodyDef bd = new BodyDef();
        	bd.type = BodyType.DYNAMIC;
	        bd.position.set(0.0f + 3.5f*i, 20.0f);
	        
	        m_bodies[i] = getWorld().createBody(bd);
	        m_bodies[i].createFixture(fd);
        }
        
        // Cube
    	BodyDef bd2 = new BodyDef();
    	bd2.type = BodyType.DYNAMIC;
        bd2.position.set(7.0f, 10.0f);
        Body cube = getWorld().createBody(bd2);
        cube.createFixture(fd);
        
        
		DistanceJointDef jd = new DistanceJointDef();
		Vec2 p1 = new Vec2();
		Vec2 p2 = new Vec2();
		Vec2 d = new Vec2();
		
		//jd.frequencyHz = 0.1f;
		//jd.dampingRatio = 1.1f;
		jd.frequencyHz = 0;
		jd.dampingRatio = 0;
		
		for (int i = 0; i < 5; ++i)
		{
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
	
	@Override
	  public void step(TestbedSettings settings) {
	    super.step(settings);
	    addTextLine("Text text");
	    
	    for (int i=0; i<5; ++i)
	    {
	    	m_bodies[i].applyForce(new Vec2(0, 50), new Vec2(0,0));
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
