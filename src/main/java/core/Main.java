package core;

import audio.GlobalAudioManager;
import audio.VoiceConnectionHandler;
import club.minnced.discord.jdave.interop.JDaveSessionFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.audio.dave.DaveSessionFactory;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;
import services.IntroService;
import services.MusicService;

public class Main {
    public static void main(String[] args) {
        String token = Dotenv.load().get("TOKEN");
        //testing
        token = Dotenv.load().get("TESTING_TOKEN");

        VoiceConnectionHandler voiceConnectionHandler = new VoiceConnectionHandler();
        GlobalAudioManager globalAudioManager = new GlobalAudioManager(voiceConnectionHandler);
        MusicService musicService = new MusicService(voiceConnectionHandler, globalAudioManager);
        IntroService introService = new IntroService(musicService);
        VoiceListener voiceListener = new VoiceListener(introService);

        CommandManager commandManager = new CommandManager(introService);
        MessageListener messageListener = new MessageListener();

        DaveSessionFactory daveSessionFactory = new JDaveSessionFactory();
        AudioModuleConfig audioConfig = new AudioModuleConfig()
                .withDaveSessionFactory(daveSessionFactory);

        JDA jda = JDABuilder.createDefault(token)
                .setAudioModuleConfig(audioConfig)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(commandManager, messageListener, voiceListener)
                .build();
        commandManager.registerCommands(jda.updateCommands());
    }
}
