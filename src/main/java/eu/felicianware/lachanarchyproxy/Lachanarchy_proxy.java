package eu.felicianware.lachanarchyproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.felicianware.lachanarchyproxy.commands.JoinCommand;
import org.slf4j.Logger;

@Plugin(
        id = "lachanarchy-proxy",
        name = "lachanarchy-proxy",
        version = BuildConstants.VERSION
)
public class Lachanarchy_proxy {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer proxy;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = proxy.getCommandManager();
        commandManager.register("join", new JoinCommand(proxy, logger));
    }
}
