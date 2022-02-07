package com.forgottenartsstudios.harmony.Data;

import net.dv8tion.jda.api.entities.Member;

public class Moderation {
    String name;
    Member modPanelMember;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Member getModPanelMember() {
        return modPanelMember;
    }
    public void setModPanelMember(Member modPanelMember) {
        this.modPanelMember = modPanelMember;
    }
}
