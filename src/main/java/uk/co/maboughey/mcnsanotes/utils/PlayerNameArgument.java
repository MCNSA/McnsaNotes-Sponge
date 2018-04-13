package uk.co.maboughey.mcnsanotes.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.mcnsanotes.McnsaNotes;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerNameArgument extends CommandElement {

    public PlayerNameArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return args.next();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        String name = null;
        List<String> list = new ArrayList<String>();
        try {
            name = args.next();
        }
        catch (ArgumentParseException e) {
            McnsaNotes.log.error(e.getLocalizedMessage());
        }
        UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        Collection<GameProfile> results = uss.match(name);

        for (Iterator<GameProfile> iterator = results.iterator(); iterator.hasNext();) {
            list.add(iterator.next().getName().get());
        }

        //TODO: Add support for getting usernames from DB
        return list;
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("<Player>");
    }
}
