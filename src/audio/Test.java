package audio;

import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * A class used to test audio.
 *
 * @author Aaron Frazer
 */
public class Test
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        disableWarning();

        AudioMaster.init();
        AudioMaster.setListenerData(0, 0, 0);

        int buffer = AudioMaster.loadSound("audio/bounce.wav");
        Source source = new Source();
        source.setLooping(true);
        source.play(buffer);

        float xPos = 8;
        source.setPosition(xPos, 0, 2);

        char c = ' ';
        while (c != 'q')
        {
            xPos -= 0.03f;
            source.setPosition(xPos, 0, 2);
            Thread.sleep(10);
        }

        source.delete();
        AudioMaster.cleanUp();
    }

    /**
     * Disables warning if you are running JDK 9.
     */
    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }
}