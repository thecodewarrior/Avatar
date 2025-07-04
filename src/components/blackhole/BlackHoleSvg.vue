<script setup lang="ts">
import AstroJets from "@/components/blackhole/AstroJets.vue";

const props = withDefaults(defineProps<{
  astroJets: boolean
}>(), {
  astroJets: false
})
</script>

<template>
  <svg id="main-svg" x="0px" y="0px" width="1000px" height="1000px" viewBox="-500 -500 1000 1000" xml:space="preserve">
    <rect id="background" x="-10000" y="-10000" width="20000" height="20000" fill="#000000"/>

    <g transform="rotate(-45)">
      <circle v-if="!astroJets" cx="0" cy="0" r="40" fill="#FFF"/>
      <circle cx="0" cy="0" r="30" fill="#000"/>
      <ellipse cx="0" cy="0" rx="90" ry="20" fill="#FFF"/>
      <path d="M -30,0 A 1,1 0 0,1 30,0 A 9,2 0 0,1 -30,0" fill="#000"/>

      <AstroJets v-if="astroJets" :radius="30" :angle="Math.PI/6" :tangent-angle="Math.PI/3" :beam-width="10"
                 :beam-spread="1" :beam-length="1500"/>
    </g>

    <g id="orbits" transform="rotate(-45) scale(1 0.22222222)">
<!--            <script>-->
      <!--                createOrbitingBody({-->
      <!--                  index: 1,-->
      <!--                  radius: 250,-->
      <!--                  period: 3,-->
      <!--                  size: 6,-->
      <!--                  angle: -3 / 16-->
      <!--                });-->
      <!--                document.currentScript.parentNode.removeChild(document.currentScript);-->
      <!--            </script>-->
      <!--      <script>-->
      <!--                createOrbitingBody({-->
      <!--                  index: 2,-->
      <!--                  radius: {-->
      <!--                    resonanceWith: {radius: 250, period: 3},-->
      <!--                    factor: 2-->
      <!--                  },-->
      <!--                  size: 6,-->
      <!--                  angle: 5 / 32-->
      <!--                });-->
      <!--                document.currentScript.parentNode.removeChild(document.currentScript);-->
      <!--            </script>-->
      <!--      <script>-->
      <!--                createOrbitingBody({-->
      <!--                  index: 3,-->
      <!--                  radius: {-->
      <!--                    resonanceWith: {radius: 250, period: 3},-->
      <!--                    factor: 4-->
      <!--                  },-->
      <!--                  size: 8,-->
      <!--                  angle: 11 / 16-->
      <!--                });-->
      <!--                document.currentScript.parentNode.removeChild(document.currentScript);-->
      <!--            </script>-->
    </g>

    <g id="guides">
      <circle id="round-bounds" cx="0" cy="0" r="100" stroke="#F00" stroke-width="1" fill="none"/>
      <rect id="square-bounds" x="-80" y="-80" width="160" height="160" stroke="#F00" stroke-width="1" fill="none"/>
      <rect id="twitter-banner-bounds" x="-450" y="-150" width="900" height="300" stroke="#F00" stroke-width="1" fill="none"/>
    </g>
  </svg>
</template>

<style scoped>

</style>