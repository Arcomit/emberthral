package mod.arcomit.emberthral.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FindEntityHelper {
    // 寻找离玩家准心最近的实体，MaxAngle为搜索角度，MaxDist为最大索敌距离
    public static Entity findClosestToCrosshair(Player player, double maxAngle, double maxDist) {
        // 基础数据预计算（减少重复获取）
        final Vec3 eyePos = player.getEyePosition(1.0f);
        final Vec3 lookVec = player.getLookAngle().normalize();
        final double cosThreshold = Math.cos(Math.toRadians(maxAngle));
        final double maxDistSqr = maxDist * maxDist; // 与AABB.inflate(100)匹配的平方距离

        // 使用更紧凑的候选实体筛选
        List<Entity> candidates = player.level().getEntitiesOfClass(
                Entity.class,
                new AABB(eyePos, eyePos).inflate(maxDist),
                entity -> entity.isAlive()
                        && entity.isPickable()
                        && eyePos.distanceToSqr(entity.position()) <= maxDistSqr
        );

        return candidates.stream()
                .filter(entity -> {
                    // 快速距离检查
                    Vec3 entityPos = entity.getBoundingBox().getCenter();
                    Vec3 toEntity = entityPos.subtract(eyePos);
                    double distSqr = toEntity.lengthSqr();
                    if (distSqr < 0.0001) return false;

                    // 角度筛选（使用平方比较优化）
                    double projection = toEntity.dot(lookVec);
                    double actualCos = projection / Math.sqrt(distSqr);
                    return actualCos >= cosThreshold;
                })
                .filter(entity -> {
                    // 延迟视线检测到最后阶段
                    Vec3 entityPos = entity.getBoundingBox().getCenter();
                    return player.level().clip(
                            new ClipContext(
                                    eyePos, entityPos,
                                    ClipContext.Block.VISUAL,
                                    ClipContext.Fluid.NONE,
                                    player
                            )
                    ).getType() != HitResult.Type.BLOCK;
                })
                .min(Comparator.comparingDouble(entity -> {
                    // 最终距离计算（基于准星的垂直距离）
                    Vec3 entityPos = entity.getBoundingBox().getCenter();
                    Vec3 closestPoint = eyePos.add(lookVec.scale(lookVec.dot(entityPos.subtract(eyePos))));
                    return entityPos.distanceToSqr(closestPoint);
                }))
                .orElse(null);
    }

    // 寻找离玩家最近的实体，Radius为搜索半径
    public static Entity findNearestEntity(Player player, double radius) {
        if (player == null || !player.isAlive()) return null;

        // 创建搜索范围（以玩家为中心的正方体）
        AABB area = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        Predicate<Entity> finalFilter = entity ->
                entity != player &&
                        entity.isAlive() &&
                        entity.isPickable();

        // 获取候选实体列表
        List<Entity> candidates = player.level().getEntities(player, area, finalFilter);

        // 流式处理找到最近实体
        return candidates.stream()
                .min(Comparator.comparingDouble(e ->
                        player.distanceToSqr(e) // 使用平方距离避免开方计算
                ))
                .orElse(null);
    }
}
