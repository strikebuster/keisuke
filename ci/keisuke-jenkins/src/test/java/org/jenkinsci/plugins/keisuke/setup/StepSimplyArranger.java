package org.jenkinsci.plugins.keisuke.setup;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;

/**
 * Arranging inputSetting to be set up when countingMode is step_simply.
 */
public class StepSimplyArranger extends InputSettingArranger {

	public StepSimplyArranger() {
		super(CountingModeEnum.ONLY_STEP_SIMPLY);
	}

}
