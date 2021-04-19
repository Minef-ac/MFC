/*    */
package ac.minef.mfc.bungee.commands;
/*    */
/*    */

import ac.minef.mfc.bungee.MFC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/*    */
/*    */ public class MFCB
        /*    */ extends Command
        /*    */ {
    /*    */ MFC m;

    /*    */
    /*    */
    public MFCB(MFC m) {
        /* 12 */
        super("mfcb");
        /* 13 */
        this.m = m;
        /*    */
    }

    /*    */
    /*    */
    /*    */
    public void execute(CommandSender commandSender, String[] args) {
        /* 18 */
        if (!(commandSender instanceof ProxiedPlayer) &&
                /* 19 */       args.length == 1 && args[0].equalsIgnoreCase("reload"))
            /* 20 */ this.m.reloadConfig();
        /*    */
    }
    /*    */
}


/* Location:              D:\Users\Luke\Downloads\DCWM.jar!\eu\mip\alandioda\DCWM\bungee\Reload1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */