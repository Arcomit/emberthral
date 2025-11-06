package mod.arcomit.emberthral.core.bedrock.v1.common.resource;

import mod.arcomit.emberthral.core.bedrock.v1.common.resource.exclusion.NullAdapter;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.exclusion.ServerExclusionStrategyForRootMotion;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.exclusion.ServerNormalExclusionStrategy;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.pojo.AnimationKeyframes;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.pojo.BedrockModelPOJO;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.pojo.CubesItem;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.pojo.SoundEffectKeyframes;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.serialize.AnimationKeyframesSerializer;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.serialize.SoundEffectKeyframesSerializer;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.serialize.Vector3fSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class GsonUtil {
    public static final Gson CLIENT_GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer())
            .registerTypeAdapter(Vector3f.class, new Vector3fSerializer())
            .registerTypeAdapter(AnimationKeyframes.class, new AnimationKeyframesSerializer())
            .registerTypeAdapter(SoundEffectKeyframes.class, new SoundEffectKeyframesSerializer())
            .create();

    public static final Gson SERVER_NORMAL_GSON = new GsonBuilder()
            .addDeserializationExclusionStrategy(new ServerNormalExclusionStrategy())
            .registerTypeAdapter(BedrockModelPOJO.class, new NullAdapter<>()) // 常规情况下服务端无需加载模型
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(SoundEffectKeyframes.class, new SoundEffectKeyframesSerializer())
            .create();

    public static final Gson SERVER_GSON_FOR_ROOT_MOTION = new GsonBuilder()
            .addDeserializationExclusionStrategy(new ServerExclusionStrategyForRootMotion())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(Vector3f.class, new Vector3fSerializer())
            .registerTypeAdapter(AnimationKeyframes.class, new AnimationKeyframesSerializer())
            .registerTypeAdapter(SoundEffectKeyframes.class, new SoundEffectKeyframesSerializer())
            .create();
}
