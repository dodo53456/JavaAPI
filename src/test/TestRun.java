package test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.dao.H2InMemoryDBDAO;


public class TestRun {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//H2InMemoryDBDAO h2DAO = new H2InMemoryDBDAO();
		try {
			
			H2InMemoryDBDAO.createDbSchemaWithPreparedStatement();
			
			int id = H2InMemoryDBDAO.getUserIdByEmail("andy@example.com");
			System.out.println("Get ID: " + id);
			int newEmailId = H2InMemoryDBDAO.createNewUser("test@xyz.com");
			System.out.println("New email ID: " + newEmailId);
			//H2InMemoryDBDAO.createFriendUserRelationshipRecords("dd123@gmail.com", "dd456@gmail.com");
			//H2InMemoryDBDAO.createFriendUserRelationshipRecords("dd123@gmail.com", "andy@example.com");
			H2InMemoryDBDAO.getAllUsers();
			H2InMemoryDBDAO.getAllUserRelationshipRecords();
			String email1 ="john@example.com";
			String email2 = "dodo@example.com";
			System.out.println("Is blocked: " + H2InMemoryDBDAO.isUserBlocked(email1, email2));
			System.out.println("Is blocked: " + H2InMemoryDBDAO.isUserBlocked(email2, email1)); 

			int requestorId = H2InMemoryDBDAO.createNewUser(email1);
			int targetId = H2InMemoryDBDAO.createNewUser(email2);
			
			System.out.println(email1 + ":" + requestorId);
			System.out.println(email2 + ":" + targetId);
			
			
			
			List<String> emailList = new ArrayList<String>();
			   String s = "test test@gmail.com &&^ test2@gmail.com ((& ";
			    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(s);
			    while (m.find()) {
			        System.out.println(m.group());
			        emailList.add(m.group());
			    }
			    System.out.println(emailList.size());
			

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
