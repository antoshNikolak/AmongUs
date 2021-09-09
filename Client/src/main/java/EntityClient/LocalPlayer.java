package EntityClient;

import ConnectionClient.ConnectionClient;
import KeyManager.KeyManager;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.EntityState.NewEntityState;
import Packet.Position.PosRequest;
import StartUp.AppClient;
import javafx.scene.canvas.GraphicsContext;

public class LocalPlayer extends ChangingEntity {
    private PosRequest prevRequest;
    private boolean scrollingEnabled = false;

    public LocalPlayer(NewAnimatedEntityState entityState) {
        super(entityState);
    }

    public void sendInput() {
        PosRequest request = createPosRequest();
        if (hasUserInput(request) || hasMovementInputChanged(request) || hasUserInputSpecialMove(request)) {
            ConnectionClient.sendUDP(request);
        }
        this.prevRequest = request;
    }

    private boolean hasUserInputSpecialMove(PosRequest posRequest){
        return posRequest.isKillKey() || posRequest.isTaskKey();
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
        if (KeyManager.isTaskKeyPressed()){
            posRequest.setTaskKey(true);
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

    @Override
    public void render(GraphicsContext gc) {
        if (scrollingEnabled) AppClient.currentGame.getCamera().centreOnEntity(this);
        super.render(gc);
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }
}
