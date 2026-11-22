package net.mcreator.uranium.item;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResult;

import net.mcreator.uranium.procedures.CrystalstagspawneggLorsqueVousCliquezAvecLeBoutonDroitDeLaSourisSurUnBlocProcedure;

public class CrystalstagspawneggItem extends Item {
	public CrystalstagspawneggItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		CrystalstagspawneggLorsqueVousCliquezAvecLeBoutonDroitDeLaSourisSurUnBlocProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ());
		return InteractionResult.SUCCESS;
	}
}