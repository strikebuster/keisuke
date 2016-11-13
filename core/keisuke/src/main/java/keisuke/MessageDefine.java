package keisuke;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageDefine {

	private Map<String, String> messageMap = null;
	
	public MessageDefine(String prefix) {
		messageMap = new HashMap<String, String>();
		String[] prefixes = { prefix };
		setMessageMap(prefixes);
	}
	
	public MessageDefine(String[] prefixes) {
		messageMap = new HashMap<String, String>();
		for (int i=0; i < prefixes.length; i++) {
			setMessageMap(prefixes);
		}
	}
	
	private void setMessageMap(String[] prefixes) {
		ResourceBundle bundle = null;
		String key = null;
		int prefixlen = prefixes.length;;
        try {
        	//System.out.println("[DEBUG] Locale = " + Locale.getDefault());
            bundle = ResourceBundle.getBundle("keisuke/message");
        } catch (MissingResourceException ex) {
        	throw new RuntimeException("!! Not found message[_xx].properties");
        }
        try {
        	Enumeration<?> en = bundle.getKeys();
    		while (en.hasMoreElements()) {
    			key = (String)en.nextElement();
    			for (int i = 0; i < prefixlen; i++) {
	    			if (key.startsWith(prefixes[i])) {
	    				String msg = bundle.getString(key);
	        			this.messageMap.put(key, msg);
	    			}
    			}
        	}
        } catch (MissingResourceException e) {
        	System.err.println("!! Not found key(" + key + ") in message[_xx].properties");
        	throw new RuntimeException(e);
        }
	}
	
	public Map<String, String> getMessageMap() {
		return this.messageMap;
	}
	
	public String getMessage(String key) {
		if (key == null) {
			return null;
		}
		return this.messageMap.get(key);
	}
}
