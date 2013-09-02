package org.leap;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.tools.ant.BuildException;

import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.ws.ConnectionException;

public class SFieldsTask extends LeapTask {
	private String class_template_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/classes/SObjectFields.cls";
	private String meta_template_url = "https://api.github.com/repos/cubiccompass/leap/contents/templates/src/classes/SObjectFields.cls-meta.xml";
	private String MERGE_TEMPLATE_TAG = "{placeholder}";
    
    Integer limit = -1;
    public void setLimit(String l){
    	limit = Integer.valueOf(l);
    }
    
    boolean customOnly = false;
    public void setCustomOnly(String setting) {
    	customOnly = Boolean.valueOf(setting);
    }
    
	public void execute() {
		this.validateConnectionParams();
		
		Integer recordCount = (this.limit == -1 ? this.sObjects().length : this.limit);
		System.out.println("Generating SObjectFields.cls file for " + recordCount + " objects...");
		
		String fieldContent = this.getSFieldEntries();
		String finalOutput = this.getClassTemplate().decodedContent().replace(MERGE_TEMPLATE_TAG, fieldContent);
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(this.getProjectRoot() + "classes/SObjectFields.cls", "UTF-8");
			writer.write( finalOutput );
			writer.close();
			System.out.println("Created " + this.getProjectRoot() + "classes/SObjectFields.cls");
						
			writer = new PrintWriter(this.getProjectRoot() + "classes/SObjectFields.cls-meta.xml", "UTF-8");
			writer.write( this.getMetaTemplate().decodedContent() );
			writer.close();
			System.out.println("Created " + this.getProjectRoot() + "classes/SObjectFields.cls-meta.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done.");
	}
	
	private LeapTemplate m_leapTemplate = null;
    public LeapTemplate getLeapTemplate(){
    	if(m_leapTemplate == null){
    		m_leapTemplate = new LeapTemplate().withContent( this.getClassTemplate().decodedContent() );
    	}
    	return m_leapTemplate;
    }
    
    private Leaplet m_fieldRowTemplate = null;
    public Leaplet getSFieldRowTemplate(){
    	if(m_fieldRowTemplate == null && this.getLeapTemplate().hasLeaplets()){
    		m_fieldRowTemplate = this.getLeapTemplate().getLeapletByName("sfield");
    	}
    	return m_fieldRowTemplate;
    }
    
    private GitHubContent m_metaTemplate = null;
    protected GitHubContent getMetaTemplate(){
    	if(m_metaTemplate == null){
    		m_metaTemplate = GitHubContent.get(this.meta_template_url);
    	}
    	return m_metaTemplate;
    }
    
    private GitHubContent m_classTemplate = null;
    protected GitHubContent getClassTemplate(){
    	if(m_classTemplate == null){
    		m_classTemplate = GitHubContent.get(this.class_template_url);
    	}
    	return m_classTemplate;
    }
	
	private String getSFieldEntries(){
		String output = "";
		Integer counter = 0;		
		for (int i = 0; i < this.sObjects().length; i++) {
			if(this.customOnly && this.sObjects()[i].isCustom()){ continue;}
			if(this.limit != -1 && counter++ > this.limit){ break;}
			
			String object_name = "ALL_" + this.getObjectName(this.sObjects()[i]) + "_FIELDS";
			String sfield = this.getSFieldRowTemplate().content; // public static final String {object_name} = '{field_list}';
			sfield = sfield.replace("{object_name}", object_name);
			
			String field_list = "";
			
			DescribeSObjectResult describeSObjectResult = null;
			try {
				describeSObjectResult = this.salesforceConnection().getPartnerConnection().describeSObject(this.sObjects()[i].getName());
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
			
			Field[] fields = describeSObjectResult.getFields();
			
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				field_list += field.getName() + ",";
			}
			field_list = field_list.substring(0, field_list.length() - 1);
			sfield = sfield.replace("{field_list}", field_list);
			output += sfield;
		}
		return output;
	}
	
	private String getObjectName(DescribeGlobalSObjectResult sobject){
		String objectName = "";
		String[] objectTokens = sobject.getName().split("__");		
		if(objectTokens.length == 1){ 		// Example "Account"
			objectName = sobject.getName();
		}
		else if(objectTokens.length == 2){	// Example "Order__c"
			objectName = objectTokens[0];
		}
		else if(objectTokens.length == 3){	// Example "ISV__Order__c"
			objectName = objectTokens[2];
		}
		return objectName.toUpperCase();
	}
}