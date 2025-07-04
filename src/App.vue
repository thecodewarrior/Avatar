<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/prebuilt">Prebuilt</RouterLink>
      <RouterLink to="/blackhole">Black Hole</RouterLink>
    </nav>
  </header>

  <RouterView />
</template>

<style scoped>
nav {
  display: flex;
  justify-content: center;
  gap: 0.75rem;
  margin: 0.25rem 0;
  border-bottom: 1px solid white;
}

nav > a {
  text-decoration: underline;
}
nav > a:hover {
  text-decoration: none;
}
</style>
