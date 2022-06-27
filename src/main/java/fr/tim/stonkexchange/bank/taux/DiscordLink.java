package fr.tim.stonkexchange.bank.taux;

import fr.tim.stonkexchange.StonkExchange;
import fr.tim.stonkexchange.files.ConfigManager;
import fr.tim.stonkexchange.misc.DiscordWebhook;

import java.io.IOException;

public class DiscordLink {
    private static final String TauxWebhook = ConfigManager.getString("Discord.webhook");

    public static final String mcoinEmoji = ConfigManager.getString("Discord.emoji");

    public static void sendMessage(String msg,String... desc) {
        DiscordWebhook wh = new DiscordWebhook(TauxWebhook);

        wh.setContent(msg);

        for (String s : desc) wh.addEmbed(new DiscordWebhook.EmbedObject().setDescription(s));

        wh.setAvatarUrl("https://raw.githubusercontent.com/TimEpsilon/Stonks-Exchange/master/img/sam.png");
        wh.setUsername("[S.A.M.]");

        try {
            wh.execute();
        } catch (IOException e) {
            StonkExchange.getPlugin().getLogger().info("Failed to load up webhook");
            e.printStackTrace();
        }
    }
}
