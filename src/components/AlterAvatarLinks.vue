<script setup lang="ts">
import ImagePreviewLink from "@/components/ImagePreviewLink.vue";

const props = defineProps<{
  name: string,
  dir: string,
  colorCode: string,
}>()

function prebuiltUrl(name: string): string {
  return `/prebuilt/alters/${props.dir}/${name}`
}
</script>

<template>
  <div class="flex flex-col items-center">
    <div class="text-xl">{{name}}</div>
    <div class="flex gap-2">
      <ImagePreviewLink
          :main-url="prebuiltUrl('circle.png')"
          :alt-urls="{'SVG': prebuiltUrl('circle.svg')}"
          :width="100" :height="100"
          rounded
      />
      <ImagePreviewLink
          :main-url="prebuiltUrl('square.png')"
          :alt-urls="{'SVG': prebuiltUrl('square.svg')}"
          :width="100" :height="100"
      />
    </div>
    <div class="mt-2 px-2 py-0.5 rounded text-white font-black" :style="{'background-color': colorCode}">
      Color: {{colorCode}}
    </div>
  </div>
</template>

<style scoped>

</style>