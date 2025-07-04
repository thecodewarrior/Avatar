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
  let {angle, tangentAngle, radius, beamWidth, beamSpread, beamLength} = props

  const slope = -Math.tan(tangentAngle)

  let tangentX = radius*Math.sin(angle)
  let tangentY = radius*Math.cos(angle)

  let curveEndX = beamWidth/2
  // y = m(x-x1)+y1
  let controlX = beamWidth/2
  let controlY = slope*(curveEndX-tangentX)+tangentY

  let controlPointDistance = Math.sqrt(Math.pow(curveEndX-tangentX, 2) + Math.pow(controlY-tangentY, 2))
  let curveEndY = controlY + controlPointDistance
  curveEndY += 20
  curveEndX += 20*(beamSpread/beamLength)
  if(beamSpread != 0) {
    let spreadSlope = beamLength/beamSpread
    // x = ( (y2-m2*x2)-(y1-m1*x1) ) / (m1-m2)
    let intersectionX =
        ( (curveEndY-spreadSlope*curveEndX) - (tangentY-slope*tangentX) ) / (slope - spreadSlope)
    // y = m(x-x1)+y1
    let intersectionY = slope*(intersectionX-tangentX) + tangentY
    controlX = intersectionX
    controlY = intersectionY
  }

  let path = []

  path.push("M", -tangentX, tangentY)
  path.push("A", 9, 2, 0, 0, 0 , tangentX, tangentY)
  path.push("Q", controlX, controlY, curveEndX, curveEndY)
  path.push("L", curveEndX+beamSpread, curveEndY+beamLength)
  path.push("L", -(curveEndX+beamSpread), curveEndY+beamLength)
  path.push("L", -curveEndX, curveEndY)
  path.push("Q", -controlX, controlY, -tangentX, tangentY)
  path.push("Z")

  // const path1 = path.join(' ')
  // let tag = "<path d=\"" + path.join(' ') + "\" fill=\"#fff\"></path>"
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