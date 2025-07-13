package mod.arcomit.emberthral.util;
import net.minecraft.nbt.*;
import java.util.*;

public class NBTHelper {

    public static boolean contains(Tag checkTag, Tag targetTag) {
        // 基本类型检查
        if (checkTag.getId() != targetTag.getId()) return false;

        return switch (checkTag.getId()) {
            case Tag.TAG_COMPOUND -> containsCompound((CompoundTag) checkTag, (CompoundTag) targetTag);
            case Tag.TAG_LIST -> containsList((ListTag) checkTag, (ListTag) targetTag);
            default -> checkTag.equals(targetTag);
        };
    }

    // 复合标签处理
    private static boolean containsCompound(CompoundTag checkTag, CompoundTag targetTag) {
        // 检查所有键是否存在且匹配
        for (String key : checkTag.getAllKeys()) {
            if (!targetTag.contains(key, checkTag.getTagType(key))) return false;
            if (!contains(checkTag.get(key), targetTag.get(key))) return false;
        }
        return true;
    }

    // 列表处理（支持无序匹配）
    private static boolean containsList(ListTag checkTag, ListTag targetTag) {
        // 特殊情况处理
        if (checkTag.isEmpty()) return true;
        if (checkTag.size() > targetTag.size()) return false;

        // 原始标签缓存以提升性能
        List<Tag> bTags = new ArrayList<>();
        for (int i = 0; i < targetTag.size(); i++) {
            bTags.add(targetTag.get(i));
        }

        // 创建匹配状态数组
        boolean[] matched = new boolean[bTags.size()];

        // 尝试匹配A中的每个元素
        for (int i = 0; i < checkTag.size(); i++) {
            Tag aTag = checkTag.get(i);
            boolean foundMatch = false;

            // 在B中查找匹配元素
            for (int j = 0; j < bTags.size(); j++) {
                if (matched[j]) continue; // 跳过已匹配项

                if (contains(aTag, bTags.get(j))) {
                    matched[j] = true;
                    foundMatch = true;
                    break;
                }
            }

            // 如果A中有元素未找到匹配项
            if (!foundMatch) return false;
        }

        return true;
    }
}