package mod.arcomit.emberthral.render.particles.pipeline;

import net.minecraft.client.particle.Particle;

import java.util.*;

public class ParticleEngineHelper {

    public record PostParticles(PostParticleRenderType rt, Queue<Particle> particles) implements Comparable<PostParticles>{
        public static PostParticles of(PostParticleRenderType rt, Queue<Particle> particles){
            return new PostParticles(rt, particles);
        }

        @Override
        public int compareTo(PostParticles other) {
            return Integer.compare(other.rt.priority, this.rt.priority);  // 降序排列
        }
    }

    public static PriorityQueue<PostParticles> createQueue() {
        return new PriorityQueue<>();
    }
}
