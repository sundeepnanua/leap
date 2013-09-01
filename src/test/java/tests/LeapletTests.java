package tests;

import java.io.File;

import junit.framework.Assert;
import org.junit.Test;
import org.leap.GitHubContent;

public class LeapletTests {	
	
	@Test
	public void basicTests(){
		String url = getClass().getResource("/test.txt").getFile();
		File file = new File(url);
		Assert.assertTrue(file.exists());
	}
}