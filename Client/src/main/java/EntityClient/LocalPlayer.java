package EntityClient;

import ConnectionClient.ConnectionClient;
import KeyManager.KeyManager;
import Packet.Position.NewEntityState;
import Packet.Position.PosRequest;

public class LocalPlayer extends ChangingEntity {
    private PosRequest prevRequest;

    public LocalPlayer(NewEntityState entityState) {
        super(entityState);
    }

    public void sendInput() {
        PosRequest request = createPosRequest();
        if (hasUserInput(request) || hasMovementInputChanged(request) || request.isKillKey()) {
            ConnectionClient.sendUDP(request);
        }
        this.prevRequest = request;
    }

    private boolean hasMovementInputChanged(PosRequest posRequest) {
        if (prevRequest == null) return  true;
        return !(posRequest.isUp() == prevRequest.isUp() &&
                posRequest.isRight() == prevRequest.isRight() &&
                posRequest.isDown() == prevRequest.isDown() &&
                posRequest.isLeft() == prevRequest.isLeft());
    }

    private PosRequest createPosRequest() {
        PosRequest request = new PosRequest();
        checkHorizontalMovement(request);
        checkVerticalMovement(request);
        checkSpecialMoves(request);
        return request;
    }

    private void checkSpecialMoves(PosRequest posRequest){
        if (KeyManager.isKillKeyPressed()){
            posRequest.setKillKey(true);
        }
    }

    private void checkVerticalMovement(PosRequest request) {
        if (KeyManager.isUpKeyPressed()) {
            request.setUp(true);
        } else if (KeyManager.isDownKeyPressed()) {
            request.setDown(true);
        }
    }

    private void checkHorizontalMovement(PosRequest request) {
        if (KeyManager.isRightKeyPressed()) {
            request.setRight(true);
        } else if (KeyManager.isLeftKeyPressed()) {
            request.setLeft(true);
        }
    }

    private boolean hasUserInput(PosRequest request) {
        return request.isDown() || request.isLeft() ||
                request.isUp() || request.isRight();
    }


}
