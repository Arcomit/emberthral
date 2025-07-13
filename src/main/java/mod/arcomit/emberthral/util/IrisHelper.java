package mod.arcomit.emberthral.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.fml.ModList;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL20C;

public class IrisHelper {
    // 属性,不确定未来版本会不会发生变化

    // vec3, position (x, y, z)
    public static final int vaPosition = 0;
    // vec4, color (r, g, b, a)
    public static final int vaColor = 1;
    // vec2, texture (u, v)
    public static final int vaUV0 = 2;
    // ivec2, overlay (u, v)
    public static final int vaUV1 = 3;
    // ivec2, lightmap (u, v)
    public static final int vaUV2 = 4;
    // vec3, normal (x, y, z)
    public static final int vaNormal = 5;

    // vec3, xy = blockId, renderType, "blockId" is used only for blocks specified in "block.properties"
    public static final int mc_Entity = 11;
    // vec2, st = midTexU, midTexV, Sprite middle UV coordinates
    public static final int mc_midTexCoord = 12;
    // vec4, xyz = tangent vector, w = handedness
    public static final int at_tangent = 13;
    //vec3, offset to block center in 1/64m units, Only for blocks
    public static final int at_midBlock = 14;

    public static final int COLOR_MAP_INDEX = GL13.GL_TEXTURE0;
    // Iris需要动态获取
    public static int NORMAL_MAP_INDEX = GL13.GL_TEXTURE1;
    public static int SPECULAR_MAP_INDEX = GL13.GL_TEXTURE3;
    public static void getNormalAndSpecularIndex(ShaderInstance shaderInstance) {
        int shaderProgram = shaderInstance.getId();
        int normals = GL20.glGetUniformLocation(shaderProgram, "normals");
        int specular = GL20.glGetUniformLocation(shaderProgram, "specular");
        if (normals != -1) {
            NORMAL_MAP_INDEX = GL13.GL_TEXTURE0 + GL20.glGetUniformi(shaderProgram, normals);
        }
        if (specular != -1) {
            SPECULAR_MAP_INDEX = GL13.GL_TEXTURE0 + GL20.glGetUniformi(shaderProgram, specular);
        }
    }

    // 判断是否加载了Iris并且启用了着色器包
    public static boolean irisIsLoadedAndShaderPackon() {
        if (ModList.get().isLoaded(Iris.MODID)) {
            return IrisApi.getInstance().isShaderPackInUse();
        }
        return false;
    }
}
