<script setup lang="ts">
import AppHeader from './AppHeader.vue'
</script>

<template>
  <div class="layout">
    <AppHeader />
    <main class="layout__main">
      <slot />
    </main>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  background: #fafafa;
}

.layout__main {
  padding: 24px;
  height: calc(100vh - 60px);
  overflow: hidden;
}
</style>
