package me.frxq15.mooncodes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CodeCommand implements CommandExecutor {
    MoonCodes plugin = MoonCodes.getInstance();
    CodeManager manager = plugin.getCodeManager();
    String sub;
    String code;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            for(String line : plugin.getConfig().getStringList("HELP_MESSAGE")) {
                sender.sendMessage(MoonCodes.getInstance().colourize(line));
            }
            return true;
        }
        if(args.length == 1) {
            sub = args[0];
            switch(sub.toLowerCase()) {

                case "reload":
                    if(!sender.hasPermission("mooncodes.reload")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    plugin.reloadConfig();
                    plugin.getFileManager().reloadCodesFile();
                    sender.sendMessage(plugin.formatMsg("PLUGIN_RELOADED"));
                    return true;

                case "listcodes":
                case "list":
                    if(!sender.hasPermission("mooncodes.listcodes")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    sender.sendMessage(plugin.formatMsg("CODES_HEADER"));
                    plugin.getFileManager().getCodesFile().getKeys(false).forEach(c -> {
                        sender.sendMessage(plugin.formatMsg("CODES_FORMAT").replace("%code%", c));
                    });
                    return true;

                case "help":
                    for(String line : plugin.getConfig().getStringList("HELP_MESSAGE")) {
                        sender.sendMessage(MoonCodes.getInstance().colourize(line));
                    }
                    return true;

                default:
                    sender.sendMessage(plugin.colourize("&cInvalid sub command specified, please use /codes help"));
                    return true;
            }
        }
        if(args.length == 2) {
            sub = args[0];
            code = args[1];
            switch(sub.toLowerCase()) {

                case "delete":
                    if(!sender.hasPermission("mooncodes.delete")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    if(!manager.codeExists(code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_DOESNT_EXIST"));
                        return true;
                    }
                    manager.deleteCode(code);
                    sender.sendMessage(plugin.formatMsg("CODE_DELETED").replace("%code%", code));
                    return true;

                case "info":
                case "information":
                    if(!sender.hasPermission("mooncodes.info")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    if(!manager.codeExists(code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_DOESNT_EXIST"));
                        return true;
                    }
                    for(String line : plugin.getConfig().getStringList("CODE_INFORMATION")) {
                        sender.sendMessage(MoonCodes.getInstance().colourize(line)
                                .replace("%createdby%", manager.getCreator(code))
                                .replace("%code%", code)
                                .replace("%uses%", manager.getUses(code)+""));
                    }
                    return true;

                case "commands":
                case "cmds":
                    if(!sender.hasPermission("mooncodes.commands")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    if(!manager.codeExists(code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_DOESNT_EXIST"));
                        return true;
                    }
                    sender.sendMessage(plugin.formatMsg("COMMANDS_HEADER").replace("%code%", code));
                    for(String commands : manager.getCommands(code)) {
                        sender.sendMessage(plugin.formatMsg("COMMANDS_FORMAT").replace("%command%", commands));
                    }
                    return true;

                case "check":
                case "uses":
                case "status":
                    if(!sender.hasPermission("mooncodes.check")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    if(!manager.codeExists(code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_DOESNT_EXIST"));
                        return true;
                    }
                    if(manager.getUses(code) <= 0) {
                        for(String line : plugin.getConfig().getStringList("CODE_STATUS")) {
                            sender.sendMessage(plugin.getInstance().colourize(line)
                                    .replace("%code%", code)
                                    .replace("%uses%", manager.getUses(code)+"")
                                    .replace("%status%", plugin.colourize("&cUnavailable")));
                        }
                        return true;
                    }
                    for(String line : plugin.getConfig().getStringList("CODE_STATUS")) {
                        sender.sendMessage(plugin.getInstance().colourize(line)
                                .replace("%code%", code)
                                .replace("%uses%", manager.getUses(code)+"")
                                .replace("%status%", plugin.colourize("&aRedeemable")));
                    }
                    return true;

                case "redeem":
                case "redeemcode":
                    if(!(sender instanceof Player)) {
                        plugin.log("This command cannot be executed from console.");
                        return true;
                    }
                    Player p = (Player) sender;
                    if(!sender.hasPermission("mooncodes.redeem")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    if(!manager.codeExists(code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_DOESNT_EXIST"));
                        return true;
                    }
                    if(manager.getUses(code) == 0) {
                        sender.sendMessage(plugin.formatMsg("CODE_ZERO_USES"));
                        return true;
                    }
                    if(manager.hasRedeemed(sender, code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_ALREADY_REDEEMED"));
                        return true;
                    }
                    manager.redeemCode(p, code);
                    p.sendMessage(plugin.formatMsg("CODE_REDEEMED").replace("%code%", code));
                    return true;

                default:
                    sender.sendMessage(plugin.colourize("&cInvalid sub command specified, please use /codes help"));
                    return true;
            }

        }
        if(args.length == 3) {
            sub = args[0];
            code = args[1];
            int uses;
            try {
                uses = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.formatMsg("INVALID_USES_SPECIFIED"));
                return true;
            }
            switch(sub.toLowerCase()) {

                case "create":
                    if(!sender.hasPermission("mooncodes.create")) {
                        sender.sendMessage(plugin.formatMsg("NO_PERMISSION"));
                        return true;
                    }
                    if(manager.codeExists(code)) {
                        sender.sendMessage(plugin.formatMsg("CODE_EXISTS"));
                        return true;
                    }
                    manager.createCode(sender, code, uses);
                    for(String line : plugin.getConfig().getStringList("CODE_GENERATED")) {
                        sender.sendMessage(plugin.getInstance().colourize(line).replace("%code%", code).replace("%uses%", uses+""));
                    }
                    return true;

                default:
                    sender.sendMessage(plugin.colourize("&cInvalid sub command specified, please use /codes help"));
                    return true;
            }
        }
        for(String line : plugin.getConfig().getStringList("HELP_MESSAGE")) {
            sender.sendMessage(plugin.getInstance().colourize(line));
        }
        return true;
    }
}
