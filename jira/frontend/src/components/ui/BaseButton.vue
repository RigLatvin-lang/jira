<script setup lang="ts">
defineProps<{
  variant?: 'primary' | 'secondary' | 'danger' | 'ghost'
  size?: 'sm' | 'md' | 'lg'
  loading?: boolean
  disabled?: boolean
}>()
</script>

<template>
  <button
    class="base-button"
    :class="[`base-button--${variant ?? 'primary'}`, `base-button--${size ?? 'md'}`]"
    :disabled="disabled || loading"
  >
    <span v-if="loading" class="base-button__spinner" />
    <slot />
  </button>
</template>

<style scoped>
.base-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease, background-color 0.15s ease;
  white-space: nowrap;
  font-family: inherit;
}

.base-button:hover:not(:disabled) {
  transform: scale(1.03);
}

.base-button:active:not(:disabled) {
  transform: scale(0.97);
}

.base-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.base-button--sm {
  padding: 6px 12px;
  font-size: 13px;
}

.base-button--md {
  padding: 8px 18px;
  font-size: 14px;
}

.base-button--lg {
  padding: 12px 24px;
  font-size: 15px;
}

.base-button--primary {
  background: #f97316;
  color: white;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.3);
}

.base-button--primary:hover:not(:disabled) {
  background: #ea580c;
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.4);
}

.base-button--secondary {
  background: #f5f5f5;
  color: #374151;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.base-button--secondary:hover:not(:disabled) {
  background: #e5e5e5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.base-button--danger {
  background: #ef4444;
  color: white;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.base-button--danger:hover:not(:disabled) {
  background: #dc2626;
}

.base-button--ghost {
  background: transparent;
  color: #6b7280;
}

.base-button--ghost:hover:not(:disabled) {
  background: #f5f5f5;
  color: #374151;
}

.base-button__spinner {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
