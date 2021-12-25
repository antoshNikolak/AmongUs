package VoiceChat;

import javax.sound.sampled.*;

public class Speaker{
    private SourceDataLine speaker;

    public Speaker(AudioFormat audioFormat) {
        start(audioFormat);
    }

    public void output(byte [] data) {
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        speaker.write(data, 0, data.length);
    }

    public void start(AudioFormat format) {
        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speaker.open(format);
            speaker.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
