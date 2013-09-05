package org.leap;

public class TriggerTask extends LeapTask {
	private String trigger_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/triggers/TriggerTemplate.trigger";
	private String meta_trigger_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/triggers/TriggerTemplate.trigger-meta.xml";
	
	private String class_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/classes/TriggerHandlerTemplate.cls";
	private String meta_class_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/classes/TriggerHandlerTemplate.cls-meta.xml";	
		
	public void execute() {
		for (int i = 0; i < this.sObjects().length; i++) {
			String triggerTemplate = this.getLeapTriggerTemplate().content;
			triggerTemplate	= triggerTemplate.replace("{{object_name}}", this.sObjects()[i].getName())
						.replace("{{class_name}}", this.sObjects()[i].getName());
			
			String classTemplate = this.getClassTemplate().content;
			classTemplate	= classTemplate.replace("{{object_name}}", this.sObjects()[i].getName())
						.replace("{{class_name}}", this.sObjects()[i].getName());
			
			System.out.println(triggerTemplate);
			System.out.println(classTemplate);
			// TODO: Write content to file system. Check if this will overwrite.
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