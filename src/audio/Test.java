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
    public static void main(String[] args) throws IOException
    {
        disableWarning();

        AudioMaster.init();
        AudioMaster.setListenerData();

        int buffer = AudioMaster.loadSound("audio/bounce.wav");
        Source source = new Source();

        char c = ' ';
        while (c != 'q')
        {
            c = (char) System.in.read();

            if (c == 'p')
            {
                source.play(buffer);
            }
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