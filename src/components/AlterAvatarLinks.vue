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
          :alt-urls="{'SVG': prebuiltUrl('circle.svg'), 'Display P3': prebuiltUrl('circle@p3.png')}"
          :alt-text="alter.name"
          alt-classes="flex text-center items-center justify-center bg-[#949494]"
          :width="100" :height="100"
          rounded
      />
      <ImagePreviewLink
          :main-url="prebuiltUrl('square.png')"
          :alt-urls="{'SVG': prebuiltUrl('square.svg'), 'Display P3': prebuiltUrl('square@p3.png')}"
          :alt-text="alter.name"
          alt-classes="flex text-center items-center justify-center bg-[#949494]"
          :width="100" :height="100"
      />
      <ImagePreviewLink
          :main-url="prebuiltUrl('squircle.png')"
          :alt-urls="{'SVG': prebuiltUrl('squircle.svg'), 'Display P3': prebuiltUrl('squircle@p3.png')}"
          :alt-text="alter.name"
          alt-classes="flex text-center items-center justify-center bg-[#949494]"
          :width="100" :height="100"
      />
      <template v-if="alter.animated">
        <ImagePreviewLink
            :main-url="prebuiltUrl('animated/circle.webp')"
            :alt-urls="{'SVG': prebuiltUrl('animated/squircle.svg'), 'GIF': prebuiltUrl('circle.gif')}"
            :alt-text="alter.name"
            alt-classes="flex text-center items-center justify-center bg-[#949494]"
            :width="100" :height="100"
            rounded
        />
        <ImagePreviewLink
            :main-url="prebuiltUrl('animated/square.webp')"
            :alt-urls="{'SVG': prebuiltUrl('animated/square.svg'), 'GIF': prebuiltUrl('square.gif')}"
            :alt-text="alter.name"
            alt-classes="flex text-center items-center justify-center bg-[#949494]"
            :width="100" :height="100"
        />
        <ImagePreviewLink
            :main-url="prebuiltUrl('animated/squircle.webp')"
            :alt-urls="{'SVG': prebuiltUrl('animated/squircle.svg'), 'GIF': prebuiltUrl('squircle.gif')}"
            :alt-text="alter.name"
            alt-classes="flex text-center items-center justify-center bg-[#949494]"
            :width="100" :height="100"
        />
      </template>
    </div>
    <div class="mt-2 px-2 py-0.5 rounded text-white font-black" :style="{'background-color': alter.backgroundColor}">
      Color: {{alter.backgroundColor.toUpperCase()}}
    </div>
  </div>
</template>

<style scoped>

</style>
