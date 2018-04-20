package test.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import test.dao.H2InMemoryDBDAO;
import test.json.FriendListResponse;
import test.json.FriendRequest;

@Path("/getCommonFriends")
public class GetCommonFriends {

	@POST
	public Response addFriend(String payload) {

		System.out.println("==> Payload from HTTP Request: " + payload);
		FriendListResponse response = new FriendListResponse();
		Gson gson = new Gson();
		try {

			FriendRequest friendRequest = new FriendRequest();
			friendRequest = gson.fromJson(payload, FriendRequest.class);

			List<String> friendList = friendRequest.getFriends();
			if (friendList.size() != 2) {
				return Response.serverError().entity("[Error]: Wrong number of input email addresses.").build();
			}

			String email1 = friendList.get(0);
			String email2 = friendList.get(1);

			if ((email1 == null) || (email1.length() == 0) || (email2 == null) || (email2.length() == 0)) {
				return Response.serverError().entity("[Error]: invalid json input.").build();
			}

			List<String> friends1 = H2InMemoryDBDAO.getFriendListByEmail(email1);
			List<String> friends2 = H2InMemoryDBDAO.getFriendListByEmail(email2);

			List<String> commonFriends = new ArrayList<String>();
			for (String friendEmail : friends1) {
				if (friends2 != null && friends2.contains(friendEmail)) {
					commonFriends.add(friendEmail);
				}
			}

			//Create Common Friend List JSON Response
			response.setCount(commonFriends.size());
			if (commonFriends.size() == 0) {
				response.setSuccess(false);
			} else {
				response.setSuccess(true);
			}
			response.setFriends(commonFriends);

		} catch (JsonSyntaxException e) {

			return Response.serverError().entity("[Error]: Invalid JSON format payload.").build();

		} catch (SQLException e) {
			return Response.serverError()
					.entity("[Error]: exception encountered while accessing H2 in memory database.").build();
		} catch (Exception e) {
			return Response.serverError().entity("[Error]: exception encountered while processing payload.").build();
		}

		String json = gson.toJson(response);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

}
