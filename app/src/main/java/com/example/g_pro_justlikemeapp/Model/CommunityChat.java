package com.example.g_pro_justlikemeapp.Model;

public class CommunityChat {

    String groupId,groupTitle,groupDescription,groupIcon,Timestamp,creatBY;
    public CommunityChat() {
    }

    public CommunityChat(String groupId,String groupTitle,String groupDescription,String groupIcon,String Timestamp,String creatBY) {
        this.groupId = groupId;
        this.groupTitle=groupTitle;
        this.groupDescription=groupDescription;
        this.groupIcon=groupIcon;
        this.Timestamp=Timestamp;
        this.creatBY=creatBY;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getCreatBY() {
        return creatBY;
    }

    public void setCreatBY(String creatBY) {
        this.creatBY = creatBY;
    }
}
