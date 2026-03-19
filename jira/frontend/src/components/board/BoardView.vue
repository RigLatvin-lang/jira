<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useTasksStore } from '@/stores/tasks'
import type { TaskResponse, TaskStatus } from '@/types'
import { TASK_STATUSES } from '@/types'
import BoardColumn from './BoardColumn.vue'
import TaskModal from '@/components/task/TaskModal.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const tasksStore = useTasksStore()
const { tasksByStatus, loading, filterMyTasks } = storeToRefs(tasksStore)

const showCreateModal = ref(false)
const selectedTask = ref<TaskResponse | null>(null)

onMounted(() => {
  tasksStore.fetchTasks()
})

function onTaskClick(task: TaskResponse) {
  selectedTask.value = task
}

function onDrop(status: TaskStatus, items: TaskResponse[]) {
  const moved = items.find((item) => item.status !== status)
  if (moved) {
    tasksStore.updateTaskLocally(moved.id, status)
    tasksStore.changeStatus(moved.id, status).catch(() => {
      tasksStore.fetchTasks()
    })
  }
}

function onModalClose() {
  selectedTask.value = null
  showCreateModal.value = false
}

function onSaved() {
  onModalClose()
}

function onDeleted() {
  onModalClose()
}
</script>

<template>
  <div class="board">
    <div class="board__toolbar">
      <h2 class="board__title">Доска задач</h2>
      <div class="board__actions">
        <button
          class="board__filter"
          :class="{ 'board__filter--active': filterMyTasks }"
          @click="filterMyTasks = !filterMyTasks"
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <circle cx="8" cy="8" r="5.5" stroke="currentColor" stroke-width="1.5"/>
            <path d="M8 5.5v5M5.5 8h5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
          {{ filterMyTasks ? 'Мои задачи' : 'Все задачи' }}
        </button>
        <BaseButton @click="showCreateModal = true">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          Создать задачу
        </BaseButton>
      </div>
    </div>

    <div v-if="loading && !Object.values(tasksByStatus).some(t => t.length)" class="board__loading">
      <div class="board__spinner" />
      <span>Загрузка задач...</span>
    </div>

    <div class="board__columns">
      <BoardColumn
        v-for="status in TASK_STATUSES"
        :key="status"
        :status="status"
        :tasks="tasksByStatus[status]"
        @task-click="onTaskClick"
        @drop="onDrop"
      />
    </div>

    <TaskModal
      v-if="showCreateModal"
      @close="onModalClose"
      @saved="onSaved"
    />

    <TaskModal
      v-if="selectedTask"
      :task="selectedTask"
      @close="onModalClose"
      @saved="onSaved"
      @deleted="onDeleted"
    />
  </div>
</template>

<style scoped>
.board {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.board__toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  flex-shrink: 0;
}

.board__title {
  font-size: 22px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.board__actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.board__filter {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  background: #f5f5f5;
  color: #6b7280;
  font-size: 13px;
  font-weight: 500;
  font-family: inherit;
  cursor: pointer;
  transition: transform 0.15s ease, background-color 0.15s ease, color 0.15s ease, box-shadow 0.15s ease;
}

.board__filter:hover {
  transform: scale(1.03);
  background: #ebebeb;
}

.board__filter--active {
  background: #fff7ed;
  color: #f97316;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.15);
}

.board__filter--active:hover {
  background: #ffedd5;
}

.board__columns {
  display: flex;
  gap: 12px;
  flex: 1;
  overflow-x: auto;
  padding-bottom: 8px;
}

.board__columns::-webkit-scrollbar {
  height: 6px;
}

.board__columns::-webkit-scrollbar-track {
  background: transparent;
}

.board__columns::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 4px;
}

.board__loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 60px 0;
  color: #9ca3af;
  font-size: 14px;
}

.board__spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #e5e7eb;
  border-top-color: #f97316;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
