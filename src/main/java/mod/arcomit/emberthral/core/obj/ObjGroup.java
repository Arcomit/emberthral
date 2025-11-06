package mod.arcomit.emberthral.core.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Getter;
import lombok.Setter;
import mod.arcomit.emberthral.utils.PoseStackAutoCloser;
import mod.arcomit.emberthral.core.obj.utils.WriteVerticesInfo;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-08-05 12:10
 * @Description: 模型组
 */
@OnlyIn(Dist.CLIENT)
@Getter@Setter
public class ObjGroup {

    private final String        name;
    private final List<ObjFace> faces    = new ArrayList<>();


    public ObjGroup(String name) {
        this.name = name;
    }

    public void writeVertices(VertexConsumer vertexConsumer){
            for (ObjFace face : faces) {

                face.writeVertices(vertexConsumer);

            }
    }

}
