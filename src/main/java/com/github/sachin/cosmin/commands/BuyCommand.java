package com.github.sachin.cosmin.commands;

import com.github.sachin.cosmin.Cosmin;
import com.github.sachin.cosmin.armor.CosmeticSet;
import com.github.sachin.cosmin.armor.CosminArmor;
import com.github.sachin.cosmin.player.CosminPlayer;
import com.github.sachin.cosmin.utils.CosminConstants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BuyCommand extends SubCommands{

    private Cosmin plugin;

    public BuyCommand(Cosmin plugin){
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getPermission() {
        return CosminConstants.PERM_COMMAND_BUY;
    }

    @Override
    public String getUsage() {
        return "&3/cosmin &fbuy &9[player_name] [item-name/set-name] (take-money)";
    }

    @Override
    public String getDescription() {
        return "adds the specified item to the player's purchased item list";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length < 3) return;
        boolean takeMoney = args.length == 4 ? Boolean.parseBoolean(args[3]) : false;
        Player targetPlayer = Bukkit.getPlayerExact(args[1]);
        if(targetPlayer == null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CosminConstants.MESSAGE_PREFIX+"player dosn't exsist or is offline"));
            return;
        }
        String name = args[2];
        if(!plugin.getPlayerManager().containsPlayer(targetPlayer)) return;
        CosminPlayer cPlayer = plugin.getPlayerManager().getPlayer(targetPlayer);
        int cost=0;
        if(plugin.getArmorManager().containsArmor(name) && !cPlayer.getPurchasedItems().contains(name)){
            CosminArmor armor = plugin.getArmorManager().getArmor(name);
            cost = armor.getCost();
            cPlayer.getPurchasedItems().add(name);
            
        }else if(plugin.getArmorManager().containsSet(name) && !cPlayer.getPurchasedSets().contains(name)){
            CosmeticSet set = plugin.getArmorManager().getSet(name);
            cost = set.getCost();
            cPlayer.getPurchasedSets().add(name);
        }
        if(takeMoney){
            if(plugin.isEconomyEnabled() && cost != 0){
                if(plugin.getEconomy().getBalance(targetPlayer) >= cost){
                    plugin.getEconomy().withDraw(targetPlayer, cost);
                }
            }
        }
        
    }

}
    
