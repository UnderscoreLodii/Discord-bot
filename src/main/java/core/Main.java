package core;

import audio.GlobalAudioManager;
import audio.VoiceConnectionHandler;
import calendar.CalendarEventDispatcher;
import calendar.eventhandlers.BirthdayCalendarEventHandler;
import calendar.events.CalendarEvent;
import club.minnced.discord.jdave.interop.JDaveSessionFactory;
import commands.DeleteIntroCommand;
import commands.SetBirthdayCommand;
import commands.SetIntroCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.audio.dave.DaveSessionFactory;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.CalendarDataRepository;
import repositories.IntroDataRepository;
import services.CalendarCheckerService;
import services.CalendarService;
import services.IntroService;
import services.MusicService;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static void main(String[] args) {
        String token = Dotenv.load().get("TOKEN");
//        testing
        token = Dotenv.load().get("TESTING_TOKEN");

        VoiceConnectionHandler voiceConnectionHandler = new VoiceConnectionHandler();
        GlobalAudioManager globalAudioManager = new GlobalAudioManager(voiceConnectionHandler);
        MusicService musicService = new MusicService(voiceConnectionHandler, globalAudioManager);
        IntroDataRepository introDataRepository = new IntroDataRepository();
        IntroService introService = new IntroService(musicService, introDataRepository);
        VoiceListener voiceListener = new VoiceListener(introService);

        CalendarDataRepository calendarDataRepository = new CalendarDataRepository();
        CalendarService calendarService = new CalendarService(calendarDataRepository);

        CommandManager commandManager = new CommandManager()
                .addCommand(new SetIntroCommand(introService))
                .addCommand(new DeleteIntroCommand(introService))
                .addCommand(new SetBirthdayCommand(calendarService));

        MessageListener messageListener = new MessageListener();

        DaveSessionFactory daveSessionFactory = new JDaveSessionFactory();
        AudioModuleConfig audioConfig = new AudioModuleConfig()
                .withDaveSessionFactory(daveSessionFactory);

        JDA jda = JDABuilder.createDefault(token)
                .setAudioModuleConfig(audioConfig)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(commandManager, messageListener, voiceListener)
                .build();

        CalendarEventDispatcher calendarEventDispatcher = new CalendarEventDispatcher()
                .addHandler(new BirthdayCalendarEventHandler(calendarDataRepository, jda));
        CalendarCheckerService calendarCheckerService = new CalendarCheckerService(calendarDataRepository, calendarEventDispatcher);

        try {
            jda.awaitReady();
            commandManager.registerCommands(jda.updateCommands());
            calendarCheckerService.start();
            log.info("Bot is online and ready!");
        }
        catch (InterruptedException e){
            log.error("The bot's startup was interrupted!", e);
        }
    }
}
