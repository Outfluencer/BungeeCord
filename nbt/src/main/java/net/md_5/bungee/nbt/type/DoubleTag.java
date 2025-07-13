package net.md_5.bungee.nbt.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.nbt.NumericTag;
import net.md_5.bungee.nbt.Tag;
import net.md_5.bungee.nbt.limit.NBTLimiter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleTag implements NumericTag
{

    private double value;

    @Override
    public void read(DataInput input, NBTLimiter limiter) throws IOException
    {
        limiter.countBytes( OBJECT_HEADER + Double.BYTES );
        value = input.readDouble();
    }

    @Override
    public void write(DataOutput output) throws IOException
    {
        output.writeDouble( value );
    }

    @Override
    public byte getId()
    {
        return Tag.DOUBLE;
    }

    @Override
    public byte asByte()
    {
        return (byte) ( (int) Math.floor( getValue() ) & 0xFF );
    }

    @Override
    public short asShort()
    {
        return (short) ( (int) Math.floor( getValue() ) & 0xFFFF );
    }

    @Override
    public int asInt()
    {
        return (int) Math.floor( getValue() );
    }

    @Override
    public long asLong()
    {
        return (long) Math.floor( getValue() );
    }
}
