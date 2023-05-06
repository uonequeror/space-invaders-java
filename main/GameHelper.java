package main;
public class GameHelper {
    public GameHelper() {}
    public boolean checkAirDropCollision(int playerX, int playerY, int objectX, int objectY) {
        return (objectY + Commons.AIRDROP_HEIGHT) >= playerY
                && objectX <= ((playerX + 45) + Commons.PLAYER_WIDTH)
                && objectX >=  playerX - 45;
    }
    public boolean checkShotCollision(int shotX, int shotY, int alienX, int alienY, int ammoType) {
        if (ammoType == 1 || ammoType == 3) {
            return shotX >= (alienX)
                    && shotX <= (alienX + Commons.ALIEN_WIDTH)
                    && shotY >= (alienY)
                    && shotY <= (alienY + Commons.ALIEN_HEIGHT);
        } else if (ammoType == 2) {
            return shotY <= alienY + Commons.ALIEN_HEIGHT;
        }
        return false;
    }
}
