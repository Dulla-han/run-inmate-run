package game.entity;

import game.ability.Ability;
import game.gameControll.Controlls;
import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.gameControll.screen.RespawnMenu;
import game.items.Clock;
import game.world.RoomManager;
import game.world.Room;
import game.world.TileMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlayerTest {

    @Mock GamePanel gp;
    @Mock RoomManager roomManager;
    @Mock Room room;
    @Mock TileMap tileMap;
    @Mock Ability ability;

    @Mock RespawnMenu respawnMenu;
    @Mock Clock clock;

    private Controlls keyH;
    private Player player;

    @BeforeEach
    void setUp() {
        keyH = new Controlls();

        lenient().when(gp.getKeyH()).thenReturn(keyH);
        lenient().when(gp.getAbility()).thenReturn(ability);
        lenient().when(ability.isActive()).thenReturn(false);

        when(gp.getTileSize()).thenReturn(16);

        when(gp.getRoomManager()).thenReturn(roomManager);
        when(roomManager.getCurrentRoom()).thenReturn(room);
        when(room.getTileMap()).thenReturn(tileMap);
        when(room.getEntities()).thenReturn(Collections.emptyList());

        when(gp.getRespawnMenu()).thenReturn(respawnMenu);
        when(gp.getClock()).thenReturn(clock);

        player = new Player(gp, ImageId.PLAYER, 50, 50, 5);
    }

    @Test
    void isCloseEnough_boundaryChecks() {
        Entity other = mock(Entity.class);
        when(other.getX()).thenReturn(player.getX());
        when(other.getY()).thenReturn(player.getY());

        double dx = player.getCenterX() - (player.getX() + gp.getTileSize()/2.0);
        double dy = player.getCenterY() - (player.getY() + gp.getTileSize()/2.0);
        double neededRadius = Math.sqrt(dx*dx + dy*dy);

        assertTrue(player.isCloseEnough(other, neededRadius));

        when(other.getX()).thenReturn(player.getX() + 100);
        assertFalse(player.isCloseEnough(other, neededRadius));
    }

    @Test
    void update_movesUpWhenUpPressed_noCollision() {
        keyH.setUpPressed(true);
        player.update();

        assertEquals(45, player.getY());
        assertEquals("up", player.getDirection());
    }

    @Test
    void isCollision_detectsWall_and_noWall() {
        int nextX = 20, nextY = 20;

        when(tileMap.isSolid(anyInt(), anyInt())).thenReturn(true);
        assertTrue(player.isCollision(nextX, nextY));

        when(tileMap.isSolid(anyInt(), anyInt())).thenReturn(false);
        assertFalse(player.isCollision(nextX, nextY));
    }
}
