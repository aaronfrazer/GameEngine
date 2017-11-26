package audio;

import org.lwjgl.openal.AL10;

/**
 * A source that can play sound in the game.
 *
 * @author Aaron Frazer
 */
public class Source
{
    /**
     * ID of this source
     */
    private int sourceID;

    /**
     * Creates a new source.
     */
    public Source()
    {
        sourceID = AL10.alGenSources();
        AL10.alSourcef(sourceID, AL10.AL_GAIN, 1);
        AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
        AL10.alSource3f(sourceID, AL10.AL_POSITION, 0, 0, 0);
    }

    /**
     * Plays a sound effect.
     * @param buffer buffer
     */
    public void play(int buffer)
    {
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
        AL10.alSourcePlay(sourceID);
    }

    /**
     * Deletes this source.
     */
    public void delete()
    {
        AL10.alDeleteSources(sourceID);
    }
}