<script setup lang="ts">
defineProps<{
  modelValue: string
  label?: string
  placeholder?: string
  rows?: number
}>()

defineEmits<{
  'update:modelValue': [value: string]
}>()
</script>

<template>
  <div class="base-textarea">
    <label v-if="label" class="base-textarea__label">{{ label }}</label>
    <textarea
      class="base-textarea__field"
      :placeholder="placeholder"
      :rows="rows ?? 3"
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)"
    />
  </div>
</template>

<style scoped>
.base-textarea {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.base-textarea__label {
  font-size: 13px;
  font-weight: 500;
  color: #6b7280;
}

.base-textarea__field {
  padding: 10px 14px;
  border: none;
  border-radius: 8px;
  background: #f5f5f5;
  font-size: 14px;
  font-family: inherit;
  color: #1f2937;
  resize: vertical;
  transition: box-shadow 0.15s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.base-textarea__field:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(249, 115, 22, 0.3), 0 2px 8px rgba(0, 0, 0, 0.08);
}

.base-textarea__field::placeholder {
  color: #9ca3af;
}
</style>
