package eu.felicianware.lachanarchyproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class JoinCommand implements SimpleCommand {

    private final ProxyServer proxy;
    private final Logger logger;

    private static final TextColor YELLOW = NamedTextColor.GOLD;
    private static final TextColor LIGHT_RED = NamedTextColor.RED;

    private static final String MAIN_SERVER = "main";
    private static final String TEST_SERVER = "test";

    public JoinCommand(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            sendUsageMessage(source);
            return;
        }

        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Only players can use this command.").color(YELLOW));
            return;
        }

        Player player = (Player) source;
        String serverName = args[0].toLowerCase();

        if (!serverName.equals(MAIN_SERVER) && !serverName.equals(TEST_SERVER)) {
            sendUsageMessage(source);
            return;
        }

        Optional<RegisteredServer> optionalServer = proxy.getServer(serverName);

        if (!optionalServer.isPresent()) {
            sendUsageMessage(source);
            return;
        }

        RegisteredServer server = optionalServer.get();

        if (server.getPlayersConnected().stream().anyMatch(p -> p.getUniqueId().equals(player.getUniqueId()))) {
            source.sendMessage(Component.text("You are already on server ")
                    .color(YELLOW)
                    .append(Component.text(serverName).color(LIGHT_RED))
                    .append(Component.text(".").color(YELLOW))
            );
            return;
        }

        source.sendMessage(Component.text("Connecting to ")
                .color(YELLOW)
                .append(Component.text(serverName).color(LIGHT_RED))
                .append(Component.text("...").color(YELLOW))
        );
        player.createConnectionRequest(server).fireAndForget();
    }

    private void sendUsageMessage(CommandSource source) {
        source.sendMessage(Component.text("Command join requires a valid server input. Select one from: ")
                .color(YELLOW)
                .append(Component.text(MAIN_SERVER).color(LIGHT_RED))
                .append(Component.text(", ").color(YELLOW))
                .append(Component.text(TEST_SERVER).color(LIGHT_RED))
                .append(Component.text(".").color(YELLOW))
        );
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return List.of(MAIN_SERVER, TEST_SERVER);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}
