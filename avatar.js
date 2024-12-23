
function createElement(html) {
    const el = document.createElement('template');
    el.innerHTML = html;
    return el.content.childNodes[0];
}

function createBeams() {
    const angle = Math.PI/6
    const tangentAngle = Math.PI/3
    const r = 30
    const beamWidth = 10
    const beamSpread = 1

    var beamLength = 1500
    var slope = -Math.tan(tangentAngle)

    var tangentX = r*Math.sin(angle)
    var tangentY = r*Math.cos(angle)

    var curveEndX = beamWidth/2
    // y = m(x-x1)+y1
    var controlX = beamWidth/2
    var controlY = slope*(curveEndX-tangentX)+tangentY

    var controlPointDistance = Math.sqrt(Math.pow(curveEndX-tangentX, 2) + Math.pow(controlY-tangentY, 2))
    var curveEndY = controlY + controlPointDistance
    curveEndY += 20
    curveEndX += 20*(beamSpread/beamLength)
    if(beamSpread != 0) {
        var spreadSlope = beamLength/beamSpread
        // x = ( (y2-m2*x2)-(y1-m1*x1) ) / (m1-m2)
        var intersectionX = 
            ( (curveEndY-spreadSlope*curveEndX) - (tangentY-slope*tangentX) ) / (slope - spreadSlope)
        // y = m(x-x1)+y1
        var intersectionY = slope*(intersectionX-tangentX) + tangentY
        controlX = intersectionX
        controlY = intersectionY
    }

    var path = []
    
    path.push("M", -tangentX, tangentY)
    path.push("A", 9, 2, 0, 0, 0 , tangentX, tangentY)
    path.push("Q", controlX, controlY, curveEndX, curveEndY)
    path.push("L", curveEndX+beamSpread, curveEndY+beamLength)
    path.push("L", -(curveEndX+beamSpread), curveEndY+beamLength)
    path.push("L", -curveEndX, curveEndY)
    path.push("Q", -controlX, controlY, -tangentX, tangentY)

    var tag = "<path d=\"" + path.join(' ') + "\" fill=\"#fff\"></path>"
    document.write(tag)

    tangentY *= -1
    curveEndY *= -1
    controlY *= -1
    beamLength *= -1
    path = []
    
    path.push("M", -tangentX, tangentY)
    path.push("A", 9, 2, 0, 0, 0 , tangentX, tangentY)
    path.push("Q", controlX, controlY, curveEndX, curveEndY)
    //path.push("L", controlX, controlY)
    //path.push("L", curveEndX, curveEndY)
    path.push("L", curveEndX+beamSpread, curveEndY+beamLength)
    path.push("L", -(curveEndX+beamSpread), curveEndY+beamLength)
    path.push("L", -curveEndX, curveEndY)
    path.push("Q", -controlX, controlY, -tangentX, tangentY)
    //path.push("L", -controlX, controlY)
    //path.push("L", -tangentX, tangentY)

    tag = "<path d=\"" + path.join(' ') + "\" fill=\"#fff\"></path>"
    document.write(tag)
}

function createOrbitingBody(args) {
    const index = args['index'] || 0;
    let radius = args['radius'] || 100;
    let period = args['period'] || 1;
    const size = args['size'] || 5;
    const angle = args['angle'] || 0;
    const ringInside = args['ring_inside'] || 0;
    const ringOutside = args['ring_outside'] || 0;

    if(radius['resonanceWith']) {
        const otherRadius = radius['resonanceWith']['radius'];
        const otherPeriod = radius['resonanceWith']['period'];
        const factor = radius['factor'];
        radius = otherRadius * Math.pow(factor, 2.0/3);
        period = otherPeriod * factor
    }

    const orbitDegreeLength = (Math.PI * 2 * radius) / 360;
    const tag =
        '<g id="orbit-' + index + '">\n' +
        '    <circle id="orbit-' + index + '-circle" cx="0" cy="0" r="' + radius + '" fill="none"\n' +
        '        stroke-dasharray="' + (orbitDegreeLength * 2) + '" stroke-dashoffset="' + orbitDegreeLength + '" stroke="#FFF" stroke-width="2"/>\n' +
        '    <g transform="rotate(' + (angle * 360) + ')">\n' +
        '        <animateTransform attributeName="transform" type="rotate" from="' + (angle * 360) + '" to="' + (angle * 360 + 360) + '" dur="' + period + 's" repeatCount="indefinite"/>\n' +
        '        <g transform="translate(' + radius + ' 0)">\n' +
        '            <g transform="rotate(' + -(angle * 360) + ')">\n' +
        '                <animateTransform attributeName="transform" type="rotate" from="' + -(angle * 360) + '" to="' + -(angle * 360 + 360) + '" dur="' + period + 's" repeatCount="indefinite"/>\n' +
        '                <g transform="scale(1 4.5)">\n' +
        '                    <circle id="orbit-' + index + '-body" cx="0" cy="0" r="' + size + '" fill="#FFF"/>\n' +
        (ringOutside == 0 ? '' :
            '<g transform="skewY(30)">\n' + 
        '                    <circle id="orbit-' + index + '-rings" cx="0" cy="0" r="' + (ringInside+ringOutside)/2 + '" fill="none" stroke-width="' + (ringOutside-ringInside)/2 + '" stroke="#FFF" transform="scale(1 0.3)">\n' +
        '                <animateTransform id="blah1" attributeName="transform" type="scale" from="1 0.3" to="1 0.5" dur="' + period/2 + 's" begin="' + (period * -angle) + 's;blah2.end"/>\n' +
        '                <animateTransform id="blah2" attributeName="transform" type="scale" from="1 0.5" to="1 0.3" dur="' + period/2 + 's" begin="blah1.end"/>\n' +
        //'                        <animateTransform id="orbit-' + index + '-rings-anim-first" attributeName="transform" begin="' + (period * -angle) + 's;orbit-' + index + '-rings-anim-second.end" type="scale" from="1 0.3" to="1 0.2" dur="' + period/2 + 's"/>\n' +
        //'                        <animateTransform id="orbit-' + index + '-rings-anim-second" attributeName="transform" begin="orbit-' + index + '-rings-anim-first.end" type="scale" from="1 0.2" to="1 0.3" dur="' + period/2 + 's"/>\n' +
        '                    </circle>\n' +
            '</g>\n' 
        ) +
        '                </g>\n' +
        '            </g>\n' +
        '        </g>\n' +
        '    </g>\n' +
        '    </circle>\n' +
        '</g>\n';
    document.write(tag);
}

function svgToImage(svg, imageWidth, imageHeight, callback) {
    const canvas = document.createElement('canvas');
    canvas.width  = imageWidth;
    canvas.height = imageHeight;
    const ctx = canvas.getContext('2d');

    const img = new Image();
    img.onload = function() {
        ctx.drawImage(img, 0, 0);
        callback(canvas, ctx)
    };

    img.src = "data:image/svg+xml," + encodeURIComponent(svg.outerHTML);
}

function viewBoxSVG(svg, x, y, w, h, pixelWidth, pixelHeight) {
    const newSVG = svg.cloneNode(true);
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
    newSVG.setAttribute('viewBox', x + ' ' + y + ' ' + w + ' ' + h);
    return newSVG
}

function exportImage(name, svg, options) {
    const data = viewBoxSVG(svg, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight);

    const template = document.importNode(document.querySelector('#export-template').content, true);

    const exportImage = template.querySelector(".export-image");

    svgToImage(data, options.imageWidth, options.imageHeight, function(canvas, ctx) {
        exportImage.src = canvas.toDataURL("image/png");
    });

    template.querySelector('.export-svg-link-static').href = 'data:text/plain,' + encodeURIComponent(vkbeautify.xml(
        viewBoxSVG(svg, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight).outerHTML, 4
    ));

    const animatedSVG = generateAnimations(svg.cloneNode(true));
    const animatedData = viewBoxSVG(animatedSVG, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight);

    const animationPreview = animatedData.cloneNode(true);
    animationPreview.removeAttribute('width');
    animationPreview.removeAttribute('height');
    template.querySelector('.export-svg-animation-preview').appendChild(animationPreview);

    template.querySelector('.export-svg-link-animated').href = 'data:text/plain,' + encodeURIComponent(vkbeautify.xml(
        viewBoxSVG(animatedSVG, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight).outerHTML, 4
    ));

    const video = template.querySelector('.export-video');
    template.querySelector('.export-generate-video').onclick = function() {
        exportVideo(animatedData, options.imageWidth, options.imageHeight, options.framerate || 10, options.duration || 1, function(blob) {
            const url = (window.webkitURL || window.URL).createObjectURL(output);
            video.src = url;
            video.removeAttribute('hidden')
        })
    };
    return template;
}

function exportVideo(animatedData, imageWidth, imageHeight, framerate, duration, callback) {
    let frame = 0;
    const frameMillis = 1000 / framerate;
    const frameCount = duration * framerate;


    const zip = new JSZip();


    const animatedWithOrigins = animatedData.cloneNode(true);
    /*animatedWithOrigins.setAttribute("width", animatedWithOrigins.getAttribute("width")/10)
    animatedWithOrigins.setAttribute("height", animatedWithOrigins.getAttribute("height")/10)*/
    const animations = animatedWithOrigins.querySelectorAll("animate, animateColor, animateMotion, animateTransform, discard, mpath, set");
    const animatedElements = new Set();

    for (var i=0, element; element = animations[i]; i++) {
        animatedElements.add(element.parentElement)
    }

    const iter = animatedElements.entries();
    for (let entry of iter) {
        var element = entry[0]; // set iterator is of [item, item] to keep it similar to the map iterator
        const randomId = 'a' + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15) + 'a';
        const timeOrigin = createElement('<animate id="' + randomId + '" class="time-origin" begin="0s"/>');
        element.insertBefore(timeOrigin, element.firstChild);
        const children = element.children;
        for (var i=0, child; child = children[i]; i++) {
            const value =
                child.setAttribute("begin", (child.getAttribute("begin") || (randomId + ".begin")).replace("0000ms", (randomId + ".begin")));
        }
    }

    let addFrame;
    addFrame = function() {
        const clone = animatedWithOrigins.cloneNode(true);
        const timeOrigins = clone.getElementsByClassName("time-origin");
        let i = 0, element;
        for (; element = timeOrigins[i]; i++) {
            element.setAttribute("begin", '-' + frameMillis*frame + 'ms'); 
        }

        frame++;
        const persistantFrame = frame;
        svgToImage(clone, imageWidth, imageHeight, function(canvas, ctx) {
            canvas.toBlob(function(blob) {
                zip.file('frame' + (persistantFrame.toString().padStart(4, '0')) + '.png', blob, { binary: true });
                const image = new Image();
                image.src = canvas.toDataURL('image/png');
            }, 'image/png');

            if(frame === frameCount) {
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
    const wipes = svg.querySelectorAll("animate, animateColor, animateMotion, animateTransform, discard, mpath, set");
    let i = 0, element;
    for (; element = wipes[i]; i++) {
        element.outerHTML = ""
    }
    return svg;
}

function generateAnimations(svg) {
    const wipes = svg.querySelectorAll("[data-animate-wipe-duration]");
    let i = 0, element;
    for (; element = wipes[i]; i++) {
        generateWipe(element);
    }
    return svg;
}

function generateWipe(element) {
    const duration = element.getAttribute("data-animate-wipe-duration");
    const begin = element.getAttribute("data-animate-wipe-begin") || "0s";
    const phase = element.getAttribute("data-animate-wipe-phase") || 0;
    const rx = element.tagName === "ellipse" ? element.getAttribute("rx") : element.getAttribute("r");
    const ry = element.tagName === "ellipse" ? element.getAttribute("ry") : element.getAttribute("r");

    const maxRadius = Math.max(rx, ry);
    const circumference = Math.PI * 2 * (maxRadius / 2); //maxRadius/2 because the actual shape is a circle half the radius with the stroke extending r/2 inward and r/2 outward
    const xScale = rx / maxRadius;
    const yScale = ry / maxRadius;

    const randomId = 'a' + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15) + 'a';

    const tag =
        '<circle id="' + element.id + '" cx="' + element.getAttribute('cx') + '" cy="' + element.getAttribute('cy') + '" r="' + (maxRadius / 2) + '" fill="none" ' +
        '    transform="scale(' + xScale + ' ' + yScale + ') rotate(' + phase + ')" stroke="' + element.getAttribute('fill') + '" stroke-width="' + maxRadius + '" stroke-dasharray="' + circumference + '">' +
        '    <animate id="' + randomId + '" begin="0000ms;' + randomId + '.end" attributeName="stroke-dashoffset" from="0" to="' + (-199 * circumference) + '" dur="' + (100 * duration) + 's"/>' +
        '</circle>';

    element.outerHTML = tag;
}

window.onload = function() {
    const container = document.getElementById('export-container');
    const guideless = document.getElementById('main-svg').cloneNode(true);
    guideless.getElementById('guides').outerHTML = "";
    stripAnimations(document.getElementById('main-svg'));
    document.getElementById('main-svg').parentElement.appendChild(generateAnimations(guideless.cloneNode(true)));

    document.getElementById('svg-full').textContent = vkbeautify.xml(stripAnimations(guideless.cloneNode(true)).outerHTML, 4);
    document.getElementById('svg-full-link').href = "data:text/plain," + encodeURIComponent(vkbeautify.xml(stripAnimations(guideless.cloneNode(true)).outerHTML, 4));
    document.getElementById('svg-animated').textContent = vkbeautify.xml(generateAnimations(guideless.cloneNode(true)).outerHTML, 4);
    document.getElementById('svg-animated-link').href = "data:text/plain," + encodeURIComponent(vkbeautify.xml(generateAnimations(guideless.cloneNode(true)).outerHTML, 4));

    container.appendChild(exportImage('Full', guideless, {
        x: -500, y: -500,
        w: 1000, h: 1000,
        imageWidth: 1000,
        imageHeight: 1000,
        framerate: 30,
        duration: 12
    }));
    container.appendChild(exportImage('Twitter Banner', guideless, {
        x: -450, y: -150,
        w: 900, h: 300,
        imageWidth: 1500,
        imageHeight: 500,
        framerate: 30,
        duration: 12
    }));

    const orbitless = guideless.cloneNode(true);
    orbitless.getElementById('orbits').outerHTML = "";

    container.appendChild(exportImage('Square Avatar', orbitless, {
        x: -100, y: -100,
        w: 200, h: 200,
        imageWidth: 500,
        imageHeight: 500,
        framerate: 30,
        duration: 4
    }));
    container.appendChild(exportImage('Square Avatar', orbitless, {
        x: -80, y: -80,
        w: 160, h: 160,
        imageWidth: 500,
        imageHeight: 500,
        framerate: 30,
        duration: 4
    }));

    staticSVG = stripAnimations(guideless.cloneNode(true));
    animatedSVG = generateAnimations(guideless.cloneNode(true));
};

var staticSVG, animatedSVG;

function setupCustom() {
    var element = document.getElementById('export')
    element.getElementsByClassName('')
}

function updateImages() {
    const data = viewBoxSVG(svg, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight);

    const template = document.importNode(document.querySelector('#export-template').content, true);

    const exportImage = template.querySelector(".export-image");

    svgToImage(data, options.imageWidth, options.imageHeight, function(canvas, ctx) {
        exportImage.src = canvas.toDataURL("image/png");
    });

    template.querySelector('.export-svg-link-static').href = 'data:text/plain,' + encodeURIComponent(vkbeautify.xml(
        viewBoxSVG(svg, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight).outerHTML, 4
    ));

    const animatedSVG = generateAnimations(svg.cloneNode(true));
    const animatedData = viewBoxSVG(animatedSVG, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight);

    const animationPreview = animatedData.cloneNode(true);
    animationPreview.removeAttribute('width');
    animationPreview.removeAttribute('height');
    template.querySelector('.export-svg-animation-preview').appendChild(animationPreview);

    template.querySelector('.export-svg-link-animated').href = 'data:text/plain,' + encodeURIComponent(vkbeautify.xml(
        viewBoxSVG(animatedSVG, options.x, options.y, options.w, options.h, options.imageWidth, options.imageHeight).outerHTML, 4
    ));

    const video = template.querySelector('.export-video');
    template.querySelector('.export-generate-video').onclick = function() {
        exportVideo(animatedData, options.imageWidth, options.imageHeight, options.framerate || 10, options.duration || 1, function(blob) {
            const url = (window.webkitURL || window.URL).createObjectURL(output);
            video.src = url;
            video.removeAttribute('hidden')
        })
    };
    return template;
}
