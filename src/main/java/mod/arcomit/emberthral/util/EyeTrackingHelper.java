package mod.arcomit.emberthral.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EyeTrackingHelper {
    // 获取离玩家准星未被方块遮挡的最近的实体
    public static Entity findClosestToCrosshair(Player player, double maxAngle) {
        // 基础数据准备
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getLookAngle().normalize();
        double cosThreshold = Math.cos(Math.toRadians(maxAngle));

        // 获取候选实体
        List<Entity> candidates = player.level().getEntities(
                player,
                new AABB(eyePos, eyePos).inflate(100),
                entity -> entity.isAlive() && entity.isPickable()
        );

        // 筛选逻辑
        Entity result = null;
        double minDistSqr = Double.MAX_VALUE;

        for (Entity entity : candidates) {
            Vec3 entityPos = entity.getBoundingBox().getCenter();
            Vec3 PQ = entityPos.subtract(eyePos);

            // 计算投影和垂直距离
            double projection = PQ.dot(lookVec);
            Vec3 closestPoint = eyePos.add(lookVec.scale(projection));
            double verticalDistSqr = entityPos.distanceToSqr(closestPoint);

            // 角度筛选（避免除以零）
            double hypotenuseSqr = PQ.lengthSqr();
            if (hypotenuseSqr < 0.0001) continue; // 排除极近距离实体
            double actualCos = projection / Math.sqrt(hypotenuseSqr);
            if (actualCos < cosThreshold) continue;

            // 视线遮挡检测
            if (player.level().clip(new ClipContext(
                    eyePos, entityPos,
                    ClipContext.Block.VISUAL,
                    ClipContext.Fluid.NONE,
                    player
            )).getType() == HitResult.Type.BLOCK) continue;

            // 更新最近目标
            if (verticalDistSqr < minDistSqr) {
                minDistSqr = verticalDistSqr;
                result = entity;
            }
        }

        return result;
    }
}
