package main;
public class GameHelper {
    public GameHelper() {}
    public boolean checkAirDropCollision(int playerX, int playerY, int objectX, int objectY) {
        return (objectY + Commons.AIRDROP_HEIGHT) >= playerY
                && objectX <= ((playerX + 45) + Commons.PLAYER_WIDTH)
                && objectX >=  playerX - 45;
    }
    public boolean checkShotCollision(int shotX, int shotY, int alienX, int alienY) {
        return shotX >= (alienX)
                && shotX <= (alienX + Commons.ALIEN_WIDTH)
                && shotY >= (alienY)
                && shotY <= (alienY + Commons.ALIEN_HEIGHT);
    }
    public boolean checkBombCollision(int bombX, int bombY, int playerX, int playerY) {
        return bombX >= (playerX)
                && bombX <= (playerX + Commons.PLAYER_WIDTH)
                && bombY >= (playerY)
                && bombY <= (playerY + Commons.PLAYER_HEIGHT);
    }
}
