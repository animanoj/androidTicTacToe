# TicTacToe
An Android Tic Tac Toe game for single and two player matches. Grids are created programmatically to enable different size boards.

The AI for single player matches employs a Minimax algorithm with Alpha-Beta pruning to choose the best moves to play. For optimization, the recursive algorithm is memoized using Zorbist hashing.

## Bugs

- Currently, due to the memoization, the AI does not choose the best move in all playable scenarios. I speculate that this is due to the fact that alpha-beta pruning gives an upper or lower bound of the best value of a certain scenario, but does not compute the value itself.

## Next Steps

- Look into implementing different game modes, like Ultimate Tic Tac Toe: https://mathwithbaddrawings.com/2013/06/16/ultimate-tic-tac-toe/
