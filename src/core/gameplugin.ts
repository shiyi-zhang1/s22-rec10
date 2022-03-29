import { GameFramework } from './framework'

/**
 * The game plug-in interface that plug-ins use to implement and register games
 * with the {@link GameFramework}.  The type parameter, {@code P}, allows an
 * arbitrary type to represent a player.
 */
interface GamePlugin {

  /**
     * Gets the name of the plug-in game.
     */
  getGameName: () => string

  /**
     * Gets the width (in squares) of the plug-in game's grid.
     */
  getGridWidth: () => number

  /**
     * Returns the width (in squares) of the plug-in game's grid.
     */
  getGridHeight: () => number

  /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link GameFramework} instance with which the plug-in
     *                  was registered.
     */
  onRegister: (framework: GameFramework) => void

  /**
     * Called when a new game is about to begin.
     */
  onNewGame: () => void

  /**
     * Called when a new move is about to begin (i.e. immediately after
     * {@link #onNewGame()}, and before each subsequent move in the game). This
     * method will only be called if the last move was valid and did not end the
     * game.
     */
  onNewMove: () => void

  /**
     * Returns true if a move at location (x, y) is allowed (based on the game's
     * current state). Returns false otherwise.
     */
  isMoveValid: (x: number, y: number) => boolean

  /**
     * Returns true if the current move is over (based on the game's current
     * state). Returns false otherwise.
     */
  isMoveOver: () => boolean

  /**
     * Called when a move has been played.
     *
     * @param x The x coordinate of the grid square that has been played.
     * @param y The y coordinate of the grid square that has been played.
     */
  onMovePlayed: (x: number, y: number) => void

  /**
     * Returns true if the game is over (based on the game's current state).
     * Returns false otherwise.
     */
  isGameOver: () => boolean

  /**
     * Returns the message to display when this game is deemed to be over. This
     * method is called immediately after {@link #isGameOver()} returns true.
     */
  getGameOverMessage: () => string

  /**
     * Called when the plugin is closed to allow the plugin to do any final cleanup.
     */
  onGameClosed: () => void

  /**
     * Returns the player whose turn it is to play.
     */
  currentPlayer: () => string
}

export { GamePlugin }
