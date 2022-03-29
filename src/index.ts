/**
 * this code will be run from within the tic.html page
 *
 * it has access to the DOM of the web page
 */

import express from 'express'
import exphbs from 'express-handlebars'
import { GameFrameworkImpl } from './core/frameworkimpl'
import { loadPlugins } from './pluginloader'
import { renderPage } from './ui'

console.log('starting server')
const app = express()
const port = 8080

// setting up to use handlebars for templates by default
app.engine('hbs', exphbs({
  defaultLayout: 'game_template',
  extname: '.hbs'
}))
app.set('view engine', 'hbs')

const PLUGIN_DIR = 'plugins'

// game always holds the current version of the game
// updates on actions and when starting a new game
const framework = new GameFrameworkImpl()
const pluginsPromise = loadPlugins(PLUGIN_DIR)
pluginsPromise.then(ps =>
  ps.forEach(p => {
    console.log('Registering plugin ' + p.getGameName())
    framework.registerPlugin(p)
  })).catch(e => console.error(`Failed to load plugins: ${e}`))

app.get('/plugin', (req, res) => {
  if (req.query.i) { framework.startNewGame(parseInt(req.query.i as string)) }
  renderPage(framework, res)
})

app.get('/play', (req, res) => {
  if (req.query.x && req.query.y) { framework.playMove(parseInt(req.query.x as string), parseInt(req.query.y as string)) }
  renderPage(framework, res)
})

app.get('/undo', (req, res) => {
  // ...
  renderPage(framework, res)
})

app.get('/', (req, res) => {
  renderPage(framework, res)
})

// start the Express server
app.listen(port, () => {
  console.log(`server started at http://localhost:${port}`)
})
