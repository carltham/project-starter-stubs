
rm -rf npm-dist
rsync -avm --include='src/**.js' --include 'src/css/**.css' --include='package.json' --include='Readme.md' --exclude 'node_modules' -f 'hide,! */' . npm-dist
