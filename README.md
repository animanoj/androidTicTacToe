# TicTacToe
An Android Tic Tac Toe game for single and two player matches. Supports 3x3 and 4x4 grid sizes.

The AI for single player matches employs a Minimax algorithm with Alpha-Beta pruning to choose the best moves to play. For optimization, the recursive algorithm is memoized using Zorbist hashing.

## Bugs

- Currently, due to the memoization, the AI does not choose the best move in all playable scenarios. I speculate that this is due to the fact that alpha-beta pruning gives an upper or lower bound of the best value of a certain scenario, but does not compute the value itself.
- On the 4x4 grid, the AI still takes a non-trivial amount of time to calculate its move after optimizations and depth limiting.

## Next Steps

- Create grids of any size programmatically.
- Look into implementing different game modes, like Ultimate Tic Tac Toe: https://mathwithbaddrawings.com/2013/06/16/ultimate-tic-tac-toe/
