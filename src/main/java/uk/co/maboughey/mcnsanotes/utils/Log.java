package uk.co.maboughey.mcnsanotes.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Log {

    public Log() {
    }
    public void info(String message){
        Sponge.getServer().getConsole().sendMessage(colour("&A[MCNSA Notes] "+message));
    }
    public void warn(String message){
        Sponge.getServer().getConsole().sendMessage(colour("&5[MCNSA Notes] "+message));
    }
    public void error(String message){
        Sponge.getServer().getConsole().sendMessage(colour("&C[MCNSA Notes] "+message));
    }
    public Text colour(String input) {
        return TextSerializers.FORMATTING_CODE.deserialize(input);
    }
}
