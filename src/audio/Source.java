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
    }

    /**
     * Plays a sound effect.
     * @param buffer buffer
     */
    public void play(int buffer)
    {
        stop();
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    /**
     * Deletes this source.
     */
    public void delete()
    {
        stop();
        AL10.alDeleteSources(sourceID);
    }

    /**
     * Pauses this source if it is currently playing.
     */
    public void pause()
    {
        AL10.alSourcePause(sourceID);
    }

    /**
     * Continues playing the sound when this source was paused.
     */
    public void continuePlaying()
    {
        AL10.alSourcePlay(sourceID);
    }

    /**
     * Stops this source from playing.
     */
    public void stop()
    {
        AL10.alSourceStop(sourceID);
    }

    /**
     * Sets the velocity of this source.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public void setVelocity(float x, float y, float z)
    {
        AL10.alSource3f(sourceID, AL10.AL_VELOCITY, x, y, z);
    }

    /**
     * Sets the loop of this source
     * @param loop true if looping
     */
    public void setLooping(boolean loop)
    {
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    /**
     * Checks if this source is currently playing
     * @return true if source is playing.
     */
    public boolean isPlaying()
    {
        return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    /**
     * Sets the volume of this source.
     * @param volume volume
     */
    public void setVolume(float volume)
    {
        AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
    }

    /**
     * Sets the pitch of this source.
     * @param pitch pitch
     */
    public void setPitch(float pitch)
    {
        AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
    }

    /**
     * Sets the position of this source
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public void setPosition(float x, float y, float z)
    {
        AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
    }
}