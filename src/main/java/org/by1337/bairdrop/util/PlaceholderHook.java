package org.by1337.bairdrop.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.by1337.bairdrop.AirDrop;
import org.by1337.bairdrop.BAirDrop;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "By1337";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "BAirDrop";
    }

    @Override
    public @NotNull String getVersion() {
        return BAirDrop.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {// %bairdrop_test% = test %bairdrop_time_to_open_<air id>%
        if (params.contains("is_start_")) { //%bairdrop_is_start_<air id>%
            String[] args = params.split("_");
            if(args.length != 3) {
                return "error";
            }
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[2], null);
            if(airDrop == null) {
                return "error";
            }
            return "" + airDrop.isAirDropStarted();
        }
        if (params.contains("is_locked_")) { //%bairdrop_is_locked_<air id>%
            String[] args = params.split("_");
            if(args.length != 3) {
                return "error";
            }
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[2], null);
            if(airDrop == null) {
                return "error";
            }
            if(!airDrop.isAirDropStarted()) {
                return BAirDrop.getConfigMessage().getMessage("air-no-respawn")
                        .replace("{id}", airDrop.getId());
            }
            return "" + airDrop.isAirDropLocked();
        }
        if (params.contains("world_")) { //%bairdrop_world_<air id>%
            String[] args = params.split("_");
            if(args.length != 2) {
                return "error";
            }
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[1], null);
            if(airDrop == null) {
                return "error";
            }
            if(!airDrop.isAirDropStarted()) {
                return BAirDrop.getConfigMessage().getMessage("air-no-respawn")
                        .replace("{id}", airDrop.getId());
            }
            String world = airDrop.getWorld().getName();
            switch (world) {
                case "world":
                    return "" + BAirDrop.getConfigMessage().getMessage("NORMAL");
                case "world_the_end":
                    return "" + BAirDrop.getConfigMessage().getMessage("THE_END");
                case "world_nether":
                    return "" + BAirDrop.getConfigMessage().getMessage("NETHER");
                default:
                return "" + BAirDrop.getConfigMessage().getMessage("CUSTOM");
            }
        }
        if (params.equals("near")) { //%bairdrop_near%
            if (player == null) return "";
            AirDrop airDrop = null;
            int dist = 0;
            for (AirDrop air : BAirDrop.airDrops.values()) {
                if (!air.isAirDropStarted()) continue;
                if (!air.getAnyLoc().getWorld().equals(player.getPlayer().getWorld())) continue;
                if (dist > player.getPlayer().getLocation().distance(air.getAirDropLocation()) || airDrop == null) {
                    dist = (int) player.getPlayer().getLocation().distance(air.getAirDropLocation());
                    airDrop = air;
                }
            }
            if (airDrop == null)
                return BAirDrop.getConfigMessage().getMessage("air-near-none");
            return Message.messageBuilder(airDrop.replaceInternalPlaceholder(BAirDrop.getConfigMessage().getMessage("air-near").replace("{dist}", dist + "")));
        }
        if (params.contains("is_activated_")) { //%bairdrop_is_activated_<air id>%
            String[] args = params.split("_");
            if(args.length != 3) {
                return "error";
            }
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[2], null);
            if(airDrop == null) {
                return "error";
            }
            if(!airDrop.isAirDropStarted()) {
                return BAirDrop.getConfigMessage().getMessage("air-no-respawn")
                        .replace("{id}", airDrop.getId());
            }
            return "" + airDrop.isActivated();
        }
        if (params.contains("time_to_open_")) { //%bairdrop_time_to_open_<air id>%
            String[] args = params.split("_");
            if (args.length != 4) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[3], null);
            if (airDrop == null) return "error";
            return airDrop.getTimeToOpen() + "";
        }
        if (params.equals("time_start")) { //%bairdrop_time_start%
            if (BAirDrop.globalTimer == null)
                return AirManager.getTimeToNextAirdrop() + "";
            int time = 0;
            time += BAirDrop.globalTimer.getTimeToStart();
            if (BAirDrop.globalTimer.getAir() != null)
                time += BAirDrop.globalTimer.getAir().getTimeToStart();
            return time + "";
        }
        if (params.equals("time_start_new_format")) { //%bairdrop_time_start_new_format%
            if (BAirDrop.globalTimer == null)
                return AirManager.formatTime(AirManager.getTimeToNextAirdrop());
            int time = 0;
            time += BAirDrop.globalTimer.getTimeToStart();
            if (BAirDrop.globalTimer.getAir() != null)
                time += BAirDrop.globalTimer.getAir().getTimeToStart();
            return AirManager.formatTime(time);
        }
        if (params.equals("time_start_format")) { //%bairdrop_time_start_format%
            if (BAirDrop.globalTimer == null)
                return AirManager.getFormat(AirManager.getTimeToNextAirdrop());
            int time = 0;
            time += BAirDrop.globalTimer.getTimeToStart();
            if (BAirDrop.globalTimer.getAir() != null)
                time += BAirDrop.globalTimer.getAir().getTimeToStart();
            return AirManager.getFormat(time);
        }
        if (params.contains("time_to_end_format_")) { //%bairdrop_time_to_end_format_<air id>%
            String[] args = params.split("_");
            if (args.length != 5) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[4], null);
            if (airDrop == null) return "error";
            return AirManager.getFormat(airDrop.getTimeStop());
        }
        if (params.contains("time_to_end_new_format_")) { //%bairdrop_time_to_end_new_format_<air id>%
            String[] args = params.split("_");
            if (args.length != 6) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[5], null);
            if (airDrop == null) return "error";
            return AirManager.formatTime(airDrop.getTimeStop());
        }
        if (params.contains("time_to_start_format_")) { //%bairdrop_time_to_start_format_<air_id>%
            String[] args = params.split("_");
            if (args.length != 5) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[4], null);
            if (airDrop == null) return "error";
            return  AirManager.getFormat(airDrop.getTimeToStart());
        }
        if (params.contains("time_to_start_new_format_")) { //%bairdrop_time_to_start_new_format_<air_id>%
            String[] args = params.split("_");
            if (args.length != 6) {
                return "error1";
            }
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[5], null);
            if (airDrop == null) {
                return "error2";
            }
            return  AirManager.formatTime(airDrop.getTimeToStart());
        }
        if (params.contains("time_to_start_")) { //%bairdrop_time_to_start_<air_id>%
            String[] args = params.split("_");
            if (args.length != 4) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[3], null);
            if (airDrop == null) return "error";
            return airDrop.getTimeToStart() + "";
        }
        if (params.contains("time_to_end_")) { //%bairdrop_time_to_end_<air id>%
            String[] args = params.split("_");
            if (args.length != 4) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[3], null);
            if (airDrop == null) return "error";
            return airDrop.getTimeStop() + "";
        }
        if (params.contains("air_name_")) { //%bairdrop_air_name_<air id>%
            String[] args = params.split("_");
            if (args.length != 3) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[2], null);
            if (airDrop == null) return "error";
            return airDrop.getDisplayName();
        }
        if (params.contains("x_")) { //%bairdrop_x_<air id>%
            String[] args = params.split("_");
            if (args.length != 2) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[1], null);
            if (airDrop == null) return "error";

            if (airDrop.getAnyLoc() == null) {
                return "?";
            } else {
                return String.valueOf(airDrop.getAnyLoc().getX()).replace(".0", "");
            }

        }
        if (params.contains("y_")) { //%bairdrop_y_<air id>%
            String[] args = params.split("_");
            if (args.length != 2) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[1], null);
            if (airDrop == null) return "error";
            if (airDrop.getAnyLoc() == null) {
                return "?";
            } else {
                return String.valueOf(airDrop.getAnyLoc().getY()).replace(".0", "");
            }
        }
        if (params.contains("z_")) { //%bairdrop_z_<air id>%
            String[] args = params.split("_");
            if (args.length != 2) return "error";
            AirDrop airDrop = BAirDrop.airDrops.getOrDefault(args[1], null);
            if (airDrop == null) return "error";
            if (airDrop.getAnyLoc() == null) {
                return "?";
            } else {
                return String.valueOf(airDrop.getAnyLoc().getZ()).replace(".0", "");
            }
        }
        return null;
    }


}
