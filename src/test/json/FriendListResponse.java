package test.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendListResponse {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("friends")
@Expose
private List<String> friends = null;
@SerializedName("count")
@Expose
private Integer count;

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public List<String> getFriends() {
return friends;
}

public void setFriends(List<String> friends) {
this.friends = friends;
}

public Integer getCount() {
return count;
}

public void setCount(Integer count) {
this.count = count;
}

}