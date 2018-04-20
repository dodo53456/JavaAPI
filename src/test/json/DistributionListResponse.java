package test.json;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistributionListResponse {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("recipients")
@Expose
private List<String> recipients = null;

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public List<String> getRecipients() {
return recipients;
}

public void setRecipients(List<String> recipients) {
this.recipients = recipients;
}

}