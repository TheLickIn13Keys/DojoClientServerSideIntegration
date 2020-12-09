package DojoClient.server;

import DojoClient.Shared.MiscConstants;
import DojoClient.Shared.MultiplayerConstants;
import DojoClient.server.packets.CustomPacketManager;
import DojoClient.server.packets.server.SPacketTest;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private List<UUID> peopleUsingClient = new ArrayList<UUID>();

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Started");
        DojoClient.server.packets.CustomPacketManager.registerPackets();
        Bukkit.getPluginManager().registerEvents(this, this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Status.Server.SERVER_INFO) {

            @Override
            public void onPacketSending(PacketEvent event) {

                WrappedServerPing ping = event.getPacket().getServerPings().read(0);
                //ping.setVersionProtocol(MultiplayerConstants.PING_VERSION);
                //ping.setVersionName("DojoClient v" + MiscConstants.CLIENT_VERSION);

            }
        });
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){

        String[] hostnameParts = e.getHostname().split(":")[0].split("\0");
        boolean isUsingClient = false;

        if(hostnameParts.length == 2){
            if(hostnameParts[1].equals(MultiplayerConstants.AUTH_KEY)){
                isUsingClient = true;
                new BukkitRunnable(){
                    @Override
                    public void run() {

                        e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "[+] Thank You For Using DojoClient!");
                        peopleUsingClient.add(e.getPlayer().getUniqueId());
                        CustomPacketManager.sendCustomPacket(e.getPlayer(), new SPacketTest());

                    }
                }.runTaskLater(this, 2);

            }
        }


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        if(peopleUsingClient.contains(uuid)){
            peopleUsingClient.remove(uuid);
        }
    }

    public List<UUID> getPeopleUsingClient() {
        return peopleUsingClient;
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player p = e.getPlayer();

        //TEMP
       //p.setGameMode(GameMode.CREATIVE);
        //p.setOp(true);

        new BukkitRunnable(){
            @Override
            public void run() {
                DojoClient.server.packets.CustomPacketManager.sendCustomPacket(p, new DojoClient.server.packets.server.SPacketTest());

            }
        }.runTaskLater(this, 2);


    }

}
