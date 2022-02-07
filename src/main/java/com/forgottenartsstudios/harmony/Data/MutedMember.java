package com.forgottenartsstudios.harmony.Data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class MutedMember {
    long id;
    long timeMuted;
    long lengthOfMute;
    long timeToUnmute;
    Member member;
    List<Role> roles;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeMuted() {
        return timeMuted;
    }

    public void setTimeMuted(long timeMuted) {
        this.timeMuted = timeMuted;
    }

    public long getLengthOfMute() {
        return lengthOfMute;
    }

    public void setLengthOfMute(long lengthOfMute) {
        this.lengthOfMute = lengthOfMute;
    }

    public long getTimeToUnmute() {
        return timeToUnmute;
    }

    public void setTimeToUnmute(long timeToUnmute) {
        this.timeToUnmute = timeToUnmute;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
