package nl.itz_kiwisap_.spigot.pergroupdrops;

import nl.itz_kiwisap_.spigot.common.network.interceptor.PacketInterceptorHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.plugin.KiwiPerGroupDropsPlugin;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.KiwiPerGroupDropsProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GlowProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.provider.types.GroupProvider;
import nl.itz_kiwisap_.spigot.pergroupdrops.scoreboard.PerGroupDropsScoreboardHandler;
import nl.itz_kiwisap_.spigot.pergroupdrops.settings.KiwiPerGroupDropsSettingsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface KiwiPerGroupDrops {

    static @NotNull KiwiPerGroupDrops create(@NotNull JavaPlugin plugin, @NotNull KiwiPerGroupDropsSettingsProvider settingsProvider) {
        return new KiwiPerGroupDropsImpl(plugin, settingsProvider);
    }

    static @NotNull KiwiPerGroupDrops create(@NotNull JavaPlugin plugin) {
        return new KiwiPerGroupDropsImpl(plugin, KiwiPerGroupDropsSettingsProvider.DEFAULT);
    }

    static @Nullable KiwiPerGroupDrops getInstance(@NotNull JavaPlugin plugin) {
        return KiwiPerGroupDropsImpl.getInstance(plugin);
    }

    static @Nullable KiwiPerGroupDrops getPluginInstance() {
        if (Bukkit.getPluginManager().isPluginEnabled("KiwiPerGroupDrops")) {
            KiwiPerGroupDropsPlugin plugin = JavaPlugin.getPlugin(KiwiPerGroupDropsPlugin.class);
            return getInstance(plugin);
        }

        return null;
    }

    @NotNull JavaPlugin getPlugin();

    @NotNull KiwiPerGroupDropsSettingsProvider getSettingsProvider();

    @ApiStatus.Internal
    @NotNull PacketInterceptorHandler getPacketInterceptorHandler();

    @NotNull KiwiPerGroupDropsProvider getProvider();

    @ApiStatus.Internal
    @NotNull PerGroupDropsScoreboardHandler getScoreboardHandler();

    void setGroupProvider(@NotNull GroupProvider groupProvider);

    void setGlowProvider(@NotNull GlowProvider glowProvider);
}