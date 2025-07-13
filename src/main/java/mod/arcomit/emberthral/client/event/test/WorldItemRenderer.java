package mod.arcomit.emberthral.client.event.test;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.jellysquid.mods.sodium.client.render.vertex.buffer.SodiumBufferBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static mod.arcomit.emberthral.Emberthral.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class WorldItemRenderer {
    // 在游戏世界渲染阶段触发,测试用,暂时当彩蛋,有兴趣了改成渲染Herobrine
    @SubscribeEvent
    public static void onWorldRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        Matrix4f invertMatrix = new Matrix4f(poseStack.last().pose()).invert();
        //PoseStack poseStack = new PoseStack();

        // 定义要渲染的世界坐标（示例坐标）
        BlockPos renderPos = new BlockPos(100, 100, 200);
        Vec3 targetPos = Vec3.atCenterOf(renderPos);

        // 获取相机位置
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        // 矩阵变换开始
        poseStack.pushPose();

        // 转换到相对摄像机的位置
        poseStack.translate(
                targetPos.x - cameraPos.x,
                targetPos.y - cameraPos.y,
                targetPos.z - cameraPos.z
        );

        // 添加动态旋转（可选动画）
        float rotation = (mc.player.tickCount + event.getPartialTick()) * 2f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        // 调整物品大小（可选缩放）
        float scale = 0.8f;
        poseStack.scale(scale, scale, scale);

        // 获取光照数据
        int packedLight = mc.getEntityRenderDispatcher().getPackedLightCoords(
                mc.player,
                event.getPartialTick()
        );

        // 获取正确的 BufferSource
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        // 创建物品并渲染
        ItemStack stack = new ItemStack(Items.DIAMOND);
        mc.getItemRenderer().render(
                stack,
                ItemDisplayContext.GROUND,
                false,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                mc.getItemRenderer().getModel(stack, null, null, 0)
        );


        //根据变换矩阵逆推坐标
        Matrix4f invertPoseMatrix = invertMatrix.mul(poseStack.last().pose());
        // 创建原点并应用矩阵变换（使用列向量乘法）
        Vector4f origin = new Vector4f(0, 0, 0, 1);
        invertPoseMatrix.transform(origin);
        // 转换为世界坐标：相对相机坐标 + 相机位置
        Vec3 transformedWorldPos = new Vec3(
                origin.x() / origin.w()  + cameraPos.x,
                origin.y() / origin.w()  + cameraPos.y,
                origin.z() / origin.w() + cameraPos.z
        );
        //System.out.println("逆向得到的世界坐标: " + transformedWorldPos);

        // 结束变换
        poseStack.popPose();
    }
}