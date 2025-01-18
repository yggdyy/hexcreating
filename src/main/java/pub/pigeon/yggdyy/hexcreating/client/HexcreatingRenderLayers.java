package pub.pigeon.yggdyy.hexcreating.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class HexcreatingRenderLayers extends RenderLayer {
        public static final RenderLayer WIRE = RenderLayer.of(
                "wire",
                VertexFormats.POSITION_COLOR_LIGHT,
                VertexFormat.DrawMode.TRIANGLE_STRIP,
                256,
                false,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.LEASH_PROGRAM)
                        .texture(RenderPhase.NO_TEXTURE)
                        .cull(RenderPhase.DISABLE_CULLING)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .build(false)
        );

        public HexcreatingRenderLayers(String string, VertexFormat arg, VertexFormat.DrawMode arg2, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
            super(string, arg, arg2, i, bl, bl2, runnable, runnable2);
        }

}
