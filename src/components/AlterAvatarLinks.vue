<script setup lang="ts">
import ImagePreviewLink from "@/components/ImagePreviewLink.vue";

import type {AlterInfo} from "@/data/alters.ts";

const props = defineProps<{
  alter: AlterInfo,
}>()

function prebuiltUrl(name: string): string {
  return `/prebuilt/alters/${props.alter.id}/${name}`
}
</script>

<template>
  <div class="flex flex-col items-center">
    <div class="text-xl">{{alter.name}}</div>
    <div class="flex gap-2">
      <ImagePreviewLink
          :main-url="prebuiltUrl('circle.png')"
          :alt-urls="{'SVG': prebuiltUrl('circle.svg')}"
          :alt-text="alter.name"
          alt-classes="flex text-center items-center justify-center bg-[#949494]"
          :width="100" :height="100"
          rounded
      />
      <ImagePreviewLink
          :main-url="prebuiltUrl('square.png')"
          :alt-urls="{'SVG': prebuiltUrl('square.svg')}"
          :alt-text="alter.name"
          alt-classes="flex text-center items-center justify-center bg-[#949494]"
          :width="100" :height="100"
      />
    </div>
    <div class="mt-2 px-2 py-0.5 rounded text-white font-black" :style="{'background-color': alter.backgroundColor}">
      Color: {{alter.backgroundColor}}
    </div>
  </div>
</template>

<style scoped>

</style>