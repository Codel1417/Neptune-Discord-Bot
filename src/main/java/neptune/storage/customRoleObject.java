package neptune.storage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class customRoleObject {
    @JsonIgnore()
    private Map<String, String> customRoles;
    public customRoleObject(){
        customRoles = new HashMap<>();
    }
    @JsonIgnore
    public String getRoleID(String MemberID){
        return customRoles.getOrDefault(MemberID,null);
    }
    @JsonIgnore
    public void addRole(String MemberID, String RoleID){
        customRoles.put(MemberID,RoleID);
    }
    @JsonIgnore
    public void removeRole(String MemberID){
        customRoles.remove(MemberID);
    }
}