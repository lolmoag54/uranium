package net.mcreator.uranium.block.display;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;

import net.mcreator.uranium.block.renderer.WindturbineDisplayItemRenderer;

import java.util.function.Consumer;

import com.geckolib.util.GeckoLibUtil;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.AnimationController;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.GeoItem;

public class WindturbineDisplayItem extends BlockItem implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public WindturbineDisplayItem(Block block, net.minecraft.world.item.Item.Properties settings) {
		super(block, settings);
	}

	private PlayState predicate(AnimationTest<WindturbineDisplayItem> state) {
		return state.setAndContinue(RawAnimation.begin().thenLoop("0"));
	}

	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private WindturbineDisplayItemRenderer renderer;

			@Override
			public GeoItemRenderer<?> getGeoItemRenderer() {
				if (this.renderer == null)
					this.renderer = new WindturbineDisplayItemRenderer();
				return this.renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>("controller", 0, this::predicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}