// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelpoisson_chat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "poisson_chat"), "main");
	private final ModelPart tête;
	private final ModelPart machoire supérieur;
	private final ModelPart machoire inferieur;
	private final ModelPart corps;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart queue;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart nagoire pectorales;
	private final ModelPart nagoire droit;
	private final ModelPart nagoire gauche;
	private final ModelPart nageoire pelviennes;
	private final ModelPart nagoire
	pelviennes droite;
	private final ModelPart nagoire
	pelviennes gauche;

	public Modelpoisson_chat(ModelPart root) {
		this.tête = root.getChild("tête");
		this.machoire supérieur = this.tête.getChild("machoire supérieur");
		this.machoire inferieur = this.tête.getChild("machoire inferieur");
		this.corps = root.getChild("corps");
		this.bone = this.corps.getChild("bone");
		this.bone2 = this.corps.getChild("bone2");
		this.queue = root.getChild("queue");
		this.bone3 = this.queue.getChild("bone3");
		this.bone4 = this.bone3.getChild("bone4");
		this.bone5 = this.bone4.getChild("bone5");
		this.bone6 = this.bone5.getChild("bone6");
		this.nagoire pectorales = root.getChild("nagoire pectorales");
		this.nagoire droit = this.nagoire pectorales.getChild("nagoire droit");
		this.nagoire gauche = this.nagoire pectorales.getChild("nagoire gauche");
		this.nageoire pelviennes = root.getChild("nageoire pelviennes");
		this.nagoire pelviennes droite = this.nageoire pelviennes.getChild("nagoire pelviennes droite");
		this.nagoire pelviennes gauche = this.nageoire pelviennes.getChild("nagoire pelviennes gauche");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tête = partdefinition.addOrReplaceChild("tête", CubeListBuilder.create(), PartPose.offset(-3.0F, 21.0F, -13.0F));

		PartDefinition machoire supérieur = tête.addOrReplaceChild("machoire supérieur", CubeListBuilder.create().texOffs(32, 37).addBox(-4.0F, -1.7131F, -6.9931F, 8.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 1.0F));

		PartDefinition machoire inferieur = tête.addOrReplaceChild("machoire inferieur", CubeListBuilder.create().texOffs(0, 38).addBox(-3.5F, -3.0F, -8.0F, 7.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 1.0F));

		PartDefinition corps = partdefinition.addOrReplaceChild("corps", CubeListBuilder.create(), PartPose.offset(1.0F, 20.0F, -6.0F));

		PartDefinition bone = corps.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, -1.0F, 10.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 50).addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -9.0F, 8.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 61).addBox(0.0F, -8.0F, -1.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -9.0F, 1.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition bone2 = corps.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 22).addBox(-8.0F, -9.0F, -1.0F, 9.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 7.0F));

		PartDefinition cube_r3 = bone2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(44, 61).addBox(0.0F, -5.0F, -1.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.0F, 2.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition queue = partdefinition.addOrReplaceChild("queue", CubeListBuilder.create(), PartPose.offset(-0.6F, 19.0F, 8.0F));

		PartDefinition bone3 = queue.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(32, 22).addBox(-3.5F, -7.0F, -1.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.0F, 0.0F));

		PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(62, 32).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, -6.0F, 5.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(62, 26).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, -6.0F, 1.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(30, 49).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition cube_r6 = bone4.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(62, 43).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, -5.0F, 3.8F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r7 = bone4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(62, 38).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, -5.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 6.0F));

		PartDefinition cube_r8 = bone5.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(52, 62).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, -2.5F, 2.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(44, 0).addBox(-0.5F, -12.0F, -1.0F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 6.0F));

		PartDefinition nagoire pectorales = partdefinition.addOrReplaceChild("nagoire pectorales", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition nagoire droit = nagoire pectorales.addOrReplaceChild("nagoire droit", CubeListBuilder.create().texOffs(54, 49).addBox(0.0F, -1.0F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 57).addBox(1.0F, -0.5F, 1.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(62, 5).addBox(8.0F, -0.5F, 1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(32, 61).addBox(11.0F, -0.5F, -1.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(58, 62).addBox(13.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 65).addBox(0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -6.0F, -9.0F));

		PartDefinition nagoire gauche = nagoire pectorales.addOrReplaceChild("nagoire gauche", CubeListBuilder.create().texOffs(6, 65).addBox(-1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 53).addBox(-11.0F, -1.0F, -1.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 60).addBox(-8.0F, -0.5F, 1.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(62, 9).addBox(-11.0F, -0.5F, 1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(62, 17).addBox(-15.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(62, 0).addBox(-13.0F, -0.5F, -1.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -6.0F, -9.0F));

		PartDefinition nageoire pelviennes = partdefinition.addOrReplaceChild("nageoire pelviennes", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition nagoire pelviennes droite = nageoire pelviennes.addOrReplaceChild("nagoire pelviennes droite", CubeListBuilder.create().texOffs(44, 20).addBox(0.0F, -0.5F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 20).addBox(1.0F, 0.0F, 0.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 59).addBox(6.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 65).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -4.5F, 5.0F));

		PartDefinition nagoire pelviennes gauche = nageoire pelviennes.addOrReplaceChild("nagoire pelviennes gauche", CubeListBuilder.create().texOffs(62, 48).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(62, 13).addBox(-6.0F, -0.5F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(62, 15).addBox(-6.0F, 0.0F, 0.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 65).addBox(-7.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -4.5F, 5.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		tête.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		corps.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		queue.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		nagoire pectorales.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		nageoire pelviennes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}