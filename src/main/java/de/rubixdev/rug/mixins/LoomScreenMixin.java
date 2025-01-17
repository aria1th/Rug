package de.rubixdev.rug.mixins;

import de.rubixdev.rug.RugSettings;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {
    @ModifyConstant(method = "onInventoryChanged", constant = @Constant(intValue = 6))
    private int overwriteMaxLayers(final int original) {
        return RugSettings.maxBannerLayers;
    }
}
