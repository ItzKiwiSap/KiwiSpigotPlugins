package nl.itz_kiwisap_.spigot.pergroupdrops.api.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GroupProvider {

    @Nullable String getGroup(@NotNull Player player);
}