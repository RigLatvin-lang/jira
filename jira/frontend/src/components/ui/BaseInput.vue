<script setup lang="ts">
defineProps<{
  modelValue: string | number | null
  label?: string
  placeholder?: string
  type?: string
  error?: string
}>()

defineEmits<{
  'update:modelValue': [value: string | number | null]
}>()
</script>

<template>
  <div class="base-input">
    <label v-if="label" class="base-input__label">{{ label }}</label>
    <input
      class="base-input__field"
      :class="{ 'base-input__field--error': error }"
      :type="type ?? 'text'"
      :placeholder="placeholder"
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
    <span v-if="error" class="base-input__error">{{ error }}</span>
  </div>
</template>

<style scoped>
.base-input {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.base-input__label {
  font-size: 13px;
  font-weight: 500;
  color: #6b7280;
}

.base-input__field {
  padding: 10px 14px;
  border: none;
  border-radius: 8px;
  background: #f5f5f5;
  font-size: 14px;
  font-family: inherit;
  color: #1f2937;
  transition: box-shadow 0.15s ease, transform 0.15s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.base-input__field:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(249, 115, 22, 0.3), 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: scale(1.01);
}

.base-input__field--error {
  box-shadow: 0 0 0 2px rgba(239, 68, 68, 0.3);
}

.base-input__field::placeholder {
  color: #9ca3af;
}

.base-input__error {
  font-size: 12px;
  color: #ef4444;
}
</style>
