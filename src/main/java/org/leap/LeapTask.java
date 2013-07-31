package org.leap;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.ws.ConnectionException;

public class LeapTask extends Task {
	
	public String rootSourceFolder(){
		//return getLocation() + "/src/";
		String[] path = this.getLocation().getFileName().split("/");
		String result = "";
		for(String p : path){
			if(p.contains(".")){
				continue;
			}
			result += p + "/";
		}
		result += "src/";
		return result;
		//return this.getLocation().getFileName();
	}
	
	String username;
    public void setUsername(String uname) {
    	username = uname;
    }
    
    String password;
    public void setPassword(String pw) {
    	password = pw;
    }
    
    String token;
    public void setToken(String t) {
    	token = t;
    }
    
    String serverurl;
    public void setServerurl(String url) {
    	serverurl = url;
    }
    
    String api = "27.0";
    public void setApi(String apiVersion){
    	api = apiVersion;
    }
    
    String namespace = "";
    public void setNamespace(String ns){
    	namespace = ns;
    }
    
    private SalesforceConnection m_salesforceConnection = null;
    public SalesforceConnection salesforceConnection() throws ConnectionException{
    	if(m_salesforceConnection == null){
    		m_salesforceConnection = new SalesforceConnection(username, password, "", serverurl);
    	}
    	return m_salesforceConnection;
    	//throw new BuildException("Missing required field.");
    }
    
    private DescribeGlobalSObjectResult[] m_sobjectResults;
	public DescribeGlobalSObjectResult[] sObjects(){
		if(m_sobjectResults == null){
			DescribeGlobalResult describeGlobalResult = null;
			try {
				describeGlobalResult = salesforceConnection().getPartnerConnection().describeGlobal();
			} catch (ConnectionException e) {
				e.printStackTrace();
				//errors.add(e.getMessage());
			}
			m_sobjectResults = describeGlobalResult.getSobjects();
		}
		return m_sobjectResults;
	}
}
