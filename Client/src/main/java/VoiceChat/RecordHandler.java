package VoiceChat;

import javax.sound.sampled.AudioFormat;

public class RecordHandler {

    private Speaker speaker;
    private Microphone mic;
    private boolean on = false;

    public void startRecording() {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        this.speaker = new Speaker(format);
        this.mic = new Microphone();
        mic.start(format);
        Thread micThread = new Thread(mic);
        micThread.start();
    }



    public void produceSound(byte [] soundData){
        if (on) {
            this.speaker.output(soundData);
        }
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
        mic.setOn(on);

    }
}


