<script setup lang="ts">

import AlterAvatarLinks from "@/components/AlterAvatarLinks.vue";
import ImagePreviewLink from "@/components/ImagePreviewLink.vue";
import AlterHeadspaceMap from "@/components/AlterHeadspaceMap.vue";
import {Alters} from "@/data/alters.ts";
</script>

<template>
  <main class="p-3">
    <h3>Basic</h3>
    <div class="preview-grid">
      <ImagePreviewLink main-url="/prebuilt/circle.png" :alt-urls="{'SVG': '/prebuilt/circle.svg', 'MOV': '/prebuilt/circle.mov'}" name="circle" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/square.png" :alt-urls="{'SVG': '/prebuilt/square.svg', 'MOV': '/prebuilt/square.mov'}" name="square" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/banner.png" :alt-urls="{'MOV': '/prebuilt/banner.mov'}" name="banner" :width="300"/>
      <ImagePreviewLink main-url="/prebuilt/full.png" :alt-urls="{'SVG': '/prebuilt/full.svg', 'MOV': '/prebuilt/full.mov'}" name="full" :width="100"/>
    </div>
    <h3>Alters</h3>
    <div class="preview-grid">
      <AlterAvatarLinks :alter="Alters.KATE"/>
      <AlterAvatarLinks :alter="Alters.RUBY"/>
      <AlterAvatarLinks :alter="Alters.JADEN"/>
      <AlterAvatarLinks :alter="Alters.TOPAZ"/>
      <AlterAvatarLinks :alter="Alters.SAPPHIRE"/>
      <AlterAvatarLinks :alter="Alters.SIXTH"/>
      <AlterAvatarLinks :alter="Alters.SEVENTH"/>
      <AlterAvatarLinks :alter="Alters.STELLA"/>
      <AlterAvatarLinks :alter="Alters.CONSTELLATION"/>
      <AlterAvatarLinks :alter="Alters.OTHER"/>
    </div>
    <h3>Headspace</h3>
    <div class="flex justify-center">
      <AlterHeadspaceMap :headspace="Alters.HEADSPACE"/>
    </div>
    <h3>Plural</h3>
    <div class="preview-grid">
      <ImagePreviewLink main-url="/prebuilt/pride/plural.svg" name="Source SVG" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/pride/plural_black.png" name="Black" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/pride/plural_thin_black.png" name="Black (thin)" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/pride/plural_white.png" name="White" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/pride/plural_thin_white.png" name="White (thin)" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/pride/plural_no_lines.png" name="No lines" :width="100"/>
    </div>
    <h3>Favicons</h3>
    <div class="preview-grid">
      <ImagePreviewLink main-url="/prebuilt/favicon/favicon_outline.png" :alt-urls="{'SVG': '/prebuilt/favicon/favicon_outline.svg'}" name="Outline" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/favicon/favicon_16.png" name="16x16" :width="100" class="pixelart"/>
      <ImagePreviewLink main-url="/prebuilt/favicon/favicon_blur.png" :alt-urls="{'SVG': '/prebuilt/favicon/favicon_blur.svg'}" name="Blur" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/favicon/favicon_32.png" name="32x32" :width="100" class="pixelart"/>
      <ImagePreviewLink main-url="/prebuilt/favicon/favicon_48.png" name="48x48" :width="100" class="pixelart"/>
      <ImagePreviewLink main-url="/prebuilt/favicon/favicon_60.png" name="60x60" :width="100" class="pixelart"/>
    </div>
    <h3>Misc.</h3>
    <div class="preview-grid">
      <ImagePreviewLink main-url="/prebuilt/22x22_pixelart.png" name="22x22px" :width="100" class="pixelart"/>
      <ImagePreviewLink main-url="/prebuilt/cyanidex_avatar.png" :alt-urls="{'PSD': '/prebuilt/cyanidex%20avatar.psd'}" name="CyanideX avatar" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/desktop_background.png" :alt-urls="{'PSD': '/prebuilt/desktop_background.psd'}" name="Desktop" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/phone_background_homescreen.png" :alt-urls="{'PSD': '/prebuilt/phone_background.psd'}" name="Phone home" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/phone_background_lockscreen.png" :alt-urls="{'PSD': '/prebuilt/phone_background.psd'}" name="Phone lock" :width="100"/>
      <ImagePreviewLink main-url="/prebuilt/transparent_path.svg" :alt-urls="{'PDF': '/prebuilt/transparent_path.pdf'}" name="Transparent Path" :width="100"/>
    </div>
  </main>
</template>

<style scoped>
h3 {
  font-size: 1.5rem;
  font-weight: 600;
  border-bottom: 1px solid white;
  margin: 0.75rem 0;
}

.preview-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
  gap: 1rem 1.5rem;
}

.pixelart :deep(img) {
  image-rendering: pixelated;
}
</style>