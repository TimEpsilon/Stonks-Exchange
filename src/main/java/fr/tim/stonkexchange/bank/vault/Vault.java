package fr.tim.stonkexchange.bank.vault;

import fr.tim.stonkexchange.gui.bank.BankInterface;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Vault implements Listener {
    private Block jigsaw;

    public Vault(Location loc, Player p) {
        loc.getBlock().setType(Material.JIGSAW);
        this.jigsaw =  loc.getBlock();

        setFacingAtPlayer(loc,p);
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
            } else {
                data.setOrientation(Jigsaw.Orientation.NORTH_UP);
            }
        } else {
            if (direction.getZ()>0) {
                data.setOrientation(Jigsaw.Orientation.SOUTH_UP);
            } else {
                data.setOrientation(Jigsaw.Orientation.WEST_UP);
            }
        }

        this.jigsaw.setBlockData(data);

    }

    @EventHandler
    public void onVaultOpen(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.JIGSAW) {
                Player p = e.getPlayer();
                new BankInterface(p);
                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1, 0.5f);
                e.setCancelled(true);
            }
        }
    }
}
