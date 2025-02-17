package net.md_5.bungee.api.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

/**
 * This event is fired when the proxy is shutting down before players are disconnected
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class ProxyShutdownEvent extends Event
{

}
