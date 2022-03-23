## Set Up Backend Server ##
Either run the Java backend by using your IDE or by typing 

```
mvn exec:exec
```

in the tictactoe folder

## Set Up Frontend Server ##
In the frontend folder, run

```
npm install
npm run compile
npm run start
```

This will start a server at http://localhost:3000

### Fixing Handlebars ###
There is an error with Handlebars working with Webpack.  There is a simple fix for this error, however.  After you run ```npm install```, this creates a node_modules folder within the frontend folder.  Then, in that folder, find the Handlebars folder, and go to its package.json.

You will find in this package.json, there is a section called ```browser```.  Add to this dictionary the line ```"fs": false```.  See the write up for a picture of what this should look like.