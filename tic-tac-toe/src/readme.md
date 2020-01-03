# “井字游戏”需求文档

“井字游戏”是两个人使用纸和笔玩的一种游戏，双方轮流在一个3×3的网格中画`X`和`O`，最先在水平、垂直或对角线上将自己的三个标记连接起来的玩家获胜



## 需求 1 落子检查

首先应该定义边界，以及将棋子放在那些地方是非法的。

可将需求分成三个测试 ： 

- 如果棋子放在超出`X`轴边界的地方，就引发`RuntimeException`异常
-  如果棋子放在超出`Y`轴边界的地方，就引发`RuntimeException`异常
- 如果棋子放在已有棋子的地方，就引发`RuntimeException`异常



### 测试1

首先检查棋子是否放在3×3棋盘的边界内

```java
/**
 * 需求1：当棋子超出X轴边界时，抛出RuntimeException
 */
@Test
public void whenXOutsideBoardThenRuntimeException(){
    exception.expect(RuntimeException.class);
    ticTacToe.play(5,2);
}
```



### 实现1

```java
public void play(int x,int y){
	if(x < 1 || x >3 ){
		throw new RuntimeException("X is outside board");
	}
}
```





### 测试2

```java
/**
 * 需求1：当棋子超出Y轴边界时，抛出RuntimeException
 */
@Test
public void whenYOutsideBoardThenRuntimeException(){
	exception.expect(RuntimeException.class);
	ticTacToe.play(2,5);
}
```



### 实现2

```java
public void play(int x,int y){
	if(x < 1 || x > 3 ){
		throw new RuntimeException("X边界检查不通过");
	}else if(y < 1 || y > 3){
		throw new RuntimeExceptioin("Y边界检查不通过");
	}
}
```



### 测试3

```java
/**
 * 需求1：如果棋子放在已有棋子的地方，就引发RuntimeException异常
 */
@Test
public void whenOccupiedThenRuntimeException(){
	ticTacToe.play(1,1);
	exception.expect(RuntimeException.class);
	ticTacToe.play(1,1);
}
```



### 实现3

```java
private char[][] board = new char[3][3];
public void play(int x,int y){
	if(x < 1 || x > 3 ){
		throw new RuntimeException("X边界检查不通过");
	}else if(y < 1 || y > 3){
		throw new RuntimeExceptioin("Y边界检查不通过");
	}
    if (board[x][y] != 0){
		throw new RuntimeException("存在重复棋子");
    }
    board[x][y] = 'X';
}
```



### 重构

以上代码虽然能满足测试指定需求，但是可读性差。应重构这个方法，将该方法拆分（`Extract Method`）。重构后的代码如下：

```java
private char[][] board = new char[3][3];

public void play(int x,int y){
    checkAxis(x);
    checkAxis(y);
    dropChessman(x,y);

}

/**
 * 落子
 * @param x
 * @param y
 */
private void dropChessman(int x, int y) {
    if (board[x][y] != 0){
        throw new RuntimeException("存在重复棋子");
    }
    board[x][y] = 'X';
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
```





## 需求2 玩家轮流落子检查

将这个需求分成三个测试：

- 玩家`X`先下
- 如果上次是`X`下的，接下来轮到`O`下
- 如果上次是`O`下的，接下来轮到`X`下



###  测试1

玩家X先下

```java
/**
 * 需求2： 玩家X先下
 */
@Test
public void givenFirstTurnWhenNextPlayerThenX(){
    assertEquals('X',ticTacToe.nextPlayer());
}
```



### 实现1

```java
public char nextPlayer(){
	return 'X';
}
```



### 测试2

如果上次是`X`下的，接下来轮到`O`下

```java
/**
 * 需求2： 上次是X下的，接下来轮到O下
 */
@Test
public void givenLastTurnWasXWhenNextPlayerThenO(){
    ticTacToe.play(1,1);
    assertEquals('O',ticTacToe.nextPlayer());
}
```



### 实现2

为跟踪接下来该谁下，需要存储前一次下棋的玩家

```java
private char[][] board = new char[3][3];
public void play(int x,int y){
    checkAxis(x);
    checkAxis(y);
    dropChessman(x,y);
    lastPlayer = nextPlayer();
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
```



### 测试3 不该存在的测试

检查如果上次是`O`下的，接下来轮到`X`下

```java
/**
 * 需求2： 上次是X下的，接下来轮到O下
 */
@Test
public void givenLastTurnWasOWhenNextPlayerThenX(){
	ticTacToe.play(1,1);
	ticTacToe.play(2,2);
	assertEquals('X',ticTacToe.nextPlayer());
}
```

编写测试后，如果这个测试在没有编写任何实现代码的情况下也能通过，就应将其删除。因为它毫无作用



## 需求3 胜利条件测试

检查所有可能获胜的情况，只要满足其中一个，就宣布玩家胜利

- 三个自己的标记水平连接起来
- 三个自己的标记垂直连接起来
- 三个自己的标记左上角到右下角的整条对角线连接起来
- 三个自己的标记右上角到左下角的整条对角线连接起来
- 平局



### 测试1

默认没有赢家

```java
/**
 * 需求3： 没有赢家
 */
@Test
public void whenPlayThenNoWinner(){
    String actual = ticTacToe.play(1,1);
    assertEquals("没有赢家",actual);
}
```



### 实现1

```java
public String play(int x,int y){
	checkAxis(x);
	checkAxis(y);
	lastPlayer = nextPlayer();
	dropChessman(x,y);
	return "没有赢家";
}
```



### 测试2

```java
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
```



### 实现2

```java
public String play(int x,int y){
    checkAxis(x);
    checkAxis(y);
    lastPlayer = nextPlayer();
    dropChessman(x,y);
    for (int index = 0; index < 3; index++) {
        // 水平检测
        if (board[0][index] == lastPlayer &&
            board[1][index] == lastPlayer &&
            board[2][index] == lastPlayer){
            return lastPlayer + "赢得比赛";
        }
    }
    return "没有赢家";
}
```



### 测试3

```java
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
```



### 实现3

```java
public String play(int x,int y){
	checkAxis(x);
	checkAxis(y);
	lastPlayer = nextPlayer();
	dropChessman(x,y);
	for (int index = 0; index < 3; index++) {
		// 水平检测
		if (board[0][index] == lastPlayer &&
			board[1][index] == lastPlayer &&
			board[2][index] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
		// 垂直检测
		if (board[index][0] == lastPlayer &&
			board[index][1] == lastPlayer &&
			board[index][2] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
	}
	return "没有赢家";
}
```



### 测试4

```
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
```



### 实现4

这里只涉及到一条线，因此可直接检查，无需使用循环

```java
public String play(int x,int y){
	checkAxis(x);
	checkAxis(y);
	lastPlayer = nextPlayer();
	dropChessman(x,y);
	for (int index = 0; index < 3; index++) {
		// 水平检测
		if (board[0][index] == lastPlayer &&
			board[1][index] == lastPlayer &&
			board[2][index] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
		// 垂直检测
		if (board[index][0] == lastPlayer &&
			board[index][1] == lastPlayer &&
			board[index][2] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
	}
	// 左上角到右下角的整条对角线连接起来赢
	if (board[0][0] == lastPlayer &&
		board[1][1] == lastPlayer &&
		board[2][2] == lastPlayer){
		return lastPlayer + "赢得比赛";
	}
	return "没有赢家";
}
```



### 测试5

```java
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
```



### 实现5

与前一个几乎完全相同

```java
public String play(int x,int y){
	checkAxis(x);
	checkAxis(y);
	lastPlayer = nextPlayer();
	dropChessman(x,y);
	for (int index = 0; index < 3; index++) {
		// 水平检测
		if (board[0][index] == lastPlayer &&
			board[1][index] == lastPlayer &&
			board[2][index] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
		// 垂直检测
		if (board[index][0] == lastPlayer &&
			board[index][1] == lastPlayer &&
			board[index][2] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
	}
	// 左上角到右下角的整条对角线连接起来赢
	if (board[0][0] == lastPlayer &&
		board[1][1] == lastPlayer &&
		board[2][2] == lastPlayer){
		return lastPlayer + "赢得比赛";
	}
	// 右上角到左下角的整条对角线连接起来赢
	if (board[0][2] == lastPlayer &&
		board[1][1] == lastPlayer &&
		board[2][0] == lastPlayer){
		return lastPlayer + "赢得比赛";
	}
	return "没有赢家";
}
```



### 测试6 平局

```java
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
```



### 实现6

```java
public String play(int x,int y){
	checkAxis(x);
	checkAxis(y);
	lastPlayer = nextPlayer();
	dropChessman(x,y);
	for (int index = 0; index < 3; index++) {
		// 水平检测
		if (board[0][index] == lastPlayer &&
			board[1][index] == lastPlayer &&
			board[2][index] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
		// 垂直检测
		if (board[index][0] == lastPlayer &&
			board[index][1] == lastPlayer &&
			board[index][2] == lastPlayer){
			return lastPlayer + "赢得比赛";
		}
	}
	// 左上角到右下角的整条对角线连接起来赢
	if (board[0][0] == lastPlayer &&
		board[1][1] == lastPlayer &&
		board[2][2] == lastPlayer){
		return lastPlayer + "赢得比赛";
	}
	// 右上角到左下角的整条对角线连接起来赢
	if (board[0][2] == lastPlayer &&
		board[1][1] == lastPlayer &&
		board[2][0] == lastPlayer){
		return lastPlayer + "赢得比赛";
	}
	if(isDraw()){
		return "平局";
	}
    
	return "没有赢家";
}

/**
 * 是否平局
 * @return
 */
public boolean isDraw(){
	for (int i = 0; i < 3; i++) {
   		for (int j = 0; j < 3; j++) {
    		if (board[i][j] == 0){
    			return false;
    		}
    	}
    }
    return true;
}
```



### 重构

以上代码还有不少重构空间，可以试试

以上完整代码放在GitHub上



## 小结

​		开发软件的最简单方式是将其分成小块；设计方案脱胎于测试，而不是预先采用复杂的方法进行制定；先编写测试并确定未通过后，在着手编写实现代码；

​		编写实现代码时，力图使其尽可能简单，只要能让测试通过即可，而不试图提供完美的解决方案；不断重复这个过程，直到认为需要对代码进行重构为止；

​		重构时不能引入新的功能（即不改变应用程序的行为），而只是对代码进行修改，使其更容易理解和维护。