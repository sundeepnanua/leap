package tests;

import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;
import org.leap.SFieldsTask;

public class SFieldsTaskTests {
	
	@Test
	public void fetchGitHubTemplateTest(){
		SFieldsTask task = new SFieldsTask();
		
		task.setUsername(System.getenv("SALESFORCE_USERNAME"));
		task.setPassword(System.getenv("SALESFORCE_PASSWORD"));
		task.setToken(System.getenv("SALESFORCE_TOKEN"));
		task.setServerurl(System.getenv("SALESFORCE_SERVER_URL"));
		task.setProjectRoot( System.getenv("PROJECT_ROOT"));
		task.setLimit("10");
		Assert.assertTrue(task.salesforceConnection().isValid());
		
		//Assert.assertNotNull(task.getLocation());
		//System.out.println("task.getLocation(): " + task.getLocation().toString() );
		
		//URL location = Test.class.getProtectionDomain().getCodeSource().getLocation();
        //System.out.println(location.getFile());
		System.out.println("rootSourceFolder: " + task.getProjectRoot() );
		
		task.execute();
		
		// Get file located on relative path
		Assert.assertTrue(true); // Path to file is valid
	}
}