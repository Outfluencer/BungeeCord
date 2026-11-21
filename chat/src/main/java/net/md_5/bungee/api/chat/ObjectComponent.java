package net.md_5.bungee.api.chat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.md_5.bungee.api.chat.objects.ChatObject;

/**
 * An object component that can be used to display objects.
 * <p>
 * It can either display a player's head or an object by a specific sprite and
 * an atlas.
 * <p>
 * Note: this was added in Minecraft 1.21.9. The {@link #fallback} was added
 * in Minecraft 26.1.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public final class ObjectComponent extends BaseComponent
{

    private ChatObject object;

    /**
     * The optional fallback component which is displayed if the object cannot
     * be displayed, and which is used when converting the component to text.
     * <p>
     * Note: this was added in Minecraft 26.1.
     */
    private BaseComponent fallback;

    /**
     * Creates a ObjectComponent from a given ChatObject.
     *
     * See {@link net.md_5.bungee.api.chat.objects.PlayerObject} and
     * {@link net.md_5.bungee.api.chat.objects.SpriteObject}.
     *
     * @param object the ChatObject
     */
    public ObjectComponent(@NonNull ChatObject object)
    {
        this.object = object;
    }

    /**
     * Creates a ObjectComponent from a given ChatObject and a fallback
     * component.
     *
     * See {@link net.md_5.bungee.api.chat.objects.PlayerObject} and
     * {@link net.md_5.bungee.api.chat.objects.SpriteObject}.
     *
     * @param object the ChatObject
     * @param fallback the fallback component, may be null
     */
    public ObjectComponent(@NonNull ChatObject object, BaseComponent fallback)
    {
        this.object = object;
        this.fallback = fallback;
    }

    /**
     * Creates an object component from the original to clone it.
     *
     * @param original the original for the new score component
     */
    public ObjectComponent(ObjectComponent original)
    {
        super( original );
        setObject( original.object );
        setFallback( original.fallback != null ? original.fallback.duplicate() : null );
    }

    @Override
    public ObjectComponent duplicate()
    {
        return new ObjectComponent( this );
    }

    @Override
    protected void toPlainText(StringVisitor builder)
    {
        if ( fallback != null )
        {
            fallback.toPlainText( builder );
        }
        super.toPlainText( builder );
    }

    @Override
    protected void toLegacyText(StringVisitor builder)
    {
        addFormat( builder );
        if ( fallback != null )
        {
            fallback.toPlainText( builder );
        }
        super.toLegacyText( builder );
    }
}
