package systems.nope.discord.event.rolls;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.util.RollUtil;

public class ExtremeRollEvent extends RollXEvent {

    private final Boolean isMaximum;

    public static ExtremeRollEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length < 3 || command.length > 4)
            throw new ParseException("Sir, please use the command like !rolle <X> <MIN/MAX> [MODIFIER]. I get that this can be confusing at times...");

        Integer nRolls;
        try {
            nRolls = Integer.parseInt(command[1]);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried rolling '%s' dice?", command[1], command[1]));
        }

        if (command.length == 3) {
            String part = command[2].toLowerCase();

            if (!(part.equals("min") || part.equals("max")))
                throw new ParseException("Use 'min' or 'max' to indicate what kind of roll you want to perform idio-, i mean 'sir'.");

            return new ExtremeRollEvent(
                    event,
                    nRolls,
                    part.equals("max")
            );
        }

        // command has to be of a length of 4 here
        boolean isMax;
        int mod;

        if (command[2].equalsIgnoreCase("min") || command[2].equalsIgnoreCase("max")) {
            isMax = command[2].equalsIgnoreCase("max");

            try {
                mod = Integer.parseInt(command[3]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried rolling '%s' dice?", command[3], command[3]));
            }

            return new ExtremeRollEvent(event, nRolls, mod, isMax);
        }

        if (command[3].equalsIgnoreCase("min") || command[3].equalsIgnoreCase("max")) {
            isMax = command[3].equalsIgnoreCase("max");

            try {
                mod = Integer.parseInt(command[2]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried rolling '%s' dice?", command[2], command[2]));
            }

            return new ExtremeRollEvent(event, nRolls, mod, isMax);
        }

        throw new ParseException("Would you please use the command like '!rolle <X> <MIN/MAX> [MODIFIER]'. I get that this can be confusing at times...");
    }

    public ExtremeRollEvent(MessageReceivedEvent event, int numberOfRolls, Boolean isMaximum) {
        this(event, numberOfRolls, 0, isMaximum);

    }

    public ExtremeRollEvent(MessageReceivedEvent event, int numberOfRolls, int modifier, Boolean isMaximum) {
        super(event, numberOfRolls, modifier);
        this.isMaximum = isMaximum;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("All rolls were: ");

        int extremeResult = getResults().getEffectiveResult(getModifier()).get(0);
        int indexOfExtremeValue = 0;

        for (int i = 0; i < getResults().getSpecification().getnRolls(); i++) {
            if (i > 0) {
                sb.append(", ");

                int result = getResults().getEffectiveResult(getModifier()).get(i);
                if ((isMaximum && result > extremeResult) || (!isMaximum && result < extremeResult)) {
                    extremeResult = result;
                    indexOfExtremeValue = i;
                }
            }

            sb.append(
                    String.format("%s",
                            RollUtil.rollString(
                                    getResults().getEmoji().get(i),
                                    getResults().getDiceType(),
                                    getResults().getResult().get(i),
                                    getResults().getModifier(),
                                    getModifier()
                            )
                    )
            );
        }

        return String.format("%s rolls %d dice, the %s roll was %s!\n%s",
                getAuthorName(),
                getResults().getSpecification().getnRolls(),
                isMaximum ? "best" : "worst",
                RollUtil.rollString(
                        "",
                        getResults().getEmoji().get(indexOfExtremeValue),
                        getResults().getDiceType(),
                        getResults().getResult().get(indexOfExtremeValue),
                        getResults().getModifier(),
                        getModifier()
                ),
                sb.toString()
        );
    }
}
