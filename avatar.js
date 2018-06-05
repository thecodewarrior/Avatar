function createOrbitingBody(index, distance, size, angle, skew) {
    var orbitDegreeLength = (Math.PI * 2 * distance) / 360;
    var tag = 
        '<g id="orbit' + index + '">\n' + 
        '    <circle id="orbit' + index + '-circle" cx="0" cy="0" r="' + distance + '" fill="none" transform="skewY(' + skew + ') scale(1 0.222222222) rotate(' + (angle*360) + ')"\n' +
        '        stroke-dasharray="' + (orbitDegreeLength*2) + '" stroke-dashoffset="' + orbitDegreeLength + '" stroke="#FFF" stroke-width="2"/>' +
        '<!-- ' + orbitDegreeLength + ' = 1 deg on circle with r=275 -->\n' +
        '    <circle id="orbit' + index + '-body" cx="0" cy="0" r="' + size + '" fill="#FFF" ' + 
        'transform="skewY(' + skew + ') scale(1 0.222222222) translate(' + distance + ' 0) rotate(' + (angle*360) + ') rotate(' + (-angle*360) + ' ' + distance + ' 0) scale(1 4.5) skewY(' + (-skew) + ')"/>\n' +
        '</g>\n';
    document.write(tag);
}


function exportImage(name, svg, options) {
    var canvas = document.createElement('canvas');
    canvas.width  = options.imageWidth;
    canvas.height = options.imageHeight;

    var ctx = canvas.getContext('2d');

    
    var data = '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" ' +
        'width="' + options.imageWidth + 'px" height="' + options.imageHeight + 'px" viewBox="' + options.x + ' ' + options.y + ' ' + options.w + ' ' + options.h + '" xml:space="preserve">'
        + svg.innerHTML +
        '</svg>'

    data = encodeURIComponent(data);

    var templateNode = document.createElement('template');
    templateNode.innerHTML = '<div class="export">' +
        '    <h3>' + name + '</h3>' +
        '</div>';
    var exportNode = templateNode.content.firstChild;
    var newImg = new Image();
    exportNode.appendChild(newImg);
    var img = new Image();

    img.onload = function() {
        ctx.drawImage(img, 0, 0);
        newImg.src = canvas.toDataURL("image/png");
    }

    img.src = "data:image/svg+xml," + data;
    return exportNode;
}

window.onload = function() {
    var container = document.getElementById('export-container')

    var guideless = document.getElementById('main-svg').cloneNode(true)
    var guides = guideless.getElementById('guides')
    guides.parentNode.removeChild(guides)

    document.getElementById('svg-text').textContent = guideless.outerHTML

    container.appendChild(exportImage('Full', guideless, {
        x: -500, y: -500,
        w: 1000, h: 1000,
        imageWidth: 1000,
        imageHeight: 1000
    }))
    container.appendChild(exportImage('Twitter Banner', guideless, {
        x: -450, y: -150,
        w: 900, h: 300,
        imageWidth: 1500,
        imageHeight: 500
    }))

    var orbitless = guideless.cloneNode(true)
    var orbits = orbitless.getElementById('orbits')
    orbits.parentNode.removeChild(orbits)

    container.appendChild(exportImage('Square Avatar', orbitless, {
        x: -100, y: -100,
        w: 200, h: 200,
        imageWidth: 500,
        imageHeight: 500
    }))
    container.appendChild(exportImage('Square Avatar', orbitless, {
        x: -80, y: -80,
        w: 160, h: 160,
        imageWidth: 500,
        imageHeight: 500
    }))
}

