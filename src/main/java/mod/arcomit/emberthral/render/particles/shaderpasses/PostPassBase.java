package mod.arcomit.emberthral.render.particles.shaderpasses;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 后处理渲染通道的基类，用于实现自定义着色器效果
 */
public class PostPassBase {
    protected EffectInstance effect;  // 着色器效果实例

    public PostPassBase(EffectInstance effect) {
        this.effect = effect;
    }

    /**
     * 通过资源路径创建着色器效果
     * @param resourceLocation 着色器资源路径
     * @param resmgr 资源管理器
     */
    public PostPassBase(String resourceLocation, ResourceManager resmgr) throws IOException {
        this(new EffectInstance(resmgr, resourceLocation));
    }

    /**
     * 创建正交投影矩阵（用于全屏渲染）
     * @param out 输出渲染目标（用于确定视口尺寸）
     * @return 正交投影矩阵
     */
    protected static Matrix4f orthographic(RenderTarget out) {
        return new Matrix4f().setOrtho(0.0F, (float) out.width, 0.0F, (float) out.height, 0.1F, 1000.0F);
    }

    /**
     * 设置着色器参数（子类可重写）
     * @param effect 着色器实例
     * @param inTarget 输入渲染目标
     * @param outTarget 输出渲染目标
     */
    protected void setParameter(EffectInstance effect, RenderTarget inTarget, RenderTarget outTarget) {
        // 默认空实现，子类可添加自定义参数设置
    }

    /**
     * 处理效果（无额外参数配置）
     */
    public void process(RenderTarget inTarget, RenderTarget outTarget) {
        process(inTarget, outTarget, null);
    }

    /**
     * 核心处理流程：应用着色器效果到渲染目标
     * @param inTarget 输入渲染目标（源纹理）
     * @param outTarget 输出渲染目标（结果纹理）
     * @param uniformConsumer 着色器参数配置回调（可为null）
     */
    public void process(RenderTarget inTarget, RenderTarget outTarget, Consumer<EffectInstance> uniformConsumer) {
        // 1. 前置处理（子类可重写）
        prevProcess(inTarget, outTarget);

        // 2. 解除当前写入绑定
        inTarget.unbindWrite();

        // 3. 设置视口尺寸
        RenderSystem.viewport(0, 0, outTarget.width, outTarget.height);

        // 4. 绑定输入纹理到着色器采样器
        this.effect.setSampler("DiffuseSampler", inTarget::getColorTextureId);

        // 5. 设置通用着色器参数
        this.effect.safeGetUniform("ProjMat").set(orthographic(outTarget));  // 投影矩阵
        this.effect.safeGetUniform("OutSize").set((float) outTarget.width, (float) outTarget.height);  // 输出尺寸

        // 6. 自定义参数配置（通过回调）
        if (uniformConsumer != null) {
            uniformConsumer.accept(effect);
        }

        // 7. 应用着色器效果
        this.effect.apply();

        // 8. 渲染全屏四边形
        pushVertex(inTarget, outTarget);

        // 9. 清理状态
        this.effect.clear();      // 清除着色器状态
        outTarget.unbindWrite();  // 解绑输出目标
        inTarget.unbindRead();    // 解绑输入目标
    }

    /**
     * 前置处理钩子方法（子类可重写）
     */
    public void prevProcess(RenderTarget inTarget, RenderTarget outTarget) {
        // 默认空实现
    }

    /**
     * 渲染全屏四边形（执行着色器效果）
     */
    public void pushVertex(RenderTarget inTarget, RenderTarget outTarget) {
        // 准备输出目标
        if (outTarget != Minecraft.getInstance().getMainRenderTarget()){
            outTarget.clear(Minecraft.ON_OSX);  // 清除缓冲区
        }
        outTarget.bindWrite(false);         // 绑定为写入目标

        // 配置深度测试（总是通过）
        RenderSystem.depthFunc(519);  // GL_ALWAYS

        // 构建全屏四边形
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        // 四个顶点（覆盖整个屏幕，Z值固定为500）
        bufferbuilder.vertex(0.0D, 0.0D, 500.0D).endVertex();
        bufferbuilder.vertex(outTarget.width, 0.0D, 500.0D).endVertex();
        bufferbuilder.vertex(outTarget.width, outTarget.height, 500.0D).endVertex();
        bufferbuilder.vertex(0.0D, outTarget.height, 500.0D).endVertex();

        // 提交绘制
        BufferUploader.draw(bufferbuilder.end());

        // 恢复深度测试（默认LEQUAL）
        RenderSystem.depthFunc(515);  // GL_LEQUAL
    }
}