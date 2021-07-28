package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ParticleRenderer {

  public static void renderParticles(MatrixStack matrix, Camera camera) {
    for (BarParticle p : BarStates.PARTICLES) {
      renderParticle(matrix, p, camera);
    }
  }

  private static void renderParticle(MatrixStack matrix, BarParticle particle, Camera camera) {
    float scaleToGui = 0.025f;

    MinecraftClient client = MinecraftClient.getInstance();
    float tickDelta = client.getTickDelta();

    double x = MathHelper.lerp((double) tickDelta, particle.xPrev, particle.x);
    double y = MathHelper.lerp((double) tickDelta, particle.yPrev, particle.y);
    double z = MathHelper.lerp((double) tickDelta, particle.zPrev, particle.z);

    Vec3d camPos = camera.getPos();
    double camX = camPos.x;
    double camY = camPos.y;
    double camZ = camPos.z;

    matrix.push();
    matrix.translate(x - camX, y - camY, z - camZ);
    matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
    matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
    matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

    RenderSystem.disableLighting();
    RenderSystem.enableDepthTest();
    RenderSystem.disableAlphaTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
        GL11.GL_ZERO);
    RenderSystem.shadeModel(7425);

    HealthBarRenderer.drawDamageNumber(matrix, particle.damage, 0, 0, 10);

    RenderSystem.shadeModel(7424);
    RenderSystem.disableBlend();
    RenderSystem.enableAlphaTest();
    RenderSystem.enableLighting();

    matrix.pop();
  }
}
