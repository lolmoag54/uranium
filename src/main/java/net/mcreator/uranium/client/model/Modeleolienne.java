package net.mcreator.uranium.client.model;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modeleolienne extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("uranium", "modeleolienne"), "main");
	public final ModelPart elice;
	public final ModelPart tronc;
	public final ModelPart electrical;

	public Modeleolienne(ModelPart root) {
		super(root);
		this.elice = root.getChild("elice");
		this.tronc = root.getChild("tronc");
		this.electrical = root.getChild("electrical");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition elice = partdefinition.addOrReplaceChild("elice", CubeListBuilder.create().texOffs(96, 3).addBox(-1.4446F, -1.4617F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(96, 1)
				.addBox(-1.0446F, -1.0617F, -3.0F, 2.1F, 2.1F, 1.0F, new CubeDeformation(0.0F)).texOffs(96, 1).addBox(-2.0446F, -2.0617F, 0.0F, 4.1F, 4.1F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.15F, -41.5F, -8.0F, 0.0F, 0.0F, -0.2112F));
		PartDefinition cube_r1 = elice.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(23, 73).addBox(-0.9F, 0.5F, 0.0F, 0.75F, 46.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 75)
				.addBox(-0.3F, 0.5F, 0.0F, 0.8F, 44.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(76, 78).addBox(0.5F, 0.5F, 0.0F, 0.7F, 44.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0054F, 0.0383F, -1.0F, 0.0F, 0.0F, 2.4435F));
		PartDefinition cube_r2 = elice.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(70, 78).addBox(0.5F, 0.5F, 0.0F, 0.7F, 44.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 78)
				.addBox(-0.3F, 0.5F, 0.0F, 0.8F, 44.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(58, 78).addBox(-0.9F, 0.5F, 0.0F, 0.75F, 46.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0054F, 0.0383F, -1.0F, 0.0F, 0.0F, -1.8326F));
		PartDefinition cube_r3 = elice.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(52, 78).addBox(-0.9F, 0.5F, 0.0F, 0.75F, 46.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 78)
				.addBox(0.5F, 0.5F, 0.0F, 0.7F, 44.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 78).addBox(-0.3F, 0.5F, 0.0F, 0.8F, 44.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0054F, 0.0383F, -1.0F, 0.0F, 0.0F, 0.2182F));
		PartDefinition tronc = partdefinition.addOrReplaceChild("tronc",
				CubeListBuilder.create().texOffs(32, 24).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(76, 52).addBox(-6.0F, -13.0F, -5.0F, 12.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(64, 10)
						.addBox(-3.4F, -69.0F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(96, 2).addBox(-2.5F, -68.0F, -7.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition cube_r4 = tronc.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 2).addBox(-3.0F, -59.0F, -3.0F, 5.0F, 61.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.0401F));
		PartDefinition cube_r5 = tronc.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(1, 2).addBox(-1.0F, -59.0F, -3.0F, 5.0F, 61.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.0401F));
		PartDefinition electrical = partdefinition.addOrReplaceChild("electrical",
				CubeListBuilder.create().texOffs(96, 16).addBox(-4.0F, -12.0F, -6.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-2.0F, -20.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(-2.0F, -28.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-2.0F, -36.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(-2.0F, -44.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-2.0F, -52.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(-2.0F, -60.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-2.0F, -68.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(0.0F, -68.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(0.0F, -60.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(0.0F, -52.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(0.0F, -44.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(0.0F, -36.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(0.0F, -28.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(0.0F, -20.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-1.0F, -20.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(-1.0F, -28.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-1.0F, -36.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(-1.0F, -44.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-1.0F, -52.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65)
						.addBox(-1.0F, -60.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)).texOffs(32, 65).addBox(-1.0F, -68.0F, -3.4F, 0.4F, 8.0F, 0.4F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

	}

}