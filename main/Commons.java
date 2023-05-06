package main;
public interface Commons {
    int BOARD_WIDTH = 400;
    int BOARD_HEIGHT = 400;
    int BORDER_RIGHT = 65;
    int BORDER_LEFT = -5;
    int GROUND = 354;
    int PLAYER_WIDTH = 35;
    int PLAYER_HEIGHT = 22;
    int PLAYER_MOVEMENT_SPEED = 2;
    int SHOOTING_SPEED = 5;
    int ALIEN_HEIGHT = 50;
    int ALIEN_WIDTH = 50;
    int BOMB_HEIGHT = 5;
    int ALIEN_INIT_X = 150;
    int ALIEN_INIT_Y = 35;
    int ALIENS_GO_DOWN = 7;
    int NUMBER_OF_ALIENS_TO_DESTROY = 20;
    int CHANCE_ALIEN_TO_BOMB = 5;
    int DELAY = 15;
    int AIRDROP_HEIGHT = 64;
    int POINTS_TO_INIT_AIRDROP = 1500;
    int KILLS_RESET_COUNTER = NUMBER_OF_ALIENS_TO_DESTROY * 3;
}
