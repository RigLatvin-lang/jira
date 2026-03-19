<script setup lang="ts">
import { ref } from 'vue'
import { useTaskDetails } from '@/composables/useTaskDetails'
import type { TaskResponse } from '@/types'
import { STATUS_LABELS, PRIORITY_LABELS, PRIORITY_COLORS } from '@/types'
import BaseBadge from '@/components/ui/BaseBadge.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'

const props = defineProps<{
  task: TaskResponse
}>()

const emit = defineEmits<{
  edit: []
  deleted: []
}>()

const { loading, error, deleteTask, logTime } = useTaskDetails()

const showTimeInput = ref(false)
const timeMinutes = ref<number | null>(null)

function formatTime(minutes: number | null): string {
  if (!minutes) return '—'
  const h = Math.floor(minutes / 60)
  const m = minutes % 60
  return h > 0 ? `${h}ч ${m}м` : `${m}м`
}

function formatDate(date: string): string {
  return new Date(date).toLocaleDateString('ru-RU', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

async function onLogTime() {
  if (timeMinutes.value && timeMinutes.value > 0) {
    await logTime(props.task.id, timeMinutes.value)
    showTimeInput.value = false
    timeMinutes.value = null
  }
}

function onDelete() {
  if (confirm('Удалить задачу?')) {
    deleteTask(props.task.id, () => emit('deleted'))
  }
}
</script>

<template>
  <div class="details">
    <div class="details__meta">
      <span class="details__id">{{ task.id.slice(0, 8).toUpperCase() }}</span>
      <BaseBadge :text="STATUS_LABELS[task.status]" />
      <BaseBadge :text="PRIORITY_LABELS[task.priority]" :color="PRIORITY_COLORS[task.priority]" />
    </div>

    <h3 class="details__title">{{ task.title }}</h3>
    <p v-if="task.description" class="details__desc">{{ task.description }}</p>

    <div class="details__grid">
      <div class="details__field">
        <span class="details__label">Автор</span>
        <span class="details__value">{{ task.creatorUsername }}</span>
      </div>
      <div class="details__field">
        <span class="details__label">Исполнитель</span>
        <span class="details__value">{{ task.assigneeUsername || '—' }}</span>
      </div>
      <div class="details__field">
        <span class="details__label">Оценка</span>
        <span class="details__value">{{ formatTime(task.estimateMinutes) }}</span>
      </div>
      <div class="details__field">
        <span class="details__label">Затрачено</span>
        <span class="details__value">{{ formatTime(task.actualMinutes) }}</span>
      </div>
      <div class="details__field">
        <span class="details__label">Создано</span>
        <span class="details__value">{{ formatDate(task.createdAt) }}</span>
      </div>
      <div class="details__field">
        <span class="details__label">Обновлено</span>
        <span class="details__value">{{ formatDate(task.updatedAt) }}</span>
      </div>
    </div>

    <div v-if="showTimeInput" class="details__time-form">
      <BaseInput
        v-model="timeMinutes"
        type="number"
        placeholder="Минуты"
      />
      <BaseButton size="sm" @click="onLogTime" :disabled="!timeMinutes">Записать</BaseButton>
      <BaseButton size="sm" variant="ghost" @click="showTimeInput = false">Отмена</BaseButton>
    </div>

    <p v-if="error" class="details__error">{{ error }}</p>

    <div class="details__actions">
      <BaseButton size="sm" variant="ghost" @click="showTimeInput = true">
        <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
          <circle cx="7" cy="7" r="6" stroke="currentColor" stroke-width="1.5"/>
          <path d="M7 4v3.5l2.5 1.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        Время
      </BaseButton>
      <BaseButton size="sm" variant="secondary" @click="emit('edit')">Редактировать</BaseButton>
      <BaseButton size="sm" variant="danger" :loading="loading" @click="onDelete">Удалить</BaseButton>
    </div>
  </div>
</template>

<style scoped>
.details {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.details__meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.details__id {
  font-size: 12px;
  font-weight: 600;
  color: #9ca3af;
  font-family: monospace;
}

.details__title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
  line-height: 1.4;
}

.details__desc {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
  line-height: 1.6;
  white-space: pre-wrap;
}

.details__grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 12px;
}

.details__field {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.details__label {
  font-size: 11px;
  font-weight: 600;
  color: #9ca3af;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.details__value {
  font-size: 14px;
  color: #374151;
  font-weight: 500;
}

.details__time-form {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.details__error {
  color: #ef4444;
  font-size: 13px;
  margin: 0;
}

.details__actions {
  display: flex;
  gap: 8px;
  padding-top: 4px;
}
</style>
