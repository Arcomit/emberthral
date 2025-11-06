package mod.arcomit.emberthral.core.bedrock.v1.resource;

import mod.arcomit.emberthral.core.bedrock.v1.common.model.BedrockModel;
import mod.arcomit.emberthral.core.bedrock.v1.common.resource.pojo.BedrockModelPOJO;

import java.util.function.Function;

public record BedrockModelResourceProcessor (RawResourceLoader rawLoader,
                                             Function<BedrockModelPOJO, BedrockModel> converter){
}
