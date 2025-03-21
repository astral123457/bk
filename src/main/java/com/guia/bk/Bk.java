package com.guia.bk;

import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Bk extends JavaPlugin implements Listener {

    private static final String DB_URL = "jdbc:sqlite:plugins/bk/bk.db";
    private static final String FOLDER_PATH = "plugins/bk";
    private static final String CONFIG_FILE = FOLDER_PATH + "/config.json";
    private static final String MESSAGES_FILE_PATH = FOLDER_PATH + "/messages.json";

    @Override
    public void onEnable() {
        // Mensagem no console para indicar ativação
        getLogger().info("Bk plugin habilitado!");

        createFolderAndConfig();
        boolean isEnabled = loadPluginStatus();

        if (!isEnabled) {
            getLogger().warning("Plugin desativado via configuração.");
            getServer().getPluginManager().disablePlugin(this);
            return; // Finaliza a inicialização se o plugin estiver desativado
        }

        // Registrar eventos
        getServer().getPluginManager().registerEvents(this, this);

        // Configurar banco de dados
        setupDatabase();

        // Carregar idioma
        String language = loadLanguage();
        getLogger().info("Idioma configurado: " + language);

        // Registrar comandos
        getCommand("oi").setExecutor(this);
        getCommand("kit").setExecutor(this);
        getCommand("bk").setExecutor(this);
        getCommand("vip").setExecutor(this);
        getCommand("addvip").setExecutor(this);
        getCommand("addpermission").setExecutor(this);
        getCommand("addvip").setExecutor(this);
        getCommand("rmvip").setExecutor(this);

        getCommand("emanuel").setExecutor(this);
        getCommand("lilith").setExecutor(this);
        getCommand("eva").setExecutor(this);
        getCommand("adan").setExecutor(this);

        getCommand("limpar").setExecutor(this);


    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Bk plugin desabilitado!");
    }



    int contador = 0;

    public void adicionarItem(Player player) {
        // Carregar o idioma configurado
        String language = loadLanguage();

        // Carregar mensagens
        MessageManager messageManager = new MessageManager();

        if (contador < 3) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 1));
            player.getInventory().addItem(new ItemStack(Material.BREAD, 1));
            player.getInventory().addItem(new ItemStack(Material.APPLE, 1));

            contador++;
            // Busca a mensagem traduzida
            String message = messageManager.getMessage("burguer_1", language, "contador", String.valueOf(contador));
            player.sendMessage(ChatColor.GREEN + message + " (" + contador + "/3)");
        } else {
            // Busca a mensagem traduzida
            String message = messageManager.getMessage("burguer_2", language);
            player.sendMessage(ChatColor.RED + message);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player2 = (Player) sender;
        String language = player2.getLocale().toLowerCase();
        if (language.startsWith("pt")) {
            language = "br";
        } else if (language.startsWith("en")) {
            language = "en";
        } else if (language.startsWith("es")) {
            language = "es";
        } else if (language.startsWith("fr")) {
            language = "fr";
        } else if (language.startsWith("de")) {
            language = "de";
        } else {
            language = "default"; // Idioma padrão caso não seja reconhecido
        }
        MessageManager messageManager = new MessageManager(); // Gerenciador de mensagens

        if (command.getName().equalsIgnoreCase("oi")||command.getName().equalsIgnoreCase("kit")||command.getName().equalsIgnoreCase("bk")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                String playerName = player.getName();

                if (!hasReceivedKit(playerName)) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE +
                            messageManager.getMessage("welcome", language, "playerName", playerName));
                    sender.sendMessage(ChatColor.GREEN + messageManager.getMessage("kit_received", language));

                    // Dando o kit ao jogador
                    player.getInventory().addItem(new ItemStack(Material.TORCH, 13));
                    player.getInventory().addItem(new ItemStack(Material.BOOK, 1));
                    player.getInventory().addItem(new ItemStack(Material.BREAD, 3));
                    player.getInventory().addItem(new ItemStack(Material.APPLE, 2));
                    player.getInventory().addItem(new ItemStack(Material.CHERRY_WOOD, 36));

                    markKitReceived(playerName);
                } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE +
                            messageManager.getMessage("welcome_back", language, "playerName", playerName));
                    sender.sendMessage(ChatColor.GREEN + messageManager.getMessage("kit_already_received", language));
                    adicionarItem(player);
                }
                return true;
            } else {
                sender.sendMessage(messageManager.getMessage("player_only_command", language));
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("addvip")) {
            if (sender.hasPermission("vip.add")) {
                if (args.length == 1) {
                    String targetPlayerName = args[0];
                    Player targetPlayer = getServer().getPlayer(targetPlayerName);

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "lp user " + targetPlayerName + " permission set vip.use true");

                    if (targetPlayer != null) {
                        addVipToDatabase(targetPlayerName);
                        sender.sendMessage(ChatColor.GREEN +
                                messageManager.getMessage("player_vip", language, "targetPlayerName", targetPlayerName));
                        targetPlayer.sendMessage(ChatColor.GOLD +
                                messageManager.getMessage("vip_congrats", language));
                    } else {
                        sender.sendMessage(ChatColor.RED +
                                messageManager.getMessage("player_not_found", language));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED +
                            messageManager.getMessage("addvip_usage", language));
                }
            } else {
                sender.sendMessage(ChatColor.RED +
                        messageManager.getMessage("no_permission", language));
            }
            return true;
        }

    else if (command.getName().equalsIgnoreCase("addpermission")) {
        if (args.length == 1) {
            String targetPlayerName = args[0];
            getServer().dispatchCommand(getServer().getConsoleSender(),
                    "lp user " + targetPlayerName + " permission set * true");

            sender.sendMessage("Você recebeu a permissão!");
        } else {
            sender.sendMessage("Uso correto: /addpermission <nome_do_jogador>");
        }
    return true;
}


        else if (command.getName().equalsIgnoreCase("rmvip")) {
            if (sender.hasPermission("vip.add")) {
                if (args.length == 1) {
                    String targetPlayerName = args[0];
                    if (removeVipFromDatabase(targetPlayerName)) {
                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "lp user " + targetPlayerName + " permission unset vip.use");

                        sender.sendMessage(ChatColor.GREEN +
                                messageManager.getMessage("remove_vip", language, "targetPlayerName", targetPlayerName));
                    } else {
                        sender.sendMessage(ChatColor.RED +
                                messageManager.getMessage("not_found_vip", language, "targetPlayerName", targetPlayerName));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED +
                            messageManager.getMessage("removevip_usage", language));
                }
            } else {
                sender.sendMessage(ChatColor.RED +
                        messageManager.getMessage("no_permission", language));
            }
            return true;
        }

        else if (command.getName().equalsIgnoreCase("emanuel")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.AQUA + messageManager.getMessage("emanuel_comando", language));

                    // Criar a Shulker Box amarela
                    ItemStack shulkerBox = new ItemStack(Material.YELLOW_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 40));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 36));
                    shulker.getInventory().addItem(new ItemStack(Material.EMERALD, 12));
                    shulker.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                    shulker.getInventory().addItem(new ItemStack(Material.COAL, 40));
                    shulker.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));

                    // Criar o capacete de Netherite
                    ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET, 1);
                    helmet.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    helmet.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    helmet.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    // Criar outras peças de armadura de Netherite
                    ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
                    chestplate.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    chestplate.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    chestplate.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
                    leggings.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    leggings.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    leggings.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS, 1);
                    boots.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    boots.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    boots.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);

                    // Adicionar as armaduras na Shulker Box
                    shulker.getInventory().addItem(helmet, chestplate, leggings, boots);

                    // Adicionar diamantes na Shulker Box
                    for (int i = 0; i < 14; i++) {
                        shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    }
                    shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 52));

                    // Criar a picareta especial encantada
                    ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE, 1);
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("DIG_SPEED"), 5); // Eficiência V
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("DURABILITY"), 3); // Inquebrável III
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("MENDING"), 1); // Remendo
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3); // Fortuna III

                    // Adicionar a picareta encantada na Shulker Box
                    shulker.getInventory().addItem(pickaxe);

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("lilith")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.DARK_GREEN + messageManager.getMessage("lilith_comando", language));

                    // Criar a Shulker Box verde
                    ItemStack shulkerBox = new ItemStack(Material.GREEN_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));
                    shulker.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
                    shulker.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 1));
                    shulker.getInventory().addItem(new ItemStack(Material.SHEEP_SPAWN_EGG, 1));
                    shulker.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                    shulker.getInventory().addItem(new ItemStack(Material.FLOWER_POT, 10));

                    // Criar uma espada de Netherite
                    ItemStack sword = new ItemStack(Material.NETHERITE_SWORD, 1);
                    sword.addUnsafeEnchantment(Enchantment.getByName("DAMAGE_ALL"), 5);
                    sword.addUnsafeEnchantment(Enchantment.getByName("UNBREAKING"), 3);
                    sword.addUnsafeEnchantment(Enchantment.getByName("MENDING"), 1);
                    sword.addUnsafeEnchantment(Enchantment.getByName("FIRE_ASPECT"), 2);
                    sword.addUnsafeEnchantment(Enchantment.getByName("LOOT_BONUS_MOBS"), 3);

                    // Adicionar a espada encantada na Shulker Box
                    shulker.getInventory().addItem(sword);

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                    sender.sendMessage(ChatColor.GREEN + messageManager.getMessage("Maldita_comando", language));

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }
        else if (command.getName().equalsIgnoreCase("eva")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + messageManager.getMessage("eva_comando", language));

                    // Criar a Shulker Box rosa
                    ItemStack shulkerBox = new ItemStack(Material.PINK_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.NETHERITE_BLOCK, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.EMERALD, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.COAL, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));

                    ItemStack hoe = new ItemStack(Material.NETHERITE_HOE, 1);
                    ItemStack axe = new ItemStack(Material.NETHERITE_AXE, 1);

                    // Criar um escudo
                    ItemStack shield = new ItemStack(Material.SHIELD, 1);

                    // Adicionar encantamentos ao escudo
                    shield.addUnsafeEnchantment(Enchantment.getByName("DURABILITY"), 3);
                    shield.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    // Adicionar os itens encantados na Shulker Box
                    shulker.getInventory().addItem(shield, hoe, axe);

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                    sender.sendMessage(ChatColor.AQUA + messageManager.getMessage("Rainha_comando", language));

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }
        else if (command.getName().equalsIgnoreCase("adan")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.GOLD + messageManager.getMessage("adan_comando", language));

                    // Criar a Shulker Box marrom
                    ItemStack shulkerBox = new ItemStack(Material.BROWN_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 40));
                    shulker.getInventory().addItem(new ItemStack(Material.TNT, 13));
                    shulker.getInventory().addItem(new ItemStack(Material.SHEEP_SPAWN_EGG, 14));
                    shulker.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 22));

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("limpar")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {

                        // Remove todos os itens do inventário do jogador
                        player.getInventory().clear();
                    sender.sendMessage(ChatColor.GREEN +messageManager.getMessage("limpo_comando", language));

                    } else {
                    sender.sendMessage(ChatColor.RED +messageManager.getMessage("jogador_comando", language));
                    }
                return true;
            } else {
                return true;
            }
        }


        else if (command.getName().equalsIgnoreCase("vip")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.GOLD + "Bem-vindo, VIP!");
                    player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                } else {
                    sender.sendMessage(ChatColor.RED +
                            messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED +messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }
        return false;
    }



    private void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS kit_log (" +
                    "player_name VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (player_name))";
            stmt.executeUpdate(sql);

            String sql2 = "CREATE TABLE IF NOT EXISTS death_locations (" +
                    "player_id VARCHAR(36) NOT NULL," +
                    "world VARCHAR(255) NOT NULL," +
                    "x DOUBLE NOT NULL," +
                    "y DOUBLE NOT NULL," +
                    "z DOUBLE NOT NULL," +
                    "yaw FLOAT NOT NULL," +
                    "pitch FLOAT NOT NULL," +
                    "PRIMARY KEY (player_id))";
            stmt.executeUpdate(sql2);


            String sql3 = "CREATE TABLE IF NOT EXISTS vip_players (" +
                    "player_name VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (player_name))";
            stmt.executeUpdate(sql3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// Métodos para armazenar e excluir
    private void addVipToDatabase(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO vip_players (player_name) VALUES (?)")) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean removeVipFromDatabase(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM vip_players WHERE player_name = ?")) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean hasReceivedKit(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT player_name FROM kit_log WHERE player_name = ?")) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void markKitReceived(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO kit_log (player_name) VALUES (?)")) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createFolderAndConfig() {
        // Criar a pasta blockdata, se não existir
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Criar o arquivo config.json com configuração padrão
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            JsonObject defaultConfig = new JsonObject();
            defaultConfig.addProperty("enabled", true);
            defaultConfig.addProperty("language", "br"); // Adiciona o idioma padrão

            try (FileWriter writer = new FileWriter(configFile)) {
                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(defaultConfig, writer);
                getLogger().info("Arquivo config.json criado com configuração padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo config.json: " + e.getMessage());
            }
        }

        // Criar o arquivo messages.json com mensagens padrão, apenas se não existir
        File messagesFile = new File(MESSAGES_FILE_PATH);
        if (!messagesFile.exists()) {
            JsonObject messages = new JsonObject();

            JsonObject lockChestMessages = new JsonObject();
            lockChestMessages.addProperty("en", "You received a burger during the day today! (");
            lockChestMessages.addProperty("es", "¡Recibiste una hamburguesa hoy! (");
            lockChestMessages.addProperty("br", "Você recebeu um hambúrguer durante o dia de hoje! (");
            lockChestMessages.addProperty("fr", "Vous avez reçu un burger aujourd'hui ! (");
            lockChestMessages.addProperty("de", "Sie haben heute einen Burger erhalten! (");
            messages.add("burguer_1", lockChestMessages);


            JsonObject unlockChestMessages = new JsonObject();
            unlockChestMessages.addProperty("en", "You have already received the maximum number of burgers allowed for today.");
            unlockChestMessages.addProperty("es", "Ya has recibido el número máximo de hamburguesas permitidas hoy.");
            unlockChestMessages.addProperty("br", "Você já recebeu o máximo de hambúrgueres permitido durante o dia de hoje.");
            unlockChestMessages.addProperty("fr", "Vous avez déjà reçu le nombre maximum de burgers autorisés pour aujourd'hui.");
            unlockChestMessages.addProperty("de", "Sie haben bereits die maximale Anzahl an erlaubten Burgern für heute erhalten.");
            messages.add("burguer_2", unlockChestMessages);

            JsonObject vipMessages = new JsonObject();
            vipMessages.addProperty("en", "Welcome, VIP!");
            vipMessages.addProperty("es", "¡Bienvenido, VIP!");
            vipMessages.addProperty("br", "Bem vindo, VIP!");
            vipMessages.addProperty("fr", "Bienvenue, VIP !");
            vipMessages.addProperty("de", "Willkommen, VIP!");

            // Mensagem 1
            JsonObject maxBurgerMessages = new JsonObject();
            maxBurgerMessages.addProperty("br", "Você já recebeu o máximo de hambúrgueres permitido durante o dia de hoje.");
            maxBurgerMessages.addProperty("en", "You have already received the maximum number of burgers allowed for today.");
            maxBurgerMessages.addProperty("es", "Ya has recibido el número máximo de hamburguesas permitidas hoy.");
            maxBurgerMessages.addProperty("fr", "Vous avez déjà reçu le nombre maximum de burgers autorisés pour aujourd'hui.");
            maxBurgerMessages.addProperty("de", "Sie haben bereits die maximale Anzahl an erlaubten Burgern für heute erhalten.");
            messages.add("max_burger", maxBurgerMessages);

// Mensagem 2
            JsonObject welcomeMessages = new JsonObject();
            welcomeMessages.addProperty("br", "Bem vindo, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("en", "Welcome, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("es", "¡Bienvenido, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("fr", "Bienvenue, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("de", "Willkommen, §e§l☾☼§d§l{playerName}§r!");
            messages.add("welcome", welcomeMessages);

// Mensagem 3
            JsonObject kitReceivedMessages = new JsonObject();
            kitReceivedMessages.addProperty("br", "Você acaba de receber um kit de sobrevivência básico!");
            kitReceivedMessages.addProperty("en", "You just received a basic survival kit!");
            kitReceivedMessages.addProperty("es", "¡Acabas de recibir un kit de supervivencia básico!");
            kitReceivedMessages.addProperty("fr", "Vous venez de recevoir un kit de survie de base !");
            kitReceivedMessages.addProperty("de", "Sie haben gerade ein grundlegendes Überlebenskit erhalten!");
            messages.add("kit_received", kitReceivedMessages);

// Mensagem 4
            JsonObject welcomeBackMessages = new JsonObject();
            welcomeBackMessages.addProperty("br", "Bem vindo de volta, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("en", "Welcome back, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("es", "¡Bienvenido de nuevo, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("fr", "Bon retour, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("de", "Willkommen zurück, §e§l☾☼§d§l{playerName}§r!");
            messages.add("welcome_back", welcomeBackMessages);

// Mensagem 5
            JsonObject kitAlreadyReceivedMessages = new JsonObject();
            kitAlreadyReceivedMessages.addProperty("br", "Você já recebeu seu kit!");
            kitAlreadyReceivedMessages.addProperty("en", "You have already received your kit!");
            kitAlreadyReceivedMessages.addProperty("es", "¡Ya has recibido tu kit!");
            kitAlreadyReceivedMessages.addProperty("fr", "Vous avez déjà reçu votre kit !");
            kitAlreadyReceivedMessages.addProperty("de", "Sie haben Ihr Kit bereits erhalten!");
            messages.add("kit_already_received", kitAlreadyReceivedMessages);

// Mensagem 6
            JsonObject playerOnlyCommandMessages = new JsonObject();
            playerOnlyCommandMessages.addProperty("br", "Este comando só pode ser executado por um jogador.");
            playerOnlyCommandMessages.addProperty("en", "This command can only be executed by a player.");
            playerOnlyCommandMessages.addProperty("es", "Este comando solo puede ser ejecutado por un jugador.");
            playerOnlyCommandMessages.addProperty("fr", "Cette commande ne peut être exécutée que par un joueur.");
            playerOnlyCommandMessages.addProperty("de", "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            messages.add("player_only_command", playerOnlyCommandMessages);

// Mensagem 7
            JsonObject playerVIPMessages = new JsonObject();
            playerVIPMessages.addProperty("br", "O jogador §e§l☾☼§d§l{targetPlayerName}§r agora é um VIP!");
            playerVIPMessages.addProperty("en", "The player §e§l☾☼§d§l{targetPlayerName}§r is now a VIP!");
            playerVIPMessages.addProperty("es", "¡El jugador §e§l☾☼§d§l{targetPlayerName}§r ahora es un VIP!");
            playerVIPMessages.addProperty("fr", "Le joueur §e§l☾☼§d§l{targetPlayerName}§r est maintenant un VIP !");
            playerVIPMessages.addProperty("de", "Der Spieler §e§l☾☼§d§l{targetPlayerName}§r ist jetzt ein VIP!");
            messages.add("player_vip", playerVIPMessages);

            JsonObject vipCongratsMessages = new JsonObject();
            vipCongratsMessages.addProperty("br", "Parabéns, você recebeu o status de VIP!");
            vipCongratsMessages.addProperty("en", "Congratulations, you have received VIP status!");
            vipCongratsMessages.addProperty("es", "¡Felicidades, has recibido el estado VIP!");
            vipCongratsMessages.addProperty("fr", "Félicitations, vous avez obtenu le statut VIP !");
            vipCongratsMessages.addProperty("de", "Herzlichen Glückwunsch, Sie haben den VIP-Status erhalten!");
            messages.add("vip_congrats", vipCongratsMessages);

            JsonObject playerNotFoundMessages = new JsonObject();
            playerNotFoundMessages.addProperty("br", "Jogador não encontrado.");
            playerNotFoundMessages.addProperty("en", "Player not found.");
            playerNotFoundMessages.addProperty("es", "Jugador no encontrado.");
            playerNotFoundMessages.addProperty("fr", "Joueur non trouvé.");
            playerNotFoundMessages.addProperty("de", "Spieler nicht gefunden.");
            messages.add("player_not_found", playerNotFoundMessages);

            JsonObject addVIPUsageMessages = new JsonObject();
            addVIPUsageMessages.addProperty("br", "Uso correto: /addvip <nome_do_jogador>");
            addVIPUsageMessages.addProperty("en", "Correct usage: /addvip <player_name>");
            addVIPUsageMessages.addProperty("es", "Uso correcto: /addvip <nombre_del_jugador>");
            addVIPUsageMessages.addProperty("fr", "Utilisation correcte : /addvip <nom_du_joueur>");
            addVIPUsageMessages.addProperty("de", "Korrekte Verwendung: /addvip <spielername>");
            messages.add("addvip_usage", addVIPUsageMessages);

            JsonObject noPermissionMessages = new JsonObject();
            noPermissionMessages.addProperty("br", "Você não tem permissão para usar este comando.");
            noPermissionMessages.addProperty("en", "You do not have permission to use this command.");
            noPermissionMessages.addProperty("es", "No tienes permiso para usar este comando.");
            noPermissionMessages.addProperty("fr", "Vous n'avez pas la permission d'utiliser cette commande.");
            noPermissionMessages.addProperty("de", "Sie haben keine Berechtigung, diesen Befehl zu verwenden.");
            messages.add("no_permission", noPermissionMessages);

            JsonObject removeVIPMessages = new JsonObject();
            removeVIPMessages.addProperty("br", "O jogador §e§l☾☼§d§l{targetPlayerName}§r não é mais VIP.");
            removeVIPMessages.addProperty("en", "The player §e§l☾☼§d§l{targetPlayerName}§r is no longer a VIP.");
            removeVIPMessages.addProperty("es", "El jugador §e§l☾☼§d§l{targetPlayerName}§r ya no es VIP.");
            removeVIPMessages.addProperty("fr", "Le joueur §e§l☾☼§d§l{targetPlayerName}§r n'est plus VIP.");
            removeVIPMessages.addProperty("de", "Der Spieler §e§l☾☼§d§l{targetPlayerName}§r ist nicht mehr VIP.");
            messages.add("remove_vip", removeVIPMessages);

            JsonObject notFoundVIPMessages = new JsonObject();
            notFoundVIPMessages.addProperty("br", "O jogador {targetPlayerName} não foi encontrado como VIP.");
            notFoundVIPMessages.addProperty("en", "The player {targetPlayerName} was not found as a VIP.");
            notFoundVIPMessages.addProperty("es", "El jugador {targetPlayerName} no fue encontrado como VIP.");
            notFoundVIPMessages.addProperty("fr", "Le joueur {targetPlayerName} n'a pas été trouvé comme VIP.");
            notFoundVIPMessages.addProperty("de", "Der Spieler {targetPlayerName} wurde nicht als VIP gefunden.");
            messages.add("not_found_vip", notFoundVIPMessages);

            JsonObject removeVIPUsageMessages = new JsonObject();
            removeVIPUsageMessages.addProperty("br", "Uso correto: /rmvip <nome_do_jogador>");
            removeVIPUsageMessages.addProperty("en", "Correct usage: /rmvip <player_name>");
            removeVIPUsageMessages.addProperty("es", "Uso correcto: /rmvip <nombre_del_jugador>");
            removeVIPUsageMessages.addProperty("fr", "Utilisation correcte : /rmvip <nom_du_joueur>");
            removeVIPUsageMessages.addProperty("de", "Korrekte Verwendung: /rmvip <spielername>");
            messages.add("removevip_usage", removeVIPUsageMessages);

            JsonObject inventoryClearedMessages = new JsonObject();
            inventoryClearedMessages.addProperty("br", "Todo o seu inventário foi limpo!");
            inventoryClearedMessages.addProperty("en", "Your entire inventory has been cleared!");
            inventoryClearedMessages.addProperty("es", "Todo tu inventario ha sido limpiado!");
            inventoryClearedMessages.addProperty("fr", "Tout votre inventaire a été nettoyé!");
            inventoryClearedMessages.addProperty("de", "Ihr gesamtes Inventar wurde geleert!");
            messages.add("inventory_cleared", inventoryClearedMessages);

            JsonObject vipWelcomeMessages = new JsonObject();
            vipWelcomeMessages.addProperty("br", "Bem-vindo, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("en", "Welcome, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("es", "¡Bienvenido, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("fr", "Bienvenue, §e§l☾☼§d§lVIP !");
            vipWelcomeMessages.addProperty("de", "Willkommen, §e§l☾☼§d§lVIP!");
            messages.add("vip_welcome", vipWelcomeMessages);


            JsonObject jogadorcomando = new JsonObject();
            jogadorcomando.addProperty("br", "Este comando só pode ser executado por um jogador.");
            jogadorcomando.addProperty("en", "This command can only be executed by one player.");
            jogadorcomando.addProperty("es", "Este comando solo puede ser ejecutado por un jugador.");
            jogadorcomando.addProperty("fr", "Cette commande ne peut être exécutée que par un joueur.");
            jogadorcomando.addProperty("de", "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            messages.add("jogador_comando", jogadorcomando);

            JsonObject emanuelcomando = new JsonObject();
            emanuelcomando.addProperty("br", "§b§lXAΓ§6, Cristo estratégia de precificação, escala, série de ideias, uma estrela, um macho dominante!");
            emanuelcomando.addProperty("en", "§b§lXAΓ§6, Christ pricing strategy, scale, series of ideas, a star, a dominant male!");
            emanuelcomando.addProperty("es", "§b§lXAΓ§6, Cristo estrategia de precios, escala, serie de ideas, una estrella, un macho dominante!");
            emanuelcomando.addProperty("fr", "§b§lXAΓ§6, Christ stratégie de tarification, échelle, série d'idées, une étoile, un mâle dominant!");
            emanuelcomando.addProperty("de", "§b§lXAΓ§6, Christus Preisstrategie, Skala, Ideenreihe, ein Stern, ein dominantes Männchen!");
            messages.add("emanuel_comando", emanuelcomando);

            JsonObject lilithcomando = new JsonObject();
            lilithcomando.addProperty("br", "§b§lΘΕ§6, Consciência emocional é pequena e leva a Morte Princesa Lilith");
            lilithcomando.addProperty("en", "§b§lΘΕ§6, Emotional awareness is small and leads to Death Princess Lilith");
            lilithcomando.addProperty("es", "§b§lΘΕ§6, La conciencia emocional es pequeña y lleva a la Muerte Princesa Lilith");
            lilithcomando.addProperty("fr", "§b§lΘΕ§6, La conscience émotionnelle est faible et mène à la Mort Princesse Lilith");
            lilithcomando.addProperty("de", "§b§lΘΕ§6, Emotionale Bewusstheit ist gering und führt zur Todesprinzessin Lilith");
            messages.add("lilith_comando", lilithcomando);

            JsonObject evacomando = new JsonObject();
            evacomando.addProperty("br", "§b§lΣΚ§6, Lesbica Feminista é uma criatura aquática, semelhante a uma tartaruga, que habita rios e lagos (EVA e Seus pecados Do mundo)");
            evacomando.addProperty("en", "§b§lΣΚ§6, Lesbian Feminist is an aquatic creature, similar to a turtle, that inhabits rivers and lakes (EVE and Her Sins of the World)");
            evacomando.addProperty("es", "§b§lΣΚ§6, Feminista lesbiana es una criatura acuática, similar a una tortuga, que habita ríos y lagos (EVA y sus pecados del mundo)");
            evacomando.addProperty("fr", "§b§lΣΚ§6, Féministe lesbienne est une créature aquatique, semblable à une tortue, qui habite les rivières et les lacs (EVE et ses péchés du monde)");
            evacomando.addProperty("de", "§b§lΣΚ§6, Lesbische Feministin ist ein Wasserwesen, ähnlich einer Schildkröte, das Flüsse und Seen bewohnt (EVA und ihre Sünden der Welt)");
            messages.add("eva_comando", evacomando);

            JsonObject adancomando = new JsonObject();
            adancomando.addProperty("br", "§b§lΔΙ§6, Latino Pequeno (Homem Barro Adão)");
            adancomando.addProperty("en", "§b§lΔΙ§6, Small Latin (Clay Man Adam)");
            adancomando.addProperty("es", "§b§lΔΙ§6, Latino pequeño (Hombre de barro Adán)");
            adancomando.addProperty("fr", "§b§lΔΙ§6, Petit latin (Homme d'argile Adam)");
            adancomando.addProperty("de", "§b§lΔΙ§6, Kleiner Lateiner (Tonmann Adam)");
            messages.add("adan_comando", adancomando);

            JsonObject Rainhacomando = new JsonObject();
            Rainhacomando.addProperty("br", "Rainha do Mar do Lado Oeste 27 Talentos!");
            Rainhacomando.addProperty("en", "Queen of the West Sea 27 Talents!");
            Rainhacomando.addProperty("es", "Reina del Mar del Oeste 27 Talentos!");
            Rainhacomando.addProperty("fr", "Reine de la mer de l'Ouest 27 Talents!");
            Rainhacomando.addProperty("de", "Königin des Westmeeres 27 Talente!");
            messages.add("Rainha_comando", Rainhacomando);

            JsonObject Malditacomando = new JsonObject();
            Malditacomando.addProperty("br", "Será Maldita entre todas as serpentes, você mata os animais!");
            Malditacomando.addProperty("en", "You will be cursed among all creatures, you kill animals!");
            Malditacomando.addProperty("es", "Serás maldita entre todas las serpientes, ¡matas a los animales!");
            Malditacomando.addProperty("fr", "Tu seras maudite parmi toutes les créatures, tu tues les animaux!");
            Malditacomando.addProperty("de", "Du wirst unter allen Kreaturen verflucht sein, du tötest Tiere!");
            messages.add("Maldita_comando", Malditacomando);

            JsonObject limpocomando = new JsonObject();
            limpocomando.addProperty("br", "Todo o seu inventário foi limpo!");
            limpocomando.addProperty("en", "Your entire inventory has been cleared!");
            limpocomando.addProperty("es", "¡Todo tu inventario ha sido limpiado!");
            limpocomando.addProperty("fr", "Tout votre inventaire a été nettoyé!");
            limpocomando.addProperty("de", "Ihr gesamtes Inventar wurde geleert!");
            messages.add("limpo_comando", limpocomando);



            try (FileWriter writer = new FileWriter(messagesFile)) {

                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(messages, writer);
                getLogger().info("Arquivo messages.json criado com mensagens padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo messages.json: " + e.getMessage());
            }
        }
    }


    private boolean loadPluginStatus() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            JsonObject config = new Gson().fromJson(content, JsonObject.class);
            return config.get("enabled").getAsBoolean();
        } catch (IOException e) {
            getLogger().severe("Erro ao ler o arquivo config.json: " + e.getMessage());
        }
        return false; // Desabilita o plugin em caso de erro
    }

    public String loadLanguage() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
                JsonObject config = new Gson().fromJson(content, JsonObject.class);
                return config.get("language").getAsString(); // Retorna o idioma configurado
            } catch (IOException e) {
                getLogger().severe("Erro ao ler o arquivo config.json: " + e.getMessage());
            }
        }
        return "br"; // Padrão para português, caso ocorra um erro
    }
}
