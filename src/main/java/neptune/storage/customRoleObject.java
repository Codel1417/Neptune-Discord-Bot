package neptune.storage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class customRoleObject {
    @JsonProperty
    private Map<String, String> customRoles;
    public customRoleObject(){
        customRoles = new HashMap<>();
    }
    public String getRoleID(String MemberID){
        return customRoles.getOrDefault(MemberID,null);
    }
    public void addRole(String MemberID, String RoleID){
        customRoles.put(MemberID,RoleID);
    }
    public void removeRole(String MemberID){
        customRoles.remove(MemberID);
    }
}