<script setup lang="ts">
defineProps<{
  modelValue: string
  label?: string
  options: { value: string; label: string }[]
}>()

defineEmits<{
  'update:modelValue': [value: string]
}>()
</script>

<template>
  <div class="base-select">
    <label v-if="label" class="base-select__label">{{ label }}</label>
    <select
      class="base-select__field"
      :value="modelValue"
      @change="$emit('update:modelValue', ($event.target as HTMLSelectElement).value)"
    >
      <option v-for="opt in options" :key="opt.value" :value="opt.value">
        {{ opt.label }}
      </option>
    </select>
  </div>
</template>

<style scoped>
.base-select {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.base-select__label {
  font-size: 13px;
  font-weight: 500;
  color: #6b7280;
}

.base-select__field {
  padding: 10px 14px;
  border: none;
  border-radius: 8px;
  background: #f5f5f5;
  font-size: 14px;
  font-family: inherit;
  color: #1f2937;
  cursor: pointer;
  transition: box-shadow 0.15s ease, transform 0.15s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%239ca3af' d='M3 4.5L6 7.5L9 4.5'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  padding-right: 32px;
}

.base-select__field:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(249, 115, 22, 0.3), 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: scale(1.01);
}
</style>
