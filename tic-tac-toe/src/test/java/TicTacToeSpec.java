import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/**
 * 测试方法关键词
 * tip : given 前提条件
 *       when 操作
 *       then 预期结果
 * @author hc
 * @version 1.0.0
 * @date 2020/1/3
 */
public class TicTacToeSpec {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private TicTacToe ticTacToe;

    @Before
    public void before(){
        ticTacToe = new TicTacToe();
    }

    /**
     * 需求1：当棋子超出X轴边界时，抛出RuntimeException
     */
    @Test
    public void whenXOutsideBoardThenRuntimeException(){
        exception.expect(RuntimeException.class);
        ticTacToe.play(5,2);
    }

    /**
     * 需求1：当棋子超出Y轴边界时，抛出RuntimeException
     */
    @Test
    public void whenYOutsideBoardThenRuntimeException(){
        exception.expect(RuntimeException.class);
        ticTacToe.play(2,5);
    }

    /**
     * 需求1：如果棋子放在已有棋子的地方，就引发RuntimeException异常
     */
    @Test
    public void whenOccupiedThenRuntimeException(){
        ticTacToe.play(1,1);
        exception.expect(RuntimeException.class);
        ticTacToe.play(1,1);
    }

    /**
     * 需求2： 玩家X先下
     */
    @Test
    public void givenFirstTurnWhenNextPlayerThenX(){
        assertEquals('X',ticTacToe.nextPlayer());
    }

    /**
     * 需求2： 上次是X下的，接下来轮到O下
     */
    @Test
    public void givenLastTurnWasXWhenNextPlayerThenO(){
        ticTacToe.play(1,1);
        assertEquals('O',ticTacToe.nextPlayer());
    }

    /**
     * 需求2： 上次是X下的，接下来轮到O下
     * 编写测试后，如果这个测试在没有编写任何实现代码的情况下也能通过，就应将其删除。因为它毫无作用
     */
    @Test
    public void givenLastTurnWasOWhenNextPlayerThenX(){
        ticTacToe.play(1,1);
        ticTacToe.play(2,2);
        assertEquals('X',ticTacToe.nextPlayer());
    }

    /**
     * 需求3： 没有赢家
     */
    @Test
    public void whenPlayThenNoWinner(){
        String actual = ticTacToe.play(1,1);
        assertEquals("没有赢家",actual);
    }

    /**
     * 需求3： 三个自己的标记水平连接起来的赢
     */
    @Test
    public void whenPlayAndWholeHorizontalLineThenWinner(){
        ticTacToe.play(1,1); // X
        ticTacToe.play(1,2); // O
        ticTacToe.play(2,1); // X
        ticTacToe.play(2,2); // O
        String actualWinner = ticTacToe.play(3,1); // X
        assertEquals("X赢得比赛",actualWinner);
    }

    /**
     * 需求3： 三个自己的标记垂直连接起来的赢
     */
    @Test
    public void whenPlayAndWholeVerticalLineThenWinner(){
        ticTacToe.play(2,1); // X
        ticTacToe.play(1,1); // O
        ticTacToe.play(3,1); // X
        ticTacToe.play(1,2); // O
        ticTacToe.play(2,2); // X
        String actualWinner = ticTacToe.play(1,3); // O
        assertEquals("O赢得比赛",actualWinner);
    }

    /**
     * 需求3： 三个自己的标记左上角到右下角的整条对角线连接就赢了
     */
    @Test
    public void whenPlayAndTopBottomDiagonalLineThenWinner(){
        ticTacToe.play(1,1); // X
        ticTacToe.play(1,2); // O
        ticTacToe.play(2,2); // X
        ticTacToe.play(1,3); // O
        String actualWinner = ticTacToe.play(3, 3); // X
        assertEquals("X赢得比赛",actualWinner);
    }

    /**
     * 需求3： 三个自己的标记左上角到右下角的整条对角线连接就赢了
     */
    @Test
    public void whenPlayAndBottomTopDiagonalLineThenWinner(){
        ticTacToe.play(1,3); // X
        ticTacToe.play(1,2); // O
        ticTacToe.play(2,2); // X
        ticTacToe.play(2,3); // O
        String actualWinner = ticTacToe.play(3, 1); // X
        assertEquals("X赢得比赛",actualWinner);
    }

    /**
     * 需求3： 平局检测
     */
    @Test
    public void whenAllBoxesAreFilledThenDraw(){
        ticTacToe.play(1,1);
        ticTacToe.play(1,2);
        ticTacToe.play(1,3);
        ticTacToe.play(2,1);
        ticTacToe.play(2,3);
        ticTacToe.play(2,2);
        ticTacToe.play(3,1);
        ticTacToe.play(3,3);
        String actualWinner = ticTacToe.play(3, 2);
        assertEquals("平局",actualWinner);
    }
}
