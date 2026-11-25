package game.items;

import game.gameControll.GamePanel;
import game.gameControll.ImageId;
import game.gameControll.screen.GameOver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClockTest {

    @Mock GamePanel gp;
    @Mock GameOver  gameOver;

    private Clock clock;

    @BeforeEach
    void setUp() {

        when(gp.getGameOver()).thenReturn(gameOver);

        clock = new Clock(ImageId.CLOCK, 2, gp);
    }

    @Test
    void getAndSetRemainingSeconds() {
        assertEquals(120, clock.getRemainingSeconds());

        clock.setRemainingSeconds(45);
        assertEquals(45, clock.getRemainingSeconds());
    }

    @Test
    void decreaseSeconds_subtractsCorrectly() {
        clock.setRemainingSeconds(30);
        assertEquals(30, clock.getRemainingSeconds());

        clock.decreaseSeconds(10);
        assertEquals(20, clock.getRemainingSeconds());

        clock.decreaseSeconds(25);

        assertEquals(0, clock.getRemainingSeconds());
    }

    @Test
    void stopClock_and_update_withoutTrigger() {
        clock.start();
        clock.stopClock();
        clock.update();
        verify(gameOver, never()).show();
    }

    @Test
    void update_triggersGameOver_whenTimeRunsOut() {
        clock.setRemainingSeconds(0);
        clock.start();

        clock.update();


        verify(gameOver, times(1)).show();
        assertEquals(0, clock.getRemainingSeconds(),
                "Clock should not go negative");
    }
}
