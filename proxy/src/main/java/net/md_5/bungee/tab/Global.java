package net.md_5.bungee.tab;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PlayerListItemRemove;
import net.md_5.bungee.protocol.packet.PlayerListItemUpdate;

public class Global extends TabList
{

    private static final ConcurrentHashMap<UUID, Integer> UUID_GAMEMODE_MAP = new ConcurrentHashMap<>();

    public Global(ProxiedPlayer player)
    {
        super( player );
    }
    
    @Override
    public void onConnect()
    {
        PlayerListItem.Item currentPlayerItem = new PlayerListItem.Item();
        currentPlayerItem.setUuid( player.getUniqueId() );
        currentPlayerItem.setUsername( player.getName() );

        currentPlayerItem.setGamemode( UUID_GAMEMODE_MAP.getOrDefault( player.getUniqueId(), 0 ) );
        currentPlayerItem.setPing( 0 );
        currentPlayerItem.setListed( true );
        currentPlayerItem.setProperties( ( (InitialHandler)player.getPendingConnection() ).getLoginProfile().getProperties() );
        PlayerListItem oldPacket = new PlayerListItem();
        oldPacket.setAction( PlayerListItem.Action.ADD_PLAYER );
        oldPacket.setItems( new PlayerListItem.Item[]
        {
                currentPlayerItem
        } );

        PlayerListItemUpdate newPacket = new PlayerListItemUpdate();
        newPacket.setActions( EnumSet.of( PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_GAMEMODE, PlayerListItemUpdate.Action.UPDATE_LATENCY, PlayerListItemUpdate.Action.UPDATE_LISTED ) );
        newPacket.setItems( oldPacket.getItems() );

        ProxyServer.getInstance().getPlayers().forEach( player ->
        {
            // add the current player to the all other players tablist
            if ( !this.player.equals( player ) )
            {
                if ( player.getPendingConnection().getVersion() <= ProtocolConstants.MINECRAFT_1_19_1 )
                {
                    player.unsafe().sendPacket( oldPacket );
                } else
                {
                    ((UserConnection) player).sendPacketQueued( newPacket );
                }
            }

            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid( player.getUniqueId() );
            item.setUsername( player.getName() );
            item.setGamemode( UUID_GAMEMODE_MAP.getOrDefault( player.getUniqueId(), 0 ) );
            item.setPing( 0 );
            item.setProperties( ( (InitialHandler)player.getPendingConnection() ).getLoginProfile().getProperties() );
            item.setListed( true );

            // add the looped player to the current player's tablist
            if ( this.player.getPendingConnection().getVersion() <= ProtocolConstants.MINECRAFT_1_19_1 )
            {
                PlayerListItem packet = new PlayerListItem();
                packet.setAction( PlayerListItem.Action.ADD_PLAYER );
                packet.setItems( new PlayerListItem.Item[]
                {
                    item
                } );
                this.player.unsafe().sendPacket( packet );
            } else
            {
                PlayerListItemUpdate packet = new PlayerListItemUpdate();
                packet.setActions( EnumSet.of( PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_GAMEMODE, PlayerListItemUpdate.Action.UPDATE_LATENCY , PlayerListItemUpdate.Action.UPDATE_LISTED) );
                packet.setItems( new PlayerListItem.Item[]
                {
                    item
                } );
                ((UserConnection) this.player).sendPacketQueued( packet );
            }
        } );
    }

    @Override
    public void onDisconnect()
    {

        UUID_GAMEMODE_MAP.remove( player.getUniqueId() );
        PlayerListItem.Item item = new PlayerListItem.Item();
        item.setUuid( player.getUniqueId() );

        PlayerListItem oldPacket = new PlayerListItem();
        oldPacket.setAction( PlayerListItem.Action.REMOVE_PLAYER );
        oldPacket.setItems( new PlayerListItem.Item[]
        {
            item
        } );

        PlayerListItemRemove newPacket = new PlayerListItemRemove();
        newPacket.setUuids( new UUID[]
        {
            player.getUniqueId()
        } );

        ProxyServer.getInstance().getPlayers().forEach( player ->
        {
            // don't need to send the packet to this one, he's leaving anyway
            if ( this.player.equals( player ) )
            {
                return;
            }
            // but to the other ones
            if ( player.getPendingConnection().getVersion() <= ProtocolConstants.MINECRAFT_1_19_1 )
            {
                player.unsafe().sendPacket( oldPacket );
            } else
            {
                ((UserConnection) player).sendPacketQueued( newPacket );
            }
        } );
    }


    @Override
    public void onServerChange() {
        // reset the tablist
        // remove all
        if ( player.getPendingConnection().getVersion() <= ProtocolConstants.MINECRAFT_1_19_1 )
        {
            List<PlayerListItem.Item> items = new ArrayList<>();

            ProxyServer.getInstance().getPlayers().forEach( player -> {
                PlayerListItem.Item item = new PlayerListItem.Item();
                item.setUuid( player.getUniqueId() );
                items.add(item);
            });

            PlayerListItem remove = new PlayerListItem();
            remove.setAction( PlayerListItem.Action.REMOVE_PLAYER );
            remove.setItems( items.toArray( new PlayerListItem.Item[0] ) );
            player.unsafe().sendPacket( remove );
        } else
        {
            List<UUID> uuids = new ArrayList<>();
            ProxyServer.getInstance().getPlayers().forEach( player -> uuids.add(player.getUniqueId()));
            PlayerListItemRemove remove = new PlayerListItemRemove();
            remove.setUuids( uuids.toArray( new UUID[0] ) );
            ((UserConnection) player).sendPacketQueued( remove );
        }

        // add all again
        ProxyServer.getInstance().getPlayers().forEach( player ->
        {
            PlayerListItem.Item item = new PlayerListItem.Item();
            item.setUuid( player.getUniqueId() );
            item.setUsername( player.getName() );
            item.setGamemode( UUID_GAMEMODE_MAP.getOrDefault( player.getUniqueId(), 0 ) );
            item.setPing( 0 );
            item.setProperties( ( (InitialHandler)player.getPendingConnection() ).getLoginProfile().getProperties() );
            item.setListed( true );

            // add the looped player to the current players tablist
            if ( this.player.getPendingConnection().getVersion() <= ProtocolConstants.MINECRAFT_1_19_1 )
            {
                PlayerListItem packet = new PlayerListItem();
                packet.setAction( PlayerListItem.Action.ADD_PLAYER );
                packet.setItems( new PlayerListItem.Item[]
                {
                    item
                } );
                this.player.unsafe().sendPacket( packet );
            } else
            {
                PlayerListItemUpdate packet = new PlayerListItemUpdate();
                packet.setActions( EnumSet.of( PlayerListItemUpdate.Action.ADD_PLAYER, PlayerListItemUpdate.Action.UPDATE_GAMEMODE, PlayerListItemUpdate.Action.UPDATE_LATENCY , PlayerListItemUpdate.Action.UPDATE_LISTED) );
                packet.setItems( new PlayerListItem.Item[]
                {
                    item
                } );
                ((UserConnection) this.player).sendPacketQueued( packet );
            }
        } );
    }

    @Override
    public void onUpdate(PlayerListItem playerListItem)
    {
        if ( playerListItem.getAction() == PlayerListItem.Action.ADD_PLAYER || playerListItem.getAction() == PlayerListItem.Action.UPDATE_GAMEMODE )
        {
            for ( PlayerListItem.Item item : playerListItem.getItems() )
            {
                updateGameMode(item);
            }
        }
    }


    @Override
    public void onUpdate(PlayerListItemUpdate playerListItem)
    {
        if ( playerListItem.getActions().contains(PlayerListItemUpdate.Action.UPDATE_GAMEMODE ) )
        {
            for ( PlayerListItem.Item item : playerListItem.getItems() )
            {
                updateGameMode(item);
            }
        }
    }

    private void updateGameMode(PlayerListItem.Item item)
    {
        Integer olfGameMode = UUID_GAMEMODE_MAP.put( item.getUuid(), item.getGamemode() );
        // we only need to update if switched to spec mode
        boolean update = olfGameMode == null || ( olfGameMode == 3 ^ item.getGamemode() == 3 );
        if ( update )
        {
            PlayerListItem.Item playerListItemItem = new PlayerListItem.Item();
            playerListItemItem.setUuid( item.getUuid() );
            playerListItemItem.setGamemode( item.getGamemode() );

            PlayerListItem oldPacket = new PlayerListItem();
            oldPacket.setAction( PlayerListItem.Action.UPDATE_GAMEMODE );
            oldPacket.setItems( new PlayerListItem.Item[]
            {
                playerListItemItem
            } );

            PlayerListItemUpdate newPacket = new PlayerListItemUpdate();
            newPacket.setActions( EnumSet.of( PlayerListItemUpdate.Action.UPDATE_GAMEMODE ) );
            newPacket.setItems( new PlayerListItem.Item[]
            {
                playerListItemItem
            } );
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

    @Override
    public void onUpdate(PlayerListItemRemove playerListItem)
    {

    }

    @Override
    public void onPingChange(int ping)
    {

    }
}
