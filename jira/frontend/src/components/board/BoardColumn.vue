<script setup lang="ts">
import { computed } from 'vue'
import { VueDraggable } from 'vue-draggable-plus'
import type { TaskResponse, TaskStatus } from '@/types'
import { STATUS_LABELS } from '@/types'
import TaskCard from './TaskCard.vue'

const props = defineProps<{
  status: TaskStatus
  tasks: TaskResponse[]
}>()

const emit = defineEmits<{
  taskClick: [task: TaskResponse]
  drop: [status: TaskStatus, tasks: TaskResponse[]]
}>()

const localTasks = computed({
  get: () => props.tasks,
  set: (val) => emit('drop', props.status, val),
})

const columnColor = computed(() => {
  const colors: Record<TaskStatus, string> = {
    WAITING: '#9ca3af',
    ANALYSIS: '#8b5cf6',
    DEVELOPMENT: '#3b82f6',
    TESTING: '#eab308',
    DONE: '#22c55e',
  }
  return colors[props.status]
})
</script>

<template>
  <div class="column">
    <div class="column__header">
      <div class="column__indicator" :style="{ background: columnColor }" />
      <h3 class="column__title">{{ STATUS_LABELS[status] }}</h3>
      <span class="column__count">{{ tasks.length }}</span>
    </div>
    <VueDraggable
      v-model="localTasks"
      class="column__list"
      :group="{ name: 'tasks' }"
      :animation="200"
      ghost-class="task-ghost"
      drag-class="task-drag"
    >
      <TaskCard
        v-for="task in localTasks"
        :key="task.id"
        :task="task"
        @click="emit('taskClick', task)"
      />
    </VueDraggable>
  </div>
</template>

<style scoped>
.column {
  flex: 1;
  min-width: 260px;
  max-width: 320px;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
  border-radius: 16px;
  padding: 12px;
  height: 100%;
}

.column__header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 4px 12px;
}

.column__indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.column__title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.column__count {
  font-size: 12px;
  font-weight: 600;
  color: #9ca3af;
  background: white;
  padding: 1px 8px;
  border-radius: 10px;
}

.column__list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
  overflow-y: auto;
  min-height: 60px;
  padding: 4px;
}

.column__list::-webkit-scrollbar {
  width: 4px;
}

.column__list::-webkit-scrollbar-track {
  background: transparent;
}

.column__list::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 4px;
}
</style>

<style>
.task-ghost {
  opacity: 0.4;
  transform: scale(0.95);
}

.task-drag {
  transform: rotate(2deg) scale(1.05);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15) !important;
}
</style>
