<script setup lang="ts">
const props = withDefaults(defineProps<{
  mainUrl: string;
  altUrls?: Record<string, string>,
  name?: string;
  width?: number;
  height?: number;
  rounded?: boolean;
}>(), {
  rounded: false,
  altUrls: () => ({})
})
</script>

<template>
  <div class="flex flex-col items-center">
    <a :href="mainUrl" :class="{'rounded-full overflow-hidden': rounded}"><img :src="mainUrl" :width="width" :height="height"/></a>
    <span v-if="name">{{name}}</span>
    <span v-for="(altUrl, altName) in altUrls" :key="altName">(<a :href="altUrl" class="underline">{{altName}}</a>)</span>
  </div>
</template>

<style scoped>

</style>