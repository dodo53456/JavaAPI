package test.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import test.dao.H2InMemoryDBDAO;

@Path("/readAllUsers")
public class ReadAllUsers {
	
	@GET
	public String readFromH2DB() {
		String resource = "<h1> Read data from H2 DB HTML with log</h1>";
		System.out.println("==> Test log Read DATA from H2 DB");

		try{
		ArrayList<String> result =	H2InMemoryDBDAO.getAllUsers();	
		H2InMemoryDBDAO.getAllUserRelationshipRecords();
	
		for(String email : result){
			resource = resource + "<br/> " + email;
		}
		
		}catch(Exception e){
			
		}

		return resource;
		
	}
	
}
