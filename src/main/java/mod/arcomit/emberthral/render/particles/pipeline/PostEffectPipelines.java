package mod.arcomit.emberthral.render.particles.pipeline;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.render.particles.targets.TargetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.PriorityQueue;
import java.util.Queue;

import static net.minecraft.client.Minecraft.ON_OSX;

/**
 * 后处理管线管理器
 * 负责协调和渲染所有后处理效果
 */
public class PostEffectPipelines {

    // === 全局管线队列 ===
    public static final Queue<Pipeline> PostEffectQueue = Queues.newConcurrentLinkedQueue(); // 外部添加的管线队列
    public static final PriorityQueue<Pipeline> PostEffectQueueInternal = Queues.newPriorityQueue(); // 内部按优先级排序的队列

    // === 深度缓存管理 ===
    static ResourceLocation depth_target = Emberthral.prefix("depth_target");
    public static RenderTarget depth; // 深度缓冲区副本

    /**
     * 执行所有后处理效果的主入口
     */
    public static void RenderPost() {
        // 检查是否有待处理管线
        if (!PostEffectQueue.isEmpty()) {
            RenderSystem.enableBlend(); // 开启混合模式（用于透明效果）

            // 准备深度缓冲区
            depth = TargetManager.getTarget(depth_target);
            depth.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            depth.unbindWrite();

            // === 管线处理流程 ===
            // 1. 将外部队列转移到内部优先级队列
            PostEffectQueue.removeIf((ppl) -> {
                PostEffectQueueInternal.add(ppl);
                return true;
            });

            // 2. 更新正交投影矩阵
            updateOrthoMatrix();

            // 3. 按优先级顺序执行所有管线
            Pipeline renderType;
            while (!PostEffectQueueInternal.isEmpty()) {
                renderType = PostEffectQueueInternal.poll(); // 获取最高优先级管线
                renderType.HandlePostEffect(); // 执行后处理
            }

            // === 恢复主渲染目标 ===
            // 1. 恢复深度缓冲区
            Minecraft.getInstance().getMainRenderTarget().copyDepthFrom(depth);
            // 2. 重新绑定主渲染目标
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }

        // 清理状态
        close();
        TargetManager.ReleaseAll(); // 释放所有渲染目标资源
    }

    /**
     * 更新正交投影矩阵
     * 用于着色器的屏幕空间渲染
     */
    public static Matrix4f shaderOrthoMatrix;
    static void updateOrthoMatrix() {
        var mainTarget = Minecraft.getInstance().getMainRenderTarget();
        // 创建正交投影矩阵（覆盖整个屏幕）
        shaderOrthoMatrix = (new Matrix4f()).setOrtho(
                0.0F,
                (float) mainTarget.width, // 屏幕宽度
                0.0F,
                (float) mainTarget.height, // 屏幕高度
                0.1F,
                1000.0F // 近/远平面
        );
    }

    // === 状态管理 ===
    private static boolean Active = false; // 全局激活状态

    public static boolean isActive() { return Active; }
    public static void active() { Active = true; } // 激活后处理系统
    public static void close() { Active = false; } // 关闭后处理系统

    /**
     * 获取当前渲染源
     * 根据是否使用透明通道返回不同的渲染目标
     */
    public static RenderTarget getSource() {
        if (Minecraft.getInstance().levelRenderer.transparencyChain == null) {
            return Minecraft.getInstance().getMainRenderTarget(); // 主渲染目标
        } else {
            return Minecraft.getInstance().levelRenderer.getParticlesTarget(); // 粒子专用目标
        }
    }

    /**
     * 后处理管线抽象基类
     * 所有具体后处理效果需继承此类
     */
    public static abstract class Pipeline implements Comparable<Pipeline> {
        protected boolean called = false;   // 是否已调用
        protected boolean started = false;  // 是否已开始
        protected RenderTarget bufferTarget; // 管线专用渲染目标
        public final ResourceLocation name; // 管线唯一标识
        public int priority = 0; // 执行优先级（数值越大越先执行）

        @Override
        public int compareTo(Pipeline o) {
            // 优先级比较：数值大的优先执行
            return Integer.compare(o.priority, this.priority);
        }

        public Pipeline(ResourceLocation name) {
            this.name = name;
        }

        /**
         * 启动管线处理
         * 准备渲染目标并加入队列
         */
        public void start() {
            if (started) {
                // 已启动：更新深度缓冲区
                if (Active) {
                    bufferTarget.copyDepthFrom(getSource());
                    bufferTarget.bindWrite(false);
                }
            } else {
                // 首次启动：初始化渲染目标
                if (bufferTarget == null) {
                    bufferTarget = TargetManager.getTarget(name);
                    bufferTarget.clear(ON_OSX); // 清空目标
                }

                if (Active) {
                    // 1. 复制深度缓冲区
                    bufferTarget.copyDepthFrom(getSource());
                    // 2. 加入处理队列
                    PostEffectQueue.add(this);
                    // 3. 绑定为当前渲染目标
                    bufferTarget.bindWrite(false);
                    started = true;
                }
            }
        }

        /**
         * 标记管线需要执行后处理
         */
        public void call() {
            if (Active) called = true;
        }

        /**
         * 暂停管线处理
         * 恢复主渲染目标
         */
        public void suspend() {
            if (Active) {
                // 解绑当前目标
                bufferTarget.unbindWrite();
                bufferTarget.unbindRead();

                // 恢复主渲染目标
                RenderTarget rt = getSource();
                rt.copyDepthFrom(bufferTarget); // 恢复深度
                rt.bindWrite(false); // 绑定主目标
            } else {
                // 非激活状态下直接绑定主目标
                getSource().bindWrite(false);
            }
        }

        // 抽象方法：具体后处理实现
        public abstract void PostEffectHandler();

        /**
         * 执行后处理主入口
         */
        public void HandlePostEffect() {
            if (called) PostEffectHandler(); // 执行具体处理
            // 重置状态
            bufferTarget = null;
            started = false;
            called = false;
        }
    }
}