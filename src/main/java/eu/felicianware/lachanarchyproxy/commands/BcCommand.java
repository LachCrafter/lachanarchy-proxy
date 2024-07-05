package eu.felicianware.lachanarchyproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class BcCommand implements SimpleCommand {

    private final ProxyServer server;

    private static final TextColor GOLD = NamedTextColor.GOLD;

    public BcCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            source.sendMessage(Component.text("Usage: /bc <message>").color(GOLD));
            return;
        }

        String message = String.join(" ", args);
        Component broadcastMessage = Component.text("[Server] " + message).color(GOLD);

        server.getAllPlayers().forEach(player -> player.sendMessage(broadcastMessage));
    }
}
