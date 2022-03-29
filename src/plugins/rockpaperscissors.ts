import { GameFramework } from '../core/framework'
import { GamePlugin } from '../core/gameplugin'

/**
 * An instance of this class represents a move (rock, paper, or scissors) in a rock-paper-scissors game.
 *
 * This also contains several convenience methods for determining the winner of two moves and for
 * playing rock-paper-scissors against a computer opponent.
 */

enum State { ROCK, PAPER, SCISSORS }
const stateCount = Object.keys(State).length

/** Represents a result of a rock-paper-scissors game between two players. */
enum Result { WIN, LOSE, TIE }

/**
 * Returns the winner of this rock-paper-scissors move played against the given opponent's move.
 *
 * @throws NullPointerException if {@code opponent} is null.
 */
function winner (thisState: State, opponentState: State): Result {
  if (thisState === opponentState) return Result.TIE
  if (((thisState + 1) % stateCount) === opponentState) return Result.LOSE
  return Result.WIN
}

/**
 *  Returns a uniformly random rock-paper-scissors move, for use in simulating a game against a
 * computer opponent.
 */
function randomMove (): State {
  return Math.floor(Math.random() * stateCount)
}

/**
 * Returns the result of the given move played against a uniformly random rock-paper-scissors move,
 * for use in playing a game against a computer opponent.
 */
function play (move: State): Result {
  return winner(move, randomMove())
}

const GAME_START_FOOTER = 'You are playing Rocks Paper Scissors with a computer!'
const PLAYER_WON_MSG = 'You won!'
const COMPUTER_WON_MSG = 'The computer won!'
const GAME_TIED_MSG = 'The game ended in a tie.'

function init (): GamePlugin {
  let framework: GameFramework | null = null
  let gameResult: Result | null = null
  return {
    getGameName () { return 'Rocks Paper Scissors' },

    getGridWidth (): number { return 3 },

    getGridHeight (): number { return 1 },
    onRegister (f: GameFramework): void { framework = f },
    onNewGame (): void {
      if (framework === null) return
      framework.setFooterText(GAME_START_FOOTER)
      framework.setSquare(State.ROCK, 0, 'Rock')
      framework.setSquare(State.PAPER, 0, 'Paper')
      framework.setSquare(State.SCISSORS, 0, 'Scissors')
      gameResult = null
    },
    onNewMove (): void { }, // Nothing to do here.
    isMoveValid (x: number, y: number): boolean {
      return true // Impossible to make an invalid move.
    },
    isMoveOver (): boolean {
      return gameResult !== null
    },
    onMovePlayed (x: number, y: number): void {
      gameResult = play(x)
    },
    isGameOver (): boolean {
      return gameResult !== null
    },
    getGameOverMessage (): string {
      switch (gameResult) {
        case Result.TIE: return GAME_TIED_MSG
        case Result.WIN: return PLAYER_WON_MSG
        case Result.LOSE: return COMPUTER_WON_MSG
        default: throw new Error('Called getGameOverMessage with incomplete game')
      }
    },
    onGameClosed (): void { }, // Nothing to do here.
    currentPlayer (): string { return 'Human' }
  }
}

export { init }
