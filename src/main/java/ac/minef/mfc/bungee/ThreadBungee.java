/*    */
package ac.minef.mfc.bungee;
/*    */
/*    */

import com.tjplaysnow.discord.object.ProgramThread;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/*    */
/*    */
/*    */ public class ThreadBungee
        /*    */ extends ProgramThread
        /*    */ {
    /*    */   private final Plugin plugin;

    /*    */
    /*    */
    public ThreadBungee(Plugin plugin) {
        /* 15 */
        super(false);
        /* 16 */
        this.plugin = plugin;
        /*    */
    }

    /*    */
    /*    */
    /*    */
    public void addAction(Runnable action, int seconds) {
        /* 21 */
        ProxyServer.getInstance().getScheduler().schedule(this.plugin, action, seconds, TimeUnit.SECONDS);
        /*    */
    }

    /*    */
    /*    */
    public void stop() {
    }

    /*    */
    /*    */
    public void run() {
    }
    /*    */
}


/* Location:              D:\Users\Luke\Downloads\DCWM.jar!\eu\mip\alandioda\DCWM\bungee\ThreadBungee.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */