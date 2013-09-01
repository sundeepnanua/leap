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
	private GitHubContent classTemplate;
	private GitHubContent metaTemplate;
	
	public void execute() {
		classTemplate = GitHubContent.get(this.class_template_url);
		metaTemplate = GitHubContent.get(this.meta_template_url);
		
		this.validateConnectionParams();
		
		System.out.println("root: " + this.getProjectRoot());
		System.out.println("Decoded template content: " + classTemplate.decodedContent());
		
		Integer recordCount = (this.limit == -1 ? this.sObjects().length : this.limit);
		System.out.println("Generating SObjectFields.cls file for " + recordCount + " objects...");
		
		String fieldContent = this.getSFieldEntries();
		
		String finalOutput = classTemplate.decodedContent().replace("{placeholder}", fieldContent);
		//create files from merge templates, to relative path
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(this.getProjectRoot() + "classes/SObjectFields.cls", "UTF-8");
			writer.write( finalOutput );
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done.");
		
		// merge with {placeholder} tag
	}
	
	private String getSFieldEntries(){
		String output = "";
		Integer counter = 0;		
		for (int i = 0; i < this.sObjects().length; i++) {
			if(this.limit != -1 && counter++ > this.limit){
				break;
			}
			String fieldName = "ALL_" + this.getObjectName(this.sObjects()[i]) + "_FIELDS";
			String sfield = "\tpublic static final String " + fieldName + " = '";
			
			DescribeSObjectResult describeSObjectResult = null;
			try {
				describeSObjectResult = this.salesforceConnection().getPartnerConnection().describeSObject(this.sObjects()[i].getName());
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
			
			Field[] fields = describeSObjectResult.getFields();
			
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				sfield += field.getName() + ",";
			}
			sfield += "';";
			sfield = sfield.replace(",'", "'");
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