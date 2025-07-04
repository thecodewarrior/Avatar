<script setup lang="ts">
import {computed} from "vue";

const props = withDefaults(defineProps<{
  radius: number,
  angle: number,
  tangentAngle: number,
  beamWidth: number,
  beamSpread: number,
  beamLength: number,
}>(), {

})

const svgPath = computed(() => {
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
  path.push("Z")

  // const path1 = path.join(' ')
  // var tag = "<path d=\"" + path.join(' ') + "\" fill=\"#fff\"></path>"
  // document.write(tag)

  tangentY *= -1
  curveEndY *= -1
  controlY *= -1
  beamLength *= -1

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
  path.push("Z")

  return path.join(' ')

  // return [path1, path2]
})
</script>

<template>
  <path :d="svgPath" fill="#fff"/>
</template>

<style scoped>
</style>