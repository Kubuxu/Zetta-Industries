package com.bymarcin.zettaindustries.mods.ecatalogue;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import li.cil.oc.api.driver.EnvironmentAware;
import li.cil.oc.api.driver.EnvironmentHost;
import li.cil.oc.api.driver.Item;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.internal.Agent;
import li.cil.oc.api.internal.Drone;
import li.cil.oc.api.internal.Robot;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.client.KeyBindings;
import li.cil.oc.util.ItemCosts;

public class MailmanItem extends BasicItem implements Item, EnvironmentAware, HostAware{
	private static final int maxWidth = 220;

	public MailmanItem() {
		super("mailmanupgrade");
		iconString = ZettaIndustries.MODID+":mailman_upgrade";
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.uncommon;
	}
	
	@Override
	public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
		return worksWith(stack) && (Robot.class.isAssignableFrom(host) || Drone.class.isAssignableFrom(host));
	}

	@Override
	public Class<? extends Environment> providedEnvironment(ItemStack stack) {
		return MailmanUpgrade.class;
	}

	@Override
	public boolean worksWith(ItemStack stack) {
		return stack.getItem().equals(this);
	}

	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		return new MailmanUpgrade((Agent) host);
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.Upgrade;
	}

	@Override
	public int tier(ItemStack stack) {
		return 1;
	}

	@Override
	public NBTTagCompound dataTag(ItemStack stack) {
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		final NBTTagCompound nbt = stack.getTagCompound();
		// This is the suggested key under which to store item component data.
		// You are free to change this as you please.
		if(!nbt.hasKey("oc:data")) {
			nbt.setTag("oc:data", new NBTTagCompound());
		}
		return nbt.getCompoundTag("oc:data");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean par4) {
		{
			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			String tip =  StatCollector.translateToLocal("item.mailman.tip").replace("\\n", "\n");
			if(!tip.equals("item.mailman.tip")) {
				String[] lines = tip.split("\n");
				boolean shouldShorten = (font.getStringWidth(tip) > maxWidth) && !KeyBindings.showExtendedTooltips();
				if(shouldShorten) {
					tooltip.add(StatCollector.translateToLocalFormatted("oc:tooltip.TooLong",
						KeyBindings.getKeyBindingName(KeyBindings.extendedTooltip())));
				} else {
					for(String line : lines) {
						List list = font.listFormattedStringToWidth(line, maxWidth);
						tooltip.addAll(list);
					}
				}
			}
		}
		if(ItemCosts.hasCosts(stack)) {
			if(KeyBindings.showMaterialCosts()) {
				ItemCosts.addTooltip(stack, tooltip);
			} else {
				tooltip.add(StatCollector.translateToLocalFormatted(
					"oc:tooltip.MaterialCosts",
					KeyBindings.getKeyBindingName(KeyBindings.materialCosts())));
			}
		}
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("oc:data")) {
			NBTTagCompound data = stack.getTagCompound().getCompoundTag("oc:data");
			if(data.hasKey("node") && data.getCompoundTag("node").hasKey("address")) {
				tooltip.add(EnumChatFormatting.DARK_GRAY
					+ data.getCompoundTag("node").getString("address").substring(0, 13) + "..."
					+ EnumChatFormatting.GRAY);
			}
		}
	}

}
