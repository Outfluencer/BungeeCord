package net.md_5.bungee.entitymap;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.protocol.DefinedPacket;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class EntityMap_1_20_2 extends EntityMap
{
    static final EntityMap_1_20_2 INSTANCE_1_20_2 = new EntityMap_1_20_2( 0x33 );
    static final EntityMap_1_20_2 INSTANCE_1_20_3 = new EntityMap_1_20_2( 0x34 );
    //
    private final int spectateId;

    @Override
    public void rewriteClientbound(ByteBuf packet, int oldId, int newId, int protocolVersion)
    {

    }

    @Override
    public void rewriteServerbound(ByteBuf packet, int oldId, int newId)
    {
        // Special cases
        if ( !BungeeCord.getInstance().getConfig().isIpForward() )
        {
            int readerIndex = packet.readerIndex();
            int packetId = DefinedPacket.readVarInt( packet );
            int packetIdLength = packet.readerIndex() - readerIndex;

            if ( packetId == spectateId )
            {
                rewriteSpectate( packet, readerIndex, packetIdLength );
            }
            packet.readerIndex( readerIndex );
        }
    }
}
