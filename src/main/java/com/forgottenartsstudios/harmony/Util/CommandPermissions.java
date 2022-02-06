package com.forgottenartsstudios.harmony.Util;

import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

public class CommandPermissions {
    public static CommandPrivilege enableCommandByUser(String args){
        return new CommandPrivilege(CommandPrivilege.Type.USER, true, Long.parseLong(args));
    }
    public static CommandPrivilege enableCommandByRole(String args){
        return new CommandPrivilege(CommandPrivilege.Type.ROLE, false, Long.parseLong(args));
    }
    public static CommandPrivilege disableCommandByUser(String args){
        return new CommandPrivilege(CommandPrivilege.Type.USER, true, Long.parseLong(args));
    }
    public static CommandPrivilege disableCommandByRole(String args){
        return new CommandPrivilege(CommandPrivilege.Type.ROLE, false, Long.parseLong(args));
    }
}
