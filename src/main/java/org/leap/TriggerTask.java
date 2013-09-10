package org.leap;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TriggerTask extends LeapTask {
	private String trigger_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/triggers/TriggerTemplate.trigger";
	private String meta_trigger_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/triggers/TriggerTemplate.trigger-meta.xml";
	
	private String class_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/classes/TriggerHandlerTemplate.cls";
	private String meta_class_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/classes/TriggerHandlerTemplate.cls-meta.xml";	
	
	public void execute() {
		for (int i = 0; i < this.sObjects().length; i++) {
			if( !sObjects()[i].getTriggerable() ){
				continue;
			}
			
			String triggerTemplate = this.getLeapTriggerTemplate().content;
			triggerTemplate	= triggerTemplate.replace("{{object_name}}", this.sObjects()[i].getName())
						.replace("{{class_name}}", this.sObjects()[i].getName());
			
			String classTemplate = this.getLeapClassTemplate().content;
			classTemplate	= classTemplate.replace("{{object_name}}", this.sObjects()[i].getName())
						.replace("{{class_name}}", this.sObjects()[i].getName());
						
			PrintWriter writer = null;
			try {
				//TODO: Check if these will overwrite
				writer = new PrintWriter(this.getProjectRoot() + "triggers/" + this.sObjects()[i].getName() + ".trigger", "UTF-8");
				writer.write( triggerTemplate );
				writer.close();
				System.out.println("Created " + this.getProjectRoot() + "triggers/" + this.sObjects()[i].getName() + ".trigger");
				
				writer = new PrintWriter(this.getProjectRoot() + "triggers/" + this.sObjects()[i].getName() + ".trigger-meta.xml", "UTF-8");
				writer.write( this.getLeapMetaTriggerTemplate().content );
				writer.close();
				System.out.println("Created " + this.getProjectRoot() + "triggers/" + this.sObjects()[i].getName() + ".trigger-meta.xml");
				
				writer = new PrintWriter(this.getProjectRoot() + "classes/" + this.sObjects()[i].getName() + "TriggerHandler.cls", "UTF-8");
				writer.write( classTemplate );
				writer.close();
				System.out.println("Created " + this.getProjectRoot() + "classes/" + this.sObjects()[i].getName() + "TriggerHandler.cls");
				
				writer = new PrintWriter(this.getProjectRoot() + "classes/" + this.sObjects()[i].getName() + "TriggerHandler.cls-meta.xml", "UTF-8");
				writer.write( this.getLeapMetaClassTemplate().content );
				writer.close();
				System.out.println("Created " + this.getProjectRoot() + "classes/" + this.sObjects()[i].getName() + "TriggerHandler.cls-meta.xml");				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	private LeapTemplate m_leapClassTemplate = null;
    public LeapTemplate getLeapClassTemplate(){
    	if(m_leapClassTemplate == null){
    		m_leapClassTemplate = new LeapTemplate().withContent( this.getClassTemplate().decodedContent() );
    	}
    	return m_leapClassTemplate;
    }
    
    private LeapTemplate m_leapMetaClassTemplate = null;
    public LeapTemplate getLeapMetaClassTemplate(){
    	if(m_leapMetaClassTemplate == null){
    		m_leapMetaClassTemplate = new LeapTemplate().withContent( this.getClassMetaTemplate().decodedContent() );
    	}
    	return m_leapMetaClassTemplate;
    }
	
	private LeapTemplate m_leapTriggerTemplate = null;
    public LeapTemplate getLeapTriggerTemplate(){
    	if(m_leapTriggerTemplate == null){
    		m_leapTriggerTemplate = new LeapTemplate().withContent( this.getTriggerTemplate().decodedContent() );
    	}
    	return m_leapTriggerTemplate;
    }
    
    private LeapTemplate m_leapMetaTriggerTemplate = null;
    public LeapTemplate getLeapMetaTriggerTemplate(){
    	if(m_leapMetaTriggerTemplate == null){
    		m_leapMetaTriggerTemplate = new LeapTemplate().withContent( this.getTriggerMetaTemplate().decodedContent() );
    	}
    	return m_leapMetaTriggerTemplate;
    }
    
    private GitHubContent m_classTemplate = null;
    protected GitHubContent getClassTemplate(){
    	if(m_classTemplate == null){
    		m_classTemplate = GitHubContent.get(this.class_url);
    	}
    	return m_classTemplate;
    }
        
    private GitHubContent m_classMetaTemplate = null;
    protected GitHubContent getClassMetaTemplate(){
    	if(m_classMetaTemplate == null){
    		m_classMetaTemplate = GitHubContent.get(this.meta_class_url);
    	}
    	return m_classMetaTemplate;
    }
    
    private GitHubContent m_triggerTemplate = null;
    protected GitHubContent getTriggerTemplate(){
    	if(m_triggerTemplate == null){
    		m_triggerTemplate = GitHubContent.get(this.trigger_url);
    	}
    	return m_triggerTemplate;
    }
        
    private GitHubContent m_triggerMetaTemplate = null;
    protected GitHubContent getTriggerMetaTemplate(){
    	if(m_triggerMetaTemplate == null){
    		m_triggerMetaTemplate = GitHubContent.get(this.meta_trigger_url);
    	}
    	return m_triggerMetaTemplate;
    }
}