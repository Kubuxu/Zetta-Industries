package com.bymarcin.zettaindustries.mods.ocwires;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.item.ItemStack;

import blusunrize.immersiveengineering.api.WireType;

public class TelecommunicationWireType extends WireType{

	public static TelecommunicationWireType TELECOMMUNICATION = new TelecommunicationWireType();
	
	private TelecommunicationWireType() {}
	
	public static void register(){
		getValues().add(TELECOMMUNICATION);
	}
	
	@Override
	public int getColour() {
		return 1318204;
	}

	@Override
	public double getLossRatio() {
		return 0;
	}

	@Override
	public int getMaxLength() {
		return 32;
	}

	@Override
	public int getTransferRate() {
		return 0;
	}

	@Override
	public String getUniqueName() {
		return ZettaIndustries.MODID + ":telecommunication";
	}

	@Override
	public ItemStack getWireCoil() {
		return new ItemStack(OCWires.wire);
	}

}