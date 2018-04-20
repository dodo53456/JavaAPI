package test.json;




import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendRequest {

@SerializedName("friends")
@Expose
private List<String> friends = null;

public List<String> getFriends() {
return friends;
}

public void setFriends(List<String> friends) {
this.friends = friends;
}

}