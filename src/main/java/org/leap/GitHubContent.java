package org.leap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

import junit.framework.Assert;

public class GitHubContent {
	// JSON Deserialization Properties
	public String type;
	public String encoding;
	public int size;
	public String name;
	public String path;
	public String content;
	public String sha;
	public String url;
	public String git_url;
	public String html_url;
	public GitHubLinks _links;
	
	// Class properties
	private String m_owner;
	private String m_repo;
	private String m_path;
	public GitHubContent(){}
	
	public GitHubContent withOwner(String owner){ this.m_owner = owner; return this; }
	public GitHubContent withRepo(String repo){ this.m_repo = repo; return this; }
	public GitHubContent withPath(String path){ this.m_path = path; return this; }
	
	public static GitHubContent get(String url_str){		
		GitHubContent contentObject = null;
        URL url;
		try {
			url = new URL(url_str);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String content 	= "";
			String line		= "";
	        
			while ((line = in.readLine()) != null) {
				content += line;
			}
			in.close();
			
			contentObject = new Gson().fromJson(content, GitHubContent.class);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return contentObject;
	}
}