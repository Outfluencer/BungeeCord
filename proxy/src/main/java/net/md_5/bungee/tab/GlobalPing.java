package net.md_5.bungee.tab;

import java.util.EnumSet;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItemUpdate;

public class GlobalPing extends Global
{

    public GlobalPing(ProxiedPlayer player)
    {
        super( player );
    }

    @Override
    public void onPingChange(int ping)
    {
        PlayerListItem.Item currentPlayerItem = new PlayerListItem.Item();
        currentPlayerItem.setUuid( player.getUniqueId() );
        currentPlayerItem.setPing( ping );

        PlayerListItem oldPacket = new PlayerListItem();
        oldPacket.setAction( PlayerListItem.Action.UPDATE_LATENCY );
        oldPacket.setItems( new PlayerListItem.Item[]
        {
            currentPlayerItem
        } );

        PlayerListItemUpdate newPacket = new PlayerListItemUpdate();
        newPacket.setActions( EnumSet.of( PlayerListItemUpdate.Action.UPDATE_LATENCY ) );
        newPacket.setItems( oldPacket.getItems() );

        ProxyServer.getInstance().getPlayers().forEach( player ->
        {
            if ( player.getPendingConnection().getVersion() <= ProtocolConstants.MINECRAFT_1_19_1 )
            {
                player.unsafe().sendPacket( oldPacket );
            } else
            {
                ((UserConnection) player).sendPacketQueued( newPacket );
            }
        } );
    }

}
