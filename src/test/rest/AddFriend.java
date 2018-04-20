package test.rest;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import test.dao.H2InMemoryDBDAO;
import test.json.FriendRequest;

@Path("/addFriend")
public class AddFriend {

	@POST
	public Response addFriend(String payload) {

		System.out.println("==> Payload from HTTP Request: " + payload);

		try {
			Gson gson = new Gson();

			FriendRequest friendRequest = gson.fromJson(payload, FriendRequest.class);

			List<String> friendList = friendRequest.getFriends();
			if (friendList.size() != 2) {
				return Response.serverError().entity("[Error]: Wrong number of input email addresses.").build();
			}

			String email1 = friendList.get(0);
			String email2 = friendList.get(1);

			if(H2InMemoryDBDAO.isUserBlocked(email1, email2)|| H2InMemoryDBDAO.isUserBlocked(email2, email1)){
				return Response.serverError().entity("[Error]: Block update found. Can not create friend relationship.").build();

			}
			//Create User and User Relationship records
			H2InMemoryDBDAO.createFriendUserRelationshipRecords(email1, email2);
			
		} catch (JsonSyntaxException e) {
			return Response.serverError().entity("[Error]: Invalid JSON format payload.").build();

		} catch(SQLException e){
			return Response.serverError().entity("[Error]: exception encountered while accessing H2 in memory database.").build();
		}
		catch (Exception e) {
			return Response.serverError().entity("[Error]: exception encountered while processing payload.").build();
		}

		String json = "{\"success\": true }";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}
}
