package net.md_5.bungee.nbt;

public interface NumericTag extends TypedTag
{
    default Number asNumber()
    {
        return (Number) getValue();
    }

    default byte asByte()
    {
        return (byte) getValue();
    }

    default short asShort()
    {
        return (short) getValue();
    }

    default int asInt()
    {
        return (int) getValue();
    }

    default long asLong()
    {
        return (long) getValue();
    }

    default float asFloat()
    {
        return (float) getValue();
    }

    default double asDouble()
    {
        return (double) getValue();
    }

    default boolean asBoolean()
    {
        return asLong() != 0;
    }

    default String asString() {
        return String.valueOf( getValue() );
    }

    Object getValue();

}
