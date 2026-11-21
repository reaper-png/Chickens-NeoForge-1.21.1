package net.creeperhost.chickens.client;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

/**
 * Draws the actual vanilla chicken geometry with the legacy breed texture.
 *
 * The original 1.0.32 chicken_item was not an egg: it used a custom entity
 * renderer. This is its NeoForge 1.21.1 equivalent. A single reusable model
 * renders all 46 variants; no temporary entities or world access are needed.
 */
public final class ChickenItemRenderer extends BlockEntityWithoutLevelRenderer {
    private ChickenModel<Chicken> model;
    private final Map<String, EntityChickensChicken> previews = new HashMap<>();
    private ClientLevel previewLevel;

    // GUI-only scale: keep adult chicken geometry inside a 16x16 inventory slot.
    // Do not change ground/hand scale or actual entity dimensions.
    private static final float GUI_SCALE = 0.85F;

    public ChickenItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
                             PoseStack poseStack, MultiBufferSource buffers,
                             int packedLight, int packedOverlay) {
        ChickenSpecies species = ChickenItemData.type(stack);
        if (species == null) return;

        // The inventory must use the same adult renderer as an in-world
        // chicken. Rendering a raw ChickenModel skips the entity renderer's
        // age/pose setup and produces a visibly incorrect silhouette.
        if (displayContext == ItemDisplayContext.GUI) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                if (previewLevel != level) {
                    previews.clear();
                    previewLevel = level;
                }
                EntityChickensChicken chicken = previews.get(species.id());
                if (chicken == null) {
                    chicken = ModEntities.create(species, level);
                    if (chicken == null) return;
                    chicken.setAge(0);
                    previews.put(species.id(), chicken);
                }
                poseStack.pushPose();
                try {
                    poseStack.translate(0.5D, 0.08D, 0.5D);
                    poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(35)));
                    poseStack.scale(GUI_SCALE, GUI_SCALE, GUI_SCALE);
                    Minecraft.getInstance().getEntityRenderDispatcher().render(
                            chicken, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F,
                            poseStack, buffers, packedLight);
                } finally {
                    poseStack.popPose();
                }
                return;
            }
        }

        // Fallback for rendering item stacks without a loaded client world.
        if (model == null) {
            model = new ChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.CHICKEN));
        }
        model.young = false;

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                "chickens", "textures/entity/" + species.id() + ".png");

        poseStack.pushPose();
        try {
            // Match the classic chicken-item silhouette in inventory, hand and on the ground.
            // The chicken model is stored in vanilla entity coordinates (Y axis inverted).
            boolean inGui = displayContext == ItemDisplayContext.GUI;
            // Keep the feet just above the bottom of the inventory slot.
            poseStack.translate(0.5, inGui ? 0.08D : 0.12D, 0.5);
            if (inGui) {
                poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(35)));
            }
            // Never change the real entity's scale or held/ground item size.
            float scale = inGui ? GUI_SCALE
                    : displayContext == ItemDisplayContext.GROUND ? 0.75F : 0.95F;
            poseStack.scale(scale, -scale, -scale);
            poseStack.translate(0.0, -1.5, 0.0);
            model.renderToBuffer(poseStack,
                    buffers.getBuffer(RenderType.entityCutoutNoCull(texture)),
                    packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        } finally {
            poseStack.popPose();
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        super.onResourceManagerReload(manager);
        model = null; // Re-bake after resource-pack changes.
        previews.clear(); // Cached visual entities must also be refreshed.
    }
}
