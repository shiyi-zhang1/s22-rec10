import { GameFramework } from './framework'
import { GamePlugin } from './gameplugin'

/**
 * The framework core implementation.
 */
class GameFrameworkImpl implements GameFramework {
  readonly #NO_GAME_NAME = 'A game framework'
  readonly #DEFAULT_FOOTER = 'No ongoing game'
  readonly #DEFAULT_HEIGHT = 1
  readonly #DEFAULT_WIDTH = 1
    #gameGrid: Array<Array<string | null>>
    #currentPlugin: GamePlugin | null = null
    #footer: string
    #registeredPlugins: GamePlugin[] = []

    constructor () {
      this.#gameGrid = this.#initGrid(this.#DEFAULT_WIDTH, this.#DEFAULT_HEIGHT)
      this.#footer = this.#DEFAULT_FOOTER
      this.#registeredPlugins = []
    }

    #initGrid (width: number, height: number): Array<Array<string | null>> {
      return new Array(width).fill([]).map((a) => new Array(height).fill(null))
    }

    /**
     * Registers a new {@link GamePlugin} with the game framework
     */
    registerPlugin (plugin: GamePlugin): void {
      plugin.onRegister(this)
      this.#registeredPlugins.push(plugin)
    }

    /**
     * Starts a new game for the provided {@link GamePlugin}
     */
    startNewGame (pluginIdx: number): void {
      const plugin = this.#registeredPlugins[pluginIdx]
      const width = plugin.getGridWidth()
      const height = plugin.getGridHeight()

      if (this.#currentPlugin !== plugin) {
        // If this.#currentPlugin != plugin, then we are switching to a new
        // plug-in's game. This requires a bit more work to setup since
        // different games can have different grid widths and grid heights.
        if (this.#currentPlugin != null) { this.#currentPlugin.onGameClosed() }
        this.#currentPlugin = plugin
        this.#gameGrid = this.#initGrid(width, height)
      }

      // Reset the game's internal state.
      for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
          this.#gameGrid[y][x] = null
        }
      }
      this.#currentPlugin.onNewGame()
      this.#currentPlugin.onNewMove()
    }

    /**
     * Performs a move a specified location
     *
     * @param x The x coordinate of the grid square.
     * @param y The y coordinate of the grid square.
     */
    playMove (x: number, y: number): void {
      if (this.#currentPlugin == null) return
      if (!this.#currentPlugin.isMoveValid(x, y)) {
        return
      }

      this.#currentPlugin.onMovePlayed(x, y)

      if (this.#currentPlugin.isGameOver()) {
        // startNewGame(this.#currentPlugin);
        return
      }

      if (this.#currentPlugin.isMoveOver()) {
        this.#currentPlugin.onNewMove()
      }
    }

    /* GameFramework methods. */

    getCurrentPlayerName (): string {
      if (this.#currentPlugin != null) {
        return this.#currentPlugin.currentPlayer()
      } else {
        return ''
      }
    }

    getSquare (x: number, y: number): string | null {
      return this.#gameGrid[y][x]
    }

    setSquare (x: number, y: number, obj: string | null): void {
      this.#gameGrid[y][x] = obj
    }

    setFooterText (text: string): void {
      this.#footer = text
    }

    /* GameState methods: getter for Gui purposes */
    getGameName (): string {
      if (this.#currentPlugin === null) {
        return this.#NO_GAME_NAME
      } else {
        return this.#currentPlugin.getGameName()
      }
    }

    getGridWidth (): number {
      if (this.#currentPlugin === null) {
        return this.#DEFAULT_WIDTH
      } else {
        return this.#currentPlugin.getGridWidth()
      }
    }

    getGridHeight (): number {
      if (this.#currentPlugin == null) {
        return this.#DEFAULT_HEIGHT
      } else {
        return this.#currentPlugin.getGridHeight()
      }
    }

    getFooter (): string {
      return this.#footer
    }

    getRegisteredPluginName (): string[] {
      return this.#registeredPlugins.map(p => p.getGameName())
    }

    getGameOverMsg (): string | null {
      if (this.#currentPlugin == null) {
        return null
      }
      if (this.#currentPlugin.isGameOver()) {
        return this.#currentPlugin.getGameOverMessage()
      } else {
        return null
      }
    }

    isSquarePlayable (x: number, y: number): string {
      if (this.#currentPlugin == null) {
        return ''
      } else if (this.#currentPlugin.isMoveValid(x, y)) {
        return 'playable'
      } else {
        return ''
      }
    }
}

export { GameFrameworkImpl }
