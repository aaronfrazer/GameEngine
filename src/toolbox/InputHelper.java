package toolbox;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Handles mouse and keyboard input and stores values for keys down,
 * released, or pressed.  This class can be accessed from anywhere.
 * 
 * To updated the input helper, add this line into the main draw loop:
 * 	InputHelper.update();
 * 
 * Use as so (can be used from anywhere):
 * 	InputHelper.isKeyDown(Keyboard.KEY_SPACE);
 * 
 * @author Aaron Frazer
 *
 */
public class InputHelper
{
	/**
	 * Singleton class instance
	 */
	private static InputHelper input = new InputHelper();
	
	/**
	 * State of key or mouse button
	 */
	private enum EventState {
		NONE,
		PRESSED,
		DOWN,
		RELEASED;
	}
	
	/**
	 * Array list of mouse events
	 */
	private ArrayList<EventState> mouseEvents;
	
	/**
	 * Array list of keyboard events
	 */
	private ArrayList<EventState> keyboardEvents;
	
	/**
	 * Constructor used to initialize mouse and keyboard events.
	 */
	public InputHelper()
	{
		// Mouse initialization
		mouseEvents = new ArrayList<EventState>();
		
		// Add mouse events to array list
		for (int i = 0; i < Mouse.getButtonCount(); i++)
			mouseEvents.add(EventState.NONE);
		
		
		// Keyboard initialization
		keyboardEvents = new ArrayList<EventState>();
		
		// Add keyboard events to array list
		for (int i = 0; i < Keyboard.KEYBOARD_SIZE; i++)
			keyboardEvents.add(EventState.NONE);
	}
	
	/**
	 * Updates keyboard and mouse inputs.
	 */
	private void updateInputs()
	{
		resetKeys();
        //Set Key down events (more accurate than using repeat-event method)
        for(int i = 0; i < Keyboard.KEYBOARD_SIZE; i++)
        {
            if(Keyboard.isKeyDown(i))
            {
                keyboardEvents.set(i, EventState.DOWN);
            }
        }
        
        while(Keyboard.next()) { //Handle all Keyboard events
            int key = Keyboard.getEventKey();
            if (key < 0) continue; //Ignore no events

            if (Keyboard.getEventKeyState())
            {
                if (!Keyboard.isRepeatEvent())
                {
                    keyboardEvents.set(key, EventState.PRESSED);
                }
            }else
            {
                keyboardEvents.set(key, EventState.RELEASED);
            }
        }
		
        
		resetMouse();
		// Set mouse down events
        for (int i = 0; i < Mouse.getButtonCount(); i++)
        {
            if (Mouse.isButtonDown(i))
                mouseEvents.set(i, EventState.DOWN);
        }
        
        while (Mouse.next()) { // Handle all Mouse events
            int button = Mouse.getEventButton();
            if(button < 0) continue; //Ignore no events
            
            if (Mouse.getEventButtonState()) 
            {
                mouseEvents.set(button, EventState.PRESSED);
            }else 
            {
                mouseEvents.set(button, EventState.RELEASED);
            }
        }
	}
	
	/**
	 * Sets all keyboard events to NONE.
	 */
	private void resetKeys()
	{
		for (int i = 0; i < Keyboard.KEYBOARD_SIZE; i++)
			keyboardEvents.set(i, EventState.NONE);
	}
	
	/**
	 * Sets all mouse events to NONE.
	 */
	private void resetMouse()
	{
		for (int i = 0; i < Mouse.getButtonCount(); i++)
			mouseEvents.set(i, EventState.NONE);
	}
	
    //Non-static version of methods (Only used in the singleton instance)
    private boolean KeyDown(int key){
        return keyboardEvents.get(key)==EventState.DOWN;
    }
    private boolean KeyPressed(int key){
        return keyboardEvents.get(key)==EventState.PRESSED;
    }
    private boolean KeyReleased(int key){
        return keyboardEvents.get(key)==EventState.RELEASED;
    }
    private boolean MouseButtonDown(int key){
        return mouseEvents.get(key)==EventState.DOWN;
    }
    private boolean MouseButtonPressed(int key){
        return mouseEvents.get(key)==EventState.PRESSED;
    }
    private boolean MouseButtonReleased(int key){
        return mouseEvents.get(key)==EventState.RELEASED;
    }
	
    //Static version of methods (called from anywhere, return singleton instance value)
    /**
     * Checks to see if a key is currently being held down.
     * @param key - keycode to check
     * @return true if the key is down
     */
    public static boolean isKeyDown(int key){
        return input.KeyDown(key);
    }
    /**
     * Checks to see if a key is pressed a single time.
     * @param key - keycode to check
     * @return true if the key is pressed
     */
    public static boolean isKeyPressed(int key){
        return input.KeyPressed(key);
    }
    /**
     * Checks to see if a key is released.
     * @param key - keycode to check
     * @return true if the key is released
     */
    public static boolean isKeyReleased(int key){
        return input.KeyReleased(key);
    }
    /**
     * Checks to see if a mouse button is currently being held down.
     * @param button - index of the button
     * @return true if the mouse button is down
     */
    public static boolean isButtonDown(int button){
        return input.MouseButtonDown(button);
    }
    /**
     * Checks to see if a mouse button is pressed a single time.
     * @param button - index of the button
     * @return true if the mouse button is pressed
     */
    public static boolean isButtonPressed(int button){
        return input.MouseButtonPressed(button);
    }
    /**
     * Checks to see if a mouse button is released.
     * @param button - index of the button
     * @return true if the button is released
     */
    public static boolean isButtonReleased(int button){
        return input.MouseButtonReleased(button);
    }
    
	/**
	 * Updates the keyboard and mouse inputs by checking if they are
	 * down, pressed, released, or none.
	 * 
	 * This method should always be called before code that uses inputs.
	 */
    public static void update(){
        input.updateInputs();
    }
}
