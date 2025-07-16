package mod.arcomit.emberthral.render.particles.custom;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.render.particles.pipeline.PostEffectPipelines;
import mod.arcomit.emberthral.render.particles.pipeline.PostParticleRenderType;
import mod.arcomit.emberthral.render.particles.pipeline.PostPasses;
import mod.arcomit.emberthral.render.particles.targets.ScaledTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static net.minecraft.client.Minecraft.ON_OSX;

/**
 * 泛光粒子渲染类型（Bloom效果）
 * 实现高质量泛光效果的后处理管线
 */
public class BloomParticleRenderType extends PostParticleRenderType {

    // 构造函数
    public BloomParticleRenderType(ResourceLocation renderTypeID, ResourceLocation tex) {
        super(renderTypeID, tex);
    }

    // 获取关联的后处理管线
    @Override
    public PostEffectPipelines.Pipeline getPipeline() {
        return ppl;
    }

    // 静态泛光管线实例（单例模式）
    static final PostEffectPipelines.Pipeline ppl = new Pipeline(Emberthral.prefix("bloom_particle"));

    /**
     * 泛光效果的具体实现管线
     */
    public static class Pipeline extends PostEffectPipelines.Pipeline {
        // 管线命名
        public Pipeline(ResourceLocation name) {
            super(name);
        }

        /**
         * 核心处理流程：多级模糊与合成
         * @param src 源渲染目标（包含原始粒子图像）
         */
        void handlePasses(RenderTarget src) {
            // 设置纹理参数（边缘处理与过滤）
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL12.GL_LINEAR);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL12.GL_LINEAR);

            // === 下采样链：逐步降低分辨率（创建模糊层级）===
            // 原始 -> 1/2分辨率
            PostPasses.downSampler.process(src, blur[0]);    // 2倍下采样
            // 1/2 -> 1/4分辨率
            PostPasses.downSampler.process(blur[0], blur[1]); // 4倍下采样
            // 1/4 -> 1/8分辨率
            PostPasses.downSampler.process(blur[1], blur[2]); // 8倍下采样
            // 1/8 -> 1/16分辨率
            PostPasses.downSampler.process(blur[2], blur[3]); // 16倍下采样
            // 1/16 -> 1/32分辨率
            PostPasses.downSampler.process(blur[3], blur[4]); // 32倍下采样

            // === 上采样链：逐步恢复分辨率并混合模糊效果 ===
            // 32倍 -> 16倍（混合原始16倍图像）
            PostPasses.upSampler.process(blur[4], blur_[3], blur[3]);  // 32倍下采样 + 原始16倍 -> 混合16倍
            // 16倍 -> 8倍（混合原始8倍图像）
            PostPasses.upSampler.process(blur_[3], blur_[2], blur[2]);   // 混合16倍 + 原始8倍 -> 混合8倍
            // 8倍 -> 4倍（混合原始4倍图像）
            PostPasses.upSampler.process(blur_[2], blur_[1], blur[1]);  // 混合8倍 + 原始4倍 -> 混合4倍
            // 4倍 -> 2倍（混合原始2倍图像）
            PostPasses.upSampler.process(blur_[1], blur_[0], blur[0]);  // 混合4倍 + 原始2倍 -> 混合2倍

            // 最终合成：混合2倍模糊 + 原始图像 + 临时目标 -> 主渲染目标
            PostPasses.unity_composite.process(
                    blur_[0],      // 最终模糊结果（2倍混合）
                    temp,          // 临时渲染目标（中间存储）
                    src,           // 原始图像
                    Minecraft.getInstance().getMainRenderTarget() // 主渲染目标
            );

            // 将结果从临时目标复制到主渲染目标
            PostPasses.blit.process(temp, Minecraft.getInstance().getMainRenderTarget());
        }

        // === 渲染目标定义 ===
        RenderTarget[] blur;   // 下采样链（分辨率递减）
        RenderTarget[] blur_;  // 上采样链（分辨率递增，混合结果）
        RenderTarget temp;     // 临时渲染目标

        /**
         * 初始化渲染目标
         */
        void initTargets() {
            int cnt = 5; // 模糊层级数量（5级）

            // 初始化下采样目标链
            if (blur == null) {
                blur = new RenderTarget[cnt];
                float scale = 1.0f; // 初始缩放因子

                // 创建各级下采样目标
                for (int i = 0; i < blur.length; i++) {
                    scale /= 2; // 每级分辨率减半
                    // 创建缩放渲染目标
                    blur[i] = new ScaledTarget(scale, scale, bufferTarget.width, bufferTarget.height, false, ON_OSX);
                    blur[i].setClearColor(0.0F, 0.0F, 0.0F, 0.0F); // 透明背景
                    blur[i].clear(ON_OSX); // 清除目标
                    // 如果主目标启用模板缓冲，则当前目标也启用
                    if (bufferTarget.isStencilEnabled())
                        blur[i].enableStencil();
                }
            }

            // 初始化上采样目标链（比下采样少一级）
            if (blur_ == null) {
                blur_ = new RenderTarget[cnt - 1];
                float scale = 1.0f;
                for (int i = 0; i < blur_.length; i++) {
                    scale /= 2;
                    blur_[i] = new ScaledTarget(scale, scale, bufferTarget.width, bufferTarget.height, false, ON_OSX);
                    blur_[i].setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                    blur_[i].clear(ON_OSX);
                    if (bufferTarget.isStencilEnabled())
                        blur_[i].enableStencil(); // 注意：这里应该是blur_[i]，原代码有小错误
                }
            }

            // 初始化临时渲染目标
            if (temp == null) {
                temp = createTempTarget(bufferTarget); // 创建与主目标相同尺寸的临时目标
            }

            // 窗口大小变化时调整所有目标尺寸
            if (temp.width != bufferTarget.width || temp.height != bufferTarget.height) {
                // 调整下采样目标尺寸
                for (int i = 0; i < blur.length; i++) {
                    blur[i].resize(bufferTarget.width, bufferTarget.height, ON_OSX);
                }
                // 调整上采样目标尺寸
                for (int i = 0; i < blur_.length; i++) {
                    blur_[i].resize(bufferTarget.width, bufferTarget.height, ON_OSX);
                }
                // 调整临时目标尺寸
                temp.resize(bufferTarget.width, bufferTarget.height, ON_OSX);
            }
        }

        /**
         * 后处理主入口
         */
        @Override
        public void PostEffectHandler() {
            initTargets();       // 初始化/调整渲染目标
            handlePasses(bufferTarget); // 执行模糊处理流程
        }
    }

    // 辅助方法：安全计算缩放值（当前未使用）
    private static int NumMul(int a, float b) {
        return (int)(a * Math.max(Math.min(b, 1.5f), 0.8f));
    }
}