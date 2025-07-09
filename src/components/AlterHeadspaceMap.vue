<script setup lang="ts">
import {computed} from "vue";
import ImagePreviewLink from "@/components/ImagePreviewLink.vue";

import type {AlterInfo} from "@/data/alters.ts";

const props = defineProps<{
  headspace: (AlterInfo | null)[][],
}>()

const computedStyle = computed(() => {
  return {
    'grid-template-areas': props.headspace
        .map(row => row.map(e => e?.id ?? '.').join(' '))
        .map(row => `'${row}'`).join(' '),
  }
})

const alters = computed(() => props.headspace.flatMap(e => e).filter(e => e != null))

function avatarUrl(id: string): string {
  return `/prebuilt/alters/${id}/circle.png`
}
</script>

<template>
  <div class="grid gap-2" :style="computedStyle">
    <div v-for="alter in alters" :style="{'grid-area': alter.id}" :key="alter.id">
      <ImagePreviewLink
          :main-url="avatarUrl(alter.id)"
          :alt-text="alter.name"
          alt-classes="flex text-center items-center justify-center bg-[#949494]"
          :width="75" :height="75"
          rounded
      />
    </div>
  </div>
</template>

<style scoped>
</style>