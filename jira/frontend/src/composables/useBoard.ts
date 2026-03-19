import { onMounted } from 'vue'
import { useTasksStore } from '@/stores/tasks'
import type { TaskStatus, TaskResponse } from '@/types'

export function useBoard() {
  const tasksStore = useTasksStore()

  onMounted(() => {
    tasksStore.fetchTasks()
  })

  async function onTaskDrop(taskId: string, newStatus: TaskStatus) {
    tasksStore.updateTaskLocally(taskId, newStatus)
    try {
      await tasksStore.changeStatus(taskId, newStatus)
    } catch {
      await tasksStore.fetchTasks()
    }
  }

  function handleDragEnd(status: TaskStatus, items: TaskResponse[]) {
    const moved = items.find((item) => item.status !== status)
    if (moved) {
      onTaskDrop(moved.id, status)
    }
  }

  return {
    tasksByStatus: tasksStore.tasksByStatus,
    loading: tasksStore.loading,
    handleDragEnd,
    refresh: () => tasksStore.fetchTasks(),
  }
}
