package VoiceChat;

import ConnectionClient.ConnectionClient;
import Packet.Sound.Sound;
import javax.sound.sampled.*;
import java.util.Arrays;

public class Microphone implements Runnable {
    private TargetDataLine microphone;
    private boolean on = true;

    @Override
    public void run() {
        try {
            byte[] data = new byte[microphone.getBufferSize() / 5];
            while (on) {
                    int numBytesRead = microphone.read(data, 0, 1024);
                    ConnectionClient.sendUDP(new Sound(Arrays.copyOfRange(data, 0, numBytesRead)));
//                    Thread.sleep(0);
//                } catch (InterruptedException e) {
//                    break;
//                }
            }
            microphone.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(AudioFormat format) {
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getBufferSize() {
        return microphone.getBufferSize();
    }
}
