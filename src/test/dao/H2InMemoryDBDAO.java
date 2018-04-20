package test.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// H2 In-Memory Database Example shows about storing the database contents into memory. 

public class H2InMemoryDBDAO {

	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";
	private static final String DB_USER_TABLE = "USER";
	private static final String DB_USER_RELATIONSHIP_TABLE = "USER_RELATIONSHIP";


	public static void createDbSchemaWithPreparedStatement() throws SQLException {
		Connection connection = getDBConnection();
		PreparedStatement createPreparedStatement = null;

		String CreateSeqQuery = "CREATE SEQUENCE SEQ_USER START WITH 1 INCREMENT BY 1 ";
		String CreateUserTableQuery = "CREATE TABLE " + DB_USER_TABLE + "(id int primary key, email varchar(255))";
		String CreateUserRelationshipTableQuery = "CREATE TABLE " + DB_USER_RELATIONSHIP_TABLE
				+ "(main_id int, linked_id int, is_friend varchar(1),  subscribe_flag varchar(1), block_flag varchar(1), PRIMARY KEY (main_id, linked_id) )";

		try {
			connection.setAutoCommit(false);

			createPreparedStatement = connection.prepareStatement(CreateSeqQuery);
			createPreparedStatement.executeUpdate();
			createPreparedStatement.close();

			createPreparedStatement = connection.prepareStatement(CreateUserTableQuery);
			createPreparedStatement.executeUpdate();
			createPreparedStatement.close();

			createPreparedStatement = connection.prepareStatement(CreateUserRelationshipTableQuery);
			createPreparedStatement.executeUpdate();
			createPreparedStatement.close();

			System.out.println("H2 In-Memory Database inserted through PreparedStatement");

			connection.commit();
		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	// Get all users from USER table
	public static ArrayList<String> getAllUsers() throws SQLException {
		Connection connection = getDBConnection();

		PreparedStatement selectPreparedStatement = null;
		String SelectQuery = "select * from " + DB_USER_TABLE;

		ArrayList<String> userList = new ArrayList<String>();

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database read through PreparedStatement");
			while (rs.next()) {
				System.out.println("Id " + rs.getInt("id") + "; Email " + rs.getString("email"));
				userList.add(rs.getString("email"));
			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return userList;
	}

	// Get all data from USER_RELATIONSHIP table
	public static void getAllUserRelationshipRecords() throws SQLException {
		Connection connection = getDBConnection();

		PreparedStatement selectPreparedStatement = null;
		String SelectQuery = "select * from " + DB_USER_RELATIONSHIP_TABLE;

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database read through PreparedStatement");
			while (rs.next()) {
				System.out.println("main_id: " + rs.getInt("main_id") + "; linked id: " + rs.getInt("linked_id")
						+ "; is friend: " + rs.getString("is_friend") + "; subscribe flag: "
						+ rs.getString("subscribe_flag") + "; block flag: " + rs.getString("block_flag"));

			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	// Get user id by email address from USER table
	public static int getUserIdByEmail(String email) throws SQLException {
		int id = -1;
		Connection connection = getDBConnection();

		PreparedStatement selectPreparedStatement = null;
		String SelectQuery = "select * from " + DB_USER_TABLE + " where email = ? ";

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			selectPreparedStatement.setString(1, email);
			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database read through PreparedStatement");
			while (rs.next()) {
				System.out.println("User ID retrieved:  " + rs.getInt("id") + "; Email " + rs.getString("email"));
				id = rs.getInt("id");
			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return id;
	}

	// Check if the USER_RELATIONSHIP record exist for main id and linked id
	public static boolean isUserRelationshipExist(int mainId, int linkedId) throws SQLException {
		boolean flag = false;
		Connection connection = getDBConnection();

		PreparedStatement selectPreparedStatement = null;
		String SelectQuery = "select * from " + DB_USER_RELATIONSHIP_TABLE
				+ " where  main_id = ?  and linked_id = ?    ";

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			selectPreparedStatement.setInt(1, mainId);
			selectPreparedStatement.setInt(2, linkedId);

			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database read through PreparedStatement");
			while (rs.next()) {
				flag = true;
			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return flag;
	}

	// Create new user record
	public static int createNewUser(String email) throws SQLException {
		Connection connection = getDBConnection();

		int id = getUserIdByEmail(email);

		if (id > 0) {
			return id;
		}

		PreparedStatement insertPreparedStatement = null;

		String InsertUserQuery = "INSERT INTO " + DB_USER_TABLE + "(id, email) values" + "(SEQ_USER.NEXTVAL, ?)";

		try {
			connection.setAutoCommit(false);

			// Insert USER records
			insertPreparedStatement = connection.prepareStatement(InsertUserQuery);
			insertPreparedStatement.setString(1, email);
			insertPreparedStatement.executeUpdate();
			insertPreparedStatement.close();
			connection.commit();
			id = getUserIdByEmail(email);

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return id;
	}

	// Insert/Update Friend User_Relationship record
	public static boolean createFriendUserRelationshipRecords(String email1, String email2) throws SQLException {

		Connection connection = getDBConnection();
		boolean returnFlag = false;
		int id1 = createNewUser(email1);
		int id2 = createNewUser(email2);

		PreparedStatement insertPs = null;
		PreparedStatement updatePs = null;

		String InsertUserQuery = "INSERT INTO " + DB_USER_RELATIONSHIP_TABLE + "(main_id, linked_id, is_friend) values"
				+ "(? , ? , ?)";
		String UpdateQuery = "UPDATE " + DB_USER_RELATIONSHIP_TABLE
				+ "  SET is_friend =?  where  main_id = ?  and  linked_id = ? ";

		try {
			connection.setAutoCommit(false);

			if (!isUserRelationshipExist(id1, id2)) {
				// Insert USER records
				insertPs = connection.prepareStatement(InsertUserQuery);
				insertPs.setInt(1, id1);
				insertPs.setInt(2, id2);
				insertPs.setString(3, "y");
				insertPs.executeUpdate();
				insertPs.close();
				connection.commit();
			} else {
				updatePs = connection.prepareStatement(UpdateQuery);
				updatePs.setString(1, "y");
				updatePs.setInt(2, id1);
				updatePs.setInt(3, id2);
				updatePs.executeUpdate();
				updatePs.close();
				connection.commit();

			}

			if (!isUserRelationshipExist(id2, id1)) {
				// Insert USER records
				insertPs = connection.prepareStatement(InsertUserQuery);
				insertPs.setInt(1, id2);
				insertPs.setInt(2, id1);
				insertPs.setString(3, "y");
				insertPs.executeUpdate();
				insertPs.close();
				connection.commit();
			} else {
				updatePs = connection.prepareStatement(UpdateQuery);
				updatePs.setString(1, "y");
				updatePs.setInt(2, id2);
				updatePs.setInt(3, id1);
				updatePs.executeUpdate();
				updatePs.close();
				connection.commit();
			}

			returnFlag = true;

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return returnFlag;
	}

	// Insert/Update Subscribe User_Relationship record
	public static boolean createSubscribeUserRelationshipRecord(String targetEmail, String requestorEmail)
			throws SQLException {

		Connection connection = getDBConnection();
		boolean returnFlag = false;
		int requestorId = createNewUser(requestorEmail);
		int targetId = createNewUser(targetEmail);

		PreparedStatement insertPs = null;
		PreparedStatement updatePs = null;

		String InsertUserQuery = "INSERT INTO " + DB_USER_RELATIONSHIP_TABLE
				+ "(main_id, linked_id, subscribe_flag) values" + "(? , ? , ?)";
		String UpdateQuery = "UPDATE " + DB_USER_RELATIONSHIP_TABLE
				+ "  SET subscribe_flag =?  where  main_id = ?  and  linked_id = ? ";

		try {
			connection.setAutoCommit(false);

			if (!isUserRelationshipExist(targetId, requestorId)) {
				// Insert USER records
				insertPs = connection.prepareStatement(InsertUserQuery);
				insertPs.setInt(1, targetId);
				insertPs.setInt(2, requestorId);
				insertPs.setString(3, "y");
				insertPs.executeUpdate();
				insertPs.close();
				connection.commit();
			} else {
				updatePs = connection.prepareStatement(UpdateQuery);
				updatePs.setString(1, "y");
				updatePs.setInt(2, targetId);
				updatePs.setInt(3, requestorId);
				updatePs.executeUpdate();
				updatePs.close();
				connection.commit();

			}

			returnFlag = true;

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return returnFlag;
	}

	// Insert/Update Block User_Relationship record
	public static boolean createBlockUserRelationshipRecord(String targetEmail, String requestorEmail)
			throws SQLException {

		Connection connection = getDBConnection();
		boolean returnFlag = false;
		int requestorId = createNewUser(requestorEmail);
		int targetId = createNewUser(targetEmail);
		
		System.out.println("Block request requester ID: "+requestorId);
		System.out.println("Block request target ID: "+targetId);
		

		PreparedStatement insertPs = null;
		PreparedStatement updatePs = null;

		String InsertUserQuery = "INSERT INTO " + DB_USER_RELATIONSHIP_TABLE + "(main_id, linked_id, block_flag) values"
				+ "(? , ? , ?)";
		String UpdateQuery = "UPDATE " + DB_USER_RELATIONSHIP_TABLE
				+ "  SET   block_flag = ?  where  main_id = ?  and  linked_id = ? ";

		try {
			connection.setAutoCommit(false);

			if (!isUserRelationshipExist(targetId, requestorId)) {
				// Insert USER records
				insertPs = connection.prepareStatement(InsertUserQuery);
				insertPs.setInt(1, targetId);
				insertPs.setInt(2, requestorId);
				insertPs.setString(3, "y");
				insertPs.executeUpdate();
				insertPs.close();
				connection.commit();
			} else {
				updatePs = connection.prepareStatement(UpdateQuery);
				updatePs.setString(1, "y");
				updatePs.setInt(2, targetId);
				updatePs.setInt(3, requestorId);
				updatePs.executeUpdate();
				updatePs.close();
				connection.commit();

			}

			returnFlag = true;

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return returnFlag;
	}


	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	public static List<String> getFriendListByEmail(String email) throws SQLException {
		Connection connection = getDBConnection();

		PreparedStatement selectPreparedStatement = null;
		String SelectQuery = "select  user2.email as email  from " + DB_USER_TABLE + " user1, " + DB_USER_TABLE
				+ " user2, " + DB_USER_RELATIONSHIP_TABLE + " ur "
				+ "where user1.id = ur.main_id and ur.is_friend ='y'  and user2.id = ur.linked_id  and user1.email = ? ";

		List<String> userList = new ArrayList<String>();

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			selectPreparedStatement.setString(1, email);

			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database read through PreparedStatement");
			while (rs.next()) {
				System.out.println("Friend Email " + rs.getString("email"));
				userList.add(rs.getString("email"));
			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return userList;
	}

	public static boolean isUserBlocked(String target, String requestor) throws SQLException {
		Connection connection = getDBConnection();
		boolean flag = false;
		PreparedStatement selectPreparedStatement = null;
		String SelectQuery = "select  count(*)  from " + DB_USER_TABLE + " user1, " + DB_USER_TABLE + " user2, "
				+ DB_USER_RELATIONSHIP_TABLE + " ur "
				+ "where user1.id = ur.main_id and ur.block_flag ='y'  and user2.id = ur.linked_id  and user1.email = ?  and user2.email = ? ";

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			selectPreparedStatement.setString(1, target);
			selectPreparedStatement.setString(2, requestor);

			ResultSet rs = selectPreparedStatement.executeQuery();
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					flag = true;
				}
			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return flag;
	}

	public static List<String> getDistributionListByEmail(String sender) throws SQLException {
		Connection connection = getDBConnection();

		PreparedStatement selectPreparedStatement = null;

		String SelectQuery = "select  user2.email as email  from " + DB_USER_TABLE + " user1, " + DB_USER_TABLE
				+ " user2, " + DB_USER_RELATIONSHIP_TABLE + " ur "
				+ " where user1.id = ur.main_id  and user2.id = ur.linked_id  "
				+ " and ( (ur.block_flag is null or  ur.block_flag !='y') and (ur.is_friend = 'y' or ur.subscribe_flag = 'y' )  ) "
				+ " and user1.email = ? ";

		List<String> userList = new ArrayList<String>();

		try {
			connection.setAutoCommit(false);

			selectPreparedStatement = connection.prepareStatement(SelectQuery);
			selectPreparedStatement.setString(1, sender);

			ResultSet rs = selectPreparedStatement.executeQuery();
			System.out.println("H2 In-Memory Database read through PreparedStatement");
			while (rs.next()) {
				System.out.println("Friend Email " + rs.getString("email"));
				userList.add(rs.getString("email"));
			}
			selectPreparedStatement.close();

			connection.commit();

		} catch (SQLException e) {
			System.out.println("Exception Message " + e.getLocalizedMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return userList;
	}
}
