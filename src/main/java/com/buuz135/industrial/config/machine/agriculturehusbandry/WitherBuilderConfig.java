package com.buuz135.industrial.config.machine.agriculturehusbandry;

import com.buuz135.industrial.config.MachineAgricultureHusbandryConfig;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;

@ConfigFile.Child(MachineAgricultureHusbandryConfig.class)
public class WitherBuilderConfig {

	@ConfigVal(comment = "Cooldown Time in Ticks [20 Ticks per Second] - Default: [40 (5s)]")
	public static int maxProgress = 40;

	@ConfigVal(comment = "Amount of Power Consumed per Operation - Default: [500FE]")
	public static int powerPerOperation = 20000;

	@ConfigVal(comment = "Max Stored Power [FE] - Default: [70000 FE]")
	public static int maxStoredPower = 70000;

}
