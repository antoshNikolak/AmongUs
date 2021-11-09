package VoiceChat;

import javax.sound.sampled.*;

public class Speaker{

    private SourceDataLine speaker;
    //    private byte[] data = new byte[512];
//    private CopyOnWriteArrayList<byte []> dataBuffer = new CopyOnWriteArrayList<>();
//
    private byte[] tempData;



    public Speaker(AudioFormat audioFormat) {
        start(audioFormat);
    }

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (tempData != null) {
//                speaker.write(tempData, 0, tempData.length);
//                tempData = null;
//            }
//        }

//        while (true) {
//            try {
//                System.out.println(dataBuffer.size());
//                speaker.write(tempData, 0, tempData.length);
//                dataBuffer.clear();
//                if (dataBuffer.size() > 0) {
//                    List<byte[]> localDataBuffer = List.copyOf(dataBuffer);//posibly trying to play to many at one time
//                    dataBuffer.clear();
//                    for (byte[] data : localDataBuffer) {
//                        speakers.write(data, 0, data.length);
//                        bytesRead += data.length;
//                        System.out.println("In for loop");
//                    }
//
//                    System.out.println("in while loop");
//
//                }
//                Thread.sleep(0);
//            }catch (InterruptedException e){
//                break;
//            }
//        }
//        speakers.drain();
//        speakers.close();


    public void output(byte [] data) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
//
//    public void addBytes(byte [] bytes){
//        this.dataBuffer.add(bytes);
////        this.dataBuffer.addAll(convertToDynamicBytes(bytes));
//    }


    public void setTempData(byte[] tempData) {
        this.tempData = tempData;
    }


    public SourceDataLine getSpeakers() {
        return speaker;
    }
}
