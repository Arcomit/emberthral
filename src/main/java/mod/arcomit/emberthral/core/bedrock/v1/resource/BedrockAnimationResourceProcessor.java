package mod.arcomit.emberthral.core.bedrock.v1.resource;

import mod.arcomit.emberthral.core.bedrock.v1.common.animation.BedrockAnimation;
import mod.arcomit.emberthral.core.bedrock.v1.common.model.BedrockModel;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.pojo.BedrockAnimationFile;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiFunction;

public record BedrockAnimationResourceProcessor(RawResourceLoader rawLoader,
                                                ResourceLocation modelKey,
                                                BiFunction<BedrockAnimationFile, BedrockModel, List<BedrockAnimation>> converter) {
}
