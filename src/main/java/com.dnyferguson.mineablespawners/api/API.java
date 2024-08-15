package com.dnyferguson.mineablespawners.api;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import com.dnyferguson.mineablespawners.utils.SpawnerUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class API {
    private final MineableSpawners plugin;

    public API(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public EntityType getEntityTypeFromItemStack(ItemStack item) {
        EntityType entityType = null;

        // v3 compatibility
        try {
            NBTItem nbti = new NBTItem(item);
            if (nbti.hasKey("ms_mob")) {
                entityType = EntityType.valueOf(nbti.getString("ms_mob"));
                return entityType;
            }
        } catch (Exception ignore) {}

        if (plugin.getConfigurationHandler().getBoolean("global", "backwards-compatibility")) {
            // v2 compatibility
            try {
                entityType = EntityType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase());
                return entityType;
            } catch (Exception ignore) {}

            // v1 compatibility
            try {
                entityType = EntityType.valueOf(item.getItemMeta().getLore().toString().split(": §7")[1].split("]")[0].toUpperCase());
            } catch (Exception ignore) {}
        }

        return entityType;
    }

    public ItemStack getSpawnerFromEntityName(String entityName) {
        EntityType entityType = EntityType.valueOf(entityName.toUpperCase().replace(" ","_"));
        return getSpawnerFromEntityType(entityType);
    }

    public ItemStack getSpawnerFromEntityType(EntityType entityType) {
        return SpawnerUtils.generateSpawnerItem(entityType, 1);
    }
}
