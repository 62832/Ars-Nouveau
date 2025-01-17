package com.hollingsworth.arsnouveau.client.renderer.tile;

import com.hollingsworth.arsnouveau.common.block.tile.ImbuementTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class ImbuementRenderer extends ArsGeoBlockRenderer<ImbuementTile> {

    public ImbuementRenderer(BlockEntityRendererProvider.Context p_i226006_1_) {
        super(p_i226006_1_, new GenericModel<>("imbuement_chamber"));
    }

    @Override
    public void actuallyRender(PoseStack matrixStack, ImbuementTile tileEntityIn, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(matrixStack, tileEntityIn, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        double x = tileEntityIn.getBlockPos().getX();
        double y = tileEntityIn.getBlockPos().getY();
        double z = tileEntityIn.getBlockPos().getZ();
        if (tileEntityIn.entity == null || !ItemStack.matches(tileEntityIn.entity.getItem(), tileEntityIn.stack)) {
            tileEntityIn.entity = new ItemEntity(tileEntityIn.getLevel(), x, y, z, tileEntityIn.stack);

        }
        if (tileEntityIn.entity != null) {
            ItemEntity entityItem = tileEntityIn.entity;
            tileEntityIn.frames += 1.5f * Minecraft.getInstance().getDeltaFrameTime();
            entityItem.setYHeadRot(tileEntityIn.frames);
            entityItem.age = (int) tileEntityIn.frames;
            matrixStack.pushPose();
            matrixStack.scale(0.75f, 0.75f, 0.75f);
            float offset = 0.5f * 0.75f + 0.31f;
            Minecraft.getInstance().getEntityRenderDispatcher().render(entityItem, offset, 0.3, offset, entityItem.yRot, 0, matrixStack, bufferSource, packedLight);
            matrixStack.popPose();
        }
    }

}
