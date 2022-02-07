package com.forgottenartsstudios.harmony.Util;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;

import static com.forgottenartsstudios.harmony.Main.Harmony.jda;

public class Utilities {
    // UTILITY FUNCTIONS USED FOR DIAGNOSTICS AND TROUBLESHOOTING.

    //Prints out all Global Bot Command Names and IDs
    public static void retrieveCommands(){
        RestAction<List<Command>> commandList = jda.retrieveCommands();
        for (int i = 0; i < commandList.complete().size(); i++) {
            // Print all elements of List
            System.out.println(commandList.complete().get(i).getName());
            System.out.println(commandList.complete().get(i).getIdLong());
        }
    }
    public static void chooseClass(SlashCommandInteractionEvent event){
        assert event != null;
        event.deferReply(true).queue();
        SelectMenu menu = SelectMenu.create("menu:class")
                .setPlaceholder("Choose your class") // shows the placeholder indicating what this menu is for
                .setRequiredRange(1, 1) // only one can be selected
                .addOption("Menu 1 Option 1", "m1o1")
                .addOption("Menu 1 Option 2", "m1o2")
                .addOption("Menu 1 Option 3", "m1o3")
                .build();
        SelectMenu menu2 = SelectMenu.create("menu:class2")
                .setPlaceholder("Choose your class") // shows the placeholder indicating what this menu is for
                .addOption("Menu 2 Option 1", "m2o1")
                .addOption("Menu 2 Option 2", "m2o2")
                .addOption("Menu 3 Option 3", "m2o3")
                .build();
        event.reply("Please pick your class below")
                .setEphemeral(true)
                .addActionRow(menu)
                .addActionRow(menu2)
                .queue();
    }
    /*
    public static void testSavingPlayersToDB(MessageReceivedEvent event) throws SQLException, IOException {
        Connection c = connectProductionDB();
        Instant start = Instant.now();
        for (int x = 0; x < 10000; x++) {
            Player player = new Player();
            player.setDiscordID(Objects.requireNonNull(event.getMember()).getIdLong() + x);
            player.setGuildID(event.getMember().getGuild().getIdLong());
            player.setName(event.getMember().getEffectiveName());
            player.setLevel(1);
            player.setExp(0);
            player.setNextLevel(HarmonyRPG.nextLevel(player.getLevel() + 1));
            player.setBattlePower(1);
            player.setHp(1);
            HarmonyRPGVariables.players.add(player);
            String setDefaultParameters = "INSERT INTO players" +
                    " (ID, GUILDID, NAME, LEVEL, EXP, NEXTLEVEL, BATTLEPOWER, HP) VALUES " +
                    " (?, ?, ?, ?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = c.prepareStatement(setDefaultParameters);
                {
                    preparedStatement.setLong(1, player.getDiscordID());
                    preparedStatement.setLong(2, player.getGuildID());
                    preparedStatement.setString(3, player.getName());
                    preparedStatement.setInt(4, player.getLevel());
                    preparedStatement.setInt(5, player.getExp());
                    preparedStatement.setInt(6, player.getNextLevel());
                    preparedStatement.setInt(7, player.getBattlePower());
                    preparedStatement.setInt(8, player.getHp());
                    // Step 3: Execute the query or update query
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            } catch (SQLException e){
                printSQLException(e);
            }
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(timeElapsed);
        event.getMessage().reply("Files loaded to DB " + timeElapsed).queue();
        c.close();
    }*/

    //ADD ALIAS COMMAND FOR GUILD ALIAS//
    //NOT YET IMPLEMENTED//
    /*
    case "setalias":
                        if(args.length == 2) {
                            if (args[1].length() > 3) {
                                event.getMessage().reply("Prefix should less than 4 Characters").queue();
                                return;
                            }
                            try {
                                RPGDatabase.updateAlias(event.getGuild().getId(), args[1]);
                                for(int x = 0; x < RPGVariables.gameGuilds.size(); x++) {
                                    if(RPGVariables.gameGuilds.get(x).getID() == event.getGuild().getIdLong() ) {

                                        RPGVariables.gameGuilds.get(x).setAlias(args[1]);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            event.getMessage().reply("`alias` updated to " + args[1]).queue();
                        }else event.getMessage().reply("Invalid syntax").queue();
                        break;
     */

    /*
    .addActionRow(
                            Button.success("em2oji", Emoji.fromUnicode("U+2694U+FE0F")),
                            Button.primary("btn2", Emoji.fromMarkdown("<:minn:245267426227388416>")),
                            Button.danger("dolla2r", Emoji.fromMarkdown("<:redribbon:940009419519959051>")), // Button with only a label
                            Button.secondary("lig2htred", Emoji.fromMarkdown("<a:matrix:940009209196605530>")), // Button with only a label
                            Button.success("attac2k", "Attack").withEmoji(Emoji.fromMarkdown("<:yayPraiseTheSun:742681387617878109>"))
                    )
     */
}
