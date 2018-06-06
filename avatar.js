
function createElement(html) {
    var el = document.createElement('template');
    el.innerHTML = html;
    return el.content.childNodes[0];
}

function createOrbitingBody(index, distance, size, angle, skew) {
    var orbitDegreeLength = (Math.PI * 2 * distance) / 360;
    var tag = 
        '<g id="orbit' + index + '">\n' + 
        '    <circle id="orbit' + index + '-circle" cx="0" cy="0" r="' + distance + '" fill="none"\n' +
        '        stroke-dasharray="' + (orbitDegreeLength*2) + '" stroke-dashoffset="' + orbitDegreeLength + '" stroke="#FFF" stroke-width="2"/>\n' +


        '    <g transform="rotate(' + (angle*360) + ')">\n' +
        '        <animateTransform attributeName="transform" type="rotate" from="' + (angle*360) + '" to="' + (angle*360 + 360) + '" dur="5s" repeatCount="indefinite"/>\n' +
        '        <g transform="translate(' + distance + ' 0)">\n' +
        '            <g transform="rotate(' + -(angle*360) + ')">\n' +
        '                <animateTransform attributeName="transform" type="rotate" from="' + -(angle*360) + '" to="' + -(angle*360 + 360) + '" dur="5s" repeatCount="indefinite"/>\n' +
        '                <circle id="orbit' + index + '-body" cx="0" cy="0" r="' + size + '" fill="#FFF" transform="scale(1 4.5)">\n' +
        '            </g>\n' +
        '        </g>\n' +
        '    </g>\n' +
        '    </circle>\n' + 
        '</g>\n';
    document.write(tag);
}

function svgToImage(svg, imageWidth, imageHeight, callback) {
    var canvas = document.createElement('canvas');
    canvas.width  = imageWidth;
    canvas.height = imageHeight;
    var ctx = canvas.getContext('2d');
    
    var img = new Image();
    img.onload = function() {
        ctx.drawImage(img, 0, 0);
        callback(canvas, ctx)
    }

    img.src = "data:image/svg+xml," + encodeURIComponent(svg.outerHTML);
}

function viewBoxSVG(svg, x, y, w, h, pixelWidth, pixelHeight) {
    var newSVG = svg.cloneNode(true)
    if(pixelWidth == null) {
        newSVG.removeAttribute('width')
    } else {
        newSVG.setAttribute('width', pixelWidth + 'px')
    }
    if(pixelHeight == null) {
        newSVG.removeAttribute('height')
    } else {
        newSVG.setAttribute('height', pixelHeight + 'px')
    }
    newSVG.setAttribute('viewBox', x + ' ' + y + ' ' + w + ' ' + h)
    return newSVG
}

function exportImage(name, svg, options) {
    var data = viewBoxSVG(svg, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight)

    var template = document.importNode(document.querySelector('#export-template').content, true)

    var exportImage = template.querySelector(".export-image");

    svgToImage(data, options.imageWidth, options.imageHeight, function(canvas, ctx) {
        exportImage.src = canvas.toDataURL("image/png");
    });

    template.querySelector('.export-svg-link-static').href = 'data:text/plain,' + encodeURIComponent(vkbeautify.xml(
        viewBoxSVG(svg, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight).outerHTML, 4
    ))

    var animatedSVG = generateAnimations(svg.cloneNode(true))
    var animatedData = viewBoxSVG(animatedSVG, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight)

    var animationPreview = animatedData.cloneNode(true)
    animationPreview.removeAttribute('width')
    animationPreview.removeAttribute('height')
    template.querySelector('.export-svg-animation-preview').appendChild(animationPreview)

    template.querySelector('.export-svg-link-animated').href = 'data:text/plain,' + encodeURIComponent(vkbeautify.xml(
        viewBoxSVG(animatedSVG, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight).outerHTML, 4
    ))

    var video = template.querySelector('.export-video');
    template.querySelector('.export-generate-video').onclick = function() {
        exportVideo(animatedData, options.imageWidth, options.imageHeight, options.framerate || 10, options.duration || 1, function(blob) {
            var url = (window.webkitURL || window.URL).createObjectURL(output);
            video.src = url
            video.removeAttribute('hidden')
        })
    }
    return template;
}

function exportVideo(animatedData, imageWidth, imageHeight, framerate, duration, callback) {
    var frame = 0
    var frameMillis = 1000/framerate
    var frameCount = duration*framerate


    var zip = new JSZip()


    var animatedWithOrigins = animatedData.cloneNode(true);
    /*animatedWithOrigins.setAttribute("width", animatedWithOrigins.getAttribute("width")/10)
    animatedWithOrigins.setAttribute("height", animatedWithOrigins.getAttribute("height")/10)*/
    var animations = animatedWithOrigins.querySelectorAll("animate, animateColor, animateMotion, animateTransform, discard, mpath, set")
    var animatedElements = new Set();

    for (var i=0, element; element = animations[i]; i++) {
        animatedElements.add(element.parentElement)
    }

    var iter = animatedElements.entries()
    for (var entry of iter) {
        var element = entry[0] // set iterator is of [item, item] to keep it similar to the map iterator
        var randomId = 'a' + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15) + 'a'; 
        var timeOrigin = createElement('<animate id="' + randomId + '" class="time-origin" begin="0s"/>');
        element.insertBefore(timeOrigin, element.firstChild);
        var children = element.children
        for (var i=0, child; child = children[i]; i++) {
            var value = 
            child.setAttribute("begin", (child.getAttribute("begin") || (randomId + ".begin")).replace("0000ms", (randomId + ".begin")))
        }
    }

    var addFrame;
    addFrame = function() {
        var clone = animatedWithOrigins.cloneNode(true);
        var timeOrigins = clone.getElementsByClassName("time-origin");
        for (var i=0, element; element = timeOrigins[i]; i++) {
            element.setAttribute("begin", '-' + frameMillis*frame + 'ms'); 
        }

        frame++;
        var persistantFrame = frame
        svgToImage(clone, imageWidth, imageHeight, function(canvas, ctx) {
            canvas.toBlob(function(blob) {
                zip.file('frame' + (persistantFrame.toString().padStart(4, '0')) + '.png', blob, { binary: true })
                var image = new Image();
                image.src = canvas.toDataURL('image/png');
            }, 'image/png')

            if(frame == frameCount) {
                zip.generateAsync({type : "blob"}).then(function(blob) {
                    saveAs(blob, "animation.zip")
                });
            } else {
                addFrame()
            }
        });
    };

    addFrame();
}

function stripAnimations(svg) {
    var wipes = svg.querySelectorAll("animate, animateColor, animateMotion, animateTransform, discard, mpath, set");
    for (var i=0, element; element = wipes[i]; i++) {
        element.outerHTML = ""
    }
    return svg;
}

function generateAnimations(svg) {
    var wipes = svg.querySelectorAll("[data-animate-wipe-duration]");
    for (var i=0, element; element = wipes[i]; i++) {
        generateWipe(element);
    }
    return svg;
}

function generateWipe(element) {
    var duration = element.getAttribute("data-animate-wipe-duration");
    var begin = element.getAttribute("data-animate-wipe-begin") || "0s";
    var phase = element.getAttribute("data-animate-wipe-phase") || 0;
    var rx = element.tagName == "ellipse" ? element.getAttribute("rx") : element.getAttribute("r");
    var ry = element.tagName == "ellipse" ? element.getAttribute("ry") : element.getAttribute("r");

    var maxRadius = Math.max(rx, ry);
    var circumference = Math.PI * 2 * (maxRadius/2); //maxRadius/2 because the actual shape is a circle half the radius with the stroke extending r/2 inward and r/2 outward
    var xScale = rx/maxRadius;
    var yScale = ry/maxRadius;

    var randomId = 'a' + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15) + 'a'; 
    var randomId2 = 'a' + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15) + 'a'; 

    var tag =
        '<circle id="' + element.id + '" cx="' + element.getAttribute('cx') + '" cy="' + element.getAttribute('cy') + '" r="' + (maxRadius/2) + '" fill="none" ' +
        '    transform="scale(' + xScale + ' ' + yScale + ') rotate(' + phase + ')" stroke="' + element.getAttribute('fill') + '" stroke-width="' + maxRadius + '" stroke-dasharray="' + circumference + '">' +
        '    <animate id="' + randomId + '" begin="0000ms;' + randomId2 + '.end" attributeName="stroke-dashoffset" from="' + circumference + '" to="0" dur="' + duration + 's"/>' +
        '    <set id="' + randomId2 + '" begin="' + randomId + '.end" attributeName="stroke-dashoffset" to="0" dur="' + duration + 's"/>' +
        '</circle>';

    element.outerHTML = tag;
}

window.onload = function() {
    var container = document.getElementById('export-container')
    var guideless = document.getElementById('main-svg').cloneNode(true)
    guideless.getElementById('guides').outerHTML = ""
    stripAnimations(document.getElementById('main-svg'))

    document.getElementById('svg-full').textContent = vkbeautify.xml(stripAnimations(guideless.cloneNode(true)).outerHTML, 4)
    document.getElementById('svg-full-link').href = "data:text/plain," + encodeURIComponent(vkbeautify.xml(stripAnimations(guideless.cloneNode(true)).outerHTML, 4))
    document.getElementById('svg-animated').textContent = vkbeautify.xml(generateAnimations(guideless.cloneNode(true)).outerHTML, 4)
    document.getElementById('svg-animated-link').href = "data:text/plain," + encodeURIComponent(vkbeautify.xml(generateAnimations(guideless.cloneNode(true)).outerHTML, 4))

    container.appendChild(exportImage('Full', guideless, {
        x: -500, y: -500,
        w: 1000, h: 1000,
        imageWidth: 1000,
        imageHeight: 1000,
        framerate: 30,
        duration: 10
    }))
    container.appendChild(exportImage('Twitter Banner', guideless, {
        x: -450, y: -150,
        w: 900, h: 300,
        imageWidth: 1500,
        imageHeight: 500,
        framerate: 30,
        duration: 10
    }))

    var orbitless = guideless.cloneNode(true)
    orbitless.getElementById('orbits').outerHTML = ""

    container.appendChild(exportImage('Square Avatar', orbitless, {
        x: -100, y: -100,
        w: 200, h: 200,
        imageWidth: 500,
        imageHeight: 500,
        framerate: 30,
        duration: 5
    }))
    container.appendChild(exportImage('Square Avatar', orbitless, {
        x: -80, y: -80,
        w: 160, h: 160,
        imageWidth: 500,
        imageHeight: 500,
        framerate: 30,
        duration: 5
    }))
}

