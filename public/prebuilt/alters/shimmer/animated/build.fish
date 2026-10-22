#!/usr/bin/env fish

set name $argv[1]

ffmpeg -y -framerate 30 -i $name/%02d.png -vf palettegen palette.png
ffmpeg -y -framerate 30 -i $name/%02d.png -i palette.png -filter_complex paletteuse $name.gif
ffmpeg -y -framerate 30 -i $name/%02d.png -lossless 1 -loop 0 $name.webp
rm palette.png
