package EntityClient;

import Camera.Camera;
import ConnectionClient.ConnectionClient;
import KeyManager.KeyManager;
import Packet.EntityState.NewAnimatedEntityState;
import Packet.Position.InputRequest;
import Screen.GameScreen;
import Screen.ScreenManager;
import javafx.scene.canvas.GraphicsContext;

public class LocalPlayer extends Entity {
    private InputRequest prevRequest;
    private boolean scrollingEnabled = false;

    public LocalPlayer(NewAnimatedEntityState entityState) {
        super(entityState);
    }

    public void sendInput() {
        InputRequest request = createPosRequest();
        if (hasUserInput(request) || hasMovementInputChanged(request)) {
            ConnectionClient.sendUDP(request);
        }
        this.prevRequest = request;
    }

    private boolean hasMovementInputChanged(InputRequest inputRequest) {
        if (prevRequest == null) return  true;
        return !(inputRequest.isUp() == prevRequest.isUp() &&
                inputRequest.isRight() == prevRequest.isRight() &&
                inputRequest.isDown() == prevRequest.isDown() &&
                inputRequest.isLeft() == prevRequest.isLeft());
    }

    private InputRequest createPosRequest() {
        InputRequest request = new InputRequest();
        checkHorizontalMovement(request);
        checkVerticalMovement(request);
        checkSpecialMoves(request);
        return request;
    }

    private void checkSpecialMoves(InputRequest inputRequest){
        if (KeyManager.isKillKeyPressed()){
            inputRequest.setKillKey(true);
        }
        if (KeyManager.isTaskKeyPressed()){
            inputRequest.setTaskKey(true);
        }
        if (KeyManager.isReportKeyPressed()){
            inputRequest.setReportKey(true);
        }
        if (KeyManager.isEmergencyMeetingKeyPressed()){
            inputRequest.setEmergencyMeetingKey(true);
        }
    }

    private void checkVerticalMovement(InputRequest request) {
        if (KeyManager.isUpKeyPressed()) {
            request.setUp(true);
        } else if (KeyManager.isDownKeyPressed()) {
            request.setDown(true);
        }
    }

    private void checkHorizontalMovement(InputRequest request) {
        if (KeyManager.isRightKeyPressed()) {
            request.setRight(true);
        } else if (KeyManager.isLeftKeyPressed()) {
            request.setLeft(true);
        }
    }

    private boolean hasUserInput(InputRequest request) {
        return request.isDown() || request.isLeft() ||
                request.isUp() || request.isRight() ||
                request.isKillKey() || request.isTaskKey()||
                request.isReportKey() || request.isEmergencyMeetingKey();
    }


    @Override
    public void render(GraphicsContext gc, Camera camera) {
        if (scrollingEnabled) {
            ScreenManager.getScreen(GameScreen.class).getCamera().centreOnEntity(this);
        }
        super.render(gc, camera);
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }
}
