package justjabka.project_sanguine.rendering.visual;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.Optional;
import java.util.OptionalDouble;

public class InsanityPostEffectManager {
    private static final BindGroupLayout INSANITY = BindGroupLayout.builder()
            .withUniform("Insanity", UniformType.UNIFORM_BUFFER)
            .build();

    private static final RenderPipeline INSANITY_VFX_PIPELINE = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(ProjectSanguine.MOD_ID, "pipeline/sanity_vfx"))
            .withVertexShader(Identifier.withDefaultNamespace("core/screenquad"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(ProjectSanguine.MOD_ID, "core/sanity"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withBindGroupLayout(InsanityPostEffectManager.INSANITY)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build()
    );

    private static final GpuSampler GPU_SAMPLER = RenderSystem.getDevice().createSampler(
            AddressMode.CLAMP_TO_EDGE,
            AddressMode.CLAMP_TO_EDGE,
            FilterMode.NEAREST,
            FilterMode.NEAREST,
            16,
            OptionalDouble.empty());
    private static final InsanityUniforms INSANITY_UNIFORMS = new InsanityUniforms();

    private static float insanityProgress;

    public static void extractSanity(){
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        PlayerData data = player.getAttached(ProjectSanguineAttachments.PLAYER_DATA);
        if(data == null){
            insanityProgress = 0;
            return;
        }
        insanityProgress = 1.0f - Math.clamp(data.sanity() / 100f, 0f, 1f);
    }


    public static void renderSanityEffect(){
        Minecraft client = Minecraft.getInstance();

        RenderTarget mainTarget = client.gameRenderer.mainRenderTarget();
        GpuTextureView colorTexture = mainTarget.getColorTextureView();
        GpuBufferSlice progress = INSANITY_UNIFORMS.writeProgress(insanityProgress, Util.getMillis() / 1000f);

        assert colorTexture != null;

        try(RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                () -> ProjectSanguine.MOD_ID + " Low sanity VFX",
                colorTexture,
                Optional.empty()
        )){
            renderPass.setPipeline(InsanityPostEffectManager.INSANITY_VFX_PIPELINE);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("Insanity", progress);
            renderPass.bindTexture("Sampler0", colorTexture, GPU_SAMPLER);
            renderPass.draw(3, 1, 0, 0);
        }

        INSANITY_UNIFORMS.reset();
    }
}
