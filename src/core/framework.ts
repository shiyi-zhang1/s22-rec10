
/**
 * The interface by which {@link GamePlugin} instances can directly interact
 * with the game framework.
 */
interface GameFramework {
  /**
     * Get the name of the player that currently has the move.
     */
  getCurrentPlayerName: () => string

  /**
     * Get the string associated with the grid square located at (x, y).
     *
     * @param x The x coordinate of the grid square.
     * @param y The y coordinate of the grid square.
     * @return The string associated with the grid square at location (x, y), or
     *         null if no string has been set at this location since the
     *         beginning of the game.
     */
  getSquare: (x: number, y: number) => string | null

  /**
     * Set the string associated with the grid square located at (x, y). The
     * framework will display the string, or the empty string if it is null.
     *
     * @param x The x coordinate of the grid square.
     * @param y The y coordinate of the grid square.
     * @param t The string to set at the grid square.
     */
  setSquare: (x: number, y: number, t: string | null) => void

  /**
     * Sets the text to display at the bottom of the framework's display.
     *
     * @param text The text to display.
     */
  setFooterText: (text: string) => void
}

export { GameFramework }
