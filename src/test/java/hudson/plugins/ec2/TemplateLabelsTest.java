package hudson.plugins.ec2;

import hudson.model.Label;
import hudson.model.labels.LabelAtom;

import java.util.ArrayList;
import java.util.List;

import org.jvnet.hudson.test.HudsonTestCase;

import com.amazonaws.services.ec2.model.InstanceType;

public class TemplateLabelsTest extends HudsonTestCase{
	
	private AmazonEC2Cloud ac;
	private final String LABEL1 = "label1";
	private final String LABEL2 = "label2";
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		SlaveTemplate template = new SlaveTemplate("ami", "foo", "zone", "22", InstanceType.M1Large, LABEL1 + " " + LABEL2, "foo ami", "bar", "aaa", "10", "rrr", "fff", "-Xmx1g", true);
        List<SlaveTemplate> templates = new ArrayList<SlaveTemplate>();
        templates.add(template);
        ac = new AmazonEC2Cloud("us-east-1", "abc", "def", "ghi", "3", templates);
	}
	
	public void testLabelAtom(){
		assertEquals(true, ac.canProvision(new LabelAtom(LABEL1)));
		assertEquals(true, ac.canProvision(new LabelAtom(LABEL2)));
		assertEquals(false, ac.canProvision(new LabelAtom("aaa")));
	}
	
	public void testLabelExpression() throws Exception{
		assertEquals(true, ac.canProvision(Label.parseExpression(LABEL1 + " || " + LABEL2)));
        assertEquals(true, ac.canProvision(Label.parseExpression(LABEL1 + " && " + LABEL2)));
        assertEquals(true, ac.canProvision(Label.parseExpression(LABEL1 + " || aaa")));
        assertEquals(false, ac.canProvision(Label.parseExpression(LABEL1 + " && aaa")));
        assertEquals(false, ac.canProvision(Label.parseExpression("aaa || bbb")));
        assertEquals(false, ac.canProvision(Label.parseExpression("aaa || bbb")));
    }
        
        public void testEmptyLabel() throws Exception{
            SlaveTemplate temp = new SlaveTemplate("ami", "foo", "zone", "22", InstanceType.M1Large, "", "foo ami", "bar", "aaa", "10", "rrr", "fff", "-Xmx1g", true);
            List<SlaveTemplate> templates = new ArrayList<SlaveTemplate>();
            templates.add(temp);
            ac = new AmazonEC2Cloud("us-east-1", "abc", "def", "ghi", "3", templates);
            
            assertEquals(true, ac.canProvision(null));
        }

}
