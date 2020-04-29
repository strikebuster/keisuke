package org.jenkinsci.plugins.keisuke.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jenkinsci.plugins.keisuke.DisplayStepKindEnum;
import org.jenkinsci.plugins.keisuke.Messages;

import hudson.util.ListBoxModel;

/**
 * UI utility about DisplayStepKindEnum
 */
public final class DisplayStepKindUtil {

	private DisplayStepKindUtil() { }

	/**
	 * Creates ListBoxModel for &lt;f:select /&gt;
	 * @return ListBoxModel
	 */
	public static ListBoxModel createSelectItems() {
		ListBoxModel kindItems = new ListBoxModel();

		for (DisplayStepKindEnum kind : DisplayStepKindEnum.values()) {
			String label = kind.getValue();
			Class<Messages> messages = Messages.class;
			try {
				Method method = messages.getMethod(kind.getValue());
				label = (String) method.invoke(messages);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			kindItems.add(label, kind.getValue());
		}
		return kindItems;
	}

	/**
	 * Checks that the value exists in DisplayStepKindEnum.
	 * @param value string as DisplayStepKindEnum's value.
	 * @return if exists, return true.
	 */
	public static boolean existsAsDisplayStepKind(final String value) {
		for (DisplayStepKindEnum kind : DisplayStepKindEnum.values()) {
			String label = kind.getValue();
			if (label.equals(value)) {
				return true;
			}
		}
		return false;
	}
}
