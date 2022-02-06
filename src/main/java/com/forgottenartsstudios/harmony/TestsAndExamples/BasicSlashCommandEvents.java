package com.forgottenartsstudios.harmony.TestsAndExamples;

import com.forgottenartsstudios.harmony.Main.Variables;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;

public class BasicSlashCommandEvents extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                long time = System.currentTimeMillis();
                event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                        .flatMap(v ->
                                event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                        ).queue(); // Queue both reply and edit
                break;
            case "class":
                SelectMenu menu = SelectMenu.create("menu:class")
                        .setPlaceholder("Choose your class") // shows the placeholder indicating what this menu is for
                        .setRequiredRange(1, 1) // only one can be selected
                        .addOption("Arcane Mage", "mage-arcane")
                        .addOption("Fire Mage", "mage-fire")
                        .addOption("Frost Mage", "mage-frost")
                        .build();

                event.reply("Please pick your class below")
                        .setEphemeral(true)
                        .addActionRow(menu)
                        .addActionRow(menu)
                        .addActionRow(menu)
                        .queue();
                break;
            case "prefix":
                for(int x = 0; x < Variables.guildData.size(); x++) {
                    if(Variables.guildData.get(x).getID().equals(event.getGuild().getId()) ) {
                        System.out.println(Variables.guildData.get(x).getPrefix());
                        event.reply(Variables.guildData.get(x).getPrefix()).setEphemeral(true).queue();
                    }
                }
                for(int x = 0; x < Variables.guildData.size(); x++) {
                    if(Variables.guildData.get(x).getID().equals(event.getGuild().getId()) ) {
                        System.out.println(Variables.guildData.get(x).getPrefix());
                        event.reply(Variables.guildData.get(x).getPrefix()).setEphemeral(true).queue();
                    }
                }
                break;
        }
    }
}
