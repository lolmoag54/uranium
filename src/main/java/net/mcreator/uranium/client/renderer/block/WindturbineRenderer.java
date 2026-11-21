package net.mcreator.uranium.client.renderer.block;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.uranium.procedures.WindturbineConditionDeLectureSlowProcedure;
import net.mcreator.uranium.procedures.WindturbineConditionDeLectureMediumProcedure;
import net.mcreator.uranium.procedures.WindturbineConditionDeLectureFastProcedure;
import net.mcreator.uranium.init.UraniumModBlockEntities;
import net.mcreator.uranium.client.model.animations.eolienneAnimation;
import net.mcreator.uranium.client.model.Modeleolienne;
import net.mcreator.uranium.block.entity.WindturbineBlockEntity;
import net.mcreator.uranium.block.WindturbineBlock;

import java.util.Map;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(Dist.CLIENT)
public class WindturbineRenderer implements BlockEntityRenderer<WindturbineBlockEntity, WindturbineRenderer.CustomRenderState> {
	private final CustomHierarchicalModel model;
	private final Identifier texture;

	WindturbineRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new CustomHierarchicalModel(context.bakeLayer(Modeleolienne.LAYER_LOCATION));
		this.texture = Identifier.parse("uranium:textures/block/eolienne_allumee.png");
	}

	@Override
	public CustomRenderState createRenderState() {
		return new CustomRenderState();
	}

	@Override
	public void extractRenderState(WindturbineBlockEntity blockEntity, CustomRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);
		state.blockEntity = blockEntity;
		state.blockState = blockEntity.getBlockState();
		int tickCount = (int) blockEntity.getLevel().getGameTime();
		state.entityRenderState.ageInTicks = tickCount + partialTicks;
		blockEntity.animationState0.animateWhen(WindturbineConditionDeLectureSlowProcedure.execute(blockEntity.getLevel(), blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ()), tickCount);
		blockEntity.animationState1.animateWhen(WindturbineConditionDeLectureMediumProcedure.execute(blockEntity.getLevel(), blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ()), tickCount);
		blockEntity.animationState2.animateWhen(WindturbineConditionDeLectureFastProcedure.execute(blockEntity.getLevel(), blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ()), tickCount);
	}

	@Override
	public void submit(CustomRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		poseStack.scale(-1, -1, 1);
		poseStack.translate(-0.5, -0.5, 0.5);
		BlockState state = renderState.blockState;
		Direction facing = state.getValue(WindturbineBlock.FACING);
		switch (facing) {
			case NORTH -> {
			}
			case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
			case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90));
			case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
		}
		poseStack.translate(0, -1, 0);
		model.setupBlockEntityAnim(renderState.blockEntity, renderState.entityRenderState);
		submitNodeCollector.submitModel(this.model, renderState.entityRenderState, poseStack, RenderTypes.entityCutout(texture), renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(UraniumModBlockEntities.WINDTURBINE.get(), WindturbineRenderer::new);
	}

	public static class CustomRenderState extends BlockEntityRenderState {
		protected final LivingEntityRenderState entityRenderState = new LivingEntityRenderState();
		protected WindturbineBlockEntity blockEntity;
		protected BlockState blockState;
	}

	private static final class CustomHierarchicalModel extends Modeleolienne {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;
		private final KeyframeAnimation keyframeAnimation2;

		public CustomHierarchicalModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(eolienneAnimation.slow);
			this.keyframeAnimation1 = safeBake(eolienneAnimation.medium);
			this.keyframeAnimation2 = safeBake(eolienneAnimation.fast);
		}

		private KeyframeAnimation safeBake(AnimationDefinition source) {
			try {
				return source.bake(root);
			} catch (IllegalArgumentException e) {
				return new AnimationDefinition(0, false, Map.of()).bake(root);
			}
		}

		public void setupBlockEntityAnim(WindturbineBlockEntity blockEntity, LivingEntityRenderState state) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			this.keyframeAnimation0.apply(blockEntity.animationState0, state.ageInTicks, 1f);
			this.keyframeAnimation1.apply(blockEntity.animationState1, state.ageInTicks, 2f);
			this.keyframeAnimation2.apply(blockEntity.animationState2, state.ageInTicks, 3f);
			super.setupAnim(state);
		}
	}
}