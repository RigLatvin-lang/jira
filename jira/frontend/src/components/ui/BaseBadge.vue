<script setup lang="ts">
defineProps<{
  color?: string
  text: string
}>()
</script>

<template>
  <span
    class="badge"
    :style="{ backgroundColor: color ? `${color}18` : '#f5f5f5', color: color || '#6b7280' }"
  >
    {{ text }}
  </span>
</template>

<style scoped>
.badge {
  display: inline-flex;
  align-items: center;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.02em;
  white-space: nowrap;
  transition: transform 0.15s ease;
}

.badge:hover {
  transform: scale(1.05);
}
</style>
