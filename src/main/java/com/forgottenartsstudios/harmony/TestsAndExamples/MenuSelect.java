package com.forgottenartsstudios.harmony.TestsAndExamples;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;
import org.jetbrains.annotations.NotNull;

public class MenuSelect extends ListenerAdapter {
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        super.onSelectMenuInteraction(event);
    }
}
