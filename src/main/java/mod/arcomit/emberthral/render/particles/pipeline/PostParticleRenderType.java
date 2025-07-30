package mod.arcomit.emberthral.render.particles.pipeline;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.render.particles.RenderUtils;
import mod.arcomit.emberthral.render.particles.targets.TargetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import static com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z;
import static net.minecraft.client.Minecraft.ON_OSX;

/**
 * 抽象类，用于实现带有后处理效果的粒子渲染类型。
 * 继承自Minecraft的ParticleRenderType接口，提供自定义渲染管线支持。
 */
public abstract class PostParticleRenderType implements ParticleRenderType {
    protected final ResourceLocation renderTypeID; // 渲染类型的唯一标识符
    protected final ResourceLocation texture;      // 粒子使用的纹理资源
    public int priority = 0;                       // 渲染优先级（用于排序）

    public PostParticleRenderType(ResourceLocation renderTypeID, ResourceLocation texture) {
        this.renderTypeID = renderTypeID;
        this.texture = texture;
    }

    /**
     * 开始渲染粒子前的准备工作
     * @param bufferBuilder 顶点缓冲区构建器
     * @param textureManager 纹理管理器
     */
    @Override
    public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
        // 启用混合、禁用面剔除
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        // 开启光照贴图层
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        // 设置混合模式（透明度混合）
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        // 启用深度测试并允许深度写入
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        // 设置着色器程序
        RenderSystem.setShader(this::getShader);

        // 绑定指定纹理（如果存在）
        if (texture != null) RenderUtils.GLSetTexture(texture);
        // 启动后处理管线
        getPipeline().start();
        // 初始化顶点缓冲区格式
        setupBufferBuilder(bufferBuilder);
    }

    /**
     * 获取着色器实例（默认使用位置-颜色-纹理-光照着色器）
     */
    protected ShaderInstance getShader() {
        return GameRenderer.positionColorTexLightmapShader;
    }

    /**
     * 显式调用后处理管线
     */
    public void callPipeline() {
        getPipeline().call();
    }

    /**
     * 尝试调用后处理管线（仅在管线未激活时调用）
     * @return 是否成功调用
     */
    public boolean tryCallPipeline() {
        if (!PostEffectPipelines.isActive()) {
            callPipeline();
            return true;
        } else return false;
    }

    /**
     * 结束粒子渲染
     * @param tesselator 曲面细分器（用于提交顶点数据）
     */
    @Override
    public void end(Tesselator tesselator) {
        // 按正交深度排序四边形
        tesselator.getBuilder().setQuadSorting(ORTHOGRAPHIC_Z);
        // 提交顶点数据
        tesselator.end();
        // 暂停后处理管线
        getPipeline().suspend();
        // 恢复默认渲染状态
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableCull();
    }

    /**
     * 创建临时渲染目标（离屏缓冲区）
     * @param screenTarget 参考目标（用于获取尺寸）
     * @return 新建的临时渲染目标
     */
    public static RenderTarget createTempTarget(RenderTarget screenTarget) {
        // 创建与屏幕目标同尺寸的纹理目标
        RenderTarget rendertarget = new TextureTarget(screenTarget.width, screenTarget.height, true, ON_OSX);
        // 设置透明背景色
        rendertarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        rendertarget.clear(ON_OSX);
        return rendertarget;
    }

    /**
     * 初始化顶点缓冲区格式
     * @param bufferBuilder 顶点缓冲区构建器
     */
    public void setupBufferBuilder(BufferBuilder bufferBuilder) {
        // 使用四边形图元，顶点格式：位置+颜色+纹理坐标+光照坐标
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
    }

    /**
     * 抽象方法 - 获取关联的后处理管线
     */
    public abstract PostEffectPipelines.Pipeline getPipeline();

    // 深度剔除使用的临时渲染目标标识
    static ResourceLocation tempTarget = Emberthral.prefix("depth_cull_temp");

    /**
     * 执行深度剔除操作
     * @param source 原始渲染目标
     * @param DepthBuffer 深度缓冲区目标
     */
    public static void doDepthCull(RenderTarget source, RenderTarget DepthBuffer) {
        // 获取临时渲染目标
        RenderTarget tmp = TargetManager.getTarget(tempTarget);
        // 第一步：将源目标复制到临时目标
//        PostPasses.blit.process(source, tmp);
//        source.clear(ON_OSX);
        // 第二步：应用深度剔除处理（使用临时目标和深度缓冲区）
        PostPasses.depth_cull.process(source, DepthBuffer, tmp);
        PostPasses.blit.process(tmp,source);
        // 释放临时目标
        TargetManager.ReleaseTarget(tempTarget);
    }

    /**
     * 简单图像复制（Blit）操作
     * @param source 源渲染目标
     * @param output 输出渲染目标
     */
    public static void Blit(RenderTarget source, RenderTarget output) {
        // 直接使用blit后处理步骤复制图像
        PostPasses.blit.process(source, output,
                (effect) -> {} // 空配置回调
        );
    }

    /**
     * 返回渲染类型的字符串标识
     */
    @Override
    public String toString() {
        return renderTypeID.toString();
    }
}