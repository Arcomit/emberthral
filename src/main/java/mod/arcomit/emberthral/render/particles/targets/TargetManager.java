package mod.arcomit.emberthral.render.particles.targets;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.pipeline.RenderTarget;
import mod.arcomit.emberthral.render.particles.pipeline.PostParticleRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static net.minecraft.client.Minecraft.ON_OSX;

// 渲染目标管理器：负责复用和分配渲染目标资源
public class TargetManager {
    // 空闲渲染目标池（栈结构实现）
    private static final Stack<RenderTarget> freeTargets = new Stack<>();
    // 使用中的渲染目标映射表（key: 资源标识符, value: 渲染目标）
    private static final HashMap<ResourceLocation, RenderTarget> busyTarget = Maps.newHashMap();

    // 屏幕尺寸变更时的回调接口
    public static ScreenResizeEventHandler OnResize = (w,h) -> {};

    // 记录上次的屏幕尺寸（用于检测分辨率变化）
    private static int lastW, lastH;

    /**
     * 获取指定标识符对应的渲染目标（如不存在则创建新的）
     * @param handle 渲染目标资源标识符
     * @return 可用的渲染目标对象
     */
    public static RenderTarget getTarget(ResourceLocation handle){
        if(busyTarget.containsKey(handle)) return busyTarget.get(handle); // 已存在则直接返回
        else {
            RenderTarget rt = getTargetRaw();       // 创建/复用渲染目标
            busyTarget.put(handle, rt);             // 标记为使用中
            return rt;
        }
    }

    /**
     * 释放所有正在使用的渲染目标资源
     * 1. 将所有使用中的目标回收到空闲池
     * 2. 检查分辨率变化并调整目标尺寸
     * 3. 触发屏幕调整事件回调
     */
    public static void ReleaseAll(){
        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();

        // 检测分辨率是否发生变更
        boolean shouldResize = lastW != main.width || lastH != main.height;

        // 将所有使用中的渲染目标回收到空闲池
        busyTarget.forEach((k, v)->{
            freeTargets.push(v);
        });

        // 如果分辨率变化，调整空闲池中所有目标尺寸
        if(shouldResize){
            freeTargets.forEach((v) -> {
                v.resize(main.width, main.height, ON_OSX);
            });
        }

        // 更新记录的屏幕尺寸
        lastW = main.width;
        lastH = main.height;

        // 触发屏幕调整回调
        OnResize.consume(main.width, main.height);
        busyTarget.clear(); // 清空使用中映射表
    }

    /**
     * 释放单个渲染目标（移出使用中映射表，放回空闲池）
     * @param handle 要释放的目标资源标识符
     */
    public static void ReleaseTarget(ResourceLocation handle){
        if(busyTarget.containsKey(handle)){
            freeTargets.add(busyTarget.remove(handle));
        }
    }

    /**
     * 获取原始渲染目标（优先复用空闲池资源）
     * @return 可用的渲染目标实例
     */
    private static RenderTarget getTargetRaw(){
        RenderTarget rt;
        if(freeTargets.isEmpty()){
            // 创建临时渲染目标（复用后处理粒子渲染类型的创建逻辑）
            rt = PostParticleRenderType.createTempTarget(Minecraft.getInstance().getMainRenderTarget());
        }
        else {
            rt = freeTargets.pop(); // 从空闲池获取目标
        }
        return rt;
    }
}