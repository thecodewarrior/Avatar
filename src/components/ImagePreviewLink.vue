<script setup lang="ts">
const props = withDefaults(defineProps<{
  mainUrl: string;
  altUrls?: Record<string, string>,
  altText?: string;
  altClasses?: string;
  name?: string;
  width?: number;
  height?: number;
  rounded?: boolean;
}>(), {
  rounded: false,
  altUrls: () => ({})
})

function resolveUrl(url: string): string {
  return url.startsWith('/') ? import.meta.env.BASE_URL + url.substring(1) : url
}
</script>

<template>
  <div class="flex flex-col items-center">
    <a :href="resolveUrl(mainUrl)" :class="{'rounded-full overflow-hidden': rounded}">
      <img :class="altClasses" :src="resolveUrl(mainUrl)" :width="width" :height="height" :alt="props.altText"/>
    </a>
    <span v-if="name">{{name}}</span>
    <span v-for="(altUrl, altName) in altUrls" :key="altName">(<a :href="resolveUrl(altUrl)" class="underline">{{altName}}</a>)</span>
  </div>
</template>

<style scoped>

</style>