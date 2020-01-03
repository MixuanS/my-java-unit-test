/**
 * @author hc
 * @version 1.0.0
 * @date 2020/1/3
 */
public class TicTacToe {
    private char[][] board = new char[3][3];
    private static int SIZE = 3;
    /**
     *  前一次下棋的玩家
     */
    private char lastPlayer;

    public String play(int x,int y){
        checkAxis(x);
        checkAxis(y);
        lastPlayer = nextPlayer();
        dropChessman(x,y);
        if (isWin()){
            return lastPlayer + "赢得比赛";
        }
        if (isDraw()){
            return "平局";
        }

        return "没有赢家";

    }

    /**
     * 是否平局
     * @return
     */
    private boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 落子
     * @param x
     * @param y
     */
    private void dropChessman(int x, int y) {
        if (board[x -1][y - 1] != 0){
            throw new RuntimeException("存在重复棋子");
        }
        board[x -1][y - 1] = lastPlayer;
    }

    /**
     * 边界检查
     * @param axis
     */
    private void checkAxis(int axis) {
        if (axis < 1 || axis > 3){
            throw new RuntimeException("边界检查不通过");
        }
    }


    /**
     * 下一位下棋的玩家
     * @return
     */
    public char nextPlayer() {
        if (lastPlayer == 'X'){
            return 'O';
        }
        return 'X';
    }

    private boolean isWin(){
        for (int index = 0; index < 3; index++) {
            // 水平检测
            if (board[0][index] == lastPlayer &&
                    board[1][index] == lastPlayer &&
                    board[2][index] == lastPlayer){
                return true;
            }
            // 垂直检测
            if (board[index][0] == lastPlayer &&
                    board[index][1] == lastPlayer &&
                    board[index][2] == lastPlayer){
                return true;
            }
        }
        // 左上角到右下角的整条对角线连接起来赢
        if (board[0][0] == lastPlayer &&
                board[1][1] == lastPlayer &&
                board[2][2] == lastPlayer){
            return true;
        }
        // 右上角到左下角的整条对角线连接起来赢
        if (board[0][2] == lastPlayer &&
                board[1][1] == lastPlayer &&
                board[2][0] == lastPlayer){
            return true;
        }
        return false;
    }
}
