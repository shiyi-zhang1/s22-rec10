import { GameFrameworkImpl } from './core/frameworkimpl'
import { Response } from 'express-serve-static-core'

/**
 * creates the data to fill into the template
 */
function genPage (framework: GameFrameworkImpl): any {
  // console.log("update page")

  interface PluginEntry { name: string, link: string }
  interface Cell { text: string | null, clazz: string, link: string }

  const cells: Cell[] = []
  const width = framework.getGridWidth()
  for (let x = 0; x < width; x++) {
    for (let y = 0; y < framework.getGridHeight(); y++) {
      const cell: Cell = {
        text: framework.getSquare(x, y),
        link: `/play?x=${x}&y=${y}`,
        clazz: framework.isSquarePlayable(x, y)
      }
      cells.push(cell)
    }
  }

  const plugins: PluginEntry[] = []
  const pluginNames = framework.getRegisteredPluginName()
  for (let i = 0; i < pluginNames.length; i++) {
    plugins.push({ name: pluginNames[i], link: '/plugin?i=' + i })
  }
  const numColStyle = Array(framework.getGridWidth()).fill('auto').join(' ')

  return {
    name: framework.getGameName(),
    footer: framework.getFooter(),
    cells: cells,
    plugins: plugins,
    numColStyle: numColStyle,
    currentPlayer: framework.getCurrentPlayerName(),
    gameOverMsg: framework.getGameOverMsg()
  }
}

function renderPage (framework: GameFrameworkImpl, res: Response<any, Record<string, any>, number>): void {
  res.render('main', genPage(framework))
}

export { renderPage }
