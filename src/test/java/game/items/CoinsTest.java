package game.items;

import game.entity.Player;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinsTest {

    @Mock GamePanel gp;
    @Mock Inventory inventory;

    private Coins coin;

    @BeforeEach
    void init() {
        lenient().when(gp.getInventory()).thenReturn(inventory);
        lenient().when(gp.getTileSize()).thenReturn(10);

        coin = new Coins(ImageId.COINS, 100, 200, gp);
    }

    @Test
    void isCloseEnough_trueAtExactCenter_falseOutsideRadius() {

        Player player = mock(Player.class);
        double coinCenterX = coin.getX() + gp.getTileSize() / 2.0;
        double coinCenterY = coin.getY() + gp.getTileSize() / 2.0;

        when(player.getCenterX()).thenReturn(coinCenterX);
        when(player.getCenterY()).thenReturn(coinCenterY);

        assertTrue(coin.isCloseEnough(player, 0));

        when(player.getCenterX()).thenReturn(coinCenterX + 100);
        assertFalse(coin.isCloseEnough(player, 5));
    }

    @Test
    void pickUp_decrementsOnGround_andIncrementsInventory() {
        assertTrue(coin.isOnGround());
        int value = coin.getValue();
        coin.pickUp();

        verify(inventory).addToCoinCounter(value);

        assertFalse(coin.isOnGround());
    }
}
