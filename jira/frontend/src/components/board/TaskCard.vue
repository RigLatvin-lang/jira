<script setup lang="ts">
import type { TaskResponse } from '@/types'
import { PRIORITY_LABELS, PRIORITY_COLORS } from '@/types'
import BaseBadge from '@/components/ui/BaseBadge.vue'

defineProps<{
  task: TaskResponse
}>()

defineEmits<{
  click: [task: TaskResponse]
}>()

function formatTime(minutes: number | null): string {
  if (!minutes) return ''
  const h = Math.floor(minutes / 60)
  const m = minutes % 60
  return h > 0 ? `${h}ч ${m}м` : `${m}м`
}
</script>

<template>
  <div class="task-card" @click="$emit('click', task)">
    <div class="task-card__header">
      <span class="task-card__id">{{ task.id.slice(0, 6).toUpperCase() }}</span>
      <BaseBadge
        :text="PRIORITY_LABELS[task.priority]"
        :color="PRIORITY_COLORS[task.priority]"
      />
    </div>
    <h4 class="task-card__title">{{ task.title }}</h4>
    <p v-if="task.description" class="task-card__desc">{{ task.description }}</p>
    <div class="task-card__footer">
      <div v-if="task.assigneeUsername" class="task-card__assignee">
        <div class="task-card__assignee-avatar">{{ task.assigneeUsername?.[0]?.toUpperCase() }}</div>
        <span>{{ task.assigneeUsername }}</span>
      </div>
      <div v-if="task.estimateMinutes" class="task-card__time">
        <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
          <circle cx="7" cy="7" r="6" stroke="currentColor" stroke-width="1.5"/>
          <path d="M7 4v3.5l2.5 1.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        <span>{{ formatTime(task.estimateMinutes) }}</span>
        <span v-if="task.actualMinutes" class="task-card__actual">/ {{ formatTime(task.actualMinutes) }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.task-card {
  background: white;
  border-radius: 12px;
  padding: 14px;
  cursor: grab;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  user-select: none;
}

.task-card:hover {
  transform: translateY(-2px) scale(1.01);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.task-card:active {
  cursor: grabbing;
  transform: scale(1.03);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.task-card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.task-card__id {
  font-size: 11px;
  font-weight: 600;
  color: #9ca3af;
  font-family: monospace;
}

.task-card__title {
  font-size: 14px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px;
  line-height: 1.4;
}

.task-card__desc {
  font-size: 12px;
  color: #6b7280;
  margin: 0 0 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.task-card__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.task-card__assignee {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #6b7280;
}

.task-card__assignee-avatar {
  width: 20px;
  height: 20px;
  border-radius: 6px;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
}

.task-card__time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #9ca3af;
}

.task-card__actual {
  color: #f97316;
}
</style>
