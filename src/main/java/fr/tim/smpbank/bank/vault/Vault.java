package fr.tim.smpbank.bank.vault;

import fr.tim.smpbank.StonksExchange;
import fr.tim.smpbank.gui.bank.BankInterface;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Vault implements Listener {
    public static NamespacedKey vaultKey = new NamespacedKey(StonksExchange.getPlugin(),"isVault");
    private Slime vaultInterface;
    private Block jigsaw;

    public Vault(Location loc, Player p) {
        loc.getBlock().setType(Material.JIGSAW);
        this.jigsaw =  loc.getBlock();

        setFacingAtPlayer(loc,p);

        this.vaultInterface.setInvulnerable(true);
        this.vaultInterface.setPersistent(true);
        this.vaultInterface.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1000000000,0,false,false));
        this.vaultInterface.setAI(false);
        this.vaultInterface.setSize(5);
        this.vaultInterface.getPersistentDataContainer().set(vaultKey, PersistentDataType.INTEGER,1);

    }

    public Vault() {}

    private void setFacingAtPlayer(Location loc,Player p) {
        Jigsaw data = (Jigsaw) this.jigsaw.getBlockData();

        Vector direction = p.getLocation().toVector().subtract(loc.toVector());
        direction.setY(0);
        direction.normalize();
        direction.rotateAroundY(-Math.PI/4);

        if (direction.getX()>0) {
            if (direction.getZ()>0) {
                data.setOrientation(Jigsaw.Orientation.EAST_UP);
                this.vaultInterface = (Slime) loc.getWorld().spawnEntity(loc.clone().add(1,-0.75,0.5), EntityType.SLIME);
            } else {
                data.setOrientation(Jigsaw.Orientation.NORTH_UP);
                this.vaultInterface = (Slime) loc.getWorld().spawnEntity(loc.clone().add(0.5,-0.75,0), EntityType.SLIME);
            }
        } else {
            if (direction.getZ()>0) {
                data.setOrientation(Jigsaw.Orientation.SOUTH_UP);
                this.vaultInterface = (Slime) loc.getWorld().spawnEntity(loc.clone().add(0.5,-0.75,1), EntityType.SLIME);
            } else {
                data.setOrientation(Jigsaw.Orientation.WEST_UP);
                this.vaultInterface = (Slime) loc.getWorld().spawnEntity(loc.clone().add(0,-0.75,0.5), EntityType.SLIME);

            }
        }

        this.jigsaw.setBlockData(data);

    }

    @EventHandler
    public void onVaultOpen(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Slime) {
            if (e.getRightClicked().getPersistentDataContainer().has(vaultKey,PersistentDataType.INTEGER)) {
                if (e.getRightClicked().getPersistentDataContainer().get(vaultKey,PersistentDataType.INTEGER) == 1) {
                    Player p = e.getPlayer();
                    new BankInterface(p);
                    p.playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS,1,0.5f);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSlimeAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Slime)) return;
        if (!e.getEntity().getPersistentDataContainer().has(vaultKey,PersistentDataType.INTEGER)) return;
        if (e.getEntity().getPersistentDataContainer().get(vaultKey,PersistentDataType.INTEGER) != 1) return;
        if (e.getDamager() instanceof Player p) {
            if (p.getGameMode().equals(GameMode.CREATIVE)) ((Slime) e.getEntity()).damage(1000);
            else e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent e) {
        if (!e.getEntity().getPersistentDataContainer().has(vaultKey,PersistentDataType.INTEGER)) return;
        if (e.getEntity().getPersistentDataContainer().get(vaultKey,PersistentDataType.INTEGER) != 1) return;
        e.setCancelled(true);
    }

}
