package uk.co.maboughey.mcnsanotes.utils;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Messages {

    public static void sendMessage(CommandSource src, String s) {
        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(s));
    }
}
