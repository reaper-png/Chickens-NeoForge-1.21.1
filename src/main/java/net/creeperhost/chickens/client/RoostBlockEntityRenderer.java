package net.creeperhost.chickens.client;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;
import net.creeperhost.chickens.block.BlockRoost;
import net.creeperhost.chickens.blockentities.BlockEntityRoost;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

/**
 * Port of 1.18.2 RenderRoost: render the actual breed entity in the Roost's
 * opening, facing outwards. Cache preview entities rather than allocating a
 * new Minecraft entity every frame for every Roost.
 */
public final class RoostBlockEntityRenderer implements BlockEntityRenderer<BlockEntityRoost> {
    private final Map<String, EntityChickensChicken> previews = new HashMap<>();
    private ClientLevel previewLevel;

    public RoostBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BlockEntityRoost roost, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        ItemStack stack = roost.inventory().getStackInSlot(0);
        ChickenSpecies species = ChickenItemData.type(stack);
        if (species == null) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        if (previewLevel != level) {
            previews.clear();
            previewLevel = level;
        }
        EntityChickensChicken chicken = previews.get(species.id());
        if (chicken == null) {
            chicken = ModEntities.create(species, level);
            if (chicken == null) return;
            previews.put(species.id(), chicken);
        }
        // Static preview: the Roost holds an item, not a ticking world entity.
        // Freeze current/previous animation values so interpolation cannot
        // make the model flutter or jitter between rendered frames.
        chicken.setYRot(0.0F);
        chicken.yRotO = 0.0F;
        chicken.yBodyRot = 0.0F;
        chicken.yBodyRotO = 0.0F;
        chicken.yHeadRot = 0.0F;
        chicken.yHeadRotO = 0.0F;
        chicken.flap = 0.0F;
        chicken.oFlap = 0.0F;
        chicken.flapSpeed = 0.0F;
        chicken.oFlapSpeed = 0.0F;
        chicken.flapping = 0.0F;
        chicken.walkAnimation.setSpeed(0.0F);

        Direction facing = roost.getBlockState().getValue(BlockRoost.FACING);
        poseStack.pushPose();
        try {
            // Original RenderRoost offsets and proportions (1.18.2):
            // translate(0.5, -0.008, 0.5), scale(0.9, 0.85, 0.9).
            poseStack.translate(0.5D, -0.008D, 0.5D);
            poseStack.scale(0.9F, 0.85F, 0.9F);
            // Face the opening: the previous 180-degree offset pointed the chicken toward the back.
            poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-facing.toYRot())));
            Minecraft.getInstance().getEntityRenderDispatcher().render(
                    chicken, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F,
                    poseStack, buffers, packedLight);
        } finally {
            poseStack.popPose();
        }
    }
}
